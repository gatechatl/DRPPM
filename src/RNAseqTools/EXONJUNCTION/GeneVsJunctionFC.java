package RNAseqTools.EXONJUNCTION;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class GeneVsJunctionFC {

	public static String parameter_info() {
		return "[geneDiffFile] [exonDiffFile] [outputFileConflict]";
	}
	public static void execute(String[] args) {
		
		try {

			String geneAllFile = args[0];
			String exonAllFile = args[1];
			String outputFileConflict = args[2];			
			HashMap gene_map = new HashMap();
			
			FileInputStream fstream = new FileInputStream(geneAllFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneName = split[0].replaceAll("\"", "");
				gene_map.put(geneName, split[1]);
			}
			in.close();
			
			FileWriter fwriter = new FileWriter(outputFileConflict);
			BufferedWriter out = new BufferedWriter(fwriter);

			fstream = new FileInputStream(exonAllFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write("Junction\tJunctionFC\tGeneFC\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String exonJuncName = split[0].replaceAll("\"", "");
				String geneName = exonJuncName.split("_")[exonJuncName.split("_").length - 1];
				if (gene_map.containsKey(geneName)) {
					String foldChange = (String)gene_map.get(geneName);
					out.write(exonJuncName + "\t" + split[1] + "\t" + foldChange + "\n");
				}
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
