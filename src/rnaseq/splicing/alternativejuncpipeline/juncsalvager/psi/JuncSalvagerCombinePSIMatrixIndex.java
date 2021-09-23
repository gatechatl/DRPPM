package rnaseq.splicing.alternativejuncpipeline.juncsalvager.psi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Generate a combined PSI matrix. Replace NaN with user defined value. We recommend putting 1.0 representing exon is being used. Generate a blacklist for those with > 50% junction with 0.
 * We also require the exon to contain at least 10 reads.
 * 
 * @author tshaw
 *
 */
public class JuncSalvagerCombinePSIMatrixIndex {

	public static String type() {
		return "JUNCSALVAGER";
	}
	public static String description() {
		return "Combine the psi values. Replace NaN with user defined value. Generate a blacklist for those with > 10% junction with 0.\n";
	}
	public static String parameter_info() {
		return "[inputFileLst] [index of the fileLst 1: is ] [replaceNaNwithThis] [outputFile] [double: proportion_samples_black_list_to_filter][outputFileBlackList] [final_outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFileLst = args[0];
			int indexFileLst = new Integer(args[1]);
			String replaceNaNwithThis = args[2];
			String outputFile = args[3];
			double proportion_samples_black_list_to_filter = new Double(args[4]);
			String outputFileBlackList = args[5];
			String final_outputFile = args[6];
			int count = 0;
			
			LinkedList header_list = new LinkedList();
			HashMap map = new HashMap();
			FileWriter fwriter_blacklist = new FileWriter(outputFileBlackList);
			BufferedWriter out_blacklist = new BufferedWriter(fwriter_blacklist);
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("Sample");
			boolean header_once = true;
			FileInputStream fstream = new FileInputStream(inputFileLst);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				
				count++;
				if (header_once) {
					FileInputStream fstream_head = new FileInputStream(split[indexFileLst]);
					DataInputStream din_head = new DataInputStream(fstream_head);
					BufferedReader in_head = new BufferedReader(new InputStreamReader(din_head));
					in_head.readLine();
					while (in_head.ready()) {
						String str_head = in_head.readLine();
						String[] str_head_split = str_head.split("\t");
						out.write("\t" + str_head_split[0] + "_" + str_head_split[1] + "_" + str_head_split[2]);
						header_list.add(str_head_split[0] + "_" + str_head_split[1] + "_" + str_head_split[2]);
					}
					in_head.close();
					header_once = false;
					out.write("\n");
				}
				
				File f = new File(str);
				if (f.exists()) {
					String sampleName = f.getName().replaceAll(".SJ.out.tab.psi.txt", "").replaceAll(".SJ.out.tab.pso.txt", "").replaceAll(".SJ.out.tab.5to3AltSplice.psi.txt", "");
					out.write(sampleName);
					FileInputStream fstream_psi = new FileInputStream(str);
					DataInputStream din_psi = new DataInputStream(fstream_psi);
					BufferedReader in_psi = new BufferedReader(new InputStreamReader(din_psi));
					in_psi.readLine();
					while (in_psi.ready()) {
						String str_psi = in_psi.readLine();
						String[] split_psi = str_psi.split("\t");
						String exon = split_psi[0] + "_" + split_psi[1] + "_" + split_psi[2];
						if (new Double(split_psi[split_psi.length - 2]) == 0.0 || new Double(split_psi[split_psi.length - 3]) == 0.0 || (new Double(split_psi[split_psi.length - 2]) + new Double(split_psi[split_psi.length - 3]) < 10)) {
							if (map.containsKey(exon)) {
								int prev_count = (Integer)map.get(exon);
								prev_count++;
								map.put(exon, prev_count);
							} else {
								map.put(exon, 1);
							}
						}
						out.write("\t" + split_psi[split_psi.length - 4].replaceAll("NaN", replaceNaNwithThis));
					}
					in_psi.close();
					out.write("\n");
				}
			}
			in.close();
			out.close();
			HashMap final_black_list = new HashMap();
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String exon = (String)itr.next();
				int black_list_count = (Integer)map.get(exon);
				double score = new Double(black_list_count) / count;
				if (score > proportion_samples_black_list_to_filter) {
					out_blacklist.write(exon + "\n");
					final_black_list.put(exon, exon);
				}
			}
			out_blacklist.close();
			HashMap black_list_index = new HashMap();
			int index = 0;
			itr = header_list.iterator();
			while (itr.hasNext()) {
				String header = (String)itr.next();
				index++;
				if (final_black_list.containsKey(header)) {
					black_list_index.put(index, index);
				}
			}

			
			FileWriter fwriter_final = new FileWriter(final_outputFile);
			BufferedWriter out_final = new BufferedWriter(fwriter_final);
			out_final.write("Sample");
			header_once = true;
			fstream = new FileInputStream(inputFileLst);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				count++;
				if (header_once) {
					int idx = 1;
					FileInputStream fstream_head = new FileInputStream(split[indexFileLst]);
					DataInputStream din_head = new DataInputStream(fstream_head);
					BufferedReader in_head = new BufferedReader(new InputStreamReader(din_head));
					in_head.readLine();
					while (in_head.ready()) {
						String str_head = in_head.readLine();
						String[] str_head_split = str_head.split("\t");
						if (!black_list_index.containsKey(idx)) {
							out_final.write("\t" + str_head_split[0] + "_" + str_head_split[1] + "_" + str_head_split[2]);
						}						
						idx++;
					}
					in_head.close();
					header_once = false;
					out_final.write("\n");
				}
				
				File f = new File(split[indexFileLst]);
				if (f.exists()) {
					String sampleName = f.getName().replaceAll(".SJ.out.tab.psi.txt", "").replaceAll(".SJ.out.tab.pso.txt", "").replaceAll(".SJ.out.tab.5to3AltSplice.psi.txt", "");
					out_final.write(sampleName);
					int idx = 1;
					FileInputStream fstream_psi = new FileInputStream(split[indexFileLst]);
					DataInputStream din_psi = new DataInputStream(fstream_psi);
					BufferedReader in_psi = new BufferedReader(new InputStreamReader(din_psi));
					in_psi.readLine();
					while (in_psi.ready()) {
						String str_psi = in_psi.readLine();
						String[] split_psi = str_psi.split("\t");
						if (!black_list_index.containsKey(idx)) {
							String exon = split_psi[0] + "_" + split_psi[1] + "_" + split_psi[2];							
							out_final.write("\t" + split_psi[split_psi.length - 4].replaceAll("NaN", replaceNaNwithThis));
						}
						idx++;
					}
					in_psi.close();
					out_final.write("\n");
				}
			}
			in.close();
			out_final.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
