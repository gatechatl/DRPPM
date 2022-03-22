package stjude.projects.jinghuizhang.csiminer.finalprioritization;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

/**
 * The program is used for prioritizing exon for the heatmap
 * @author 4472414
 *
 */
public class JinghuiZhangCSIMinerHeatmapExonSelection {

	public static void main(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			

			String outputFile = "/Users/4472414/Documents/Current_Manuscripts/CSIMiner/Prioritization/2022_March_10/Best_candidate.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			String gene_candidate_file = "/Users/4472414/Documents/Current_Manuscripts/CSIMiner/Prioritization/2022_March_10/gene_candidates.txt";					
			FileInputStream fstream = new FileInputStream(gene_candidate_file);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(split[0], split[3]);		
				
			}
			in.close();		
			
			
			HashMap existing_gene_map = new HashMap();
			HashMap existing_candidates_map = new HashMap();
			String existing_candidates_file = "/Users/4472414/Documents/Current_Manuscripts/CSIMiner/Prioritization/2022_March_10/existing_candidates.txt";					
			fstream = new FileInputStream(existing_candidates_file);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				existing_candidates_map.put(split[0], split[0]);
				existing_gene_map.put(split[0].split("_")[0], split[0]);
				
			}
			in.close();	
		
			HashMap restricted_exons = new HashMap();
			String restricted_exon_list_file = "/Users/4472414/Documents/Current_Manuscripts/CSIMiner/Prioritization/2022_March_10/restricted_exon_candidates.txt";					
			fstream = new FileInputStream(restricted_exon_list_file);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				restricted_exons.put(split[0], split[0]);		
				
			}
			in.close();		
			
			HashMap exon_category = new HashMap();
			String exon_category_candidate_file = "/Users/4472414/Documents/Current_Manuscripts/CSIMiner/Prioritization/2022_March_10/Output_Exon_AS_Annotations_20220218.txt";					
			fstream = new FileInputStream(exon_category_candidate_file);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				
				exon_category.put(split[0], split[1]);
				
			}
			in.close();
			
			
			
			HashMap best_exon = new HashMap();
			String exon_annotation_file = "/Users/4472414/Documents/Current_Manuscripts/CSIMiner/Prioritization/2022_March_10/complete_exons_list_and_their_scores.txt";					
			fstream = new FileInputStream(exon_annotation_file);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String exon_id = split[0];
				String gene = split[0].split("_")[0];
				if (restricted_exons.containsKey(exon_id)) {
					if (existing_gene_map.containsKey(gene)) {
	
						double weighted_score = new Double(split[1]);
						double tumor_score = new Double(split[2]);
						double normal_score = new Double(split[3]);
						int high_tumor_count = new Integer(split[5]);
						int high_normal_count = new Integer(split[6]);
						String high_normal = split[17] + split[18];
						String high_disease = split[21] + split[22];
						double psm_norm = Double.NaN;
						if (!split[35].equals("NA")) {
							psm_norm = new Double(split[35]);
						}
						String gene_category = (String)map.get(gene);
						String category = "NA";
						if (exon_category.containsKey(exon_id)) {
							category = (String)exon_category.get(exon_id);
						}
						if (existing_candidates_map.containsKey(exon_id)) {
							best_exon.put(gene, exon_id + "\t" + weighted_score + "\t" + high_normal_count + "\t" + category + "\t" + psm_norm + "\t" + high_normal + "\t" + high_disease);
						}
					} else if (map.containsKey(gene)) {
						String gene_category = (String)map.get(gene);
						String category = "NA";
						if (exon_category.containsKey(exon_id)) {
							category = (String)exon_category.get(exon_id);
						}
						
						
						double weighted_score = new Double(split[1]);
						double tumor_score = new Double(split[2]);
						double normal_score = new Double(split[3]);
						int high_tumor_count = new Integer(split[5]);
						int high_normal_count = new Integer(split[6]);
						String high_normal = split[17] + split[18];
						String high_disease = split[21] + split[22];
						double psm_norm = Double.NaN;
						if (!split[35].equals("NA")) {
							psm_norm = new Double(split[35]);
						}
						if (high_normal_count <= 5 && weighted_score >= 1.0 && high_tumor_count >= 1 && tumor_score > normal_score && !category.equals("Missing_in_annotated_transcripts") && !high_normal.contains("Brain")) {
	
							
							if (best_exon.containsKey(gene)) {
								String exon_info = (String)best_exon.get(gene);
								String[] exon_info_split = exon_info.split("\t");
								boolean prev_score_higher = (new Double(exon_info_split[1]) > weighted_score);
								boolean normal_count_lower = (new Double(exon_info_split[2]) < high_normal_count);
								boolean normal_count_equal = (new Double(exon_info_split[2]) == high_normal_count);
								boolean is_alt_exon = exon_info_split[3].equals("Alt_Transcript");
								if (gene_category.equals("AS")) {
									if (is_alt_exon) {
										if (normal_count_lower || (normal_count_equal && prev_score_higher)) {
											best_exon.put(gene, exon_id + "\t" + weighted_score + "\t" + high_normal_count + "\t" + category + "\t" + psm_norm + "\t" + high_normal + "\t" + high_disease);
										}
									}
								} else {
									if (normal_count_lower || (normal_count_equal && prev_score_higher)) {
										best_exon.put(gene, exon_id + "\t" + weighted_score + "\t" + high_normal_count + "\t" + category + "\t" + psm_norm + "\t" + high_normal + "\t" + high_disease);
									}
								}
							} else {
								if (gene_category.equals("AS")) {
									if (gene.equals("FN1")) {
										System.out.println(exon_id + "\t" + high_normal_count + "\t" + weighted_score + "\t" + high_tumor_count + "\t" + tumor_score + "\t" + normal_score);
									}	
									if (category.equals("Alt_Transcript")) {
										best_exon.put(gene, exon_id + "\t" + weighted_score + "\t" + high_normal_count + "\t" + category + "\t" + psm_norm + "\t" + high_normal + "\t" + high_disease);
									}
								} else {
									best_exon.put(gene, exon_id + "\t" + weighted_score + "\t" + high_normal_count + "\t" + category + "\t" + psm_norm + "\t" + high_normal + "\t" + high_disease);
								}
							}
						}
					
					}
				}
			}
			in.close();
			
			Iterator itr = best_exon.keySet().iterator();
			while (itr.hasNext()) {
				String gene = (String)itr.next();
				String exon_info = (String)best_exon.get(gene);
				out.write(gene + "\t" + exon_info + "\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
