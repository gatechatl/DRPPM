package EnrichmentTool.DAVID;

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
 * Generate the GO enrichment database based on DAVID's annotation
 * @author tshaw
 *
 */
public class GenerateGODatabaseDAVID {

	public static String type() {
		return "ENRICHMENT";
	}
	public static String description() {
		return "Generate a GO database based on the geneset downloaded from DAVID";
	}
	public static String parameter_info() {
		return "[geneSymbolFile] [geneSetFile] [outputFile] [outputFolder]";
	}
	public static void execute(String[] args) {
		
		try {			
			
			String geneSymbolFile = args[0];
			String geneSetFile = args[1];
			String outputFile = args[2];
			String outputFolder = args[3];
			//String outputFile = args[2];
			HashMap geneSymbol = new HashMap();
			HashMap geneSet = new HashMap();
			
			FileInputStream fstream = new FileInputStream(geneSymbolFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (!geneSymbol.containsKey(split[0])) {
					geneSymbol.put(split[0], split[1]);
				}				
			}
			in.close();
			
			fstream = new FileInputStream(geneSetFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String pathwayName = split[1].replaceAll("\\/", "_").replaceAll("\\~", "_");
				String geneName = (String)geneSymbol.get(split[0]);
				if (geneSet.containsKey(pathwayName)) {
					LinkedList list = (LinkedList)geneSet.get(pathwayName);
					if (!list.contains(geneName)) {
						list.add(geneName);
					}
					geneSet.put(pathwayName, list);					
				} else {
					LinkedList list = new LinkedList();
					if (!list.contains(geneName)) {
						list.add(geneName);
					}
					geneSet.put(pathwayName, list);
				}
			}
			in.close();
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			Iterator itr = geneSet.keySet().iterator();
			while (itr.hasNext()) {
				String pathwayName = (String)itr.next();
				String pathwayFile = outputFolder + "/" + pathwayName + ".txt";
				out.write(pathwayName + "\t" + pathwayFile + "\n");
				
				FileWriter fwriter2 = new FileWriter(pathwayFile);
				BufferedWriter out2 = new BufferedWriter(fwriter2);
				out2.write(">" + pathwayName + "\n");
				LinkedList list = (LinkedList)geneSet.get(pathwayName);
				Iterator itr2 = list.iterator();
				while (itr2.hasNext()) {
					String geneName = (String)itr2.next();
					out2.write(geneName + "\n");
				}
				out2.close();
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
