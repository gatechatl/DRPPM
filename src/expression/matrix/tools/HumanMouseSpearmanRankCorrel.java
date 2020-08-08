package expression.matrix.tools;

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
public class HumanMouseSpearmanRankCorrel {


	public static String description() {
		return "Generate human to mouse gene expression spearman rank matrix..";
	}
	public static String type() {
		return "EXPRESSION";
	}
	public static String parameter_info() {
		return "[inputHumanMatrixFile] [inputMouseMatrixFile] [hs_mm_homo_updated_v2_20170502.txt] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputHumanMatrixFile = args[0];
			String inputMouseMatrixFile = args[1];
			String geneConversionFile = args[2];
			String outputFile = args[3];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			HashMap human2mouse = new HashMap();
			HashMap mouse2human = new HashMap();
			HashMap compile_gene_in_both = new HashMap(); // check if both human and mouse have the gene
			FileInputStream fstream = new FileInputStream(geneConversionFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			//String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				human2mouse.put(split[0], split[1]);
				mouse2human.put(split[1], split[0]);
			}
			in.close();
			
			fstream = new FileInputStream(inputHumanMatrixFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] human_header_split = header.split("\t");
			HashMap[] human_value = new HashMap[human_header_split.length - 1];
			for (int i = 0; i < human_value.length; i++) {
				human_value[i] = new HashMap();
			}
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (human2mouse.containsKey(split[0])) {
					for (int i = 0; i < human_value.length; i++) {
						human_value[i].put(split[0], new Double(split[i + 1]));
					}				
				}
			}
			in.close();
			
			fstream = new FileInputStream(inputMouseMatrixFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			String[] mouse_header_split = header.split("\t");
			HashMap[] mouse_value = new HashMap[mouse_header_split.length - 1];
			for (int i = 0; i < mouse_value.length; i++) {
				mouse_value[i] = new HashMap();
			}
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (mouse2human.containsKey(split[0])) {
					String human_gene = (String)mouse2human.get(split[0]);
					if (human_value[1].containsKey(human_gene)) {
						compile_gene_in_both.put(human_gene, human_gene);
					}
					for (int i = 0; i < mouse_value.length; i++) {
						mouse_value[i].put(human_gene, new Double(split[i + 1]));
					}				
				}
			}
			in.close();
			out.write("Sample");
			for (int i = 1; i < human_header_split.length; i++) {
				out.write("\t" + human_header_split[i]);
			}
			out.write("\n");
			for (int j = 1; j < mouse_header_split.length; j++) {
				out.write(mouse_header_split[j]);
				for (int i = 1; i < human_header_split.length; i++) {
					LinkedList human_values_list = new LinkedList();
					LinkedList mouse_values_list = new LinkedList();
					Iterator itr = compile_gene_in_both.keySet().iterator();
					while (itr.hasNext()) {
						String gene = (String)itr.next();
						double human_expr = (Double)human_value[i - 1].get(gene);
						double mouse_expr = (Double)mouse_value[j - 1].get(gene);
						human_values_list.add(human_expr);
						mouse_values_list.add(mouse_expr);
						
					}
					double[] human_values_array = MathTools.convertListDouble2Double(human_values_list);
					double[] mouse_values_array = MathTools.convertListDouble2Double(mouse_values_list);
					double spearmanRank = MathTools.SpearmanRank(human_values_array, mouse_values_array);
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
