package idconversion.ensembl;

import idconversion.tools.GTFFile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class MicroarrayEnsembl2GeneName {

	public static String type() {
		return "IDCONVERSION";
	}
	public static String description() {
		return "Microarray ensembl conversion to geneName";
	}
	public static String parameter_info() {
		return "[inputFile] [entrezFile] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			String inputFile = args[0];
			String gtfFile = args[1];
			String outputFile = args[2];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			GTFFile gtf = new GTFFile();
			gtf.initialize(gtfFile);
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write("GeneName" + "\t" + header + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String ensembl = split[0].split("\\.")[0];
				String geneName = "NA";
				if (gtf.geneid2geneName.containsKey(ensembl)) {
					geneName = (String)gtf.geneid2geneName.get(ensembl);
				}
				out.write(geneName + "\t" + str + "\n");
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static HashMap ensembl2geneName(String inputFile) {
		HashMap map = new HashMap();
		try {

			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split[5].contains("Ensembl:")) {
					String geneID = split[8].split("gene_id:")[1].split("\\|")[0];
					map.put(geneID, split[2]);
				}
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
}
