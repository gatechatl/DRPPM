package IDConversion.Ensembl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class GenerateEnsembl2GeneNameTable {

	public static String parameter_info() {
		
		return "[entrezFile] [peptideFile] [outputFile]";
	}
	
	public static void execute(String[] args) {
		
		try {
			String entrezFile = args[0];
			String ensemblPeptideFasta = args[1];
			String outputFile = args[2];
			HashMap ensembl2geneName = ensembl2geneName(entrezFile);
			HashMap protein2geneID = protein2geneID(ensemblPeptideFasta);
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("EnsemblProteinID\tEnsemblGeneID\tGeneName\n");
			Iterator itr = protein2geneID.keySet().iterator();
			while (itr.hasNext()) {
				String protein_id = (String)itr.next();
				String geneID = (String)protein2geneID.get(protein_id);
				if (ensembl2geneName.containsKey(geneID)) {
					String geneName = (String)ensembl2geneName.get(geneID);
					out.write(protein_id + "\t" + geneID + "\t" + geneName + "\n");
				}
				
			}
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
					String geneID = split[5].split("Ensembl:")[1].split("\\|")[0];
					map.put(geneID, split[2]);
				}
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	public static HashMap protein2geneID(String inputFile) {
		HashMap map = new HashMap();
		try {
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains(">")) {					
					String protein = str.split("\\.")[0].replaceAll(">", "");
					String geneName = str.split("gene:")[1].split(" ")[0].split("\\.")[0];
					//System.out.println(protein + "\t" + geneName);
					map.put(protein, geneName);
				} 
				
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}
