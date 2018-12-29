package rnaseq.mapping.tools.cufflinks;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class ExtractFPKM {

	public static void execute(String[] args) {
		try {
			
			String outputFile = args[0];
			int colNum = new Integer(args[1]);
			int geneIDNum = new Integer(args[2]);
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
						
			String[] inputFiles = getRemaining(args, 3);
			String[] sampleNames = new String[inputFiles.length];
			HashMap[] map = new HashMap[inputFiles.length];
			for (int i = 0; i < inputFiles.length; i++) {
				map[i] = new HashMap();
			}
			HashMap geneNameMap = new HashMap();
			for (int i = 0; i < inputFiles.length; i++) {
				String[] split2 = inputFiles[i].split(":");
				sampleNames[i] = split2[0];
				String inputFile = split2[1];
				FileInputStream fstream = new FileInputStream(inputFile);
				DataInputStream din = new DataInputStream(fstream);
				BufferedReader in = new BufferedReader(new InputStreamReader(din));
				in.readLine();
				while (in.ready()) {
					String str = in.readLine();	
					String[] split = str.split("\t");
					String geneName = split[geneIDNum];
					String fpkm = split[colNum];
					map[i].put(geneName, fpkm);			
					geneNameMap.put(geneName, geneName);
				}
				in.close();
			}
			out.write("Gene");
			for (int i = 0; i < inputFiles.length; i++) {
				//out.write("\tSAMPLE" + (i + 1));
				out.write("\t" + sampleNames[i]);
			}
			out.write("\n");
			Iterator itr = geneNameMap.keySet().iterator();
			while (itr.hasNext()) {
					String geneName = (String)itr.next();
					out.write(geneName);
					for (int i = 0; i < inputFiles.length; i++) {										
						String fpkm = (String)map[i].get(geneName);
						out.write("\t" + fpkm);
					}
					out.write("\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String[] getRemaining(String[] args, int x) {
			String[] args_remain = new String[args.length - x]; 
			for (int i = x; i < args.length; i++) {
				args_remain[i - x] = args[i];
			}
			return args_remain;
	}
}
