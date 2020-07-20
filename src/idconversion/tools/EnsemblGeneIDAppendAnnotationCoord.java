package idconversion.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class EnsemblGeneIDAppendAnnotationCoord {

	public static String type() {
		return "IDCONVERSION";
	}
	public static String description() {
		return "Append the geneName biotype and coordinate to the matrix based on the ensembl gene id.";
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
			out.write("GeneName\tBiotype\tCoord\t" + header + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (gtf.geneid2geneName.containsKey(split[0].replaceAll("\"",  ""))) {
					String biotype = (String)gtf.geneid2biotype.get(split[0].replaceAll("\"",  ""));
					String geneName = (String)gtf.geneid2geneName.get(split[0].replaceAll("\"",  ""));
					String coord = (String)gtf.geneid2coord.get(split[0].replaceAll("\"", ""));
					
					//System.out.println(str + "\t" + geneName);
					out.write(geneName + "\t" + biotype + "\t" + coord);
					for (int i = 0; i < split.length; i++) {
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
