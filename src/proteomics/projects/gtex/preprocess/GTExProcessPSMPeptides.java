package proteomics.projects.gtex.preprocess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

import idconversion.tools.GTFFile;

/**
 * Generate an input fasta file for all the PSM peptides
 * Ran on the MAC laptop computer
 * @author 4472414
 *
 */
public class GTExProcessPSMPeptides {

	public static String type() {
		return "PROTEOMICS";
	}
	public static String description() {
		return "Generate a fasta file and output table count of the PSMs";
	}
	public static String parameter_info() {
		return "[PSM.lst] [output FASTA] [output Table]";
	}
	public static void main(String[] args) {
		
		try {
			
			String inputFile = "/Users/4472414/Projects/GTEx_Proteomics_Search/PSM/PSM.lst"; //args[0];
			String outputFasta = "/Users/4472414/Projects/GTEx_Proteomics_Search/PSM/outputFasta.fa"; //args[1];
			String outputTable = "/Users/4472414/Projects/GTEx_Proteomics_Search/PSM/outputTable.txt"; //args[2];
			
			
			GTFFile.initialize("/Users/4472414/References/HG38_GTF/gencode.v35.primary_assembly.annotation.gtf");
			
			FileWriter fwriter_fasta = new FileWriter(outputFasta);
			BufferedWriter out_fasta = new BufferedWriter(fwriter_fasta);
			
			FileWriter fwriter_table = new FileWriter(outputTable);
			BufferedWriter out_table = new BufferedWriter(fwriter_table);
			
			HashMap map = new HashMap();
			HashMap map2 = new HashMap();
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				
				File f = new File(split[0]);
				if (f.exists()) {
					FileInputStream fstream2 = new FileInputStream(split[0]);
					DataInputStream din2 = new DataInputStream(fstream2);
					BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
					while (in2.ready()) {
						String str2 = in2.readLine();
						String[] split2 = str2.split("\t");
						String peptide = split2[8].replaceAll("\"", "").replaceAll("\\.", "").replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("-",  "").toUpperCase();
						String gene = split2[14].split(" ")[0].trim();
						map2.put(peptide, gene);
						System.out.println(peptide);
						if (map.containsKey(peptide)) {
							int count = (Integer)map.get(peptide);
							count++;
							map.put(peptide, count);
						} else {
							map.put(peptide, 1);
						}
					}
					in2.close();
				}
			}
			in.close();
			
			int index = 1;
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String peptide = (String)itr.next();
				int count = (Integer)map.get(peptide);
				String gene = ((String)map2.get(peptide));
				gene = gene.replaceAll("\"", "").replaceAll("\\'",  "").trim();
				String geneName = (String)GTFFile.geneid_clean2geneName.get(gene.split("\\.")[0]);
				out_table.write(peptide + "\t" + count + "\t" + gene + "\t" + geneName + "\n");
				out_fasta.write(">" + index + "_" + gene + "_" + geneName + "_" + count + "\n");
				out_fasta.write(peptide + "\n");
			}
			out_table.close();
			out_fasta.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
