package stjude.projects.jinghuizhang.mutations;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Previously Xin Zhou gave me the SNV file 
 * Z:\ResearchHome\ProjectSpace\zhanggrp\AltSpliceAtlas\common\analysis\PCGP_References\pediatric.hg19.vcf
 * @author tshaw
 *
 */
public class JinghuiZhangExtractSCNAFromXinZhouCNVSVFile {

	public static String description() {
		return "Extract mutations from Xin Zhou's SNV file";
	}
	public static String type() {
		return "JinghuiZhang";
	}
	public static String parameter_info() {
		return "[Xin's vnv sv file] [outputRaw] [outputResult]";
	}
	public static void execute(String[] args) {
		
		try {
			boolean start_reading = false;
			String[] sample_info = {};
			double length_cutoff = new Double(args[1]);
			double log2FC_cutoff = new Double(args[2]);
			String outputFile = args[4]; //"Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_References\\PanCancer230_Sample.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			String outputFileGeneRaw = args[3]; //"Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_References\\PanCancer230_Sample_Raw.txt";
			FileWriter fwriterGeneRaw = new FileWriter(outputFileGeneRaw);
			BufferedWriter outGeneRaw = new BufferedWriter(fwriterGeneRaw);
			
			String input_cnvsv_file = args[0]; //"Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_References\\pediatric.hg19.vcf";
			FileInputStream fstream = new FileInputStream(input_cnvsv_file);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine(); // header
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String chr = split[0];
				double start = new Double(split[1]);
				double end = new Double(split[2]);
						
				String text = split[3].replaceAll(" ", "").replaceAll("\"", "").replaceAll("\\{", "").replaceAll("\\}", "");
				String sampleName = "NA";
				String type = "NA";
				String vorigin = "NA";
				String pmid = "NA";
				String dna_assay = "NA";
				String project = "NA";
				String value = "NA";
				for (String split_text: text.split(",")) {					
					if (split_text.contains("sample:")) {
						sampleName = split_text.replaceAll("sample:", "").trim();
					}
					if (split_text.contains("dt:")) {
						type = split_text.replaceAll("dt:", "").trim();						
					}

					if (split_text.contains("vorigin:")) {
						vorigin = split_text.replaceAll("vorigin:", "").trim();						
					}
					if (split_text.contains("dna_assay:")) {
						dna_assay = split_text.replaceAll("dna_assay:", "").trim();						
					}
					if (split_text.contains("value:")) {
						value = split_text.replaceAll("value:", "").trim();						
					}
				}
				if (type.equals("4")) {
					outGeneRaw.write(str + "\n");
					if ((!chr.contains("X") && !chr.contains("Y")) && (end - start) >= length_cutoff && (new Double(value) > log2FC_cutoff || new Double(value) < -log2FC_cutoff)) {
						out.write(sampleName + "\t" + chr + "\t" + start + "\t" + end + "\t" + (end - start) + "\t" + value + "\t" + dna_assay + "\t" + vorigin + "\t" + pmid + "\n");
					}
				}
				
							
			}
			in.close();
			out.close();
			outGeneRaw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
