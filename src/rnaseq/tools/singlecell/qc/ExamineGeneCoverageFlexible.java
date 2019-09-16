package rnaseq.tools.singlecell.qc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class ExamineGeneCoverageFlexible {
	public static String type() {
		return "SingleCellQC";
	}
	public static String description() {
		return "Examining the number of genes that is being expressed in gmt file that pass cutoff";
	}
	public static String parameter_info() {
		return "[inputFile] [gmtFile] [cutoff] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String gmtFile = args[1];
			double cutoff = new Double(args[2]);
			String outputFile = args[3];
			
			HashMap map = new HashMap();
			FileInputStream fstream = new FileInputStream(gmtFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String name = split[0];
				for (int i = 2; i < split.length; i++) {
					if (map.containsKey(name)) {
						LinkedList list = (LinkedList)map.get(name);
						list.add(split[i]);
						map.put(name, list);
					} else {
						LinkedList list = new LinkedList();
						list.add(split[i]);
						map.put(name, list);
					}
				}
			}
			in.close();
			
			FileWriter fwriter = new FileWriter(outputFile);
            BufferedWriter out = new BufferedWriter(fwriter);            
            
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write(header + "\n");
			String[] split_header = header.split("\t");
			double[] all_count = new double[split_header.length];
			for (int i = 0; i < all_count.length; i++) {
				all_count[i] = 0;
			}
			int total_hit = 0;
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				boolean haveExpr = false;
				for (int i = 1; i < split.length; i++) {
					
					if (new Double(split[i]) > cutoff) {
						all_count[i]++;
					}
					haveExpr = true;
				}
				if (haveExpr) {
					total_hit++;
				}
			}
			in.close();
			
			out.write("AllGenes" + "(n=" + total_hit + ")");
			for (int i = 1; i < all_count.length; i++) {
				out.write("\t" + all_count[i]);
			}
			out.write("\n");
			
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String name = (String)itr.next();
				LinkedList list = (LinkedList)map.get(name);
				 
				fstream = new FileInputStream(inputFile);
				din = new DataInputStream(fstream);
				in = new BufferedReader(new InputStreamReader(din));
				header = in.readLine();
				split_header = header.split("\t");			
				double[] count = new double[split_header.length];
				for (int i = 0; i < count.length; i++) {
					count[i] = 0;
				}
				total_hit = 0;
				while (in.ready()) {
					String str = in.readLine();
					String[] split = str.split("\t");					
					if (list.contains(split[0])) {
						for (int i = 1; i < split.length; i++) {
							if (new Double(split[i]) > cutoff) {
								count[i]++;
							}
						}
						total_hit++;
					}
					
				}
				in.close();
				out.write(name + "(n=" + total_hit + ")");
				for (int i = 1; i < count.length; i++) {
					out.write("\t" + count[i]);
				}
				out.write("\n");
				
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
