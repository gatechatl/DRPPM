package rnaseq.splicing.alternativejuncpipeline.juncsalvager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Summarize the alternative start site across samples
 * @author tshaw
 *
 */
public class SummarizeNovelExonAltStartSiteMatrix {

	public static String type() {
		return "RNApeg";
	}
	public static String description() {
		return "Summarize the alternative start site across samples\n";
	}
	public static String parameter_info() {
		return "[fileList: sampleName[tab]JuncEventPath] [outputBooleanFile] [outputStartSite] [outputTypeProprotion] [outputTypeCount]";
	}
	public static void execute(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			HashMap sampleName2type = new HashMap();
			HashMap sampleName_map = new HashMap();
			HashMap geneName_map = new HashMap();
			HashMap type_count = new HashMap();
			HashMap type_position_count = new HashMap();
			HashMap gene_position = new HashMap();
			String fileList = args[0];
			String outputFile = args[1];
			String outputPosition = args[2];
			String outputTypeProprotion = args[3];
			String outputTypeCount= args[4];
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);

			FileWriter fwriter2 = new FileWriter(outputPosition);
			BufferedWriter out2 = new BufferedWriter(fwriter2);


			FileWriter fwriter3 = new FileWriter(outputTypeProprotion);
			BufferedWriter out3 = new BufferedWriter(fwriter3);

			FileWriter fwriter4 = new FileWriter(outputTypeCount);
			BufferedWriter out4 = new BufferedWriter(fwriter4);
			

			FileInputStream fstream = new FileInputStream(fileList);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String sampleName = split[0];
				
				String type = sampleName.split("0")[0].split("1")[0].split("2")[0].split("3")[0].split("4")[0].split("5")[0].split("6")[0].split("7")[0].split("8")[0].split("9")[0];
				if (type_count.containsKey(type)) {
					int count = (Integer)type_count.get(type);
					count++;
					type_count.put(type, count);
				} else {
					type_count.put(type, 1);
				}
				sampleName2type.put(sampleName, type);
				
				File f = new File(split[1]);
				if (f.exists()) {
					FileInputStream fstream2 = new FileInputStream(split[1]);
					DataInputStream din2 = new DataInputStream(fstream2);
					BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
					in2.readLine();
					while (in2.ready()) {
						String str2 = in2.readLine();
						String[] split2 = str2.split("\t");
						String geneName = split2[0];
						String chr = split2[1];
						String start = split2[2];
						String end = split2[3];
						String direction = split2[4];
						String value = "";
						if (direction.equals("+")) {
							value = chr + ":" + end + ":for";
						} else {
							value = chr + ":" + start + ":rev";
						}
						gene_position.put(geneName + ":" + value, geneName + ":" + value);
						
						if (type_position_count.containsKey(type + ":" + geneName + ":" + value)) {
							int count = (Integer)type_position_count.get(type + ":" + geneName + ":" + value);
							count++;
							type_position_count.put(type + ":" + geneName + ":" + value, count);
						} else {
							type_position_count.put(type + ":" + geneName + ":" + value, 1);
						}
						
						if (map.containsKey(sampleName + "\t" + geneName)) {
							String line = (String)map.get(sampleName + "\t" + geneName);						
							line += "," + value;
							map.put(sampleName + "\t" + geneName, line);
						} else {
							map.put(sampleName + "\t" + geneName, value);
						}
						sampleName_map.put(sampleName, str2);
						geneName_map.put(geneName, geneName);
					}
					in2.close();
				}
			}
			in.close();
			
			
			out.write("GeneName");
			out2.write("GeneName");
			out3.write("GeneName");
			out4.write("GeneName");
			Iterator itr = sampleName_map.keySet().iterator();
			while (itr.hasNext()) {
				String name = (String)itr.next();
				out.write("\t" + name);
				out2.write("\t" + name);
			}
			out.write("\n");
			out2.write("\n");
			
			
			itr = geneName_map.keySet().iterator();
			while (itr.hasNext()) {
				String gene = (String)itr.next();
				out.write(gene);
				out2.write(gene);
				Iterator itr2 = sampleName_map.keySet().iterator();
				while (itr2.hasNext()) {
					String name = (String)itr2.next();
					int value = 0;
					String line = "NA";
					if (map.containsKey(name + "\t" + gene)) {
						line = (String)map.get(name + "\t" + gene);						
						value = 1;
					}
					out.write("\t" + value);
					out2.write("\t" + line);
				}
				out.write("\n");
				out2.write("\n");
			}
			out.close();
			out2.close();
			
			itr = type_count.keySet().iterator();
			while (itr.hasNext()) {
				String type = (String)itr.next();
				out3.write("\t" + type);
				out4.write("\t" + type);
			}
			out3.write("\n");
			out4.write("\n");
			itr = gene_position.keySet().iterator();
			while (itr.hasNext()) {
				String position = (String)itr.next();
				out3.write(position);
				out4.write(position);
				Iterator itr2 = type_count.keySet().iterator();
				while (itr2.hasNext()) {
					String type = (String)itr2.next();
					double total_type_count = (Integer)type_count.get(type);
					String type_position = type + ":" + position;
					double position_count = 0.0;
					if (type_position_count.containsKey(type_position)) {
						position_count = (Integer)type_position_count.get(type_position);
					}
					out3.write("\t" + (position_count / total_type_count));
					out4.write("\t" + position_count);
				}
				out3.write("\n");
				out4.write("\n");
			}
			out3.close();
			out4.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
