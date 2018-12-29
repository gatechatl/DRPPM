package stjude.projects.jinghuizhang;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class JinghuiZhangPatientSummary {
	
	public static String description() {
		return "Patient Info Summry";
	}
	public static String type() {
		return "JinghuiZhang";
	}
	public static String parameter_info() {
		return "[inputPatientMeta] [fpkm_matrix] [outputFile]";
	}
	public static void execute(String[] args) {			
		try {
			
			String inputPatientInfo = args[0];
			String fpkm_matrix_file = args[1];
			String outputFile = args[2];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);			
			
			HashMap sample_list = new HashMap();
			HashMap sample_result = new HashMap();
			FileInputStream fstream = new FileInputStream(fpkm_matrix_file);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("-");
				sample_list.put(split[0], split[0]);
			}
			/*String[] split_header = header.split("\t");
			for (int i = 1; i < split_header.length; i++) {
				sample_list.put(split_header[i].split("-")[0], split_header[i].split("-")[0]);
			}*/
			in.close();

			
			fstream = new FileInputStream(inputPatientInfo);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			int rnaseq_id1 = 0;
			int rnaseq_id2 = 0;
			int rnaseq_id3 = 0;
			int rnaseq_id4 = 0;
			String[] split_header = header.split("\t");
			for (int i = 1; i < split_header.length; i++) {
				if (split_header[i].equals("RNAseq_id_D")) {
					rnaseq_id1 = i;
				}
				if (split_header[i].equals("actual_RNA_SJID")) {
					rnaseq_id2 = i;
				}
				if (split_header[i].equals("target_RNA_barcode_diagnosis")) {
					rnaseq_id3 = i;
				}
				if (split_header[i].equals("RNA_barcode_D")) {
					rnaseq_id4 = i;
				}
			}
			out.write("PatientMetaInfo\t" + header + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				
				if (split.length > 5) {
					String sj_id = split[0].split("-")[0];
					String sj_id1 = split[0].split("-")[0];
					if (sample_list.containsKey(sj_id)) {
						sample_list.put(sj_id1,  "found");
						sample_result.put(sj_id1, str);
						if (sample_result.containsKey(sj_id)) {
							String original_line = (String)sample_result.get(sj_id);
							String[] original_line_split = original_line.split("\t");
							String new_line = split[0];
							for (int j = 1; j < original_line_split.length; j++) {
								split[j] = split[j].trim();
								original_line_split[j] = original_line_split[j].trim();
								if (split[j].equals(original_line_split[j])) {
									new_line += "\t" + split[j];
								} else if (split[j].equals("") && !original_line_split[j].equals("")) {
									new_line += "\t" + original_line_split[j];
								} else if (!split[j].equals("") && original_line_split[j].equals("")) {
									new_line += "\t" + split[j];
								} else {
									new_line += "\t" + original_line_split[j] + ";" + split[j];
								}
							}
							sample_result.put(sj_id, new_line);
						} else {
							sample_result.put(sj_id, str);
						}
						//out.write("Found\t" + str + "\n");
					} else {
						//out.write("Missing\t" + str + "\n");
					}
				} else {
					System.out.println("Problem");
				}
			}			
			
			Iterator itr = sample_result.keySet().iterator();
			while (itr.hasNext()) {
				String id = (String)itr.next();
				String line = (String)sample_result.get(id);
				out.write("Found\t" + line + "\n");
			}
			out.close();
			
			int count_found = 0;
			itr = sample_list.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String)itr.next();
				String found = (String)sample_list.get(key);
				
				if (found.equals("found")) {
					count_found++;
				}
			}
			System.out.println("Total: " + sample_list.size());
			System.out.println("Found: " + count_found);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
