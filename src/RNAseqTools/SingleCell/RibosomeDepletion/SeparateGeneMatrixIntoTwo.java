package RNAseqTools.SingleCell.RibosomeDepletion;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Given a gene list file provide a matrix of the genes and another matrix without the genes
 * @author tshaw
 *
 */
public class SeparateGeneMatrixIntoTwo {

	public static String description() {
		return "Given a gene list file provide a matrix of the genes and another matrix without the genes";
	}
	public static String type() {
		return "SINGLECELL";
	}
	public static String parameter_info() {
		return "[inputFile] [geneListFile] [outputWithGeneMatrix] [outputWithoutGeneMatrix]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String geneListFile = args[1];
			String outputWithGeneMatrix = args[2];
			String outputWithoutGeneMatrix = args[3];
			
			FileWriter fwriter = new FileWriter(outputWithGeneMatrix);
			BufferedWriter out = new BufferedWriter(fwriter);	
			
			FileWriter fwriter2 = new FileWriter(outputWithoutGeneMatrix);
			BufferedWriter out2 = new BufferedWriter(fwriter2);	
			
			HashMap map = new HashMap();
			FileInputStream fstream = new FileInputStream(geneListFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();				
				String[] split = str.split("\t");
				map.put(split[0], split[0]);
			}
			in.close();
			
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write(header + "\n");
			out2.write(header + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (map.containsKey(split[0])) {
					out.write(str + "\n");
				} else {
					out2.write(str + "\n");
				}
			}
			in.close();
			out.close();
			out2.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
