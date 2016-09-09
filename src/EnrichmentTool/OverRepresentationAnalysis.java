package EnrichmentTool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;

import Statistics.General.MathTools;

public class OverRepresentationAnalysis {
	public static String dependencies() {
		return "NONE";
	}
	public static String type() {
		return "ENRICHMENT";
	}
	
	public static String description() {
		return "Perform over representation analysis using the specified gene set";
	}
	
	public static String parameter_info() {
		return "[inputFile] [geneSetFile] [comprehensiveGeneListFile] [restrict to geneSet true/false] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String geneSetFile = args[1];
			String comprehensiveGeneListFile = args[2];
			String flag = args[3];
			String outputFile = args[4];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);						
			out.write("ClusterName\tGeneSetName\tPval\tFoldEnrichment\tGeneSet\tHit#\tGeneSet#\tModGene#\tTotal#\n");
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din)); 
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");				
				String cluster_name = split[0];
				String geneListStr = split[1];
				String[] geneListSplit = geneListStr.split(",");
				String buffer = UUID.randomUUID().toString();
				FileWriter fwriter2 = new FileWriter(buffer);
				BufferedWriter out2 = new BufferedWriter(fwriter2);
				out2.write(cluster_name + "\n");
				for (int i = 0; i < geneListSplit.length; i++) {
					if (!geneListSplit[i].equals("")) {
						out2.write(geneListSplit[i].toUpperCase() + "\n");
					}
				}
				out2.close();
				boolean restrict = false;
				if (flag.equals("true")) {
					restrict = true;
				}
				LinkedList list = generateEnrichmentTable(buffer, geneSetFile, comprehensiveGeneListFile, restrict);
				File f = new File(buffer);
				if (f.exists()) {
					f.delete();
				}
				
				HashMap raw_map = new HashMap();
				HashMap sort_map = new HashMap();
				int num = 1;
				Iterator itr = list.iterator();							
				while (itr.hasNext()) {
					String line = (String)itr.next();
					String[] split2 = line.split("\t");
					int hit = new Integer(split2[4]);
					System.out.println(split2[0] + "\t" + split2[1] + "\t" + split2[4]);
					if (hit > 0) {
						double pvalue = new Double(split2[1]);
						raw_map.put(num, line);
						sort_map.put(num, pvalue);					
						num++;			
					}
				}			
				sort_map = (HashMap) MapUtilsSmall2Big.sortByValue(sort_map);
				itr = sort_map.keySet().iterator();
				while (itr.hasNext()) {
					num = (Integer)itr.next();
					String line = (String)raw_map.get(num);
					out.write(cluster_name + "\t" + line + "\n");
				}
				
				
			}
			in.close();
			
			out.close();
									
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static LinkedList generateEnrichmentTable(String inputFile, String geneSetFile, String comprehensiveGeneFile, boolean restrict) {
		LinkedList result = new LinkedList();
		try {
			
			HashMap module_gene = new HashMap();
			HashMap comprehensive_gene_list = new HashMap();
			
			FileInputStream fstream = new FileInputStream(comprehensiveGeneFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din)); 
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");				
				comprehensive_gene_list.put(split[0].replaceAll("\"",  "").toUpperCase(), split[0].replaceAll("\"",  ""));			
			}
			in.close();
			
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din)); 
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (comprehensive_gene_list.containsKey(split[0].toUpperCase())) {
					module_gene.put(split[0].toUpperCase(), split[0].toUpperCase());	
				} else {
					//System.out.println("Missing in module gene list: " + str);
				}
				
			}
			in.close();
			
			HashMap uniq_gene = new HashMap();
			HashMap geneList = new HashMap();
			fstream = new FileInputStream(geneSetFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din)); 
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneSetName = split[0].toUpperCase();
				String geneSetPathFile = split[1];

				FileInputStream fstream2 = new FileInputStream(geneSetPathFile);
				DataInputStream din2 = new DataInputStream(fstream2);
				BufferedReader in2 = new BufferedReader(new InputStreamReader(din2)); 
				while (in2.ready()) {
					String str2 = in2.readLine().toUpperCase();
					if (comprehensive_gene_list.containsKey(str2)) {
						if (geneList.containsKey(geneSetName)) {
							LinkedList list = (LinkedList)geneList.get(geneSetName);
							list.add(str2);
							geneList.put(geneSetName, list);
							uniq_gene.put(str2, str2);
						} else {
							LinkedList list = new LinkedList();
							list.add(str2);
							geneList.put(geneSetName, list);
							uniq_gene.put(str2, str2);
						}
					} else {
						if (str2.length() < 30) {
							//System.out.println("Missing in geneSet: " + str2);
						}
					}					
				} 
				in2.close();
			}
			in.close();
			
			int total = 0;
			if (restrict) {
				total = uniq_gene.size();
			} else {
				total = comprehensive_gene_list.size();
			}
			Iterator itr = geneList.keySet().iterator();
			while (itr.hasNext()) {
				String geneListName = (String)itr.next();
				LinkedList list = (LinkedList)geneList.get(geneListName);
				double pvalue = calculatePvalue(module_gene, list, total);
				double enrichment = calculateFoldEnrichment(module_gene, list, total);
				int num_hit = calculateHit(module_gene, list, total);						
				String hits = calculateHit(module_gene, list);
				if (!geneListName.equals("")) {
					result.add(geneListName + "\t" + pvalue + "\t" + enrichment + "\t" + hits + "\t" + num_hit + "\t" + list.size() + "\t" + module_gene.size() + "\t" + total);
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static String calculateHit(HashMap module_map, LinkedList geneSet) {

		Iterator itr = module_map.keySet().iterator();
		
		String hit = "";
		while (itr.hasNext()) {
			String geneName = (String)itr.next();					
			if (geneSet.contains(geneName)) {
				if (hit.equals("")) {
					hit += geneName;
				} else {
					hit += "," + geneName;
				}
			} 
		}
				
		return hit;
 
	}
	public static int calculateHit(HashMap module_map, LinkedList geneSet, int total) {

		Iterator itr = module_map.keySet().iterator();
		int hit = 0;				
		while (itr.hasNext()) {
			String geneName = (String)itr.next();					
			if (geneSet.contains(geneName)) {
				hit++;
			} 
		}
		int a = hit;
		int b = geneSet.size() - hit;
		int c = module_map.size() - hit;
		int d = total - b - c - a;				
		return hit;
	}	
	public static double calculateFoldEnrichment(HashMap module_map, LinkedList geneSet, int total) {

		Iterator itr = module_map.keySet().iterator();
		int hit = 0;				
		while (itr.hasNext()) {
			String geneName = (String)itr.next();					
			if (geneSet.contains(geneName)) {
				hit++;
			} 
		}
		/*int a = hit;
		int b = geneSet.size() - hit;
		int c = module_map.size() - hit;
		int d = total - a - b - c;*/
		
		double ratio_a = new Double(hit) / module_map.size();
		double ratio_b = new Double(geneSet.size()) / total;				
		return ratio_a / ratio_b;
 
	}	
	public static double calculatePvalue(HashMap module_map, LinkedList geneSet, int total) {

		Iterator itr = module_map.keySet().iterator();
		int hit = 0;				
		while (itr.hasNext()) {
			String geneName = (String)itr.next();					
			if (geneSet.contains(geneName)) {
				hit++;
			} 
		}
		int a = hit;
		int b = geneSet.size() - hit;
		int c = module_map.size() - hit;
		int d = total - a - b - c;
		double fisher_pvalue = MathTools.fisherTest(a, b, c, d);
		return fisher_pvalue;
 
	}
	
}
