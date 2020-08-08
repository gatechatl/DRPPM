package stjude.projects.mondirakundu;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class MondiraKunduGenerateProteinProteinInteraction {

	
	public static void main(String[] args) {
		
		try {
			HashMap PPI_Database = new HashMap();
			HashMap entire_ulk1_als_proteome = new HashMap();
			HashMap stress_granule = new HashMap();
			//String inputFile = "Z:\\ResearchHome\\ProjectSpace\\kundugrp\\ULK\\common\\ULK1_PROTEOMICS\\ULK1_PPI\\PMID26777405\\stress_granule_proteome_mammal_geneName.txt";
			String inputFile = "\\\\gsc.stjude.org\\project_space\\kundugrp\\ULK\\common\\ULK1_PROTEOMICS\\ULK1_PPI\\COMBINED_STRESSGRANULE\\StressGranule.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din)); 			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				stress_granule.put(split[0], split[0]);
				entire_ulk1_als_proteome.put(split[0], split[0]);
			}
			in.close();
			
			HashMap autophagy = new HashMap();
			//inputFile = "Z:\\ResearchHome\\ProjectSpace\\kundugrp\\ULK\\common\\ULK1_PROTEOMICS\\ULK1_PPI\\PMID20972215_Autophagy\\autophagy.txt";
			//inputFile = "Z:\\ResearchHome\\ProjectSpace\\kundugrp\\ULK\\common\\ULK1_PROTEOMICS\\ULK1_PPI\\PMID_HADb\\autophagy.txt";
			inputFile = "\\\\gsc.stjude.org\\project_space\\kundugrp\\ULK\\common\\ULK1_PROTEOMICS\\ULK1_PPI\\COMBINED_AUTOPHAGY\\autophagy.txt";
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din)); 			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				autophagy.put(split[0], split[0]);
				entire_ulk1_als_proteome.put(split[0], split[0]);
			}
			in.close();
			
			//HashMap ulk1_a_sg_ptals = new HashMap(); // autophagy, stress granule, paultaylorals
			HashMap ulk1_a_only = new HashMap(); // autophagy only
			HashMap ulk1_sg_only = new HashMap(); // stress granule only
			HashMap ulk1_a_sg = new HashMap(); // autophagy, stress granule
			HashMap ulk1_interactome = new HashMap();
			inputFile = "\\\\gsc.stjude.org\\project_space\\kundugrp\\ULK\\common\\ULK1_PROTEOMICS\\ULK1_PPI\\ULK1_Interactome\\ULK1_interactome_human_geneName.txt";
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din)); 			
			while (in.ready()) {
				String str = in.readLine();
				if (!str.equals("GeneName")) {
					String[] split = str.split("\t");
					entire_ulk1_als_proteome.put(split[0], split[0]);
					ulk1_interactome.put(split[0], split[0]);
					if (autophagy.containsKey(split[0]) && stress_granule.containsKey(split[0])) {
						ulk1_a_sg.put(split[0], split[0]);					
					} else if (autophagy.containsKey(split[0])) {
						ulk1_a_only.put(split[0], split[0]);
					} else if (stress_granule.containsKey(split[0])) {
						ulk1_sg_only.put(split[0], split[0]);
					}
				}
			}
			in.close();
			System.out.println("Stress granule: " + stress_granule.size());
			System.out.println("Autophagy: " + autophagy.size());
			System.out.println("Total ULK1: " + ulk1_interactome.size());
			
			HashMap paultaylorALSgene = new HashMap();
			///home/yli4/PPIs/2017_Feb/composite_database/BioPlex_String400_Inweb150/BioPlex_String400_Inweb150_v1.0.1.sif
			inputFile = "\\\\gsc.stjude.org\\project_space\\tayl1grp\\ALS_Family3\\common\\CMPB\\BioinfoCore\\ALS_Risk_Gene\\PaulTaylorList.txt";
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din)); 			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				paultaylorALSgene.put(split[0], split[0]);	
				entire_ulk1_als_proteome.put(split[0], split[0]);
			}
			in.close();
			
			HashMap online_ulk1_network = new HashMap();
			HashMap ulk1_network_stressgranule = new HashMap();
			HashMap ulk1_network_autophagy = new HashMap();
			HashMap ulk1_hit = new HashMap();
			HashMap paultaylor_ALS_hit = new HashMap();
			HashMap network = new HashMap();
			//inputFile = "\\\\gsc.stjude.org\\clusterhome\\tshaw\\PROTEOMICS\\NETWORK\\YUXIN_Work\\BioPlex_String400_Inweb150_Biogrid_v2.sif";
			//inputFile = "\\\\gsc.stjude.org\\project_space\\kundugrp\\ULK\\common\\ULK1_PROTEOMICS\\ULK1_PPI\\BIOGRID\\BIOGRID-ALL-3.4.157.mitab.sif";
			inputFile = "\\\\gsc.stjude.org\\project_space\\kundugrp\\ULK\\common\\ULK1_PROTEOMICS\\ULK1_PPI\\BIOGRID\\BioPlex_String400_Inweb150_Biogrid_ULK1mondira_v3.sif";
			//inputFile = "C:\\Users\\tshaw\\project\\Pathways\\Network\\BioPlex_String400_Inweb150_v1.0.1.sif";
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din)); 			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				PPI_Database.put(split[0], split[0]);
				PPI_Database.put(split[2], split[2]);
				
				if (network.containsKey(split[0])) {
					LinkedList list = (LinkedList)network.get(split[0]);
					if (!list.contains(split[2])) {
						list.add(split[2]);
					}
					network.put(split[0], list);
				} else {
					LinkedList list = new LinkedList();
					list.add(split[2]);
					network.put(split[0], list);
				}
				
				if (network.containsKey(split[2])) {
					LinkedList list = (LinkedList)network.get(split[2]);
					if (!list.contains(split[0])) {
						list.add(split[0]);
					}
					network.put(split[2], list);
				} else {
					LinkedList list = new LinkedList();
					list.add(split[0]);
					network.put(split[2], list);
				}				

				// check which genes are present for ULK1 gene list
				if (ulk1_interactome.containsKey(split[0])) {
					ulk1_hit.put(split[0], split[0]);
				}
				if (ulk1_interactome.containsKey(split[2])) {
					ulk1_hit.put(split[2], split[2]);
				}
				
				// check which genes are present for paul taylor's ALS gene list
				if (paultaylorALSgene.containsKey(split[0])) {
					paultaylor_ALS_hit.put(split[0], split[0]);
				}
				if (paultaylorALSgene.containsKey(split[2])) {
					paultaylor_ALS_hit.put(split[2], split[2]);
				}
				if (split[0].equals("ULK1") && stress_granule.containsKey(split[2])) {
					ulk1_network_stressgranule.put(split[2], split[2]);
				}
				if (split[0].equals("ULK1") && autophagy.containsKey(split[2])) {
					ulk1_network_autophagy.put(split[2], split[2]);
				}
				if (split[2].equals("ULK1") && stress_granule.containsKey(split[0])) {
					ulk1_network_stressgranule.put(split[0], split[0]);
				}
				if (split[2].equals("ULK1") && autophagy.containsKey(split[0])) {
					ulk1_network_autophagy.put(split[0], split[0]);
				}
				if (split[0].equals("ULK1") || split[2].equals("ULK1")) {
					online_ulk1_network.put(split[0], split[0]);
					online_ulk1_network.put(split[2], split[2]);
					entire_ulk1_als_proteome.put(split[0],  split[0]);
					entire_ulk1_als_proteome.put(split[2],  split[2]);
				}
			}
			in.close();
			
			HashMap ulk1_neighbor = new HashMap();			
			HashMap paultaylorALSgene_neighbor = new HashMap();			
			
			System.out.println("Ulk1_hit: " + ulk1_hit.size());
			
			// iterate through all the ULK1 protein interactome
			Iterator itr = ulk1_interactome.keySet().iterator();
			while (itr.hasNext()) {
				String geneName = (String)itr.next();
				if (!ulk1_hit.containsKey(geneName)) {
					System.out.println("ULK1 Missing: " + geneName);
				}
				
				if (network.containsKey(geneName)) {
					LinkedList list = (LinkedList)network.get(geneName);
					Iterator itr2 = list.iterator();
					while (itr2.hasNext()) {
						String list_geneName = (String)itr2.next();
						ulk1_neighbor.put(list_geneName, list_geneName);
					}
				}
				
			}
			
			itr = paultaylorALSgene.keySet().iterator();
			while (itr.hasNext()) {
				String geneName = (String)itr.next();
				if (!paultaylor_ALS_hit.containsKey(geneName)) {
					System.out.println("PTaylor ALS Missing: " + geneName);
				}
				
				if (network.containsKey(geneName)) {
					LinkedList list = (LinkedList)network.get(geneName);
					Iterator itr2 = list.iterator();
					while (itr2.hasNext()) {
						String list_geneName = (String)itr2.next();
						if (paultaylorALSgene_neighbor.containsKey(list_geneName)) {
							LinkedList list2 = (LinkedList)paultaylorALSgene_neighbor.get(list_geneName);
							if (!list2.contains(geneName)) {
								list2.add(geneName);
							}
							paultaylorALSgene_neighbor.put(list_geneName, list2);
						} else {
							LinkedList list2 = new LinkedList();
							list2.add(geneName);
							paultaylorALSgene_neighbor.put(list_geneName, list2);
						}
					}
				}
				
			}
			
			
			System.out.println("TotalGenes: " + network.size());
			
			System.out.println("ulk1_a_sg: " + ulk1_a_sg.size());
			System.out.println("ulk1_a_only: " + ulk1_a_only.size());
			System.out.println("ulk1_sg_only: " + ulk1_sg_only.size());

			itr = ulk1_a_sg.keySet().iterator();
			while (itr.hasNext()) {
				String gene = (String)itr.next();
				System.out.println("In both: " + gene);
			}
			
			
			HashMap stress_granule_neighbor = new HashMap(); // obtain neighbor that is in between paul taylor's gene list and ulk1 for stress granule 
			HashMap autophagy_neighbor = new HashMap(); // obtain neighbor that is in between paul taylor's gene list and ulk1 for autophagy
			HashMap stress_granule_autophagy_neighbor = new HashMap(); // obtain neighbor that is in between paul taylor's gene list and ulk1 for autophagy and stress granule
			// check what are ulk1 and paul taylor als gene overlapping neighbors
			//itr = ulk1_neighbor.keySet().iterator();
			itr = ulk1_hit.keySet().iterator();
			while (itr.hasNext()) {
				String geneName = (String)itr.next();
				if (paultaylorALSgene_neighbor.containsKey(geneName)) {
					LinkedList list2 = (LinkedList)paultaylorALSgene_neighbor.get(geneName);
					String line = "";
					Iterator itr3 = list2.iterator();
					while (itr3.hasNext()) {
						line += "\t" + itr3.next();
					}
						
					//if (!ulk1_a_sg.containsKey(geneName) && !ulk1_a_only.containsKey(geneName) && !ulk1_sg_only.containsKey(geneName)) {
						//System.out.println("neighbor: " + geneName);
						if (autophagy.containsKey(geneName) && stress_granule.containsKey(geneName)) {
							itr3 = list2.iterator();
							while (itr3.hasNext()) {
								String als_gene = (String)itr3.next(); 
								stress_granule_autophagy_neighbor.put(als_gene, als_gene);
							}
							
							System.out.println("ulk1_a_sg_neighbor: " + geneName + line);
						} else if (autophagy.containsKey(geneName)) {
							itr3 = list2.iterator();
							while (itr3.hasNext()) {
								String als_gene = (String)itr3.next(); 
								autophagy_neighbor.put(als_gene, als_gene);
							}							
							
							System.out.println("ulk1_a_neighbor: " + geneName + line);
						} else if (stress_granule.containsKey(geneName)) {
							System.out.println("ulk1_sg_neighbor: " + geneName + line);
							itr3 = list2.iterator();
							while (itr3.hasNext()) {
								String als_gene = (String)itr3.next();
								stress_granule_neighbor.put(als_gene, als_gene);
							}
						}
					//}
					//System.out.println("Neighbor: " + geneName);
				}
			}
			
			// generate a matrix
			String outputFile3 = "\\\\gsc.stjude.org\\project_space\\kundugrp\\ULK\\common\\ULK1_PROTEOMICS\\ULK1_PPI\\ComprehensiveTable_modules.txt";
			FileWriter fwriter3 = new FileWriter(outputFile3);
			BufferedWriter out3 = new BufferedWriter(fwriter3);	
			out3.write("Gene\tULK1-house-proteomic\tOnline-ULK1-interactome\tALS\tStressGranule\tAutophagy\tPresent_In_PPI_DB\n");
			Iterator itr_comprehensive = entire_ulk1_als_proteome.keySet().iterator();
			while (itr_comprehensive.hasNext()) {
				String gene = (String)itr_comprehensive.next();
				boolean boolean_autophagy = false;
				if (autophagy.containsKey(gene)) {
					boolean_autophagy = true;
				}
				boolean boolean_stress_granule = false;
				if (stress_granule.containsKey(gene)) {
					boolean_stress_granule = true;
				}
				boolean boolean_ulk1_interactome = false;
				if (ulk1_interactome.containsKey(gene)) {
					boolean_ulk1_interactome = true;
				}
				boolean boolean_paultaylorALSgene = false;
				if (paultaylorALSgene.containsKey(gene)) {
					boolean_paultaylorALSgene = true;
				}
				boolean boolean_online_ulk1_network = false;
				if (online_ulk1_network.containsKey(gene)) {
					boolean_online_ulk1_network = true;
				}
				boolean boolean_PPI_Database = false;
				if (PPI_Database.containsKey(gene)) {
					boolean_PPI_Database = true;
				}
				out3.write(gene + "\t" + boolean_ulk1_interactome + "\t" + boolean_online_ulk1_network + "\t" + boolean_paultaylorALSgene + "\t" + boolean_stress_granule + "\t" + boolean_autophagy + "\t" + boolean_PPI_Database + "\n");
			}
			out3.close();
			
			HashMap ulk1_compile = new HashMap();
			HashMap already_used = new HashMap(); // keeping an already used gene list;
			String outputFile = "\\\\gsc.stjude.org\\project_space\\kundugrp\\ULK\\common\\ULK1_PROTEOMICS\\ULK1_PPI\\ULK1_modules.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);	
			out.write("M1_ULK\tULK1\n");
			already_used.put("ULK1", "ULK1");
			out.write("M2_ALS_OVERLAP\t");			
			itr = stress_granule_autophagy_neighbor.keySet().iterator();
			while (itr.hasNext()) {
				String gene = (String)itr.next();
				if (paultaylorALSgene.containsKey(gene) && stress_granule.containsKey(gene) && autophagy.containsKey(gene)) {					
					out.write(gene + ",");
					already_used.put(gene, gene);
				}
				
			}
			out.write("\n");
			out.write("M3_OVERLAP\t");
			itr = ulk1_a_sg.keySet().iterator();
			while (itr.hasNext()) {
				String gene = (String)itr.next();
				if (!gene.equals("ULK1") && !already_used.containsKey(gene)) {
					out.write(gene + ",");
					already_used.put(gene, gene);
				}				
			}
			out.write("\n");
			out.write("M4_AUTOPHAGY\t");
			itr = ulk1_a_only.keySet().iterator();
			while (itr.hasNext()) {
				String gene = (String)itr.next();
				if (!already_used.containsKey(gene)) {
					out.write(gene + ",");
					already_used.put(gene, gene);
				}
			}
			out.write("\n");
			out.write("M5_STRESSGRANULE\t");
			itr = ulk1_sg_only.keySet().iterator();
			while (itr.hasNext()) {
				String gene = (String)itr.next();
				if (!already_used.containsKey(gene)) {
					out.write(gene + ",");
					already_used.put(gene, gene);
				}
			}
			out.write("\n");			
			
			HashMap other_ALS = new HashMap();
			
			out.write("\n");
			
			out.write("M6_ALS_STRESSGRANULE\t");			
			itr = stress_granule_neighbor.keySet().iterator();
			while (itr.hasNext()) {
				String gene = (String)itr.next();
				if (!ulk1_interactome.containsKey(gene) && stress_granule.containsKey(gene) && !already_used.containsKey(gene)) {					
					out.write(gene + ",");
					already_used.put(gene, gene);
				}
				
			}
			out.write("\n");
			
			out.write("M7_ALS_AUTOPHAGY\t");			
			itr = autophagy_neighbor.keySet().iterator();
			while (itr.hasNext()) {
				String gene = (String)itr.next();
				if (!ulk1_interactome.containsKey(gene) && autophagy.containsKey(gene) && !already_used.containsKey(gene)) {					
					out.write(gene + ",");
					already_used.put(gene, gene);
				}
				
			}
			
			HashMap overlap_all_ulk1_network_sg_a = new HashMap();;
			System.out.println("ULK1 Stress Granule: " + ulk1_network_stressgranule.size());
			
			itr = ulk1_network_stressgranule.keySet().iterator();
			while (itr.hasNext()) {
				String gene = (String)itr.next();
				if (ulk1_network_autophagy.containsKey(gene)) {
					overlap_all_ulk1_network_sg_a.put(gene, gene);
				}
				System.out.println(gene);
			}
			System.out.println("ULK1 Autophagy: " + ulk1_network_autophagy.size());
			itr = ulk1_network_autophagy.keySet().iterator();
			while (itr.hasNext()) {
				String str = (String)itr.next();
				System.out.println(str);
			}
			
			
			
			out.write("\n");
			out.write("M8_ULK1_INTERACTOME_OVERLAP\t");			
			itr = overlap_all_ulk1_network_sg_a.keySet().iterator();
			while (itr.hasNext()) {
				String gene = (String)itr.next();
				if (!already_used.containsKey(gene)) {
					out.write(gene + ",");
					already_used.put(gene, gene);
				}
				//System.out.println(str);
			}
			out.write("\n");
			
			out.write("M9_ULK1_INTERACTOME_STRESSGRANULE\t");			
			itr = ulk1_network_stressgranule.keySet().iterator();
			while (itr.hasNext()) {
				String gene = (String)itr.next();
				if (!already_used.containsKey(gene)) {
					out.write(gene + ",");
					already_used.put(gene, gene);
				}
				//System.out.println(str);
			}
			out.write("\n");
			out.write("M10_ULK1_INTERACTOME_AUTOPHAGY\t");			
			itr = ulk1_network_autophagy.keySet().iterator();
			while (itr.hasNext()) {
				String gene = (String)itr.next();
				if (!already_used.containsKey(gene)) {
					out.write(gene + ",");
					already_used.put(gene, gene);
				}
				//System.out.println(str);
			}
			out.write("\n");
			/*out.write("\n");
			
			out.write("M8_ULK1_Other\t");						
			itr = ulk1_interactome.keySet().iterator();
			while (itr.hasNext()) {
				String gene = (String)itr.next();
				if (!ulk1_a_sg.containsKey(gene) && !ulk1_a_only.containsKey(gene) && !ulk1_sg_only.containsKey(gene)) {
					if (!gene.equals("ULK1")) {
						out.write(gene + ",");
					}
				}
			}*/
			
			out.close();
			/*out.write("M7_ALS_OTHER\t");			
			itr = stress_granule_autophagy_neighbor.keySet().iterator();
			while (itr.hasNext()) {
				String gene = (String)itr.next();
				if (!ulk1_interactome.containsKey(gene) && autophagy.containsKey(gene) && !already_used.containsKey(gene)) {					
					out.write(gene + ",");
				}
				
			}
			out.write("\n");
			out.close();*/
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
