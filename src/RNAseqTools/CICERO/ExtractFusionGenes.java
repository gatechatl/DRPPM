package RNAseqTools.CICERO;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class ExtractFusionGenes {

	public static String type() {
		return "CICERO";
	}
	public static String description() {
		return "Extract fusion gene";
	}
	public static String parameter_info() {
		return "[inputFile] [geneFile] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String geneFile = args[1];
			String outputFile = args[2];						
			
			HashMap map = new HashMap();
			FileWriter fwriter = new FileWriter(outputFile);
            BufferedWriter out = new BufferedWriter(fwriter);

            HashMap gene = new HashMap();
			FileInputStream fstream = new FileInputStream(geneFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				gene.put(str, str);
			}
			in.close();
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String gene1 = split[3];
				String gene2 = split[8];
				if (gene.containsKey(gene1) || gene.containsKey(gene2)) {
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
