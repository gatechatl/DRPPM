package rnaseq.splicing.csiminer.exonannotation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class CSIMinerAnnotateGeneWithNormalSampleIHCStaining {

	
	public static void main(String[] args) {
		
		try {
			
			HashMap gene_ihc_annotation_medium_high = new HashMap();
			HashMap gene_ihc_annotation_high = new HashMap();
			HashMap gene_brain_specific_annotation_medium_high = new HashMap();
			HashMap gene_brain_specific_annotation_high = new HashMap();
			HashMap gene_lung_specific_annotation_medium_high = new HashMap();
			HashMap gene_lung_specific_annotation_high = new HashMap();
			HashMap gene_pancreas_annotation_medium_high = new HashMap();
			HashMap gene_pancreas_annotation_high = new HashMap();
			
			HashMap gene_liver_annotation_medium_high = new HashMap();
			HashMap gene_liver_annotation_high = new HashMap();
			
			HashMap gene_pituitary_gland_annotation_medium_high = new HashMap();
			HashMap gene_pituitary_gland_annotation_high = new HashMap();
			
			HashMap gene_not_testis_ovary_medium_high = new HashMap();
			String outputFileMediumHigh = "/Users/4472414/Documents/Current_Manuscripts/CSIMiner/Current_Manuscript/NatureCommunication_Draft/CompleteAnnotationPipeline/ProteinAtlasIHC_Annotation/tim_summary_ihc_normal_tissue_medium_or_high.txt";
			String outputFileHigh = "/Users/4472414/Documents/Current_Manuscripts/CSIMiner/Current_Manuscript/NatureCommunication_Draft/CompleteAnnotationPipeline/ProteinAtlasIHC_Annotation/tim_summary_ihc_normal_tissue_high_only.txt";
			String inputFile = "/Users/4472414/Documents/Current_Manuscripts/CSIMiner/Current_Manuscript/NatureCommunication_Draft/CompleteAnnotationPipeline/ProteinAtlasIHC_Annotation/normal_tissue.tsv";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String gene = split[1];
				String tissue = split[2];
				String celltype = split[3];
				String level = split[4];
				
				if (level.equals("Medium") || level.equals("High")) {					
					if (!(tissue.equals("testis") || tissue.equals("ovary"))) {
						if (gene_not_testis_ovary_medium_high.containsKey(gene)) {
							String other_high_tissue = (String)gene_not_testis_ovary_medium_high.get(gene);
							gene_not_testis_ovary_medium_high.put(gene, other_high_tissue + "," + tissue);
						} else {
							gene_not_testis_ovary_medium_high.put(gene, tissue);
						}				
					}					
				}
				if (level.equals("Medium") || level.equals("High")) {
					if (gene_ihc_annotation_medium_high.containsKey(gene)) {
						String other_high_tissue = (String)gene_ihc_annotation_medium_high.get(gene);
						gene_ihc_annotation_medium_high.put(gene, other_high_tissue + "," + tissue);
					} else {
						gene_ihc_annotation_medium_high.put(gene, tissue);
					}				
				}
				if (level.equals("High")) {
					if (gene_ihc_annotation_high.containsKey(gene)) {
						String other_high_tissue = (String)gene_ihc_annotation_high.get(gene);
						gene_ihc_annotation_high.put(gene, other_high_tissue + "," + tissue);
					} else {
						gene_ihc_annotation_high.put(gene, tissue);
					}				
				}
				if (level.equals("Medium") || level.equals("High")) {
					if (tissue.equals("hippocampus") || tissue.equals("cerebellum") || tissue.equals("cerebral cortex") || tissue.equals("hypothalamus") || tissue.equals("substantia nigra")) {
						if (gene_brain_specific_annotation_medium_high.containsKey(gene)) {
							String other_high_tissue = (String)gene_brain_specific_annotation_medium_high.get(gene);
							gene_brain_specific_annotation_medium_high.put(gene, other_high_tissue + "," + tissue);
						} else {
							gene_brain_specific_annotation_medium_high.put(gene, tissue);
						}	
					}
					if (tissue.equals("lung")) {
						if (gene_lung_specific_annotation_medium_high.containsKey(gene)) {
							String other_high_tissue = (String)gene_lung_specific_annotation_medium_high.get(gene);
							gene_lung_specific_annotation_medium_high.put(gene, other_high_tissue + "," + tissue);
						} else {
							gene_lung_specific_annotation_medium_high.put(gene, tissue);
						}	
					}
					if (tissue.equals("pancreas")) {
						if (gene_pancreas_annotation_medium_high.containsKey(gene)) {
							String other_high_tissue = (String)gene_pancreas_annotation_medium_high.get(gene);
							gene_pancreas_annotation_medium_high.put(gene, other_high_tissue + "," + tissue);
						} else {
							gene_pancreas_annotation_medium_high.put(gene, tissue);
						}	
					}
					if (tissue.equals("pituitary gland")) {
						if (gene_pituitary_gland_annotation_medium_high.containsKey(gene)) {
							String other_high_tissue = (String)gene_pituitary_gland_annotation_medium_high.get(gene);
							gene_pituitary_gland_annotation_medium_high.put(gene, other_high_tissue + "," + tissue);
						} else {
							gene_pituitary_gland_annotation_medium_high.put(gene, tissue);
						}	
					}
					
					if (tissue.equals("liver")) {
						if (gene_liver_annotation_medium_high.containsKey(gene)) {
							String other_high_tissue = (String)gene_liver_annotation_medium_high.get(gene);
							gene_liver_annotation_medium_high.put(gene, other_high_tissue + "," + tissue);
						} else {
							gene_liver_annotation_medium_high.put(gene, tissue);
						}	
					}
				}
				
				if (level.equals("High")) {
					if (tissue.equals("hippocampus") || tissue.equals("cerebellum") || tissue.equals("cerebral cortex") || tissue.equals("hypothalamus") || tissue.equals("substantia nigra")) {
						if (gene_brain_specific_annotation_high.containsKey(gene)) {
							String other_high_tissue = (String)gene_brain_specific_annotation_high.get(gene);
							gene_brain_specific_annotation_high.put(gene, other_high_tissue + "," + tissue);
						} else {
							gene_brain_specific_annotation_high.put(gene, tissue);
						}	
					}
					if (tissue.equals("lung")) {
						if (gene_lung_specific_annotation_high.containsKey(gene)) {
							String other_high_tissue = (String)gene_lung_specific_annotation_high.get(gene);
							gene_lung_specific_annotation_high.put(gene, other_high_tissue + "," + tissue);
						} else {
							gene_lung_specific_annotation_high.put(gene, tissue);
						}	
					}
					if (tissue.equals("pancreas")) {
						if (gene_pancreas_annotation_high.containsKey(gene)) {
							String other_high_tissue = (String)gene_pancreas_annotation_high.get(gene);
							gene_pancreas_annotation_high.put(gene, other_high_tissue + "," + tissue);
						} else {
							gene_pancreas_annotation_high.put(gene, tissue);
						}	
					}
					if (tissue.equals("pituitary gland")) {
						if (gene_pituitary_gland_annotation_high.containsKey(gene)) {
							String other_high_tissue = (String)gene_pituitary_gland_annotation_high.get(gene);
							gene_pituitary_gland_annotation_high.put(gene, other_high_tissue + "," + tissue);
						} else {
							gene_pituitary_gland_annotation_high.put(gene, tissue);
						}	
					}
					if (tissue.equals("liver")) {
						if (gene_liver_annotation_high.containsKey(gene)) {
							String other_high_tissue = (String)gene_liver_annotation_high.get(gene);
							gene_liver_annotation_high.put(gene, other_high_tissue + "," + tissue);
						} else {
							gene_liver_annotation_high.put(gene, tissue);
						}	
					}
				}
			}
			in.close();
									

			FileWriter fwriter_medium_high = new FileWriter(outputFileMediumHigh);
			BufferedWriter out_medium_high = new BufferedWriter(fwriter_medium_high);
			
			FileWriter fwriter_high = new FileWriter(outputFileHigh);
			BufferedWriter out_high = new BufferedWriter(fwriter_high);
			out_medium_high.write("Gene\tHighInOneKeyTissue\tBrainStatus\tLungStatus\tPancreasStatus\tPituitaryStatus\tLiverStatus\tAllTissues\tTissueHighInAtLeastOneNormal(Exclude Testis Ovary)\n");
			out_high.write("Gene\tHighInOneKeyTissue\tBrainStatus\tLungStatus\tPancreasStatus\tPituitaryStatus\tLiverStatus\tAllTissues\tTissueHighInAtLeastOneNormal(Exclude Testis Ovary)\n");;
			Iterator itr = gene_ihc_annotation_medium_high.keySet().iterator();
			while (itr.hasNext()) {
				String gene = (String)itr.next();
				String all_tissue_annot = (String)gene_ihc_annotation_medium_high.get(gene);
				boolean medium_high_exclude_testis_ovary = false;
				if (gene_not_testis_ovary_medium_high.containsKey(gene)) {
					medium_high_exclude_testis_ovary = true;
				}
				boolean medium_high_brain = false;
				if (gene_brain_specific_annotation_medium_high.containsKey(gene)) {
					medium_high_brain = true;
				}
				boolean medium_high_lung = false;
				if (gene_lung_specific_annotation_medium_high.containsKey(gene)) {
					medium_high_lung = true;
				}
				boolean medium_high_pancreas = false;
				if (gene_pancreas_annotation_medium_high.containsKey(gene)) {
					medium_high_pancreas = true;
				}
				boolean medium_high_pituitary_gland = false;
				if (gene_pituitary_gland_annotation_medium_high.containsKey(gene)) {
					medium_high_pituitary_gland = true;
				}
				boolean medium_high_liver = false;
				if (gene_liver_annotation_medium_high.containsKey(gene)) {
					medium_high_liver = true;
				}
				out_medium_high.write(gene + "\t" + (medium_high_brain || medium_high_lung || medium_high_pancreas || medium_high_pituitary_gland || medium_high_liver) + "\t" + medium_high_brain + "\t" + medium_high_lung + "\t" + medium_high_pancreas + "\t" + medium_high_pituitary_gland + "\t" + medium_high_liver + "\t" + all_tissue_annot + "\t" + medium_high_exclude_testis_ovary + "\n");
			}
			out_medium_high.close();
			
			itr = gene_ihc_annotation_high.keySet().iterator();
			while (itr.hasNext()) {
				String gene = (String)itr.next();
				String all_tissue_annot = (String)gene_ihc_annotation_high.get(gene);
				boolean high_exclude_testis_ovary = false;
				if (gene_not_testis_ovary_medium_high.containsKey(gene)) {
					high_exclude_testis_ovary = true;
				}
				boolean high_brain = false;
				if (gene_brain_specific_annotation_high.containsKey(gene)) {
					high_brain = true;
				}
				boolean high_lung = false;
				if (gene_lung_specific_annotation_high.containsKey(gene)) {
					high_lung = true;
				}
				boolean high_pancreas = false;
				if (gene_pancreas_annotation_high.containsKey(gene)) {
					high_pancreas = true;
				}
				boolean high_pituitary_gland = false;
				if (gene_pituitary_gland_annotation_high.containsKey(gene)) {
					high_pituitary_gland = true;
				}
				boolean high_liver = false;
				if (gene_liver_annotation_high.containsKey(gene)) {
					high_liver = true;
				}
				out_high.write(gene + "\t" + (high_brain || high_lung || high_pancreas || high_pituitary_gland || high_liver) + "\t" + high_brain + "\t" + high_lung + "\t" + high_pancreas + "\t" + high_pituitary_gland + "\t" + high_liver + "\t" + all_tissue_annot + "\t" + high_exclude_testis_ovary + "\n");
			}
			out_high.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
