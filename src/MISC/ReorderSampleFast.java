package MISC;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;

public class ReorderSampleFast {
	

	public static String description() {
		return "Reordering the samples given a list of sampleNames.";
	}
	public static String type() {
		return "MISC";
	}

	public static String parameter_info() {
		return "[inputMatrixFile] [clusterOrderFile] [outputFile]";
	}
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
			
			// not sure why I'm doing this...
			LinkedList refList = new LinkedList();
			Iterator itr = reversereflist.iterator();
			while (itr.hasNext()) {
				String key = (String)itr.next();
				refList.add(key.replaceAll("_", ".").replaceAll("-", "."));
				
			}
			

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("GeneName");
			
			//reflist = reversereflist; // under reverse
			//fileName = "C:\\Users\\tshaw\\Desktop\\RNASEQ\\Mckinnon\\GSEA_05282014\\GSEA_SJMMHGGNORM_RNAseq_Exon_Read_Count_gene_fpkm_uniq.txt";
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String[] splitTitle = in.readLine().replaceAll("_", ".").replaceAll("-", ".").split("\t");
			LinkedList order = new LinkedList();
			
			for (int i = 1; i < splitTitle.length; i++) {
				if (refList.contains(splitTitle[i])) {
					out.write("\t" + splitTitle[i]);
					order.add(i);
				}
			}
			out.write("\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				out.write(split[0]);
				itr = order.iterator();
				while (itr.hasNext()) {
					int index = (Integer)itr.next();
					out.write("\t" + split[index]);
				}
				out.write("\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
