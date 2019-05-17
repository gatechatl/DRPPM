package idconversion.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class EnsemblGeneIDAppendGeneName {

	public static String type() {
		return "IDCONVERSION";
	}
	public static String description() {
		return "EnsemblGeneIDAppendGeneName ";
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
			String[] split_header = header.split("\t");
			out.write(split_header[0] + "\tGeneSymbol");
			for (int i = 1; i < split_header.length; i++) {
			out.write("\t" + header);
			}
			out.write("\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (gtf.geneid2geneName.containsKey(split[0].replaceAll("\"",  ""))) {
					String biotype = (String)gtf.geneid2biotype.get(split[0].replaceAll("\"",  ""));
					String geneName = (String)gtf.geneid2geneName.get(split[0].replaceAll("\"",  ""));
					
					//System.out.println(str + "\t" + geneName);
					out.write(split[0] + "\t" + geneName);
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
