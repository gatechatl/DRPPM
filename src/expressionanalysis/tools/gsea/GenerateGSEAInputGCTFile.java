package expressionanalysis.tools.gsea;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * 
 * @author tshaw
 *
 */
public class GenerateGSEAInputGCTFile {

	public static String parameter_info() {
		return "[matrixFile] [output_gctFile] [output_groupMetaFile]";
	}
	public static void execute(String[] args) {
		try {
			String inputFile = args[0];
			String gctOutputFile = args[1];
			String metaOutputFile = args[2];
			generateGroupFile(inputFile, metaOutputFile);
			generateGCTMatrix(inputFile, gctOutputFile);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void generateGroupFile(String inputFile, String outputFile) {
		
		try {
			
			LinkedList list = new LinkedList();
			
        	FileWriter fwriter = new FileWriter(outputFile);
            BufferedWriter out = new BufferedWriter(fwriter);
            
        
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			String header = in.readLine();
			String[] split = header.split("\t");
			int num_samples = split.length - 1;
			for (int i = 1; i < split.length; i++) {
				out.write(split[i] + "\tMETA1\n");				
			}	
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * InputFile is assumed to have first column is geneName and first row is sampleName
	 * @param inputFile
	 */
	public static void generateGCTMatrix(String inputFile, String outputFile) {
		try {
			
			LinkedList list = new LinkedList();
			
        	FileWriter fwriter = new FileWriter(outputFile);
            BufferedWriter out = new BufferedWriter(fwriter);
            
            String first_row = "NAME\tDESCRIPTION";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			String header = in.readLine();
			String[] split = header.split("\t");
			int num_samples = split.length - 1;
			for (int i = 1; i < split.length; i++) {
				first_row += "\t" + split[i];
				
			}
			while (in.ready()) {
				String str = in.readLine();
				split = str.split("\t");
				String line = split[0].replaceAll("\"", "") + "\t" + split[0].replaceAll("\"", "");
				for (int i = 1; i < split.length; i++) {
					line += "\t" + split[i];
				}
				list.add(line);				
			}
			in.close();
			out.write("#1.2\n");
			out.write(list.size() + "\t" + num_samples + "\n");
			out.write(first_row + "\n");
			Iterator itr = list.iterator();
			while (itr.hasNext()) {
				String line = (String)itr.next();
				out.write(line + "\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
