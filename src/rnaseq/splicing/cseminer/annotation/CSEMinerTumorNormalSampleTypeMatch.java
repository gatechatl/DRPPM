package rnaseq.splicing.cseminer.annotation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Check sample type match 
 * Also generate a gene specific check for Brain, Liver, Pancreas, Lung, Nerve, Pituitary Gland
 * @author 4472414
 *
 */
public class CSEMinerTumorNormalSampleTypeMatch {

	
	public static void main(String[] args) {
		
		try {
			
			
			String high_disease = "";
			HashMap pairs = new HashMap();
			HashMap low_risk_pairs = new HashMap();
			//pairs.put("ACC", "Adrenal");
			//pairs.put("EWS", "Kidney");
									
			pairs.put("HGG", "Brain");
			
			pairs.put("LGG", "Brain");

			//					
			pairs.put("BT", "Brain");
			pairs.put("MB", "Brain");			
			pairs.put("CPC", "Brain");
			pairs.put("EPD", "Brain");
			

			
			
			
			low_risk_pairs.put("ACT", "Adrenal");
			low_risk_pairs.put("ACC", "Adrenal");
			
			low_risk_pairs.put("WLM", "Kidney");
			
			low_risk_pairs.put("RHB", "Muscle");
			low_risk_pairs.put("RB", "Nerve");
			low_risk_pairs.put("NBL", "Nerve");
			low_risk_pairs.put("MEL", "Skin");
			/*low_risk_pairs.put("RB", "Nerve");
			low_risk_pairs.put("NBL", "Nerve");
			low_risk_pairs.put("MEL", "Skin");
			low_risk_pairs.put("ACT", "Adrenal");
			low_risk_pairs.put("ACC", "Adrenal");
			low_risk_pairs.put("RHB", "Muscle");
			low_risk_pairs.put("WLM", "Kidney");*/
			//pairs.put("DSRCT", "Intestine");
			HashMap all_gene = new HashMap();
			HashMap brain = new HashMap();
			HashMap lung = new HashMap();
			HashMap liver = new HashMap();
			HashMap pancreas = new HashMap();
			HashMap pituitary = new HashMap();
			HashMap nerve = new HashMap();
			
			HashMap gene_exon = new HashMap();
			HashMap brain_exon = new HashMap();
			HashMap lung_exon = new HashMap();
			HashMap liver_exon = new HashMap();
			HashMap pancreas_exon = new HashMap();
			HashMap pituitary_exon = new HashMap();
			HashMap nerve_exon = new HashMap();			
			
			HashMap non_testis_ovary = new HashMap();
			
			String outputFile = "/Users/4472414/Projects/CSIMiner/Current_Manuscript/NatureCommunication_Draft/CompleteAnnotationPipeline/Exon_Pediatric_GTEx_TissueEnrichment_TissuePairFlag_20230314.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			String outputGeneSpecific = "/Users/4472414/Projects/CSIMiner/Current_Manuscript/NatureCommunication_Draft/CompleteAnnotationPipeline/Gene_Pediatric_GTEx_TissueEnrichment_TissuePairFlag_20230314.txt";
			FileWriter fwriterGeneSpecific = new FileWriter(outputGeneSpecific);
			BufferedWriter outGeneSpecific = new BufferedWriter(fwriterGeneSpecific);
			outGeneSpecific.write("GeneName\tHighRiskNormalTumorPaired\tBrain\tLung\tLiver\tPancreas\tPituitary\tNerve\tAll\tBrain_HiExonProp\tLung_HiExonProp\tLiver_HiExonProp\tPancreas_HiExonProp\tPituitary_HiExonProp\tNerve_HiExonProp\tMin_HiExonProp\tNumNormalTissueAboveMedianOverall\tLowRiskNormalTumorPaired\n");
			
			HashMap gene_level_paired = new HashMap();
			HashMap gene_level_paired_events = new HashMap();
			HashMap low_risk_gene_level_paired = new HashMap();
			HashMap low_risk_gene_level_paired_events = new HashMap();
			
			String inputFile = "/Users/4472414/Projects/CSIMiner/Current_Manuscript/NatureCommunication_Draft/CompleteAnnotationPipeline/Exon_Pediatric_GTEx_TissueEnrichment_20230314.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write(header + "\t" + "HighRiskGeneTumorNormalMatch" + "\t" + "LowRiskGeneTumorNormalMatch" + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneName = split[0].split("_")[0];
				LinkedList list = new LinkedList();
				String normal_q1 = split[1];
				String normal_q2 = split[2];
				String normal_q3 = split[3];
				String normal_q4 = split[4];
				all_gene.put(geneName, geneName);
				
				if (gene_exon.containsKey(geneName)) {
					double count = (Double)gene_exon.get(geneName);
					count = count + 1.0;
					gene_exon.put(geneName, count);
				} else {
					gene_exon.put(geneName, 1.0);
				}
				
				String combine_q3_q4 = normal_q3 + normal_q4;
				combine_q3_q4 = combine_q3_q4.replaceAll("Ovary,", "").replaceAll("Testis,", "");
				if (combine_q3_q4.length() > 0) {
					if (non_testis_ovary.containsKey(geneName)) {
						LinkedList list_of_normal_tissue = (LinkedList)non_testis_ovary.get(geneName);
						for (String tissue: combine_q3_q4.split(",")) {
							if (!list_of_normal_tissue.contains(tissue)) {
								list_of_normal_tissue.add(tissue);
							}
						}
						non_testis_ovary.put(geneName, list_of_normal_tissue);
					} else {
						LinkedList list_of_normal_tissue = new LinkedList();
						for (String tissue: combine_q3_q4.split(",")) {
							list_of_normal_tissue.add(tissue);
						}
						non_testis_ovary.put(geneName, list_of_normal_tissue);
					}
				}
				if (normal_q3.contains("Brain") || normal_q4.contains("Brain")) {
					brain.put(geneName, geneName);
					if (brain_exon.containsKey(geneName)) {
						double count = (Double)brain_exon.get(geneName);
						count = count + 1.0;
						brain_exon.put(geneName, count);
					} else {
						brain_exon.put(geneName, 1.0);
					}
				}
				if (normal_q3.contains("Lung") || normal_q4.contains("Lung")) {
					lung.put(geneName, geneName);
					if (lung_exon.containsKey(geneName)) {
						double count = (Double)lung_exon.get(geneName);
						count = count + 1.0;
						lung_exon.put(geneName, count);
					} else {
						lung_exon.put(geneName, 1.0);
					}
				}
				if (normal_q3.contains("Liver") || normal_q4.contains("Liver")) {
					liver.put(geneName, geneName);
					if (liver_exon.containsKey(geneName)) {
						double count = (Double)liver_exon.get(geneName);
						count = count + 1.0;
						liver_exon.put(geneName, count);
					} else {
						liver_exon.put(geneName, 1.0);
					}
				}
				if (normal_q3.contains("Pancreas") || normal_q4.contains("Pancreas")) {
					pancreas.put(geneName, geneName);
					if (pancreas_exon.containsKey(geneName)) {
						double count = (Double)pancreas_exon.get(geneName);
						count = count + 1.0;
						pancreas_exon.put(geneName, count);
					} else {
						pancreas_exon.put(geneName, 1.0);
					}
				}
				if (normal_q3.contains("Pituitary") || normal_q4.contains("Pituitary")) {
					pituitary.put(geneName, geneName);
					if (pituitary_exon.containsKey(geneName)) {
						double count = (Double)pituitary_exon.get(geneName);
						count = count + 1.0;
						pituitary_exon.put(geneName, count);
					} else {
						pituitary_exon.put(geneName, 1.0);
					}
				}
				if (normal_q3.contains("Nerve") || normal_q4.contains("Nerve")) {
					nerve.put(geneName, geneName);
					if (nerve_exon.containsKey(geneName)) {
						double count = (Double)nerve_exon.get(geneName);
						count = count + 1.0;
						nerve_exon.put(geneName, count);
					} else {
						nerve_exon.put(geneName, 1.0);
					}
				}
				String tumor_q1 = split[5];
				String tumor_q2 = split[6];
				String tumor_q3 = split[7];
				String tumor_q4 = split[8];
				
				
				boolean flag_paired = false;
				boolean low_risk_flag_paired = false;
				if (!gene_level_paired.containsKey(geneName)) {
					gene_level_paired.put(geneName, flag_paired);
				}
				if (!low_risk_gene_level_paired.containsKey(geneName)) {
					low_risk_gene_level_paired.put(geneName, low_risk_flag_paired);
				}
				Iterator itr2 = pairs.keySet().iterator();
				while (itr2.hasNext()) {
					String disease = (String)itr2.next();
					String normal = (String)pairs.get(disease);
					if (tumor_q3.contains(disease) || tumor_q4.contains(disease)) {
						if (normal_q3.contains(normal) || normal_q4.contains(normal)) {
							flag_paired = true;
							gene_level_paired.put(geneName, flag_paired);
						}
					}
				}
				Iterator itr3 = low_risk_pairs.keySet().iterator();
				while (itr3.hasNext()) {
					String disease = (String)itr3.next();
					String normal = (String)low_risk_pairs.get(disease);
					if (tumor_q3.contains(disease) || tumor_q4.contains(disease)) {
						if (normal_q3.contains(normal) || normal_q4.contains(normal)) {
							low_risk_flag_paired = true;
							low_risk_gene_level_paired.put(geneName, low_risk_flag_paired);
						}
					}
				}
				out.write(str + "\t" + flag_paired + "\t" + low_risk_flag_paired + "\n");
			}
			in.close();				
			out.close();			
						
			Iterator itr = all_gene.keySet().iterator();
			while (itr.hasNext()) {
				String geneName = (String)itr.next();
				boolean flag = false;
				boolean low_risk_flag = false;
				if (gene_level_paired.containsKey(geneName)) {
					flag = (Boolean)gene_level_paired.get(geneName);
				}
				if (low_risk_gene_level_paired.containsKey(geneName)) {
					low_risk_flag = (Boolean)low_risk_gene_level_paired.get(geneName);
				}
				boolean brain_flag = false;
				boolean lung_flag = false;
				boolean liver_flag = false;
				boolean pancreas_flag = false;
				boolean pituitary_flag = false;
				boolean nerve_flag = false;
				if (brain.containsKey(geneName)) {
					brain_flag = true;
				}
				if (lung.containsKey(geneName)) {
					lung_flag = true;
				}
				if (liver.containsKey(geneName)) {
					liver_flag = true;
				}
				if (pancreas.containsKey(geneName)) {
					pancreas_flag = true;
				}
				if (pituitary.containsKey(geneName)) {
					pituitary_flag = true;
				}
				if (nerve.containsKey(geneName)) {
					nerve_flag = true;
				}
				LinkedList list_of_normal_tissue = new LinkedList();
				if (non_testis_ovary.containsKey(geneName)) {
					list_of_normal_tissue = (LinkedList)non_testis_ovary.get(geneName);
				}
				
				double brain_freq = 0.0;
				double lung_freq = 0.0;
				double liver_freq = 0.0;
				double pancreas_freq = 0.0;
				double pituitary_freq = 0.0;
				double nerve_freq = 0.0;
				double max_freq = 0.0;
				boolean hit = false;
				if (gene_exon.containsKey(geneName)) {
					double total_exons = (Double)gene_exon.get(geneName);
					
					if (brain_exon.containsKey(geneName)) {
						brain_freq = (Double)brain_exon.get(geneName) / total_exons;
						if (max_freq < brain_freq) {
							max_freq = brain_freq;
							hit = true;
						}
					}
					if (lung_exon.containsKey(geneName)) {
						lung_freq = (Double)lung_exon.get(geneName) / total_exons;
						if (max_freq < lung_freq) {
							max_freq = lung_freq;
							hit = true;
						}
					}
					if (liver_exon.containsKey(geneName)) {
						liver_freq = (Double)liver_exon.get(geneName) / total_exons;
						if (max_freq < liver_freq) {
							max_freq = liver_freq;
							hit = true;
						}
					}
					if (pancreas_exon.containsKey(geneName)) {
						pancreas_freq = (Double)pancreas_exon.get(geneName) / total_exons;
						if (max_freq < pancreas_freq) {
							max_freq = pancreas_freq;
							hit = true;
						}
					}
					if (pituitary_exon.containsKey(geneName)) {
						pituitary_freq = (Double)pituitary_exon.get(geneName) / total_exons;
						if (max_freq < pituitary_freq) {
							max_freq = pituitary_freq;
							hit = true;
						}
					}
					if (nerve_exon.containsKey(geneName)) {
						nerve_freq = (Double)nerve_exon.get(geneName) / total_exons;
						if (max_freq < nerve_freq) {
							max_freq = nerve_freq;
							hit = true;
						}
					}
					if (!hit) {
						max_freq = 0.0;
					}
					
				}
				outGeneSpecific.write(geneName + "\t" + flag + "\t" + brain_flag + "\t" + lung_flag + "\t" + liver_flag + "\t" + pancreas_flag + "\t" + pituitary_flag + "\t" + nerve_flag + "\t" + (brain_flag || lung_flag || liver_flag || pancreas_flag || nerve_flag || pituitary_flag) + "\t" + brain_freq + "\t" + lung_freq + "\t" + liver_freq + "\t" + pancreas_freq + "\t" + pituitary_freq + "\t" + nerve_freq + "\t" + max_freq + "\t" + list_of_normal_tissue.size() + "\t" + low_risk_flag + "\n");
			}
			outGeneSpecific.close();
			
			// copy file 
			copyFileUsingStream(new File("/Users/4472414/Projects/CSIMiner/Current_Manuscript/NatureCommunication_Draft/CompleteAnnotationPipeline/Exon_Pediatric_GTEx_TissueEnrichment_TissuePairFlag_20230314.txt"), new File("/Users/4472414/Documents/Current_Manuscripts/CSIMiner/Current_Manuscript/NatureCommunication_Draft/CompleteAnnotationPipeline/pipeline_input_files/GTEx_Exon_Annotation/Exon_Pediatric_GTEx_TissueEnrichment_TissuePairFlag_20230314.txt"));
			copyFileUsingStream(new File("/Users/4472414/Projects/CSIMiner/Current_Manuscript/NatureCommunication_Draft/CompleteAnnotationPipeline/Gene_Pediatric_GTEx_TissueEnrichment_TissuePairFlag_20230314.txt"), new File("/Users/4472414/Documents/Current_Manuscripts/CSIMiner/Current_Manuscript/NatureCommunication_Draft/CompleteAnnotationPipeline/pipeline_input_files/GTEx_Exon_Annotation/Gene_Pediatric_GTEx_TissueEnrichment_TissuePairFlag_20230314.txt"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private static void copyFileUsingStream(File source, File dest) throws IOException {
	    InputStream is = null;
	    OutputStream os = null;
	    try {
	        is = new FileInputStream(source);
	        os = new FileOutputStream(dest);
	        byte[] buffer = new byte[1024];
	        int length;
	        while ((length = is.read(buffer)) > 0) {
	            os.write(buffer, 0, length);
	        }
	    } finally {
	        is.close();
	        os.close();
	    }
	}
}
