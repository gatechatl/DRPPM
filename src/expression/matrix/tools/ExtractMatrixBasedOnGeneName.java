package expression.matrix.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class ExtractMatrixBasedOnGeneName {


	public static String type() {
		return "EXPRESSION";
	}
	public static String description() {
		return "Generate a matrix based on the gene list for generating heatmap. Note that it skips the first line in the genelist File";
	}
	public static String parameter_info() {
		return "[matrixFile] [pathwayFile] [geneSymbolIndex] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			String matrixFile = args[0];
			String pathwayFile = args[1];
			int geneSymbolIndex = new Integer(args[2]);
			String outputFile = args[3];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileInputStream fstream = new FileInputStream(pathwayFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String pathwayName = in.readLine();
			LinkedList list2 = new LinkedList();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				/*String pathwayName = split[1];
				String[] genes = split[4].split(",");
				LinkedList list = new LinkedList();
				for (String gene: genes) {
					list.add(gene);
				}*/
				list2.add(split[0].toUpperCase());
			}
			in.close();
			map.put(pathwayName, list2);
			HashMap data_matrix = new HashMap();
			
			fstream = new FileInputStream(matrixFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			out.write(in.readLine() + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (list2.contains(split[geneSymbolIndex])) {
					out.write(str + "\n");
				}
				
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
