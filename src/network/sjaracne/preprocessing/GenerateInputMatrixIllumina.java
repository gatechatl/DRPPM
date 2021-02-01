package network.sjaracne.preprocessing;

import idconversion.cross_species.HumanMouseGeneNameConversion;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Convert a gene symbol matrix to aracne Illumina based matrix
 * @author gatechatl
 *
 */
public class GenerateInputMatrixIllumina {
	
	public static String type() {
		return "ARACNE";
	}
	public static String description() {
		return "Convert a gene symbol matrix to aracne Illumina based matrix format.";
	}
	public static String parameter_info() {
		return "[inputFile geneSymbol Matrix] [Illumina ID conversion file] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String hs2mmFile = args[1]; //"C:\\Users\\tshaw\\Desktop\\RNASEQ\\hs_mm_homo_r66.txt";
			String outputFile = args[2];
			

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			HashMap geneSymbol2Illumina = HumanMouseGeneNameConversion.mouse2human(hs2mmFile); // hijacking a previous method for mouse 2 human conversion...
			
			String inputFile = args[0]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\GangGeneList\\GangGeneListAnalysis.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");
			out.write("isoformId\tgeneSymbol");
			for (int i = 1; i < split_header.length; i++) {
				out.write("\t" + split_header[i]);
			}
			out.write("\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				
				String data = "";
				for (int i = 1; i < split.length; i++) {
					data += "\t" + split[i];
				}
				if (geneSymbol2Illumina.containsKey(split[0])) {
					out.write(geneSymbol2Illumina.get(split[0]) + "\t" + split[0] + data + "\n");
					
				}				
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
