package idconversion.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class kgXrefAppendOfficialGeneSymbol {

	public static String description() {
		return "Append official gene symbol";
	}
	public static String type() {
		return "IDCONVERSION";
	}
	public static String parameter_info() {
		return "[fileName] [kgXrefFile] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String fileName = args[0];
			String kgXrefFile = args[1];
			String outputFile = args[2];
			
			HashMap kgXref = new HashMap();
			FileInputStream fstream = new FileInputStream(kgXrefFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				split[0] = split[0].split("\\.")[0];
				if (kgXref.containsKey(split[0])) {
					String exist = (String)kgXref.get(split[0]);
					if (exist.contains("Rik")) {
						kgXref.put(split[0], split[4]);
					}
				} else {
					kgXref.put(split[0], split[4]);
				}
			}
			in.close();
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);

			fstream = new FileInputStream(fileName);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				split[0] = split[0].split("\\.")[0];
				if (kgXref.containsKey(split[0])) {
					out.write(kgXref.get(split[0]) + "\t" + str);
					/*for (int i = 1; i < split.length; i++) {
						out.write("\t" + split[i]);
					}*/
					out.write("\n");
				} else {
					out.write("NA" + "\t" + str);
					/*for (int i = 1; i < split.length; i++) {
						out.write("\t" + split[i]);
					}*/
					out.write("\n");
					//System.out.println("Missing: " + split[0]);
				}
			}
			in.close();
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
