package idconversion.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;


public class GeneralIDConversion {

	public static String description() {
		return "Perform ID conversion based on reference file.";
	}
	public static String parameter_info() {
		return "[inputFile] [refFile] [index_original_name] [index_new_name] [outputFile]";
	}
	public static String type() {
		return "IDConversion";
	}
	public static void execute(String[] args) {
		try {
			String inputFile = args[0];						
			String refFile = args[1];
			int index_original_name = new Integer(args[2]);
			int index_new_name = new Integer(args[3]);
			String outputFile = args[4];
						
			HashMap refseq = new HashMap();
			FileInputStream fstream = new FileInputStream(refFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				refseq.put(split[0], split[1]);
			}
			in.close();
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			HashMap map = new HashMap();
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write(header + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");				
				String original_geneName = split[0];
				if (refseq.containsKey(original_geneName)) {
					String geneName = (String)refseq.get(original_geneName);
					out.write(geneName + "\t" + original_geneName);
					for (int i = 1; i < split.length; i++) {
						out.write("\t" + split[i]);
					}
					out.write("\n");
				} else {
					out.write("NA" + "\t" + original_geneName);
					for (int i = 1; i < split.length; i++) {
						out.write("\t" + split[i]);
					}
					out.write("\n");
				}
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
