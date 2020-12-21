package idconversion.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class CleanEnsemblGeneID2GeneName {

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
			out.write(header + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (gtf.geneid_clean2geneName.containsKey(split[0].replaceAll("\"",  "").split("\\.")[0])) {
					String geneName = (String)gtf.geneid_clean2geneName.get(split[0].replaceAll("\"",  "").split("\\.")[0]);
					
					//System.out.println(str + "\t" + geneName);
					out.write(geneName + "");
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
