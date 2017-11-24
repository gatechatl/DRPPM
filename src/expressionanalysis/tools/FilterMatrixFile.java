package expressionanalysis.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Filters file based on gene name (the first column).
 * @author tshaw
 *
 */
public class FilterMatrixFile {

	public static String type() {
		return "EXPRESSION";
	}
	public static String description() {
		return "Filters file based on gene name (the first column)";
	}
	public static String parameter_info() {
		return "[inputFile] [geneListFile] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String geneListFile = args[1];
			String outputFile = args[2];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			HashMap map = new HashMap();
			FileInputStream fstream = new FileInputStream(geneListFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String geneName = str.split("\t")[0].toUpperCase().replaceAll("\"", "");
				map.put(geneName, geneName);
			}
			in.close();
			
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			out.write(header + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneName = split[0].replaceAll("\"", "").toUpperCase();
				if (map.containsKey(geneName)) {
					out.write(str + "\n");
				} else {
					System.out.println("Missing geneName: " + geneName);
				}
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
