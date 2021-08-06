package rnaseq.splicing.alternativejuncpipeline.xbp1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 * Read the STAR tab file and quantify the splicing feature of XBP1s
 * chr22   28795733        28796046        2       2       1       64      0       37
chr22   28795733        28797076        2       2       1       1       0       35
chr22   28796122        28796147        0       0       1       14      0       33
chr22   28796193        28797076        2       2       1       86      0       37
chr22   28797206        28799041        2       2       1       5       0       36
chr22   28797206        28799056        2       2       1       55      0       37
 * @author gatechatl
 *
 */
public class CalculateXBP1sFeatures {

	public static String type() {
		return "Splicing";
	}
	public static String description() {
		return "Calculate the XBP1 splicing feature.";
	}
	public static String parameter_info() {
		return "[inputSTARSJTAB files list] [genome_type: hg19 or hg38] [outputFeatures]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputSTARSJTAB = args[0];
			String human_genome_type = args[1];
			String outputFile = args[2];
			

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			out.write("SampleName\txbp1s_stress_junc\txbp1_exon3exon4_junc1\txbp1_exon4exon5_junc\txbp1_skipexon4_junc\txbp1s_psi_score\texon4_skipping\n");
			FileInputStream fstream = new FileInputStream(inputSTARSJTAB);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String sampleName = split[0];
				String path = split[1];
				
				double xbp1_exon3exon4_junc1 = 0;					
				double xbp1_skipexon4_junc = 0;
				double xbp1s_stress_junc = 0;
				double xbp1_exon4exon5_junc = 0;
				
				FileInputStream fstream2 = new FileInputStream(path);
				DataInputStream din2 = new DataInputStream(fstream2);
				BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
				while (in2.ready()) {
					String str2 = in2.readLine();
					String[] split2 = str2.split("\t");
					String chr = split2[0];
					String start = split2[1];
					String end = split2[2];
					String uniq_read = split2[6];

					if (human_genome_type.equals("hg38")) {
						if (chr.equals("chr22") && start.equals("28795733") && end.equals("28796046")) {
							xbp1_exon3exon4_junc1 = new Double(uniq_read);
						}
						if (chr.equals("chr22") && start.equals("28795733") && end.equals("28797076")) {
							xbp1_skipexon4_junc = new Double(uniq_read);
						}
						if (chr.equals("chr22") && start.equals("28796122") && end.equals("28796147")) {
							xbp1s_stress_junc = new Double(uniq_read);
						}
						if (chr.equals("chr22") && start.equals("28796193") && end.equals("28797076")) {
							xbp1_exon4exon5_junc = new Double(uniq_read);
						}
					}
					
				}
				in2.close();
				
				double psi_score = (xbp1s_stress_junc + 0.01) / ((xbp1_exon3exon4_junc1 + xbp1_exon4exon5_junc) / 2 + 0.01);
				double exon4_skipping = (xbp1_skipexon4_junc + 0.01) / (xbp1_exon3exon4_junc1 + xbp1_exon4exon5_junc + xbp1_skipexon4_junc + 0.01);
			
				out.write(sampleName + "\t" + xbp1s_stress_junc + "\t" + xbp1_exon3exon4_junc1 + "\t" + xbp1_exon4exon5_junc + "\t" + xbp1_skipexon4_junc + "\t" + psi_score + "\t" + exon4_skipping + "\n");
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
