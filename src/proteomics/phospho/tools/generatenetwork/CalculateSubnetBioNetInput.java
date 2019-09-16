package proteomics.phospho.tools.generatenetwork;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

import statistics.general.MathTools;

/**
 * Generate script for calculating bionet sub network
 * @author tshaw
 *
 */
public class CalculateSubnetBioNetInput {

	public static String parameter_info() {
		//return "[inputFile] [category or ALL] [gene_pvalueFile] [outputFile]";
		return "[inputFile] [category or ALL] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String category = args[1];
			//String gene_pvalueFile = args[2];
			String outputFile = args[2];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			HashMap gene_pvalue = new HashMap();;
			
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split[0].equals(category) || category.equals("ALL")) {
					String geneName = split[1];
					double pval = new Double(split[2]);
					double enrichment = new Double(split[3]);
					if (enrichment < 1.0) {
						pval = 1;						
					}
					//if (gene_pvalue.containsKey(geneName)) {
					gene_pvalue.put(geneName, pval);
					//}
				}
			}
			in.close();
			
			/*
			fstream = new FileInputStream(gene_pvalueFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				gene_pvalue.put(split[0], 1.0);
				gene_pvalue.put(split[2], 1.0);
			}
			in.close();
			*/
			
			header = "";
			String data = "";
			Iterator itr = gene_pvalue.keySet().iterator();
			while (itr.hasNext()) {
				String geneName = (String)itr.next();
				if (header.equals("")) {
					header = geneName;
				} else {
					header += "\t" + geneName;
				}
				if (data.equals("")) {
					double value = (Double)gene_pvalue.get(geneName);
					double round = new Double(value) * 1000000;
					value = new Double(new Double(round).intValue()) / 1000000;
					data = "" + value;
				} else {
					double value = (Double)gene_pvalue.get(geneName);
					double round = new Double(value) * 1000000;
					value = new Double(new Double(round).intValue()) / 1000000;
					data += "\t" + value;
				}
			}
			
			
			out.write(header + "\n");
			out.write(data + "\n");
			out.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
}
