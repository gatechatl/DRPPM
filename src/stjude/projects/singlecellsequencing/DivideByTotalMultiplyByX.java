package stjude.projects.singlecellsequencing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class DivideByTotalMultiplyByX {

	public static String type() {
		return "STJUDE";
	}
	public static String parameter_info() {
		return "[inputRawMatrix] [use 1,000,000] [outputFile]";
	}
	public static String description() {
		return "Normalize by total count";
	}
	
	public static void execute(String[] args) {
		
		try {
			
			String inputMatrix = args[0];
			double normalize = new Double(args[1]);
			String outputFile = args[2];
						
			FileWriter fwriter = new FileWriter(outputFile);
            BufferedWriter out = new BufferedWriter(fwriter);
            
			FileInputStream fstream = new FileInputStream(inputMatrix);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");
			double total[] = new double[split_header.length - 1];
			for (int i = 0; i < total.length; i++) {
				total[i] = 0;
			}
			while (in.ready()) {
				String str = in.readLine();				
				String[] split = str.split("\t");
				String geneName = split[0];
				boolean bad = false;
				for (int i = 1; i < split.length; i++) {
					if (split[i].equals("Infinity")) {
						bad = true;
					}
					if (split[i].equals("null")) {
						bad = true;
					}
					if (split[i].equals("NaN")) {
						bad = true;
					}
				}
				if (!bad) {
					for (int i = 1; i < split.length; i++) {
						total[i - 1] += new Double(split[i]);
					}
				}
			}
			in.close();
			
			
			fstream = new FileInputStream(inputMatrix);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			out.write(in.readLine() + "\n");
			//String header = in.readLine();
			//String[] split_header = header.split("\t");			
			while (in.ready()) {
				String str = in.readLine();				
				String[] split = str.split("\t");
				String geneName = split[0];
				out.write(geneName);
				for (int i = 1; i < split.length; i++) {
					out.write("\t" + new Double(split[i]) / total[i - 1] * normalize);
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
