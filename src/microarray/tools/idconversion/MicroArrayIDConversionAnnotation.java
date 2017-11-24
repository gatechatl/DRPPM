package microarray.tools.idconversion;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class MicroArrayIDConversionAnnotation {

	public static String type() {
		return "MICROARRAY";
	}
	public static String description() {
		return "Convert the probe id to geneName";
	}
	public static String parameter_info() {
		return "[inputFile] [annotationtFile] [probe_index] [geneName_index] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String annotationtFile = args[1];
			int probe_index = new Integer(args[2]);
			int geneName_index = new Integer(args[3]);
			String outputFile = args[4];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			HashMap id_conversion = new HashMap();
			
			FileInputStream fstream = new FileInputStream(annotationtFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\",\"");
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
