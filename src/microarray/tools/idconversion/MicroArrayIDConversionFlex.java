package microarray.tools.idconversion;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class MicroArrayIDConversionFlex {

	public static String type() {
		return "MICROARRAY";
	}
	public static String description() {
		return "Convert the probe id to geneName";
	}
	public static String parameter_info() {
		return "[inputFile] [annotationtFile] [probe_index] [geneName_index] [split type: tab, comma, semicolon] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String annotationtFile = args[1];
			int probe_index = new Integer(args[2]);
			int geneName_index = new Integer(args[3]);
			String split_type = args[4];
			String split_str = ",";
			if (split_type.toUpperCase().equals("TAB")) {
				split_str = "\t";
			} else if (split_type.toUpperCase().equals("COMMA")) {
				split_str = ",";
			} else if (split_type.toUpperCase().equals("SEMICOLON")) {
				split_str = ";";
			} else {
				System.out.println("Need to define split type: TAB, COMMA, SEMICOLON");
				System.out.println("drppm -MicroArrayIDConversionFlex " + parameter_info());
				System.exit(0);
			}
			String outputFile = args[5];			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			HashMap id_conversion = new HashMap();
			
			FileInputStream fstream = new FileInputStream(annotationtFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();				
				
				String[] split = str.split(split_str);
				if (split.length > probe_index && split.length > geneName_index) {
					String probe = split[probe_index].replaceAll("\"", "");
					String geneName = split[geneName_index].replaceAll("\"", "");
					id_conversion.put(probe, geneName);
				}
			}
			in.close();

			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write(header + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (id_conversion.containsKey(split[0])) {
					String geneName = (String)id_conversion.get(split[0]);
					out.write(geneName);
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
