package idconversion.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class EnsemblGeneID2GeneNameXenograft {

	public static String type() {
		return "IDCONVERSION";
	}
	public static String description() {
		return "EnsemblGeneID2GeneName";
	}
	public static String parameter_info() {
		return "[inputFile] [gtfFile] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			String inputFile = args[0];
			String gtfFile = args[1];
			String outputFile = args[2];
			GTFFile gtf = new GTFFile();
			gtf.initialize(gtfFile);
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write("Species\t" + header + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String species = "NA";
				if (gtf.geneid2geneName.containsKey(split[0].replaceAll("\"",  ""))) {
					if (split[0].contains("ENSG")) {
						species = "HUMAN";
					} else if (split[0].contains("ENSMUSG")) {
						species = "MOUSE";
					}
					String geneName = (String)gtf.geneid2geneName.get(split[0].replaceAll("\"",  ""));
					
					//System.out.println(str + "\t" + geneName);
					out.write(species + "\t" + geneName + "");
					for (int i = 1; i < split.length; i++) {
						out.write("\t" + split[i]);
					}
					out.write("\n");
				} else {
					System.out.println("missing: " + str);
				}
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
