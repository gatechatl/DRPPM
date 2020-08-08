package idconversion.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class EnsemblTranscriptID2GeneNameAppendCoord {

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
			out.write(split_header[0] + "\tGeneID\tBiotype\tGeneSymbol\tGeneCoord");
			for (int i = 1; i < split_header.length; i++) {
				out.write("\t" + split_header[i]);
			}
			out.write("\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (gtf.transcript_clean2gene.containsKey(split[0].replaceAll("\"",  "").split("\\.")[0])) {
					String geneID = ((String)gtf.transcript_clean2gene.get(split[0].replaceAll("\"",  "").split("\\.")[0]));
					String biotype = (String)gtf.geneid2biotype.get(geneID.replaceAll("\"",  ""));
					String geneName = ((String)gtf.geneid2geneName.get(geneID.replaceAll("\"",  "")));
					String coord = ((String)gtf.geneid2coord.get(geneID.replaceAll("\"", "")));
					//System.out.println(str + "\t" + geneName);
					out.write(split[0] + "\t" + geneID + "\t" + biotype + "\t" + geneName + "\t" + coord);
					//System.out.println(str + "\t" + geneName);
					//out.write(split[0] + "\t" + geneName);
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
