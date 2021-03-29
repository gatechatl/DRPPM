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
public class WRAPCombineRNAseQCFilesIntoMatrix {

	
	public static String type() {
		return "RNASEQ";
	}
	public static String description() {
		return "WRAP combine pipeline output as a matrix.";
	}
	public static String parameter_info() {
		return "[inputFileLst] [outputFile]";
	}
	
	public static void execute(String[] args) {
		
		try {
			
			String inputFileLst = args[0];			
			String outputFile = args[1];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);						
			out.write("SampleName\tTIN(mean)\tTIN(median)\tTIN(stdev)\tTotalSplicingReads\tKnownSplicingReads\tPartialNovelSplicingReads\tNovelSplicingReads\tFilteredSplicingReads\tTotalSplicingJunctions\tKnownSplicingJunctions\tPartialNovelSplicingJunction\tNovelSplicingJunctions\n");
			int length = 0;
			
			boolean header_flag = true;
			FileInputStream fstream = new FileInputStream(inputFileLst);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				
				String sampleName = split[0];
				String inputTINFile = split[1];
				String inputJunctionAnnotationFile = split[2];

				int check_lines = 0;
				StringBuffer buffer = new StringBuffer();
				FileInputStream fstream2 = new FileInputStream(inputTINFile);
				DataInputStream din2 = new DataInputStream(fstream2);
				BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
				String header = in2.readLine();
				buffer.append(sampleName);
				
				while (in2.ready()) {
					String str2 = in2.readLine();
					out.write(str2);
				}
				in2.close();
				
				System.out.println(inputJunctionAnnotationFile);
				fstream2 = new FileInputStream(inputJunctionAnnotationFile);
				din2 = new DataInputStream(fstream2);
				in2 = new BufferedReader(new InputStreamReader(din2));
				header = in2.readLine();
				header = in2.readLine();
				header = in2.readLine();
				header = in2.readLine();
				header = in2.readLine();
				String totalSplicingReads = in2.readLine().split("\t")[1];
				out.write("\t" + totalSplicingReads);
				String knownSplicingReads = in2.readLine().split("\t")[1];
				out.write("\t" + knownSplicingReads);
				String partialNovelSplicingReads = in2.readLine().split("\t")[1];
				out.write("\t" + partialNovelSplicingReads);
				String novelSplicingReads = in2.readLine().split("\t")[1];
				out.write("\t" + novelSplicingReads);
				String filteredSplicingReads = in2.readLine().split("\t")[1];
				out.write("\t" + filteredSplicingReads);
				in2.readLine();
				String totalSplicingJunctions = in2.readLine().split("\t")[1];
				out.write("\t" + totalSplicingJunctions);
				String knownSplicingJunctions = in2.readLine().split("\t")[1];
				out.write("\t" + knownSplicingJunctions);
				String partialNovelSplicingJunctions = in2.readLine().split("\t")[1];
				out.write("\t" + partialNovelSplicingJunctions);
				String novelSplicingJunctions = in2.readLine().split("\t")[1];
				out.write("\t" + novelSplicingJunctions);				
				in2.close();
				out.write("\n");
			}
			in.close();
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
