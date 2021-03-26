package rnaseq.mapping.tools.star;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class RPM2RPKMTranscript {

	public static String type() {
		return "RNASEQ";
	}
	public static String description() {
		return "RPM conversion to RPKM";
	}
	public static String parameter_info() {
		return "[inputGTFFile] [matrixFile] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputGTFFile = args[0];
			String matrixFile = args[1];
			String outputFile = args[2];
			
			File f = new File(outputFile);
			if (f.exists()) {
				//System.out.println(outputFile + " exists already. Please remove the file before running.");
				//System.exit(0);
			}
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);			
			
			HashMap transcripts = new HashMap();
			int total = 0;
			FileInputStream fstream = new FileInputStream(inputGTFFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				total++;
				String[] split = str.split("\t");
				if (split.length > 8) {
					String type = split[2];
					String geneID = grabMeta(split[8], "gene_id");
					String direction = split[6];
					String transcriptID = grabMeta(split[8], "transcript_id");
					int start = new Integer(split[3]);
					int end = new Integer(split[4]);
					if (type.equals("transcript")) {
						int length = end - start + 1;
						if (transcripts.containsKey(geneID)) {
							int orig_length = (Integer)transcripts.get(geneID);
							if (orig_length < length) {
								transcripts.put(geneID, length);
							}
						} else {
							transcripts.put(geneID, length);
						}
					}
				}
			}
			in.close();
			
			fstream = new FileInputStream(matrixFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write(header + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneID = split[0];
				double length = 0;
				
				if (transcripts.containsKey(geneID)) {
					length = (Integer)transcripts.get(geneID);
					out.write(geneID);
					for (int i = 1; i < split.length; i++) {
						if (length < 100) {
							length = 100;
						}
						double value = new Double(split[i]) * 1000/ length;
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
	/**
	 * Function for grabbing the meta information from the 
	 * @param text
	 * @param id
	 * @return
	 */
	public static String grabMeta(String text, String id) {
		String returnval = "";
		if (text.contains(id)) {
			String[] split = text.split(id);
			if (split.length > 1) {
				String val = split[1].split(";")[0].trim();
				val = val.replaceAll("\"", "");
				val.trim();
				returnval = val;
			}
		}
		return returnval;
	}
}
