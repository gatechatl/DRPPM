package misc;

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
 * For genes with multiple expression value, take the mean or median expression value
 * @author tshaw
 *
 */
public class MergeGeneNameMAXFast {


	public static String type() {
		return "MISC";
	}
	public static String description() {
		return "Reduce the duplicated values.";
	}
	public static String parameter_info() {
		return "[InputFile] [OutputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			String inputFile = args[0]; 
			String outputFile = args[1]; 
						
			HashMap map = new HashMap();
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneName = split[0];
				LinkedList values = new LinkedList();
				for (int i = 1; i < split.length; i++) {
					values.add(new Double(split[i]));
				}
				double avg = MathTools.mean(MathTools.convertListDouble2Double(values));
				if (map.containsKey(geneName)) {
					double prev_avg = (Double)map.get(geneName);
					if (prev_avg < avg) {
						map.put(geneName, avg);
					}
				} else {
					map.put(geneName, avg);
				}
			}
			in.close();
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
					
			HashMap write_gene_once = new HashMap();
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			out.write(header + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneName = split[0];
				LinkedList values = new LinkedList();
				for (int i = 1; i < split.length; i++) {
					values.add(new Double(split[i]));
				}
				double avg = MathTools.mean(MathTools.convertListDouble2Double(values));
				double highest_avg = (Double)map.get(geneName);
				if (highest_avg == avg) {
					if (!write_gene_once.containsKey(geneName)) {
						out.write(str + "\n");
					}
					write_gene_once.put(geneName, geneName);
				}
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
}
