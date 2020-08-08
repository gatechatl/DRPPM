package proteomics.phospho.kinaseactivity.pipeline;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class ReorderIkapColumn {

	public static String type() {
		return "KINASEACTIVITY";
	}
	public static String description() {
		return "Reorder the column.";
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
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");
			String new_header = split_header[0] + "\t" + split_header[split_header.length - 1];
			for (int i = 1; i < split_header.length - 1; i++) {
				new_header += "\t" + split_header[i];
			}
			
			out.write(new_header + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String new_str = split[0] + "\t" + split[split.length - 1];
				for (int i = 1; i < split.length - 1; i++) {
					new_str += "\t" + split[i];
				}
				out.write(new_str + "\n");
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
