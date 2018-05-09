package expressionanalysis.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import statistics.general.MathTools;

/**
 * Calculate the spearman rank value between human and mouse samples
 * @author tshaw
 *
 */
public class GenerateSpearmanRankMatrix {


	public static String description() {
		return "Generate spearman rank matrix for two gene expression file..";
	}
	public static String type() {
		return "EXPRESSION";
	}
	public static String parameter_info() {
		return "[inputMatrixFile1] [inputMatrixFile2] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputMatrixFile1 = args[0];
			String inputMatrixFile2 = args[1];
			String outputFile = args[2];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);

			HashMap compile_gene_in_both = new HashMap(); // check if both human and mouse have the gene
			
			FileInputStream fstream = new FileInputStream(inputMatrixFile1);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] header_split1 = header.split("\t");
			HashMap[] value1 = new HashMap[header_split1.length - 1];
			for (int i = 0; i < value1.length; i++) {
				value1[i] = new HashMap();
			}
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");				
				for (int i = 0; i < value1.length; i++) {
					value1[i].put(split[0], new Double(split[i + 1]));
				}				
			}
			in.close();
			
			fstream = new FileInputStream(inputMatrixFile2);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			String[] header_split2 = header.split("\t");
			HashMap[] value2 = new HashMap[header_split2.length - 1];
			for (int i = 0; i < value2.length; i++) {
				value2[i] = new HashMap();
			}
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String gene2 = split[0];
				if (value1[1].containsKey(gene2)) {
					compile_gene_in_both.put(gene2, gene2);
				}
				for (int i = 0; i < value2.length; i++) {
					value2[i].put(gene2, new Double(split[i + 1]));
				}				
			
			}
			in.close();
			out.write("Sample");
			for (int i = 1; i < header_split1.length; i++) {
				out.write("\t" + header_split1[i]);
			}
			out.write("\n");
			for (int j = 1; j < header_split2.length; j++) {
				out.write(header_split2[j]);
				for (int i = 1; i < header_split1.length; i++) {
					LinkedList values1_list = new LinkedList();
					LinkedList values2_list = new LinkedList();
					Iterator itr = compile_gene_in_both.keySet().iterator();
					while (itr.hasNext()) {
						String gene = (String)itr.next();
						double expr1 = (Double)value1[i - 1].get(gene);
						double expr2 = (Double)value2[j - 1].get(gene);
						values1_list.add(expr1);
						values2_list.add(expr2);						
					}
					double[] values1_array = MathTools.convertListDouble2Double(values1_list);
					double[] values2_array = MathTools.convertListDouble2Double(values2_list);
					double spearmanRank = MathTools.SpearmanRank(values1_array, values2_array);
					out.write("\t" + spearmanRank);
					
				}
				out.write("\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
