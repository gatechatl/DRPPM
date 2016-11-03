package RNAseqTools.SingleCell.CellOfOrigin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class GenerateSIFfromMinimumSpanningTree {
	
	public static String type() {
		return "MATRIX";
	}
	public static String description() {
		return "Generate the sif network file based on MST";
	}
	public static String parameter_info() {
		return "[readMatrixFile] [readKidFile] [outputFile]";
	}
	public static void execute(String[] args) {
		
		
		try {
			String readMatrixFile = args[0];
			String readKidFile = args[1];
			String outputFile = args[2];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);	
			
			FileInputStream fstream = new FileInputStream(readMatrixFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = (String)in.readLine();
			String[] header_split = header.split("\t");
			in.close();
			
			fstream = new FileInputStream(readKidFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			int index = 2;
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				int kid = new Integer(split[1]);
				System.out.println(kid + "\t" + index + "\t" + header_split.length);
				out.write(header_split[kid] + "\tlink\t" + header_split[index] + "\n");
				index++;
			}
			in.close();
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
