package expression.matrix.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * For all samples with duplicated sample name, remove the sample
 * @author tshaw
 *
 */
public class RemoveDuplicatedSampleName {


	public static String description() {
		return "Remove samples with duplicated sampleName";
	}
	public static String type() {
		return "EXPRESSION";
	}
	public static String parameter_info() {
		return "[inputFile] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String outputFile = args[1];
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			LinkedList id = new LinkedList();
			String inputFile = args[0]; //"\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\TCGA_Reference\\RPKM\\gene\\download\\c8.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");
			out.write(split_header[0]);
			HashMap map = new HashMap();
			boolean[] contains_id = new boolean[split_header.length];
			for (int i = 1; i < split_header.length; i++) {
				if (map.containsKey(split_header[i])) {
					id.add(i);
				} else {
					map.put(split_header[i], split_header[i]);
				}
				
			}
			for (int i = 1; i < split_header.length; i++) {
			
				if (id.contains(i)) {
					contains_id[i] = true;
				} else {
					contains_id[i] = false;
				}
				if (!contains_id[i]) {
					out.write("\t" + split_header[i]);
					
				}
			}
			out.write("\n");
			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				out.write(split[0]);
				for (int i = 1; i < split.length; i++) {
					
					//if (!id.contains(i)) {
					if (!contains_id[i]) {
						out.write("\t" + split[i]);
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
