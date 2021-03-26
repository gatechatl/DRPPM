package rnaseq.tools.pipeline.htseqexon;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 * 
 * @author gatechatl
 *
 */
public class WRAPCombineFilesIntoMatrix {

	
	public static String type() {
		return "RNASEQ";
	}
	public static String description() {
		return "WRAP combine pipeline output as a matrix.";
	}
	public static String parameter_info() {
		return "[inputFileLst] [index-to-grab] [outputFile]";
	}
	
	public static void execute(String[] args) {
		
		try {
			
			String inputFileLst = args[0];
			int index = new Integer(args[1]);
			String outputFile = args[2];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);						
			
			boolean header_flag = true;
			FileInputStream fstream = new FileInputStream(inputFileLst);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				
				String sampleName = split[0];
				String inputFile = split[index];
				
				if (header_flag) {
					FileInputStream fstream2 = new FileInputStream(inputFile);
					DataInputStream din2 = new DataInputStream(fstream2);
					BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
					String header = in2.readLine();
					out.write(header.split("\t")[0]);
					while (in2.ready()) {
						String str2 = in.readLine();
						String[] split2 = str2.split("\t");
						out.write("\t" + split2[0]);					
					}
					in2.close();
					header_flag = false;
					out.write("\n");
				}

				FileInputStream fstream2 = new FileInputStream(inputFile);
				DataInputStream din2 = new DataInputStream(fstream2);
				BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
				String header = in2.readLine();
				out.write(sampleName);
				while (in2.ready()) {
					String str2 = in.readLine();
					String[] split2 = str2.split("\t");
					out.write("\t" + split2[index]);					
				}
				in2.close();
				header_flag = false;
				out.write("\n");				
				
			}
			in.close();
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
