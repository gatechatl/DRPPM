package SMPDBAnnotation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Merge gene with metabolite organized into pathways
 * GeneName and HMDB
 * @author tshaw
 *
 */
public class AnnotateSMPDB {

	public static void main(String[] args) {
		
		HashMap pathways = new HashMap();
		try {
			String metabolic_fileName = "C:\\Users\\tshaw\\Desktop\\METABOLOMICS\\NetworkAnalysis\\SMPDB\\metabolites.txt"; //args[0];
			String protein_fileName = "C:\\Users\\tshaw\\Desktop\\METABOLOMICS\\NetworkAnalysis\\SMPDB\\proteins.txt"; // args[1];
			String outputFile = "C:\\Users\\tshaw\\Desktop\\METABOLOMICS\\NetworkAnalysis\\SMPDB\\Pathway_metabolites_geneName.txt";

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileInputStream fstream = new FileInputStream(metabolic_fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));		
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String key = split[2] + "_" + split[1] + "_" + split[0];
				
				if (pathways.containsKey(key)) {
					LinkedList list = (LinkedList)pathways.get(key);
					if (!split[5].equals("")) {
						list.add(split[5]);
						pathways.put(key, list);
					}
				} else {
					LinkedList list = new LinkedList();
					if (!split[5].equals("")) {
						list.add(split[5]);
						pathways.put(key, list);
					}
				}
				
			}
			in.close();
			
			
			fstream = new FileInputStream(protein_fileName);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));		
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String key = split[2] + "_" + split[1] + "_" + split[0];
				if (!key.equals("")) {
					if (pathways.containsKey(key)) {
						LinkedList list = (LinkedList)pathways.get(key);
						if (split.length > 8) {
							list.add(split[8]);
							pathways.put(key, list);
						}
					} else {
						LinkedList list = new LinkedList();
						if (split.length > 8) {
							list.add(split[8]);
							pathways.put(key, list);
						}
					}
				}
			}
			in.close();
			
			Iterator itr = pathways.keySet().iterator();
			while (itr.hasNext()) {
				String pathway_str = (String)itr.next();
				System.out.println(">" + pathway_str);
				out.write(">" + pathway_str + "\n");
				LinkedList list = (LinkedList)pathways.get(pathway_str);
				Iterator itr2 = list.iterator();
				while (itr2.hasNext()) {
					String gene_meta = (String)itr2.next();
					System.out.println(gene_meta);
					out.write(gene_meta + "\n");
				}
			}
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
