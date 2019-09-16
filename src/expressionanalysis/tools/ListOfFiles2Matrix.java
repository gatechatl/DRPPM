package expressionanalysis.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Generate a matrix from a list of files
 * Example: /rgs01/project_space/zhanggrp/AltSpliceAtlas/common/analysis/BALL_Mullighan
 * @author tshaw
 *
 */
public class ListOfFiles2Matrix {

	public static String description() {
		return "Generate a matrix from a list of files";
	}
	public static String type() {
		return "EXPRESSION";
	}
	public static String parameter_info() {
		return "[htseq_list_of_files] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			HashMap matrix = new HashMap();
			HashMap geneName_map = new HashMap();
			LinkedList sampleName_map = new LinkedList();
			String htseq_list_of_files = args[0];
			String outputFile = args[1];

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileInputStream fstream = new FileInputStream(htseq_list_of_files);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String name = in.readLine();
				sampleName_map.add(name);
				/*FileInputStream fstream2 = new FileInputStream(name);
				DataInputStream din2 = new DataInputStream(fstream2);
				BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
				while (in2.ready()) {
					String str2 = in2.readLine();
					String[] split = str2.split("\t");
					matrix.put(name + "\t" + split[0], split[1]);
					geneName_map.put(split[0], split[0]);
				}
				in2.close();
				*/				
			}
			in.close();
			FileInputStream[] fstream2 = new FileInputStream[sampleName_map.size()];
			DataInputStream[] din2 = new DataInputStream[sampleName_map.size()];
			BufferedReader[] in2 = new BufferedReader[sampleName_map.size()];
			
			int i = 0;
			out.write("GeneName");			
			
			Iterator itr2 = sampleName_map.iterator();
			while (itr2.hasNext()) {
				String sampleName = (String)itr2.next();
				out.write("\t" + sampleName);				
				fstream2[i] = new FileInputStream(sampleName);
				din2[i] = new DataInputStream(fstream2[i]);
				in2[i] = new BufferedReader(new InputStreamReader(din2[i]));
				if (i == 0) {
					FileInputStream fstream3 = new FileInputStream(sampleName);
					DataInputStream din3 = new DataInputStream(fstream3);
					BufferedReader in3 = new BufferedReader(new InputStreamReader(din3));
					while (in3.ready()) {
						String str2 = in3.readLine();
						String[] split2 = str2.split("\t");
						geneName_map.put(split2[0], split2[0]);
					}
					in3.close();
				}
				i++;
				
			}
			out.write("\n");
								
			
			Iterator itr = geneName_map.keySet().iterator();
			while (itr.hasNext()) {
				String geneName = (String)itr.next();
				out.write(geneName);
				i = 0;
				itr2 = sampleName_map.iterator();
				while (itr2.hasNext()) {
					String sampleName = (String)itr2.next();
					String str = in2[i].readLine();
					String[] split = str.split("\t");
					
					out.write("\t" + split[1]);					
					i++;
				}
				out.write("\n");
			}
			out.close();
			i = 0;
			itr2 = sampleName_map.iterator();
			while (itr2.hasNext()) {
				itr2.next();
				in2[i].close();
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
