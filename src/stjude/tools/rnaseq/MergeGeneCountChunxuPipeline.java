package stjude.tools.rnaseq;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;


public class MergeGeneCountChunxuPipeline {

	public static String type() {
		return "STJUDE";
	}
	public static String description() {
		return "Merge the gene count for Chunxu's count data";
	}
	public static String parameter_info() {
		return "[inputFile] [annotation_types: example core,extended] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String[] annotation_types = args[1].split(",");
			String outputFile = args[2];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);

			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] header_split = header.split("\t");
			HashMap data_map = new HashMap();
			out.write("GeneName");
			for (int i = 10; i < header_split.length; i++) {
				out.write("\t" + header_split[i]);
				data_map.put(header_split[i], new HashMap());
			}
			out.write("\n");
			
			HashMap geneName_map = new HashMap();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneName = split[4];
				String annotation = split[5];
				boolean match_annotation = false;
				for (String annotation_type: annotation_types) {
					if (annotation_type.toUpperCase().equals(annotation.toUpperCase())) {
						match_annotation = true;
					}
				}
				if (match_annotation) {
					
					for (int i = 10; i < split.length; i++) {
						geneName_map.put(geneName, geneName);
						HashMap sample_data_map = (HashMap)data_map.get(header_split[i]);
						if (sample_data_map.containsKey(geneName)) {
							double prev_count = (Double)sample_data_map.get(geneName);
							prev_count += new Double(split[i]);
							sample_data_map.put(geneName, prev_count);
						} else {
							sample_data_map.put(geneName, new Double(split[i]));
						}
						data_map.put(header_split[i], sample_data_map);
						//out.write("\t" + split[i]);
					}
				}
			}
			in.close();
			Iterator itr = geneName_map.keySet().iterator();
			while (itr.hasNext()) {
				String geneName = (String)itr.next();
				out.write(geneName);
				for (int i = 10; i < header_split.length; i++) {
					HashMap sample_data_map = (HashMap)data_map.get(header_split[i]);
					double count = (Double)sample_data_map.get(geneName);
					out.write("\t" + count);
					//out.write("\t" + header_split[i]);
					//data_map.put(header_split[i], new HashMap());
				}
				out.write("\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
