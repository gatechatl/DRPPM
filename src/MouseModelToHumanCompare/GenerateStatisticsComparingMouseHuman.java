package MouseModelToHumanCompare;

import idconversion.cross_species.HumanMouseGeneNameConversion;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

import misc.StringManipulationTools;
import statistics.general.MathTools;

/**
 * Generate a report comparing mouse and human
 * @author tshaw
 *
 */
public class GenerateStatisticsComparingMouseHuman {

	public static void main(String[] args) {
		try {
			String mouseInput = "C:\\Users\\tshaw\\Desktop\\RNASEQ\\Mckinnon\\FPKM_07282014\\SJMMHGG\\SJMMHGG_RNAseq_Exon_Read_Count_gene_fpkm_uniq_NRremove_RemoveDup.txt";
			String humanInput = "C:\\Users\\tshaw\\Desktop\\RNASEQ\\PCGP_HGG\\Human_PCGP_HGG_Expression_ContainsUnpublished_20141203.txt";
			String outputCombineFile = "C:\\Users\\tshaw\\Desktop\\RNASEQ\\Mckinnon\\HumanMouseComparison\\HumanMouseGeneExprMatrix_20141208.txt";
			String hs2msFile = "C:\\Users\\tshaw\\Desktop\\RNASEQ\\hs_mm_homo_r66.txt";
			CombineMouseHumanExpression(mouseInput, humanInput, outputCombineFile, hs2msFile);
			
			String outputMatrixFile = "C:\\Users\\tshaw\\Desktop\\RNASEQ\\Mckinnon\\HumanMouseComparison\\HumanMouseGeneSpearmanMatrix_20141208.txt";
			createSpearmanRankMatrix(outputCombineFile, outputMatrixFile);
			
			String outputPearsonMatrixFile = "C:\\Users\\tshaw\\Desktop\\RNASEQ\\Mckinnon\\HumanMouseComparison\\HumanMouseGenePearsonMatrix_20141208.txt";
			createPearsonMatrix(outputCombineFile, outputPearsonMatrixFile);
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	public static void createPearsonMatrix(String fileName, String outputFile) {
		try {
			HashMap map = new HashMap();
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] headers = header.split("\t");			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				for (int i = 1; i < split.length; i++) {
					if (map.containsKey(headers[i])) {
						String stuff = (String)map.get(headers[i]);
						stuff += "\t" + split[i];
						map.put(headers[i], stuff);
					} else {
						map.put(headers[i], split[i]);
					}
				}
			}
			in.close();
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("Samples");
			for (int i = 1; i < headers.length; i++) {
				out.write("\t" + headers[i]);
			}
			out.write("\n");
			for (int i = 1; i < headers.length; i++) {
				String head1 = headers[i];
				String stuff1 = (String)map.get(head1);
				String[] split1 = stuff1.split("\t");
				double[] val1 = str2Double(split1);				
				boolean first = true;
				for (int j = 1; j < headers.length; j++) {
					String head2 = headers[j];
					String stuff2 = (String)map.get(head2);
					String[] split2 = stuff2.split("\t");
					double[] val2 = str2Double(split2);
					if (first) {
						out.write(head1 + "\t" + MathTools.PearsonCorrel(val1, val2));
						first = false;
					} else {
						out.write("\t" + MathTools.PearsonCorrel(val1, val2));
					}
				}
				out.write("\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void createSpearmanRankMatrix(String fileName, String outputFile) {
		try {
			HashMap map = new HashMap();
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] headers = header.split("\t");			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				for (int i = 1; i < split.length; i++) {
					if (map.containsKey(headers[i])) {
						String stuff = (String)map.get(headers[i]);
						stuff += "\t" + split[i];
						map.put(headers[i], stuff);
					} else {
						map.put(headers[i], split[i]);
					}
				}
			}
			in.close();
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("Samples");
			for (int i = 1; i < headers.length; i++) {
				out.write("\t" + headers[i]);
			}
			out.write("\n");
			for (int i = 1; i < headers.length; i++) {
				String head1 = headers[i];
				String stuff1 = (String)map.get(head1);
				String[] split1 = stuff1.split("\t");
				double[] val1 = str2Double(split1);				
				boolean first = true;
				for (int j = 1; j < headers.length; j++) {
					String head2 = headers[j];
					String stuff2 = (String)map.get(head2);
					String[] split2 = stuff2.split("\t");
					double[] val2 = str2Double(split2);
					if (first) {
						out.write(head1 + "\t" + MathTools.SpearmanRank(val1, val2));
						first = false;
					} else {
						out.write("\t" + MathTools.SpearmanRank(val1, val2));
					}
				}
				out.write("\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static double[] str2Double(String[] stuff) {
		double[] results = new double[stuff.length];
		for (int i = 0; i < results.length; i++) {
			results[i] = new Double(stuff[i]);
		}
		return results;
	}
	public static void CombineMouseHumanExpression(String mouseInput, String humanInput, String combineOutput, String mouse2humanFile) {
		HashMap mouse2human = HumanMouseGeneNameConversion.mouse2human(mouse2humanFile);
		HashMap mouseValues = new HashMap();
		HashMap humanValues = new HashMap();
		try {
			FileInputStream fstream = new FileInputStream(mouseInput);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String mouse_header = in.readLine();
			mouse_header = StringManipulationTools.removeFirst(mouse_header);
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneExpr = split[1];
				for (int i = 2; i < split.length; i++) {
					geneExpr += "\t" + split[i];
				}
				if (mouse2human.containsKey(split[0])) {
					String human_gene = (String)mouse2human.get(split[0]);
					mouseValues.put(human_gene, geneExpr);
				}
			}
			in.close();
			
			FileInputStream fstream2 = new FileInputStream(humanInput);
			DataInputStream din2 = new DataInputStream(fstream2);
			BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
			String human_header = in2.readLine();
			human_header = StringManipulationTools.removeFirst(human_header);
			while (in2.ready()) {
				String str = in2.readLine();
				String[] split = str.split("\t");
				String geneExpr = split[1];
				for (int i = 2; i < split.length; i++) {
					geneExpr += "\t" + split[i];
				}
				
				String human_gene = split[0];
				humanValues.put(human_gene, geneExpr);
			}
			in.close();
			
			FileWriter fwriter = new FileWriter(combineOutput);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("gene\t" + mouse_header + "\t" + human_header + "\n");
			Iterator itr = mouseValues.keySet().iterator();
			while (itr.hasNext()) {
				String human_gene = (String)itr.next();
				if (humanValues.containsKey(human_gene)) {
					String gene_expr = (String)mouseValues.get(human_gene) + "\t" + (String)humanValues.get(human_gene);
					out.write(human_gene + "\t" + gene_expr + "\n");
				}				
			}
			out.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
