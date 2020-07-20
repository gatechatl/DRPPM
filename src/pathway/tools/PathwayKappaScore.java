package pathway.tools;

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
 * Generate the pathway kappa score of a gmt file
 * @author tshaw
 *
 */
public class PathwayKappaScore {

	private HashMap pathways = new HashMap();
	private HashMap genes = new HashMap();
	private StringBuffer kappa_matrix = new StringBuffer();	
	private HashMap kappa_scores = new HashMap();
	
	public StringBuffer getKappaMatrix() {
		return kappa_matrix;
	}
	public static String type() {
		return "KAPPA";
	}
	public static String description() {
		return "Generate the pathway kappa score of a gmt file";
	}
	public static String parameter_info() {
		return "[gmtFile] [cutoff] [kappaMatrixOutput] [sifOutputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			String gmtFile = args[0];
			double cutoff = new Double(args[1]);
			String kappaMatrixOutput = args[2];
			String sifOutputFile = args[3];
			PathwayKappaScore pathwayKappa = new PathwayKappaScore(gmtFile);
			StringBuffer matrix_buffer = pathwayKappa.getKappaMatrix();
			
			// generate kappa matrix file
			FileWriter fwriter = new FileWriter(kappaMatrixOutput);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write(matrix_buffer.toString());
			out.close();
			
			StringBuffer sif = pathwayKappa.generate_SIF_file(cutoff);
			// generate sif file
			fwriter = new FileWriter(sifOutputFile);
			out = new BufferedWriter(fwriter);
			out.write(sif.toString());
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Input a gmt file from GSEA
	 * @param inputFile
	 */
	public PathwayKappaScore(String inputFile) {
		try {
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String pathway = split[0];
				LinkedList list = new LinkedList();
				for (int i = 2; i < split.length; i++) {
					String gene = split[i];
					if (!list.contains(gene)) {
						list.add(gene);
					}
					if (genes.containsKey(gene)) {
						LinkedList list_pathway = (LinkedList)genes.get(gene);
						list_pathway.add(pathway);
						genes.put(gene, list_pathway);
					} else {
						LinkedList list_pathway = new LinkedList();
						list_pathway.add(pathway);
						genes.put(gene, list_pathway);
					}
				}
				pathways.put(pathway, list);				
			}
			in.close();
					
		} catch (Exception e) {
			e.printStackTrace();
		}
		kappa_matrix = generate_kappa_matrix();
		
	}
	
	public StringBuffer generate_SIF_file(double cutoff) {
		StringBuffer sif = new StringBuffer();
		Iterator itr = kappa_scores.keySet().iterator();
		while (itr.hasNext()) {
			String pathwayLink = (String)itr.next();
			String[] split = pathwayLink.split("\t");
			double score = (Double)kappa_scores.get(pathwayLink);
			if (score >= cutoff) {
				sif.append(split[0] + "\t" + "kappa" + "\t" + split[1] + "\n");
			}
		}
		return sif;
	}
	/**
	 * Generate the kappa matrix
	 * @return
	 */
	public StringBuffer generate_kappa_matrix() {
		StringBuffer result = new StringBuffer();
		result.append("Pathways");
		Iterator itr = pathways.keySet().iterator();
		while (itr.hasNext()) {
			String pathway1 = (String)itr.next();
			result.append("\t" + pathway1);
		}
		result.append("\n");
		itr = pathways.keySet().iterator();
		while (itr.hasNext()) {
			String pathway1 = (String)itr.next();			
			Iterator itr2 = pathways.keySet().iterator();
			result.append(pathway1);
			while (itr2.hasNext()) {
				String pathway2 = (String)itr2.next();
				
				//if (!pathway1.equals(pathway2)) {
					LinkedList list1 = (LinkedList)pathways.get(pathway1);
					LinkedList list2 = (LinkedList)pathways.get(pathway2);
					double kappa_score = kappa_score_matrix(list1, list2, genes);
					if (!kappa_scores.containsKey(pathway1 + "\t" + pathway2) && !kappa_scores.containsKey(pathway2 + "\t" + pathway1)) {
						kappa_scores.put(pathway1 + "\t" + pathway2, kappa_score);
					}
					result.append("\t" + kappa_score);					
				//}				
			}
			result.append("\n");
		}		
		return result;
	}
	
	/**
	 * Calculate kappa score based on Huang et al.
	 * @param list1
	 * @param list2
	 * @param geneList
	 * @return
	 */
	public double kappa_score_matrix(LinkedList list1, LinkedList list2, HashMap comprehensive) {
		
		double C11 = 0;
		double C00 = 0;
		double C01 = 0;
		double C10 = 0;
		HashMap combine_list1_list2 = new HashMap();
		Iterator itr = list1.iterator();
		while (itr.hasNext()) {
			String gene = (String)itr.next();
			if (list2.contains(gene)) {
				C11++;
			} else {
				C01++;
			}
			combine_list1_list2.put(gene, gene);
		}
		itr = list2.iterator();
		while (itr.hasNext()) {
			String gene = (String)itr.next();
			if (!list1.contains(gene)) {
				C10++;
			}
			combine_list1_list2.put(gene, gene);
		}
		C00 = comprehensive.size() - combine_list1_list2.size();
		double Oab = (C11 + C00) / comprehensive.size();
		double Aab = ((C11 + C01) * (C11 + C10) + (C01 + C00) * (C10 + C00)) / (comprehensive.size() * comprehensive.size());
		double Kab = (Oab - Aab) / (1 - Aab);
		return Kab;
	}
	public static HashMap generate_pathway_binary(String inputFile) {
		HashMap map = new HashMap();
		try {
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}
