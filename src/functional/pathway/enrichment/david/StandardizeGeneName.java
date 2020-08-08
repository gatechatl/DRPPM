package functional.pathway.enrichment.david;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Standardize the geneName based on DAVID's annotation
 * @author tshaw
 *
 */
public class StandardizeGeneName {

	public static String type() {
		return "ENRICHMENT";
	}
	public static String description() {
		return "Standardize the geneName based on DAVID's annotation";
	}
	public static String parameter_info() {
		return "[inputFile] [enrichmentInputFile] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			String inputFile = args[0];
			String enrichmentInputFile = args[1];
			String outputFile = args[2];
			HashMap map = new HashMap();
			HashMap link = new HashMap();
			String lastID = "";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				
				if (link.containsKey(split[0])) {
					LinkedList list = (LinkedList)link.get(split[0]);
					if (!list.contains(split[1])) {
						list.add(split[1]);
					}
					link.put(split[0], list);				
				} else {
					LinkedList list = new LinkedList();
					if (!list.contains(split[1])) {
						list.add(split[1]);
					}
					link.put(split[0], list);
				}
				String firstGene = (String)((LinkedList)link.get(split[0])).get(0);
				map.put(split[1], firstGene);
				lastID = split[0];
			}
			in.close();
			

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			
			fstream = new FileInputStream(enrichmentInputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String type = split[0];
				String[] genes = split[1].split(",");
				HashMap geneList = new HashMap();
				String result = type + "\t";
				for (String gene: genes) {
					if (map.containsKey(gene)) {
						gene = (String)map.get(gene);
						
					}
					if (!geneList.containsKey(gene)) {
						result += gene + ",";
						geneList.put(gene, gene);
					}
				}
				out.write(result + "\n");
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
