package expression.matrix.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class KeepColumnsFromMatrix {

	public static String description() {
		return "Keep specific column from a matrix";
	}
	public static String type() {
		return "EXPRESSION";
	}
	public static String parameter_info() {
		return "[inputFile] [keyTerm] [outputFile]";
	}
	public static void execute(String[] args) {
				
		try {

			String inputFile = args[0];
			String[] keyTerms = args[1].split(",");
			String outputFile = args[2];

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");
			out.write(split_header[0]);
			for (int i = 1 ;i < split_header.length; i++) {
				for (String keyTerm: keyTerms) {
					if (split_header[i].contains(keyTerm)) { 
						out.write("\t" + split_header[i]);
					}
				}
			}
			out.write("\n");
			while (in.ready()) {
				String str = in.readLine();
				String line = "";
				String[] split = str.split("\t");
				for (int i = 0 ;i < split.length; i++) {
					boolean hit = false;
					//for (String idx: idxes) {
					//	if (new Integer(idx) == i) {
					//		hit = true;
					//	}
					//}
					for (String keyTerm: keyTerms) {
						if (split_header[i].contains(keyTerm) || i == 0) { 
							hit = true;
						}
					}
					if (hit) {
						if (line.equals("")) {
							line += split[i];
						} else {
							line += "\t" + split[i];
						}
					}
				}
				out.write(line + "\n");
				//System.out.println(line);
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
