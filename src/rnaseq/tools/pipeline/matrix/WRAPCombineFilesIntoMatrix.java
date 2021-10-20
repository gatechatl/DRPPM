package rnaseq.tools.pipeline.matrix;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
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
		return "[inputFileLst] [index-to-grab-filelst] [index-to-grab-matrix-column] [outputFile]";
	}
	
	public static void execute(String[] args) {
		
		try {
			
			String inputFileLst = args[0];
			int index_in_filelst = new Integer(args[1]);
			int index_in_matrix = new Integer(args[2]);
			String outputFile = args[3];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);						
			
			int length = 0;
			
			
			// check whether the files exist... provide a warning if missing and continue processing the rest of the files.
			FileWriter fwriter_newInputFileLst = new FileWriter(inputFileLst + "_tmp");
			BufferedWriter out_newInputFileLst = new BufferedWriter(fwriter_newInputFileLst);
						
			FileInputStream fstream = new FileInputStream(inputFileLst);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				File f = new File(split[index_in_filelst]);
				if (f.exists()) {
					out_newInputFileLst.write(str + "\n");
				} else {
					System.out.println("Skipped... File Missing: " + split[1]);
				}
			}
			in.close();
			out_newInputFileLst.close();
			
			boolean header_flag = true;
			
			fstream = new FileInputStream(inputFileLst + "_tmp");
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				
				String sampleName = split[0];
				String inputFile = split[index_in_filelst];
				
				
				if (header_flag) {
					FileInputStream fstream2 = new FileInputStream(inputFile);
					DataInputStream din2 = new DataInputStream(fstream2);
					BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
					String header = in2.readLine();
					out.write(header.split("\t")[0]);
					while (in2.ready()) {
						String str2 = in2.readLine();
						String[] split2 = str2.split("\t");
						out.write("\t" + split2[0]);
						length++;
					}
					in2.close();
					header_flag = false;
					out.write("\n");
					
				}

				int check_lines = 0;
				StringBuffer buffer = new StringBuffer();
				FileInputStream fstream2 = new FileInputStream(inputFile);
				DataInputStream din2 = new DataInputStream(fstream2);
				BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
				String header = in2.readLine();
				buffer.append(sampleName);
				//out.write(sampleName);
				while (in2.ready()) {
					String str2 = in2.readLine();
					String[] split2 = str2.split("\t");
					//out.write("\t" + split2[index]);
					buffer.append("\t" + split2[index_in_matrix]);
					check_lines++;
				}
				in2.close();
				header_flag = false;
				buffer.append("\n");
				//out.write("\n");				
				if (length == check_lines) {
					out.write(buffer.toString());
				}
			}
			in.close();
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
