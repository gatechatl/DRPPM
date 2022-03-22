package idconversion.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class EnsemblGeneIDAppendAnnotationFlex {

	public static String type() {
		return "IDCONVERSION";
	}
	public static String description() {
		return "EnsemblGeneID2GeneName";
	}
	public static String parameter_info() {
		return "[inputFile] [EnsemblID index] [gtfFile] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			String inputFile = args[0];
			int index = new Integer(args[1]);
			String gtfFile = args[2];
			String outputFile = args[3];
			GTFFile gtf = new GTFFile();
			gtf.initialize(gtfFile);
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write("GeneName\tBiotype\t" + header + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (gtf.geneid2geneName.containsKey(split[index].replaceAll("\"",  ""))) {
					String biotype = (String)gtf.geneid2biotype.get(split[index].replaceAll("\"",  ""));
					String geneName = (String)gtf.geneid2geneName.get(split[index].replaceAll("\"",  ""));
					
					//System.out.println(str + "\t" + geneName);
					out.write(split[0]);
					for (int i = 1; i < split.length; i++) {
						out.write("\t" + split[i]);
					}
					out.write("\t" + geneName + "\t" + biotype);
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
