package customScript;

import idconversion.tools.GTFFile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;

public class AppendChromosomeNumber {

	public static String type() {
		return "CUSTOM";
	}
	public static String description() {
		return "AppendChromosomeNumber";
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
				if (gtf.geneName2geneID.containsKey(split[0].replaceAll("\"",  ""))) {
					
					LinkedList geneNameList = (LinkedList)gtf.geneName2geneID.get(split[0].replaceAll("\"",  ""));				
					//System.out.println(str + "\t" + geneName);
					Iterator itr = geneNameList.iterator();
					String coord = "";
					String geneNames = "";
					while (itr.hasNext()) {
						String geneName = (String)itr.next();
						coord = (String)gtf.geneid2coord.get(geneName);
						/*for (int i = 1; i < split.length; i++) {
							out.write("\t" + split[i]);
						}*/
						//out.write("\n");
					}
					if (!coord.contains("chr")) {
						coord = "chr" + coord;
					}
					out.write(str + "\t" + coord + "\n");
				} else {
					out.write(str + "\tnull\n");
				}
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
