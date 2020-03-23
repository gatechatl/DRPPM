package stjude.projects.xiaotuma.fredhutch.amlproject.fusion;

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
 * After the fusion is captured by Xiaotu's script, we will then run this script to capture the recurrent events.
 * This function generates a list of high quality/recurrent fusions. Next, we will manually go through the candidates.
 * @author tshaw
 *
 */
public class XiaotuMaCompileFusionListHQFebUpdate {


	public static String description() {
		return "After the fusion is captured by Xiaotu's script, we will then run this script to capture the recurrent events.\nThis function generates a list of high quality/recurrent fusions. Next, we will manually go through the candidates.\ninputFileSoheilSupplementaryFile = \\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\fusion\\Tim_Compile_Fusion_List\\Soheil_Gene_List_Supplementary_Table.txt\ninputFileSoheilFusionSupplementaryFile = \\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\fusion\\Tim_Compile_Fusion_List\\Soheil_Fusion_Genes_Supplementary_Table.txt\ninputFileAMLMajorFusionSupplementaryFile = \\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\fusion\\Tim_Compile_Fusion_List\\Major_AML_fusions_genes_20200105.txt\ninputFileAMLNovelFusionSupplementaryFile = \\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\fusion\\Tim_Compile_Fusion_List\\Novel_Fusions_Genes_20200105.txt\ninputFileCiceroOutput= \\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\fusion\\fus_20200113.txt\noutputFile = \\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\fusion\\Tim_Compile_Fusion_List\\PutativeList_20200113.txt\noutputRecurrentFile = \\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\fusion\\Tim_Compile_Fusion_List\\Recurrent_Fusion_List_20200113.txt";
	}
	public static String type() {
		return "RNASEQ";
	}
	public static String parameter_info() {
		return "[inputFileSoheilSupplementaryFile] [inputFileSoheilFusionSupplementaryFile] [inputFileAMLMajorFusionSupplementaryFile] [inputFileAMLNovelFusionSupplementaryFile] [inputFileCiceroOutput] [outputFile: putative candidates] [outputRecurrentFile]";
	}
	public static void execute(String[] args) {
		
		try {
			String inputFileSoheilSupplementaryFile = args[0]; //"\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\fusion\\Tim_Compile_Fusion_List\\Soheil_Gene_List_Supplementary_Table.txt";
			String inputFileSoheilFusionSupplementaryFile = args[1]; //"\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\fusion\\Tim_Compile_Fusion_List\\Soheil_Fusion_Genes_Supplementary_Table.txt";
			String inputFileAMLMajorFusionSupplementaryFile = args[2]; //"\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\fusion\\Tim_Compile_Fusion_List\\Major_AML_fusions_genes_20200105.txt";
			String inputFileAMLNovelFusionSupplementaryFile = args[3]; //"\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\fusion\\Tim_Compile_Fusion_List\\Novel_Fusions_Genes_20200105.txt";
			String inputFileCiceroOutput= args[4]; //"\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\fusion\\fus_20200113.txt";
			String outputFile = args[5]; //"\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\fusion\\Tim_Compile_Fusion_List\\PutativeList_20200113.txt";
			String outputRecurrentFile = args[6]; //"\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\fusion\\Tim_Compile_Fusion_List\\Recurrent_Fusion_List_20200113.txt";
			
			HashMap hits = new HashMap();
			HashMap oncogene = new HashMap();
			HashMap fusion_gene = new HashMap();
			HashMap major_fusion_gene = new HashMap();
			HashMap novel_fusion_gene = new HashMap();
			HashMap sample_map = new HashMap();
			HashMap recurrent_fusions = new HashMap();
			HashMap recurrent_fusions_samples = new HashMap();
			
			FileInputStream fstream = new FileInputStream(inputFileSoheilSupplementaryFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din)); 			
			while (in.ready()) {
				String str = in.readLine();
				oncogene.put(str, str);
			}
			in.close();
			
			
			fstream = new FileInputStream(inputFileSoheilFusionSupplementaryFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din)); 			
			while (in.ready()) {
				String str = in.readLine();
				fusion_gene.put(str, str);
			}
			in.close();
			

			
			fstream = new FileInputStream(inputFileAMLMajorFusionSupplementaryFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din)); 			
			while (in.ready()) {
				String str = in.readLine();
				major_fusion_gene.put(str, str);
			}
			in.close();
			

			
			fstream = new FileInputStream(inputFileAMLNovelFusionSupplementaryFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din)); 			
			while (in.ready()) {
				String str = in.readLine();
				novel_fusion_gene.put(str, str);
			}
			in.close();
			
			
			HashMap sample_fusion = new HashMap();
			//String inputFileCiceroOutput= "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\fusion\\Tim_Compile_Fusion_List\\20200104\\fus.txt";
			//String inputFileCiceroOutput= "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\fusion\\xiaotu_fus_review_01042020.txt";
			
			fstream = new FileInputStream(inputFileCiceroOutput);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String sampleName = split[0];
				sample_map.put(sampleName, sampleName);
				if (split.length > 9) {
					String rating = split[37];
					String geneA = split[18];
					String geneB = split[20];
					String type = split[9];
					String cicero_fusion_name = split[12];
					String fusion_name = geneA + ";" +  geneB + ";" + type + ";" + cicero_fusion_name;
					if (rating.equals("HQ")) {
						if (!sample_fusion.containsKey(sampleName + ";" + geneA + ";" + geneB + ";" + type + ";" + cicero_fusion_name + ";" + rating)) {
							if (recurrent_fusions.containsKey(fusion_name)) {
								int count = (Integer)recurrent_fusions.get(fusion_name);
								count++;
								recurrent_fusions.put(fusion_name, count);
								
								LinkedList list = (LinkedList)recurrent_fusions_samples.get(fusion_name);
								if (!list.contains(sampleName)) {
									list.add(sampleName);
								}
								recurrent_fusions_samples.put(fusion_name, list);
							} else {
								recurrent_fusions.put(fusion_name, 1);
								
								LinkedList list = new LinkedList();
								if (!list.contains(sampleName)) {
									list.add(sampleName);
								}
								recurrent_fusions_samples.put(fusion_name, list);
							}
						}						
					}
					sample_fusion.put(sampleName + ";" + geneA + ";" + geneB + ";" + type + ";" + cicero_fusion_name + ";" + rating, "");
					String usage = split[11];
					String line = geneA + ";" + geneB + ";" + type + ";" + cicero_fusion_name + ";" + rating;
					if (oncogene.containsKey(geneA)) {
						if (hits.containsKey(sampleName + "\t" + geneA)) {
							String prev = (String)hits.get(sampleName + "\t" + geneA);
							prev += "|" + line;
							hits.put(sampleName + "\t" + geneA, prev);
						} else {
							hits.put(sampleName + "\t" + geneA, line);
						}
					}
					
					if (oncogene.containsKey(geneB)) {
						if (hits.containsKey(sampleName + "\t" + geneB)) {
							String prev = (String)hits.get(sampleName + "\t" + geneB);
							prev += ";" + line;
							hits.put(sampleName + "\t" + geneB, prev);
						} else {
							hits.put(sampleName + "\t" + geneB, line);
						}
					}
				} else {
					System.out.println(str);
				}
			}
			

			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("SampleName");
			Iterator itr = oncogene.keySet().iterator();
			while (itr.hasNext()) {
				String geneName = (String)itr.next();
				out.write("\t" + geneName);
			}
			out.write("\n");

			Iterator itr2 = sample_map.keySet().iterator();
			while (itr2.hasNext()) {
				String sampleName = (String)itr2.next();
				out.write(sampleName);
				itr = oncogene.keySet().iterator();
				while (itr.hasNext()) {
					String geneName = (String)itr.next();
					
					String key = sampleName + "\t" + geneName;
					if (hits.containsKey(key)) {
						String line = (String)hits.get(key);
						out.write("\t" + line);
					} else {
						out.write("\t" + "NA");
					}
				}
				out.write("\n");
			}
			out.close();

			
			FileWriter fwriter_recurrent = new FileWriter(outputRecurrentFile);
			BufferedWriter out_recurrent = new BufferedWriter(fwriter_recurrent);
			out_recurrent.write("Fusion\tFrequency\tKnown_AML_Oncogene_Flag\tKnown_AML_Fusion_Flag\tMajor_Fusion_Driver_Flag\tPutative_Novel_Fusion\tSamples\tHitOnSJNORM\n");
			itr = recurrent_fusions.keySet().iterator();
			while (itr.hasNext()) {
				String fusion = (String)itr.next();
				String[] split_fusion = fusion.split(";");
				int count = (Integer)recurrent_fusions.get(fusion);
				boolean known_aml_oncogene = false;
				
				if (oncogene.containsKey(split_fusion[0])) {
					known_aml_oncogene = true;
				} else if (oncogene.containsKey(split_fusion[1])) {
					known_aml_oncogene = true;
				}
				
				boolean known_aml_fusions = false;
				
				if (fusion_gene.containsKey(split_fusion[0])) {
					known_aml_fusions = true;
				} else if (fusion_gene.containsKey(split_fusion[1])) {
					known_aml_fusions = true;
				}
				
				boolean major_fusion = false;
				
				if (major_fusion_gene.containsKey(split_fusion[0])) {
					major_fusion = true;
				} else if (major_fusion_gene.containsKey(split_fusion[1])) {
					major_fusion = true;
				}
				String sample_list_str = "";
				
				boolean putative_novel_fusion = false;				
				if (novel_fusion_gene.containsKey(split_fusion[0])) {
					putative_novel_fusion = true;
				} else if (novel_fusion_gene.containsKey(split_fusion[1])) {
					putative_novel_fusion = true;
				}
				
				if (recurrent_fusions_samples.containsKey(fusion)) {
					LinkedList list = (LinkedList)recurrent_fusions_samples.get(fusion);
					Iterator itr3 = list.iterator();
					while (itr3.hasNext()) {
						String sample_name = (String)itr3.next();											
						sample_list_str += sample_name + ",";
					}
				}
				if (fusion.equals("XPO1;XPO1;Internal_dup;XPO1_XPO1")) {
					major_fusion = false;
				}
				if (fusion.equals("XPO1;XPO1;Internal_dup;XPO1_XPO1_XPO1")) {
					major_fusion = false;
				}
				if (fusion.equals("XPO1;XPO1;Internal_dup;XPO1_XPO1_XPO1_XPO1")) {
					major_fusion = false;
				}
				if (fusion.equals("XPO1;XPO1;Internal_dup;XPO1_XPO1_XPO1_XPO1_XPO1")) {
					major_fusion = false;
				}
				if (fusion.equals("XPO1;XPO1;Internal_dup;XPO1_XPO1_XPO1_XPO1_XPO1_XPO1")) {
					major_fusion = false;
				}
				if (split_fusion[0].equals("NA") || split_fusion[1].equals("NA")) {
					major_fusion = false;
					known_aml_fusions = false;
					known_aml_oncogene = false;
					putative_novel_fusion = false;
				}
				boolean hit_on_sjnorm = false;
				if (sample_list_str.contains("SJNORM")) {
					hit_on_sjnorm = true;
				}
				System.out.println(fusion + "\t" + count + "\t" + known_aml_oncogene + "\t" + known_aml_fusions + "\t" + major_fusion + "\t" + putative_novel_fusion + "\t" + sample_list_str + "\t" + hit_on_sjnorm + "\t");
				

				out_recurrent.write(fusion + "\t" + count + "\t" + known_aml_oncogene + "\t" + known_aml_fusions + "\t" + major_fusion + "\t" + putative_novel_fusion + "\t" + sample_list_str + "\t" + hit_on_sjnorm + "\n");
			}
			out_recurrent.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
