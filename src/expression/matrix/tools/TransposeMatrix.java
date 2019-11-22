package expression.matrix.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;

public class TransposeMatrix {

	public static String description() {
		return "Transpose the matrix (flip the matrix).";
	}
	public static String type() {
		return "EXPRESSION";
	}
	public static String parameter_info() {
		return "[inputFile] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String outputFile = args[1];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			LinkedList lines = new LinkedList();
			String new_sample_header = "GeneSets";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				new_sample_header += "\t" + split[0];
				lines.add(str);
			}
			out.write(new_sample_header + "\n");
			for (int i = 1; i < split_header.length; i++) {
				out.write(split_header[i]);
				Iterator itr = lines.iterator();
				while (itr.hasNext()) {
					String line = (String)itr.next();
					String[] split = line.split("\t");
					out.write("\t" + split[i]);
				}
				out.write("\n");
			}
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
