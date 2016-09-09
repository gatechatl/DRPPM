package RNAseqTools.Mapping;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class CombineHTSEQResult {

	public static String description() {
		return "Combine HTSEQ Result into a matrix table (normalized to RPM)";
	}
	public static String type() {
		return "RNASEQMAPPING";
	}
	public static String parameter_info() {
		return "[bamFileList] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String bamFileList = args[0];
			String outputFile = args[1];			
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);			
			
			HashMap total_count_map = new HashMap();
			HashMap map = new HashMap();
			HashMap file_map = new HashMap();
			FileInputStream fstream = new FileInputStream(bamFileList);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				File f = new File(str);
				String fileName = f.getName().replaceAll(".bam", "");
				String new_fileName = "counts." + fileName + ".txt";
				file_map.put(fileName, fileName);
				FileInputStream fstream2 = new FileInputStream(new_fileName);
				DataInputStream din2 = new DataInputStream(fstream2);
				BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
				while (in2.ready()) {
					String str2 = in2.readLine();
					String[] split = str2.split("\t");
					if (map.containsKey(split[0])) {
						HashMap count = (HashMap)map.get(split[0]);
						
						count.put(fileName, new Double(split[1]));
						map.put(split[0], count);
						
						if (total_count_map.containsKey(fileName)) {
							double read = (Double)total_count_map.get(fileName);
							read += new Double(split[1]);
							total_count_map.put(fileName, read);
						} else {
							double read = 0;
							read += new Double(split[1]);
							total_count_map.put(fileName, read);
						}
					} else {
						HashMap count = new HashMap();
						count.put(fileName, new Double(split[1]));
						map.put(split[0], count);
						
						if (total_count_map.containsKey(fileName)) {
							double read = (Double)total_count_map.get(fileName);
							read += new Double(split[1]);
							total_count_map.put(fileName, read);
						} else {
							double read = 0;
							read += new Double(split[1]);
							total_count_map.put(fileName, read);
						}
					}
					
				}
				in2.close();
				
			}
			in.close();
			
			out.write("Gene");
			Iterator itr = file_map.keySet().iterator();
			while (itr.hasNext()) {
				String file = (String)itr.next();
				out.write("\t" + file);
			}
			out.write("\n");
			itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String geneName = (String)itr.next();
				out.write(geneName);
				HashMap count = (HashMap)map.get(geneName);
				Iterator itr2 = file_map.keySet().iterator();
				while (itr2.hasNext()) {
					String file = (String)itr2.next();
					double total_count = (Double)total_count_map.get(file);
					double value = (Double)count.get(file);
					out.write("\t" + new Double(value) * 1000000/ total_count);
				}
				out.write("\n");
			}
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
