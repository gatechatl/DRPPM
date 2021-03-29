package rnaseq.tools.qc.star_2_7_1a;

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
 * Extracting the QC output from the STAR *final.out files
 * @author gatechatl
 *
 */
public class ExtractQCMetricsSTAR271a {

	public static String type() {
		return "STAR";
	}
	public static String description() {
		return "Extract the QC output from the STAR *final.out files";
	}
	public static String parameter_info() {
		return "[inputFileList] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String outputFile = args[1];
			FileWriter fwriter = new FileWriter(outputFile);
		    BufferedWriter out = new BufferedWriter(fwriter);
		    out.write("Sample");
		    HashMap map = new HashMap();
		    LinkedList list = new LinkedList();
		    boolean first = true;
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream); 
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String sampleName = split[0];
				String fileName = split[1];
				File f = new File(fileName);
				if (f.exists()) {
					FileInputStream fstream2 = new FileInputStream(fileName);
					DataInputStream din2 = new DataInputStream(fstream2); 
					BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
					in2.readLine();
					in2.readLine();
					in2.readLine();
					in2.readLine();
					in2.readLine();
					while (in2.ready()) {
						String str2 = in2.readLine();
						str2 = str2.trim();
						str2 = str2.replaceAll(" ", "").replaceAll("\t", "");
						String[] split2 = str2.split("\\|");
						if (split2.length == 2) {
							if (first) {
								list.add(split2[0]);
							}
							System.out.println(split2[0] + "\t" + split2[1]);
							map.put(split2[0], split2[1]);
						}
					}
					in2.close();
					
					if (first) {
						Iterator itr = list.iterator();
						while (itr.hasNext()) {
							String tag = (String)itr.next();
							String value = (String)map.get(tag);
							out.write("\t" + tag);
						}
						out.write("\n");
						
					}
					first = false;
					out.write(sampleName);
					Iterator itr = list.iterator();
					while (itr.hasNext()) {
						String tag = (String)itr.next();
						String value = (String)map.get(tag);
						out.write("\t" + value);
					}
					out.write("\n");
				}
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
