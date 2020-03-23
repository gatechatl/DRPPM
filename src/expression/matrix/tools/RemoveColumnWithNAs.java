package expression.matrix.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class RemoveColumnWithNAs {
	public static String description() {
		return "Remove column with nulls.";
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
			
			HashMap map = new HashMap();
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();	
			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				for (int i = 0; i < split.length; i++) {
					if (split[i].equals("NA")) {
						map.put(i, i);
					}
				}
			}
			in.close();
			

			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();	
			String[] split_header = header.split("\t");
			out.write(split_header[0]);
			for (int i = 1; i < split_header.length; i++) {
				if (!map.containsKey(i)) {
					out.write("\t" + split_header[i]);
				}
			}
			out.write("\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				out.write(split[0]);
				for (int i = 1; i < split.length; i++) {
					if (!map.containsKey(i)) {
						out.write("\t" + split[i]);
					}
				}
				out.write("\n");
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
