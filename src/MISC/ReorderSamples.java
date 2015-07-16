package MISC;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class ReorderSamples {

	
	public static void execute(String[] args) {
		
		try {
			String clusterOrderFile = args[1]; //"C:\\Users\\tshaw\\Desktop\\RNASEQ\\Mckinnon\\FPKM_05272014\\HGG_Cluster_Order.txt";
			String inputFile = args[0]; //"C:\\Users\\tshaw\\Desktop\\RNASEQ\\Mckinnon\\FPKM_05272014\\SJMMHGGNORM_RNAseq_Exon_Read_Count_gene_fpkm_uniq.txt";
			String outputFile = args[2]; //"C:\\Users\\tshaw\\Desktop\\RNASEQ\\Mckinnon\\FPKM_05272014\\SJMMHGGNORM_RNAseq_Exon_Read_Count_gene_fpkm_uniq_reorder.txt";
			
			LinkedList reversereflist = new LinkedList();
			
			FileInputStream fstream = new FileInputStream(clusterOrderFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				reversereflist.add(str.trim());
			}
			in.close();
			
			LinkedList reflist = new LinkedList();
			Iterator itr = reversereflist.iterator();
			while (itr.hasNext()) {
				String key = (String)itr.next();
				reflist.add(key);
			}
			//reflist = reversereflist; // under reverse
			
			int size = reflist.size();
			LinkedList geneList = new LinkedList();
			HashMap map = new HashMap();
			HashMap mapID = new HashMap();
			for (int i = 1; i <= size; i++) {
				geneList = new LinkedList();
				LinkedList list = new LinkedList();
				
				//fileName = "C:\\Users\\tshaw\\Desktop\\RNASEQ\\Mckinnon\\GSEA_05282014\\GSEA_SJMMHGGNORM_RNAseq_Exon_Read_Count_gene_fpkm_uniq.txt";
				fstream = new FileInputStream(inputFile);
				din = new DataInputStream(fstream);
				in = new BufferedReader(new InputStreamReader(din));
				String[] splitTitle = in.readLine().split("\t");
				
				while (in.ready()) {
					String str = in.readLine();
					String[] split = str.split("\t");
					for (int j = 1; j < splitTitle.length; j++) {
						if (!reflist.contains(splitTitle[j])) {
							System.out.println(splitTitle[j]);
						}
						map.put(splitTitle[j].trim() + "_" + split[0], split[j]);
					}
					//list.add(split[i]);
					geneList.add(split[0]);
					//System.out.println(split[0]);
				}
				in.close();
				
			}
			
			//String outputFile = "C:\\Users\\tshaw\\Desktop\\RNASEQ\\Mckinnon\\GSEA_05282014\\GSEA_SJMMHGGNORM_RNAseq_Exon_Read_Count_gene_fpkm_uniq_reorder.txt";
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("GeneSet");
			String[] fileNames = new String[reflist.size()];
			int i = 0;
			Iterator itrRef = reflist.iterator();
			while (itrRef.hasNext()) {
				
				fileNames[i] = (String)itrRef.next();
				out.write("\t" + fileNames[i]);
				i++;
			}
			out.write("\n");
			
			Iterator itrGeneName = geneList.iterator();
			while (itrGeneName.hasNext()) {
				String geneName = (String)itrGeneName.next();
				out.write(geneName);
				for (i = 0; i < fileNames.length; i++) {
					String fpkm = (String)map.get(fileNames[i].trim() + "_" + geneName);
					out.write("\t" + fpkm);
				}
				out.write("\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}

