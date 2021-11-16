package protein.features.ensembl.membranebound;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Extract membrane bound proteins from ensembl pep.fasta
 * @author 4472414
 *
 */
public class EnsemblProteinExtractMembraneProteins {

	public static String type() {
		return "PROTEINFEATURE";
	}
	public static String description() {
		return "Extract membrane bound proteins from ensembl pep.fasta";
	}
	public static String parameter_info() {
		return "[inputFasta] [geneSymbols] [ouutputFasta]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFasta = args[0];
			String geneSymbols = args[1];
			String ouutputFasta = args[2];

			FileWriter fwriter = new FileWriter(ouutputFasta);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			HashMap map = new HashMap();
			FileInputStream fstream = new FileInputStream(geneSymbols);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(split[0],  split[0]);
			}
			in.close();
			
			boolean hit = false;
			fstream = new FileInputStream(inputFasta);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains(">")) {
					if (str.contains("gene_symbol:")) {
						String gene_symbol = str.split("gene_symbol:")[1].split(" ")[0];
						if (map.containsKey(gene_symbol)) {
							hit = true;
							out.write(str + "\n");
						} else {
							hit = false;
						}
					} else {
						hit = false;
					}
				} else {
					if (hit) {
						out.write(str + "\n");
					}
				}
				
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	}
}
