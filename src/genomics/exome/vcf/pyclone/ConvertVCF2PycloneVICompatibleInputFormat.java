package genomics.exome.vcf.pyclone;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class ConvertVCF2PycloneVICompatibleInputFormat {

	/*
	 * mutation_id     sample_id       ref_counts      alt_counts      normal_cn       major_cn        minor_cn        tumour_content
   2 CRUK0001:1:1564541:C    R1      178     0       2       2       1       0.21
   3 CRUK0001:1:2117447:C    R1      272     16      2       2       1       0.21
   4 CRUK0001:1:2433563:C    R1      362     11      2       2       1       0.21
   5 CRUK0001:1:2441437:C    R1      343     26      2       2       1       0.21
   6 CRUK0001:1:4715587:C    R1      501     0       2       2       2       0.21
   7 CRUK0001:1:4832505:C    R1      766     56      2       2       2       0.21
   8 CRUK0001:1:6172947:A    R1      210     72      2       2       2       0.21
   9 CRUK0001:1:6631112:T    R1      548     113     2       2       2       0.21

#CHROM  POS     ID      REF     ALT     QUAL    FILTER  INFO    FORMAT  SL435077_st_t
chr1    1312739 .       C       T       .       PASS    DP=643;ECNT=2;FUNCOTATION=[INTS11|hg38|chr1|1312739|1312739|INTRON||SNP|C|C|T|g.chr1:13     12739C>T|ENST00000435064.5|-|||c.e13+39G>A|||0.6807980049875312|GCCCAGGCTGCCCGTGCTGAC|INTS11_ENST00000421495.6_INTRON/INTS11_ENST00000450926.6_     INTRON/INTS11_ENST00000411962.5_INTRON/INTS11_ENST00000419704.5_INTRON/INTS11_ENST00000540437.5_INTRON/INTS11_ENST00000545578.5_INTRON/INTS11_E     NST00000620829.4_INTRON/INTS11_ENST00000618806.4_INTRON/ACAP3_ENST00000354700.10_FIVE_PRIME_FLANK/MIR6727_ENST00000620702.1_FIVE_PRIME_FLANK|||     ||||||||||||||||||||||||||||||||||||||||||||||||||||||CR533557|NM_017871.6|NP_060341.2|HGNC:26052|integrator_%20_complex_%20_subunit_%20_11|App     roved|gene_%20_with_%20_protein_%20_product|protein-coding_%20_gene|CPSF3L|"cleavage_%20_and_%20_polyadenylation_%20_specific_%20_factor_%20_3-     like"|FLJ20542_%2C__%20_RC-68_%2C__%20_CPSF73L_%2C__%20_INT11||1p36.33|2016-12-15|2016-12-15|2016-12-15|AL136813||54973|ENSG00000127054|1568439     8_%2C__%20_16239144|NM_017871|1366|Integrator_%20_complex|CCDS21_%2C__%20_CCDS57959_%2C__%20_CCDS57960_%2C__%20_CCDS57961_%2C__%20_CCDS72678|OT     THUMG00000003330|54973|611354|NM_001256456|Q5TA45|ENSG00000127054|uc001aee.3|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||     |];NLOD=33.96;N_ART_LOD=-2.0664;POP_AF=0.001;P_GERMLINE=-30.657;TLOD=16.90       GT:AD:AF:F1R2:F2R1:MBQ:MFRL:MMQ:MPOS:SA_MAP_AF:SA_POST_PROB          0/1:481,14:0.055:251,9:230,5:28:201,197:60:13:0.025,0.023,0.028:0.002,0.004,0.994

	 */
	public static void main(String[] args) {
		
		try {
			String tumor_content_exon = "/Users/4472414/Projects/ORIEN_AVATAR_MutationBurden/ORIEN_SamplePurity_Path/Exom_Tumor_Purity.txt";
					
			HashMap sample_purity = new HashMap();
			FileInputStream fstream = new FileInputStream(tumor_content_exon);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (!split[1].equals("NA")) {
					sample_purity.put(split[0], new Double(split[1]));
				}
			}
			in.close();
			String path = "/Users/4472414/Projects/ORIEN_AVATAR_MutationBurden/filtered_output";
			
			String output_path = "/Users/4472414/Projects/ORIEN_AVATAR_MutationBurden/filtered_output_pyclone_input";
			File folder = new File(path);
			for (File f: folder.listFiles()) {
				if (f.getPath().contains("vcf")) {
					
					String inputFile = f.getPath();
					String sampleName = f.getName().split("_st")[0];
					if (sampleName.substring(0, 1).equals("T")) {
						sampleName = sampleName.substring(1, sampleName.length());
					}
					System.out.println(sampleName);
					
					String outputFile = output_path + "/" + f.getName() + ".pyclone.tsv";
					FileWriter fwriter = new FileWriter(outputFile);
					BufferedWriter out = new BufferedWriter(fwriter);
					out.write("mutation_id\tsample_id\tref_counts\talt_counts\tnormal_cn\tmajor_cn\tminor_cn\ttumour_content\n");
					
					double purity = 0.75;
					if (sample_purity.containsKey(sampleName)) {
						purity = (Double)sample_purity.get(sampleName);
					}
					fstream = new FileInputStream(inputFile);
					din = new DataInputStream(fstream);
					in = new BufferedReader(new InputStreamReader(din));
					while (in.ready()) {
						String str = in.readLine();
						if (!str.substring(0, 1).equals("#")) {
							String[] split = str.split("\t");
							String chr = split[0];
							String position = split[1];
							String alt_variant = split[4];
							String annotation = split[8];
							String annotation_num = split[9];
							String mutation_id = sampleName + ":" + chr + ":" + position + ":" + alt_variant;
							int ref_allele =	new Integer(annotation_num.split(":")[1].split(",")[0]);
							int alt_allele =	new Integer(annotation_num.split(":")[1].split(",")[1]);
							int normal_cn = 2;
							int major_cn = 2;
							int minor_cn = 2;
							
							out.write(mutation_id + "\t" + sampleName + "\t" + ref_allele + "\t" + alt_allele + "\t" + normal_cn + "\t" + major_cn + "\t" + minor_cn + "\t" + purity + "\n");
						}						
					}
					in.close();
					out.close();
				}
				//System.out.println(f.getPath());
			}
			
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
