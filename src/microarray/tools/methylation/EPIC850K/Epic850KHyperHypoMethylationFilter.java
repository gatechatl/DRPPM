package microarray.tools.methylation.EPIC850K;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class Epic850KHyperHypoMethylationFilter {


	public static String description() {
		return "Hyper Hypomethylation.";
	}
	public static String type() {
		return "METHYLATION";
	}
	public static String parameter_info() {
		return "[inputFile] [M value cutoff: beta of 0.3 is 1.23; beta of 0.2 is 2.0] [foldChnage group2-group1 Cutoff ] [OutputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			double hyper_hypo_cutoff = new Double(args[1]);
			double fold_change_cutoff = new Double(args[2]);
			String outputFile = args[3];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);			
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();			
			out.write(header + "\tGroup1_Status\tGroup2_Status" + "\n");
			String[] header_split = header.split("\t");
			int group1_index = -1;
			int group2_index = -1;
			int foldChangeIndex = -1;
			int snp_index = -1;
			for (int i = 0; i < header_split.length; i++) {
				if (header_split[i].equals("Group1Avg")) {
					group1_index = i;
				}
				if (header_split[i].equals("Group2Avg")) {
					group2_index = i;
				}
				if (header_split[i].equals("Group2-Group1")) {
					foldChangeIndex = i;
				}
				if (header_split[i].equals("SNP_minorallelefrequency")) {
					snp_index = i;
				}
			}
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				double group1_avg = new Double(split[group1_index]);
				double group2_avg = new Double(split[group2_index]);
				double fold_change = new Double(split[foldChangeIndex]);
				double snp_freq = 0.0;
				if (split.length > snp_index) {
					if (!split[snp_index].trim().equals("")) {
						String[] split_snp = split[snp_index].split(";");
						for (int j = 0; j < split_snp.length; j++) {
							if (new Double(split_snp[j]) > snp_freq) {
								snp_freq = new Double(split_snp[j]);
							}
						}
					}
				}
				if (snp_freq < 0.01) {
					String group1_status = "No Change";
					String group2_status = "No Change";
					if (group1_avg >=  hyper_hypo_cutoff) {
						group1_status = "Hypermethylated";
					}
					if (group1_avg <=  -hyper_hypo_cutoff) {
						group1_status = "Hypomethylated";
					}
					if (group2_avg >=  hyper_hypo_cutoff) {
						group2_status = "Hypermethylated";
					}
					if (group2_avg <=  -hyper_hypo_cutoff) {
						group2_status = "Hypomethylated";
					}
					if (group1_avg >= hyper_hypo_cutoff && fold_change <= -fold_change_cutoff && group2_avg < hyper_hypo_cutoff) {
						out.write(str + "\t" + group1_status + "\t" + group2_status + "\n");
					} else if (group2_avg >= hyper_hypo_cutoff && fold_change >= fold_change_cutoff && group1_avg < hyper_hypo_cutoff) {
						out.write(str + "\t" + group1_status + "\t" + group2_status + "\n");
					} else if (group1_avg <= -hyper_hypo_cutoff && fold_change >= fold_change_cutoff && group2_avg > -hyper_hypo_cutoff) {
						out.write(str + "\t" + group1_status + "\t" + group2_status + "\n");
					} else if (group2_avg <= -hyper_hypo_cutoff && fold_change <= -fold_change_cutoff && group1_avg > -hyper_hypo_cutoff) {
						out.write(str + "\t" + group1_status + "\t" + group2_status + "\n");
					}
				}
			}
			in.close();
			out.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
