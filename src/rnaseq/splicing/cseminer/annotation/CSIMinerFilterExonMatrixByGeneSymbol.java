package rnaseq.splicing.cseminer.annotation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Filter the exons for ECM or Pantarget genes
 * @author tshaw
 *
 */
public class CSIMinerFilterExonMatrixByGeneSymbol {
	public static String description() {
		return "Filter for ECM related genes.";
	}
	public static String type() {
		return "CSI-Miner";
	}
	public static String parameter_info() {
		return "[inputMatrixFile] [good_genes_list_file] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			String inputMatrixFile = args[0];
			String good_genes_list_file = args[1];
			String outputFile = args[2];			

			HashMap ecm_gene_list = new HashMap();
			FileInputStream fstream = new FileInputStream(good_genes_list_file);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				ecm_gene_list.put(split[0], split[0]);
				
			}
			in.close();				
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);	
			
			fstream = new FileInputStream(inputMatrixFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write(header + "\tAnnotation\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneName = split[0].split("\\|")[0];
				boolean found = false;
				for (String more_than_one_geneName: geneName.split("\\+")) {
					if (ecm_gene_list.containsKey(more_than_one_geneName)) {						
						found = true;
					} 
				}
				for (String more_than_one_geneName: geneName.split("_")) {
					if (ecm_gene_list.containsKey(more_than_one_geneName)) {						
						found = true;
					} 
				}
				for (String more_than_one_geneName: geneName.split(",")) {
					if (ecm_gene_list.containsKey(more_than_one_geneName)) {						
						found = true;
					} 
				}
				for (String more_than_one_geneName: geneName.split(";")) {
					if (ecm_gene_list.containsKey(more_than_one_geneName)) {						
						found = true;
					} 
				}
				if (found) {
					out.write(str + "\tKnownECM\n");
				} else if (geneName.split("_").length > 1) {
					if (geneName.split("_")[1].equals("PanCan")) {
						out.write(str + "\tNovel_PanCan\n");
					} else if (geneName.split("_")[1].equals("ECM")) {
						out.write(str + "\tNovel_ECM\n");
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

