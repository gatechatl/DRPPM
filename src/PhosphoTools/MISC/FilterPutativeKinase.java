package PhosphoTools.MISC;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;


/**
 * A specialized program for filtering kinase that can phosphorylate the substrate 
 * @author tshaw
 *
 */
public class FilterPutativeKinase {

	public static void execute(String[] args) {
		
		try {
			String fileName = args[0]; //Hong_Final_List.txt
			int col_index = new Integer(args[1]);
			String correlFile = args[2];
			String motifFile = args[3];
			double cutoff = new Double(args[4]);			
			String outputFile = args[5];
			
			FileWriter fwriter = new FileWriter(outputFile);
            BufferedWriter out = new BufferedWriter(fwriter);
            
			HashMap spearman = gene_spearman(correlFile);
			HashMap motif_info = map_motif_geneName(motifFile);
			
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String motifs_str = split[col_index];
				String[] motifs = motifs_str.split(";");
				
				HashMap geneList = new HashMap();
				// for each motif grab the gene list then examine their spearman rank
				for (String motif: motifs) {
					System.out.println(motif);
					if (motif.split(",").length >= 2) {
						String[] motif_split = motif.split(",");
						String motif_name = motif_split[1]; 
						for (int i = 2; i < motif_split.length; i++) {
							motif_name += "," + motif_split[i];
						}
						if (motif_info.containsKey(motif_name)) {
							String genes_str = (String)motif_info.get(motif_name);
							String[] genes = genes_str.split(",");
							for (String gene: genes) {
								if (spearman.containsKey(gene)) {
									double rho = new Double((String)spearman.get(gene));
									if (rho >= 0.8) {
										geneList.put(gene, gene);
									}
								}
							}
						} else {
							System.out.println(motif_name + " not found");
						}
					}
				} // for each motif
				String final_gene = "";
				Iterator itr = geneList.keySet().iterator();
				int count = 0;
				while (itr.hasNext()) {
					String key = (String)itr.next();
					if (count == 0) {
						final_gene = key;
					} else {
						final_gene += "," + key;
					}
					count++;
				}
				out.write(str + "\t" + final_gene + "\n");
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static HashMap gene_spearman(String fileName) {
		HashMap map = new HashMap();
		try {
			
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(split[1], split[3]);
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	public static HashMap map_motif_geneName(String fileName) {
		HashMap map = new HashMap();
		try {
			
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(split[0], split[1]);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}
