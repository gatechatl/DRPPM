package TranscriptionFactorTools;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import DifferentialExpression.AddAnnotation2DiffFisher;
import Statistics.General.MathTools;


public class TFGeneEnrichmentFilter {
	public static void execute(String[] args) {
		try {
									
			/*
			BLALOCK_ALZHEIMERS_DISEASE_DN_List.txt            Histone_Modification_List.txt
			BLALOCK_ALZHEIMERS_DISEASE_INCIPIENT_DN_List.txt  KEGG_Alzheimer_List.txt
			BLALOCK_ALZHEIMERS_DISEASE_INCIPIENT_UP_List.txt  KEGG_Neurotrophin_Signaling_Pathway_List.txt
			BLALOCK_ALZHEIMERS_DISEASE_UP_List.txt            Kinase_List.txt
			Cell_Differentiation_List.txt                     LEE_NEURAL_CREST_STEM_CELL_DN_List.txt
			Chromatin_Remodeling_List.txt                     LEE_NEURAL_CREST_STEM_CELL_UP_List.txt
			Cytokines_Growth_Factor_List.txt                  Oncogene_List.txt
			DNA_Modification_List.txt                         Transcription_Factor_List.txt
			*/
			
			String inputFile = args[0];
			
			int possible_hit = 0;
			HashMap map = AddAnnotation2DiffFisher.generateHashMapList(args[1]);
			int totalGeneCount = new Integer(args[2]);
			
			HashMap gsea_terms = new HashMap();
			String filterTermFile = args[3];
			FileInputStream fstream = new FileInputStream(filterTermFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				gsea_terms.put(split[0], split[1] + "\t" + split[2]);
			}
			in.close();
			
			HashMap total_term_count = new HashMap();
			Iterator itr = map.keySet().iterator();
			String additional_header = "";
			LinkedList tags = new LinkedList();
			while (itr.hasNext()) {
				String tag = (String)itr.next();
				tags.add(tag);
				total_term_count.put(tag, 0);
				additional_header += "\t" + tag;
			}
			HashMap terms = new HashMap();
			additional_header += "\tMETAINFOHIT";
			
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine() + additional_header;
			//System.out.println(header);
			while (in.ready()) {
				String str = in.readLine();
				possible_hit++;
				boolean isTRUE = false;
				String[] split = str.split("\t");
				String geneName = split[0].replaceAll("\"", "");
				String annotation = "";
				itr = tags.iterator();
				while (itr.hasNext()) {
					String tag = (String)itr.next();
					HashMap geneList = (HashMap)map.get(tag);
					if (geneList.containsKey(geneName)) {
						if (total_term_count.containsKey(tag)) {
							int total = (Integer)total_term_count.get(tag);
							total_term_count.put(tag, (total + 1));
						}
						if (terms.containsKey(tag)) {
							String name = (String)terms.get(tag);
							terms.put(tag, name + "," + geneName);
						} else {
							terms.put(tag, geneName);
						}
						annotation += "\tTRUE";
						isTRUE = true;
					} else {
						annotation += "\tFALSE";	
					}
				}
				if (isTRUE) {
					annotation += "\tTRUE";
				} else {
					annotation += "\tFALSE";
				}
				//System.out.println(str + annotation);
			}
			in.close();
			
			System.out.println("Tag\tPvalue\tPercentage\tnum_hit\tgeneSetSize\tDiffGeneListSize\tGeneSetGenes\tNumHitFromFilter\tGeneInFilter");
			itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String tag = (String)itr.next();
				if (gsea_terms.containsKey(tag)) {
					HashMap geneList = (HashMap)map.get(tag);
					possible_hit = possible_hit;
					int hit = (Integer)total_term_count.get(tag);
					double pval = MathTools.fisherTest(hit, geneList.size(), possible_hit - hit, totalGeneCount - geneList.size());
					System.out.println(tag + "\t" + pval + "\t" + new Double(hit) / geneList.size() + "\t" + hit + "\t" + geneList.size() + "\t" + possible_hit + "\t" + terms.get(tag) + "\t" + gsea_terms.get(tag));
					//double pval = fisherTest(overlap.size(), epigenetic.size(), topGene.size() - overlap.size(), total - epigenetic.size());
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
