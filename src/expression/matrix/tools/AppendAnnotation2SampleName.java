package expression.matrix.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class AppendAnnotation2SampleName {

	public static String description() {
		return "Append the second column information to sampleName.";
	}
	public static String type() {
		return "EXPRESSION";
	}
	public static String parameter_info() {
		return "[inputFile] [metaInformation] [outputFile]";
	}
	
	public static void execute(String[] args) {
		
		try {
			String fpkm_file = args[0];
			String metaFile = args[1];			
			String outputFile = args[2];
			
	    	FileWriter fwriter = new FileWriter(outputFile);
	        BufferedWriter out = new BufferedWriter(fwriter);
	        
	        HashMap map = new HashMap();
			FileInputStream fstream = new FileInputStream(metaFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(split[0], split[1]);
			}
			
			FileInputStream fstream2 = new FileInputStream(fpkm_file);
			DataInputStream din2 = new DataInputStream(fstream2);
			BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
			String header = in2.readLine();
			String[] headers = header.split("\t");
			out.write(headers[0]);
			for (int i = 1; i < headers.length; i++) {
				if (map.containsKey(headers[i])) {
					String meta = (String)map.get(headers[i]);
					out.write("\t" + headers[i] + "_" + meta);
				}
			}
			out.write("\n");
			while (in2.ready()) {
				String str = in2.readLine();
				out.write(str + "\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
			
	
}
