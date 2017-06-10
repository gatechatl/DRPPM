package expressionanalysis.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class RemoveColumnsFromMatrix {

	public static String description() {
		return "Remove specific column from a matrix";
	}
	public static String type() {
		return "EXPRESSION";
	}
	public static String parameter_info() {
		return "[inputFile] [column number starting from 0: 1,2 ] [outputFile]";
	}
	public static void execute(String[] args) {
				
		try {

			String inputFile = args[0];
			String[] idxes = args[1].split(",");
			String outputFile = args[2];

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String line = "";
				String[] split = str.split("\t");
				for (int i = 0 ;i < split.length; i++) {
					boolean hit = false;
					for (String idx: idxes) {
						if (new Integer(idx) == i) {
							hit = true;
						}
					}
					if (!hit) {
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
