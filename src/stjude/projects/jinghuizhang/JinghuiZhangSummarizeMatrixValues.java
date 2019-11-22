package stjude.projects.jinghuizhang;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import statistics.general.MathTools;

/**
 * Summarize the gene expression or features values to cancer subtypes
 * @author tshaw
 *
 */
public class JinghuiZhangSummarizeMatrixValues {
	public static String description() {
		return "Summarize matrix values based on SJ Disease Type";
	}
	public static String type() {
		return "JinghuiZhang";
	}
	public static String parameter_info() {
		return "[inputMatrix] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			HashMap type_map = new HashMap();

			String inputMatrix = args[0];
			String outputFile = args[1];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);			
			
			FileInputStream fstream = new FileInputStream(inputMatrix);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");
			for (int i = 1; i < split_header.length; i++) {
				String type = split_header[i].split("0")[0].split("0")[0].split("1")[0].split("2")[0].split("3")[0].split("4")[0].split("5")[0].split("6")[0].split("7")[0].split("8")[0].split("9")[0];
				type_map.put(type, type);
			}
			
			out.write("Feature");
			Iterator itr = type_map.keySet().iterator();
			while (itr.hasNext()) {
				String type = (String)itr.next();
				if (type.substring(0,2).equals("SJ")) {
					out.write("\t" + type);
				}
			}
			out.write("\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				out.write(split[0]);
				HashMap type2values = new HashMap();
				
				for (int i = 1; i < split_header.length; i++) {
					String type = split_header[i].split("0")[0].split("0")[0].split("1")[0].split("2")[0].split("3")[0].split("4")[0].split("5")[0].split("6")[0].split("7")[0].split("8")[0].split("9")[0];
					if (type2values.containsKey(type)) {
						LinkedList values = (LinkedList)type2values.get(type);
						values.add(new Double(split[i]));
						type2values.put(type, values);						
					} else {
						LinkedList values = new LinkedList();
						values.add(new Double(split[i]));
						type2values.put(type, values);
					}
				}
				
				itr = type_map.keySet().iterator();
				while (itr.hasNext()) {
					String type = (String)itr.next();
					if (type.substring(0,2).equals("SJ")) {
						LinkedList values = (LinkedList)type2values.get(type);
						double avg = MathTools.mean(MathTools.convertListDouble2Double(values));
						out.write("\t" + avg);
					}
				}
				out.write("\n");
			}
			in.close();
			out.close();

			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
