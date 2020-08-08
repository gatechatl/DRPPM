package integrate.OverRepresentationAnalysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class SummaryTable_ErinSchuetz {

	public static void main(String[] args) {
		
		try {
			HashMap map = new HashMap();
			HashMap pathway_gene = new HashMap();
			File f = new File("C:\\Users\\tshaw\\Desktop\\METABOLOMICS\\Project\\ErinSchuetz\\ORA_Analysis\\");
			File[] files = f.listFiles();
			LinkedList allFile = new LinkedList();
			
			String outputFile = "C:\\Users\\tshaw\\Desktop\\METABOLOMICS\\Project\\ErinSchuetz\\Report\\Summary_Pathway_Hit.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("PathwayName");
			for (File file: files) {
				
				String fileName = file.getPath();
				String name = file.getName();
				allFile.add(name);
				out.write("\t" + name);
				FileInputStream fstream = new FileInputStream(fileName);
				DataInputStream din = new DataInputStream(fstream);
				BufferedReader in = new BufferedReader(new InputStreamReader(din));
				in.readLine();
				while (in.ready()) {
					String str = in.readLine();					
					String[] split = str.split("\t");
					double pvalue = new Double(split[1]);
					if (!map.containsKey(split[0])) {
						LinkedList list = new LinkedList();
						map.put(split[0], list);
						LinkedList met_gene = new LinkedList();						
						pathway_gene.put(split[0], met_gene);
					}
					if (pvalue < 0.05 && split[2].equals("YES")) {
						LinkedList list = (LinkedList)map.get(split[0]);
						list.add(name);
						LinkedList met_gene_list = new LinkedList(); 
						String[] met_genes = split[3].split(",");
						for (String met_gene: met_genes) {
							if (!met_gene_list.contains(met_gene)) {
								met_gene_list.add(met_gene);
							}
						}
						map.put(split[0], list);
						pathway_gene.put(split[0], met_gene_list);
					}
				}
				in.close();
			}
			out.write("\tNumDisregulated\tGeneOrMetabolite\n");
			
			Iterator itr2 = map.keySet().iterator();
			while (itr2.hasNext()) {
				String pathwayName = (String)itr2.next();
				
				LinkedList geneList = (LinkedList)pathway_gene.get(pathwayName);
				String items = "";
				Iterator itr4 = geneList.iterator();
				while (itr4.hasNext()) {
					String met_gene = (String)itr4.next();
					if (items.equals("")) {
						items = met_gene;
					} else {
						items += "," + met_gene;
					}
				}
				out.write(pathwayName);
				int total = 0;
				Iterator itr = allFile.iterator();
				while (itr.hasNext()) {
					String name = (String)itr.next();
					LinkedList list = (LinkedList)map.get(pathwayName);
					boolean find = false;
					if (list.contains(name)) {
						find = true;
						total++;
					}
					out.write("\t" + find);
				}
				out.write("\t" + total + "\t" + items + "\n");
			}
			out.close();
		} catch (Exception e) {
			
		}
	}
}
