package rnaseq.tools.singlecell.htseq;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class HTSEQMergeCountFiles {

	public static String type() {
		return "HTSEQ";
	}
	public static String description() {
		return "Merge the count file from htseq into a single matrix";
	}
	public static String parameter_info() {
		return "[inputFileLIst] [outputMatrixFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String outputFile = args[1];

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			out.write("GeneName");
			LinkedList file_list = new LinkedList();
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				file_list.add(str.trim());
				out.write("\t" + str.trim());
			}
			in.close();			
			out.write("\n");
			
			String first_fileName = (String)file_list.get(0);
			HashMap map = new HashMap();
			fstream = new FileInputStream(first_fileName);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(split[0], split[0]);
			}
			in.close();			
			
			Iterator itr = file_list.iterator();
			while (itr.hasNext()) {
				String fileName = (String)itr.next();
				FileInputStream fstream2 = new FileInputStream(fileName);
				DataInputStream din2 = new DataInputStream(fstream2);
				BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));			
				while (in2.ready()) {
					String str = in2.readLine();
					String[] split = str.split("\t");
					String get_orig = (String)map.get(split[0]);
					get_orig = get_orig + "\t" + split[1];
					map.put(split[0], get_orig);
				}			
				in2.close();
			}
			
			itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String geneName = (String)itr.next();
				String line = (String)map.get(geneName);
				out.write(line + "\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
