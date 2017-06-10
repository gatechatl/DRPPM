package stjude.projects.peng;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Create a list of proteins that contains homology annotations
 * @author tshaw
 *
 */
public class GenerateCoreHomologTableMGISummary {


	public static String description() {
		return "Generate a table describing known homologs for proteins present in uniprot's core table.";
	}
	public static String type() {
		return "MISC";
	}
	public static String parameter_info() {
		return "[inputFileMGITable] [human_coreFasta] [mouse_coreFasta] [human_conversionTable] [mouse_conversionTable] [outputFileSummaryTable]";
	}
	public static void execute(String[] args) {
		
		try {
			
			HashMap map_human2mouse = new HashMap();
			HashMap map_refseq_human2mouse = new HashMap();
			
			HashMap map_mouse2human = new HashMap();
			HashMap map_refseq_mouse2human = new HashMap();
			
			String inputFileMGITable = args[0];
			String human_coreFasta = args[1];
			String mouse_coreFasta = args[2];
			String human_conversionTable = args[3];
			String mouse_conversionTable = args[4];
			String human2mouse_blast_result = args[5];
			String mouse2human_blast_result = args[6];
			String outputFileSummaryTable = args[7];
			
			FileWriter fwriter = new FileWriter(outputFileSummaryTable);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			HashMap human_uniprot2entrez = new HashMap();
			HashMap human_uniprot2geneName = new HashMap();
			HashMap human_refseq2entrez = new HashMap();
			HashMap human_refseq2geneName = new HashMap();
			HashMap mouse_uniprot2entrez = new HashMap();
			HashMap mouse_uniprot2geneName = new HashMap();
			HashMap mouse_refseq2entrez = new HashMap();
			HashMap mouse_refseq2geneName = new HashMap();
			HashMap refseq2uniprot = new HashMap();
			String human_homologID = "NA";
			String human_uniprot_geneID = "NA";
			String mouse_homologID = "NA";
			String mouse_uniprot_geneID = "NA";
			
			
			FileInputStream fstream = new FileInputStream(human_conversionTable);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String uniprot_accession = split[0];
				String entrezID = split[1];							
				String geneName = split[4];
				String[] refseqs = split[2].split(",");
				for (String refseq: refseqs) {
					human_refseq2entrez.put(refseq.split("\\.")[0], entrezID);
					human_refseq2geneName.put(refseq.split("\\.")[0], geneName);
					refseq2uniprot.put(refseq.split("\\.")[0], uniprot_accession);
				}
				human_uniprot2entrez.put(uniprot_accession, entrezID);
				human_uniprot2geneName.put(uniprot_accession, geneName);
			}
			in.close();
			
			fstream = new FileInputStream(mouse_conversionTable);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String uniprot_accession = split[0];
				String entrezID = split[1];
				String geneName = split[4];
				String[] refseqs = split[2].split(",");
				for (String refseq: refseqs) {
					mouse_refseq2entrez.put(refseq.split("\\.")[0], entrezID);
					mouse_refseq2geneName.put(refseq.split("\\.")[0], geneName);
					refseq2uniprot.put(refseq.split("\\.")[0], uniprot_accession);
				}
				mouse_uniprot2entrez.put(uniprot_accession, entrezID);
				mouse_uniprot2geneName.put(uniprot_accession, geneName);
			}
			in.close();
			
			String[] human_refseqs = {};
			String[] mouse_refseqs = {};
			fstream = new FileInputStream(inputFileMGITable);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				str += "\tNA";
				String[] split = str.split("\t");
				String homologID = split[0];
				String geneName = split[3];
				String entrezID = split[4];
				String speciesID = split[2];
				String[] refseqs = split[11].split(",");
				
				String uniprot_geneID = split[split.length - 2];
				if (uniprot_geneID.equals("")) {
					uniprot_geneID = "NA";
				}
				if (!uniprot_geneID.equals("NA")) {
					for (String refseq: refseqs) {
						refseq2uniprot.put(refseq.split("\\.")[0], uniprot_geneID);
					}
				}
				// if species is human 9606
				 
				if (speciesID.equals("9606")) {
					human_refseqs = refseqs;
					//System.out.println("hello");
					human_homologID = homologID;
					human_uniprot_geneID = uniprot_geneID;
					if (human_uniprot2geneName.containsKey(human_uniprot_geneID)){
						String db_geneName = (String)human_uniprot2geneName.get(human_uniprot_geneID);
						if (!db_geneName.equals(geneName) && !human_uniprot_geneID.equals("NA")) {
							System.out.println(human_uniprot_geneID + " GeneName mismatch\t" + db_geneName + "\t" + geneName);
							human_uniprot2geneName.put(human_uniprot_geneID, geneName);
						}
					} else {
						human_uniprot2geneName.put(human_uniprot_geneID, geneName);
					}
					
					if (human_uniprot2entrez.containsKey(human_uniprot_geneID)){
						String db_entrez = (String)human_uniprot2entrez.get(human_uniprot_geneID);
						if (!db_entrez.equals(entrezID) && !human_uniprot_geneID.equals("NA")) {
							System.out.println(human_uniprot_geneID + " entrezID mismatch\t" + db_entrez + "\t" + entrezID);
						}
					} else {
						human_uniprot2entrez.put(human_uniprot_geneID, entrezID);
					}
					
					for (String refseq: refseqs) {
						human_refseq2entrez.put(refseq.split("\\.")[0], entrezID);
						//System.out.println(refseq.split("\\.")[0] + "\t" + entrezID);
						human_refseq2geneName.put(refseq.split("\\.")[0], geneName);
						
						
					}
				}
				
				if (speciesID.equals("10090")) {
					mouse_refseqs = refseqs;
					mouse_homologID = homologID;
					mouse_uniprot_geneID = uniprot_geneID;
					if (mouse_uniprot2geneName.containsKey(mouse_uniprot_geneID)){
						String db_geneName = (String)mouse_uniprot2geneName.get(mouse_uniprot_geneID);
						if (!db_geneName.equals(geneName) && !mouse_uniprot_geneID.equals("NA")) {
							System.out.println(mouse_uniprot_geneID + " GeneName mismatch\t" + db_geneName + "\t" + geneName);
							mouse_uniprot2geneName.put(mouse_uniprot_geneID, geneName);
						}
					} else {
						mouse_uniprot2geneName.put(mouse_uniprot_geneID, geneName);
					}
					
					if (mouse_uniprot2entrez.containsKey(mouse_uniprot_geneID)){
						String db_entrez = (String)mouse_uniprot2entrez.get(mouse_uniprot_geneID);
						if (!db_entrez.equals(entrezID) && !mouse_uniprot_geneID.equals("NA")) {
							System.out.println(mouse_uniprot_geneID + " entrezID mismatch\t" + db_entrez + "\t" + entrezID);
						}
					} else {
						mouse_uniprot2entrez.put(mouse_uniprot_geneID, entrezID);
					}
					for (String refseq: refseqs) {
						mouse_refseq2entrez.put(refseq.split("\\.")[0], entrezID);
						mouse_refseq2geneName.put(refseq.split("\\.")[0], geneName);
						
						
					}
				}
				
				if (human_homologID.equals(mouse_homologID)) {
					if (!mouse_uniprot_geneID.equals("NA")) {
						map_human2mouse.put(human_uniprot_geneID, mouse_uniprot_geneID);
						
					} else {
						if (speciesID.equals("9606")) {
							for (String refseq: human_refseqs) {
								//map_human2mouse.put(refseq.split("\\.")[0], mouse_uniprot_geneID);
								map_refseq_human2mouse.put(refseq.split("\\.")[0], mouse_refseqs);
								//if (refseq.split("\\.")[0].equals("NP_001269371")) {
									
									//System.out.println("Found: NP_001269371\t" + mouse_refseqs[0]);
								//}
							}
						}
					}
					if (!human_uniprot_geneID.equals("NA")) {
						map_mouse2human.put(mouse_uniprot_geneID, human_uniprot_geneID);
						
					} else {
						if (speciesID.equals("10090")) {
							for (String refseq: mouse_refseqs) {
								//map_mouse2human.put(refseq.split("\\.")[0], human_uniprot_geneID);
								map_refseq_mouse2human.put(refseq.split("\\.")[0], human_refseqs);
								//if (refseq.split("\\.")[0].equals("NP_001269371")) {
									
									//System.out.println("Found: NP_001269371\t" + mouse_refseqs[0]);
								//}
							}
						}
					}
				}
			
			}
			in.close();

			HashMap human_accessions2geneName = new HashMap();
			HashMap human_geneName2accession = new HashMap();
			fstream = new FileInputStream(human_coreFasta);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains(">")) {
					String[] split = str.split("\\|");
					String geneName = split[2].split(" ")[0].replaceAll("_HUMAN", "");
					
					if (split[1].contains("NP_")) {
						if (refseq2uniprot.containsKey(split[1].split("\\.")[0])) {
							String uniprot = (String)refseq2uniprot.get(split[1].split("\\.")[0]);
							human_accessions2geneName.put(uniprot, geneName);
							//human_geneName2accession.put(geneName, uniprot);
						}
					}
					human_geneName2accession.put(geneName, split[1]);
					human_accessions2geneName.put(split[1], geneName);
				}
			}
			HashMap mouse_accessions2geneName = new HashMap();
			HashMap mouse_geneName2accession = new HashMap();
			fstream = new FileInputStream(mouse_coreFasta);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains(">")) {
					String[] split = str.split("\\|");
					String geneName = split[2].split(" ")[0].replaceAll("_MOUSE", "");
					
					if (split[1].contains("NP_")) {
						if (refseq2uniprot.containsKey(split[1].split("\\.")[0])) {
							String uniprot = (String)refseq2uniprot.get(split[1].split("\\.")[0]);
							mouse_accessions2geneName.put(uniprot, geneName);
							//mouse_geneName2accession.put(geneName, uniprot);
						}
					}
					mouse_geneName2accession.put(geneName, split[1]);
					mouse_accessions2geneName.put(split[1], geneName);
				}
			}
			
			// parse information from blast
			HashMap human2mouse_blast = new HashMap();
			HashMap human2mouse_blast_score = new HashMap();
			fstream = new FileInputStream(human2mouse_blast_result);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String human_accession = split[0].split("\\|")[1];
				String mouse_accession = split[1].split("\\|")[1];
				String score = split[2];
				if (!human2mouse_blast.containsKey(human_accession)) {
					human2mouse_blast.put(human_accession, mouse_accession);
					human2mouse_blast_score.put(human_accession, score);
				}
			}
			in.close();
			
			HashMap mouse2human_blast = new HashMap();
			HashMap mouse2human_blast_score = new HashMap();
			fstream = new FileInputStream(mouse2human_blast_result);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String mouse_accession = split[0].split("\\|")[1];
				String human_accession = split[1].split("\\|")[1];
				String score = split[2];
				if (!mouse2human_blast.containsKey(mouse_accession)) {
					mouse2human_blast.put(mouse_accession, human_accession);
					mouse2human_blast_score.put(mouse_accession, score);
				}
			}
			in.close();
			
			out.write("Species\tQueryAccession\tQueryGeneName\tQueryEntrez\tHomologSpecies\tCoreHomologGeneNameStatus\tCoreHomologAccession\tCoreHomologGeneName\tCoreHomologEntrez\tMGIStatus\tMGIHomologAccession\tMGIHomologGeneName\tMGIHomologEntrez\tBlastHitStatus\tBLASTHitAccession\tBlastHitGeneName\tBlastHitEntrez\tBlastHitScore\tBlastGeneNameMatch\tFinalAccession\tFinalGenName\tFinalEntrez\tFinalScore\n");
			fstream = new FileInputStream(human_coreFasta);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains(">")) {
					String[] split = str.split("\\|");
					String geneName = split[2].split(" ")[0].replaceAll("_HUMAN", "");
					String accession = split[1];
					String official_geneName = "NA";
					String entrez = "NA";
					if (human_uniprot2geneName.containsKey(accession)) {
						official_geneName = (String)human_uniprot2geneName.get(accession);						
					}
					if (human_uniprot2entrez.containsKey(accession)) {
						entrez = (String)human_uniprot2entrez.get(accession);						
					}
					if (human_refseq2geneName.containsKey(accession.split("\\.")[0])) {
						official_geneName = (String)human_refseq2geneName.get(accession.split("\\.")[0]);
					}
					if (human_refseq2entrez.containsKey(accession.split("\\.")[0])) {
						entrez = (String)human_refseq2entrez.get(accession.split("\\.")[0]);
					}
					
					String uniprot_accession = accession;
					if (uniprot_accession.contains("NP_")) {
						if (refseq2uniprot.containsKey(uniprot_accession.split("\\.")[0])) {
							uniprot_accession = (String)refseq2uniprot.get(uniprot_accession.split("\\.")[0]);							
						}
					}
					
					out.write("Human\t" + uniprot_accession + "\t" + official_geneName + "\t" + entrez + "\tMouse");
					
					String final_top_hit_uniprot = "NA";
					String final_top_hit_geneName = "NA";
					String final_top_hit_entrez = "NA";
					double final_top_hit_score = 0.0;	
					
					String officialHit = "";
					String mouse_accessionfromcore = "NA";
					String mouse_geneName = "NA";
					String mouse_entrez = "NA";
					if (mouse_geneName2accession.containsKey(geneName)) {
						mouse_accessionfromcore = (String)mouse_geneName2accession.get(geneName);

						if (mouse_uniprot2entrez.containsKey(mouse_accessionfromcore)) {
							mouse_entrez = (String)mouse_uniprot2entrez.get(mouse_accessionfromcore);							
						}
						if (mouse_uniprot2geneName.containsKey(mouse_accessionfromcore)) {
							mouse_geneName = (String)mouse_uniprot2geneName.get(mouse_accessionfromcore);							
						}
						if (final_top_hit_uniprot.equals("NA")) {
							final_top_hit_uniprot = mouse_accessionfromcore;
							final_top_hit_geneName = mouse_geneName;
							final_top_hit_entrez = mouse_entrez;
							final_top_hit_score = 2;
						}
						out.write("\tFound\t" + mouse_accessionfromcore + "\t" + mouse_geneName + "\t" + mouse_entrez);
					} else {
						out.write("\tMissed\t" + mouse_accessionfromcore + "\t" + mouse_geneName + "\t" + mouse_entrez);
					}

					mouse_geneName = "NA";
					mouse_entrez = "NA";
					if (map_human2mouse.containsKey(uniprot_accession)) {
						String mouse_accession = (String)map_human2mouse.get(uniprot_accession);																		
						if (mouse_uniprot2entrez.containsKey(mouse_accession)) {
							mouse_entrez = (String)mouse_uniprot2entrez.get(mouse_accession);							
						}
						if (mouse_uniprot2geneName.containsKey(mouse_accession)) {
							mouse_geneName = (String)mouse_uniprot2geneName.get(mouse_accession);							
						}
						if (final_top_hit_uniprot.equals("NA")) {
							final_top_hit_uniprot = mouse_accession;
							final_top_hit_geneName = mouse_geneName;
							final_top_hit_entrez = mouse_entrez;
							final_top_hit_score = 1;
						} else if (final_top_hit_uniprot.equals(mouse_accession)) {
							if (final_top_hit_geneName.toUpperCase().equals(mouse_geneName.toUpperCase())) {
								final_top_hit_score = 3;
							} else {
								final_top_hit_score = 2;
							}
						}
						if (mouse_accession.contains("NP_")) {
							mouse_accession = "NA";
						}
						if (mouse_accession.equals(uniprot_accession)) {
							mouse_accession = "NA";
						}
						out.write("\tFound\t" + mouse_accession + "\t" + mouse_geneName + "\t" + mouse_entrez);
					} else if (accession.contains("NP_") && map_refseq_human2mouse.containsKey(accession.split("\\.")[0])) {
						
						String[] refseqs = (String[])map_refseq_human2mouse.get(accession.split("\\.")[0]);
						for (String refseq: refseqs) {
							if (mouse_refseq2entrez.containsKey(refseq.split("\\.")[0])) {
								mouse_entrez = (String)mouse_refseq2entrez.get(refseq.split("\\.")[0]);							
							}
							if (mouse_refseq2geneName.containsKey(refseq.split("\\.")[0])) {
								mouse_geneName = (String)mouse_refseq2geneName.get(refseq.split("\\.")[0]);							
							}
						}
						
						
						if (final_top_hit_uniprot.equals("NA")) {
							//final_top_hit_uniprot = accession.split("\\.")[0];
							final_top_hit_geneName = mouse_geneName;
							final_top_hit_entrez = mouse_entrez;
							final_top_hit_score = 1;
						}
						
						if (map_human2mouse.containsKey(accession.split("\\.")[0])) {
							uniprot_accession = (String)map_human2mouse.get(accession.split("\\.")[0]);
						}
						if (uniprot_accession.contains("NP_")) {
							uniprot_accession = "NA";
						}						
						out.write("\tFound\t" + uniprot_accession + "\t" + mouse_geneName + "\t" + mouse_entrez);
					} else {
						
						out.write("\tMissed\tNA\tNA\tNA");
					}
					
					mouse_geneName = "NA";
					mouse_entrez = "NA";
					
					if (human2mouse_blast.containsKey(accession)) {
						String mouse_accession = (String)human2mouse_blast.get(accession);
						if (mouse_uniprot2entrez.containsKey(mouse_accession)) {
							mouse_entrez = (String)mouse_uniprot2entrez.get(mouse_accession);							
						}
						if (mouse_uniprot2geneName.containsKey(mouse_accession)) {
							mouse_geneName = (String)mouse_uniprot2geneName.get(mouse_accession);							
						}
						String reverse_accession = "NA";
						if (mouse2human_blast.containsKey(mouse_accession)) {
							reverse_accession = (String)mouse2human_blast.get(mouse_accession);							
						} 
						
						String blast_identity = (String)human2mouse_blast_score.get(accession); 
						String status = "OneWayHit";
						if (reverse_accession.equals(accession)) {
							status = "TwoWayHit";
						}
						String geneNameMatch = "MISMATCH";
						if (official_geneName.toUpperCase().equals(mouse_geneName.toUpperCase()) && !official_geneName.equals("NA")) {
							geneNameMatch = "MATCH";
						}
						
						
						double score = 0.0;
						
						if (status.equals("OneWayHit") && geneNameMatch.equals("MISMATCH")) {
							score = 0.1;
						} else if (status.equals("OneWayHit") && geneNameMatch.equals("MATCH")) {
							score = 0.3;
						} else if (status.equals("TwoWayHit") && geneNameMatch.equals("MISMATCH")) {
							score = 0.2;
						} else if (status.equals("TwoWayHit") && geneNameMatch.equals("MATCH")) {
							score = 0.4;
						}
						
						
						if (final_top_hit_uniprot.equals("NA")) {
							final_top_hit_entrez = mouse_entrez;
							if (final_top_hit_geneName.toUpperCase().equals(mouse_geneName.toUpperCase())) {
								final_top_hit_uniprot = mouse_accession;
								final_top_hit_geneName = mouse_geneName;
								final_top_hit_score += score;															
							} else if (final_top_hit_geneName.equals("NA")) {
								final_top_hit_uniprot = mouse_accession;
								final_top_hit_geneName = mouse_geneName;
								final_top_hit_score = score;								
							}
						} else if (final_top_hit_uniprot.equals(mouse_accession)) {
							if (final_top_hit_geneName.toUpperCase().equals(mouse_geneName.toUpperCase())) {
								final_top_hit_score += score;
							}
							
						}	
						
						out.write("\t" + status + "\t" + mouse_accession + "\t" + mouse_geneName + "\t" + mouse_entrez + "\t" + blast_identity + "\t" + geneNameMatch);
					} else {						
						out.write("\tMissed\tNA\tNA\tNA\tNA\tNA");
					}
					
					out.write("\t" + final_top_hit_uniprot + "\t" + final_top_hit_geneName + "\t" + final_top_hit_entrez + "\t" + final_top_hit_score);
					out.write("\n");
					
				}				
			}
			
			in.close();
			
			fstream = new FileInputStream(mouse_coreFasta);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();				
				if (str.contains(">")) {
					String[] split = str.split("\\|");
					String geneName = split[2].split(" ")[0].replaceAll("_MOUSE", "");
					String accession = split[1]; // added the split just in case it was a refseq id
					String official_geneName = "NA";
					String entrez = "NA";					

					String final_top_hit_uniprot = "NA";
					String final_top_hit_geneName = "NA";
					String final_top_hit_entrez = "NA";
					double final_top_hit_score = 0.0;
					
					if (mouse_uniprot2geneName.containsKey(accession)) {
						official_geneName = (String)mouse_uniprot2geneName.get(accession);						
					}
					if (mouse_uniprot2entrez.containsKey(accession)) {
						entrez = (String)mouse_uniprot2entrez.get(accession);						
					}
					if (mouse_refseq2geneName.containsKey(accession.split("\\.")[0])) {
						official_geneName = (String)mouse_refseq2geneName.get(accession.split("\\.")[0]);
					}
					if (mouse_refseq2entrez.containsKey(accession.split("\\.")[0])) {
						entrez = (String)mouse_refseq2entrez.get(accession.split("\\.")[0]);
					}
					
					String uniprot_accession = accession;
					if (uniprot_accession.contains("NP_")) {
						if (refseq2uniprot.containsKey(uniprot_accession.split("\\.")[0])) {
							uniprot_accession = (String)refseq2uniprot.get(uniprot_accession.split("\\.")[0]);							
						}
					}
					out.write("Mouse\t" + uniprot_accession + "\t" + official_geneName + "\t" + entrez + "\tHuman");
					
					String human_accessionfromcore = "NA";
					String human_geneName = "NA";
					String human_entrez = "NA";
					if (human_geneName2accession.containsKey(geneName)) {
						human_accessionfromcore = (String)human_geneName2accession.get(geneName);
						if (human_uniprot2entrez.containsKey(human_accessionfromcore)) {
							human_entrez = (String)human_uniprot2entrez.get(human_accessionfromcore);							
						}
						if (human_uniprot2geneName.containsKey(human_accessionfromcore)) {
							human_geneName = (String)human_uniprot2geneName.get(human_accessionfromcore);							
						}
						if (final_top_hit_uniprot.equals("NA")) {
							final_top_hit_uniprot = human_accessionfromcore;
							final_top_hit_geneName = human_geneName;
							final_top_hit_entrez = human_entrez;
							final_top_hit_score = 2;
						}
						out.write("\tFound\t" + human_accessionfromcore + "\t" + human_geneName + "\t" + human_entrez);
					} else {
						out.write("\tMissed\t" + human_accessionfromcore + "\t" + human_geneName + "\t" + human_entrez);
					}
					human_geneName = "NA";
					human_entrez = "NA";					
					if (map_mouse2human.containsKey(uniprot_accession)) {
						String human_accession = (String)map_mouse2human.get(uniprot_accession);
						if (human_uniprot2entrez.containsKey(human_accession)) {
							human_entrez = (String)human_uniprot2entrez.get(human_accession);							
						}
						if (human_uniprot2geneName.containsKey(human_accession)) {
							human_geneName = (String)human_uniprot2geneName.get(human_accession);							
						}
						if (final_top_hit_uniprot.equals("NA")) {
							final_top_hit_uniprot = human_accession;
							final_top_hit_geneName = human_geneName;
							final_top_hit_entrez = human_entrez;
							final_top_hit_score = 1;
						} else if (final_top_hit_uniprot.equals(human_accession)) {
							if (final_top_hit_geneName.toUpperCase().equals(human_geneName.toUpperCase())) {
								final_top_hit_score = 3;
							} else {
								final_top_hit_score = 2;
							}
						}
						if (human_accession.contains("NP_")) {
							human_accession = "NA";
						}
						if (human_accession.equals(uniprot_accession)) {
							human_accession = "NA";
						}
						out.write("\tFound\t" + human_accession + "\t" + human_geneName + "\t" + human_entrez);
					} else if (accession.contains("NP_") && map_refseq_mouse2human.containsKey(accession.split("\\.")[0])) {
						String[] refseqs = (String[])map_refseq_mouse2human.get(accession.split("\\.")[0]);
						for (String refseq: refseqs) {
							if (human_refseq2entrez.containsKey(refseq.split("\\.")[0])) {
								human_entrez = (String)human_refseq2entrez.get(refseq.split("\\.")[0]);							
							}
							if (human_refseq2geneName.containsKey(refseq.split("\\.")[0])) {
								human_geneName = (String)human_refseq2geneName.get(refseq.split("\\.")[0]);							
							}
						}
						if (final_top_hit_uniprot.equals("NA")) {
							final_top_hit_uniprot = accession;
							final_top_hit_geneName = human_geneName;
							final_top_hit_entrez = human_entrez;
							final_top_hit_score = 1;
						}
						if (map_mouse2human.containsKey(accession.split("\\.")[0])) {
							uniprot_accession = (String)map_mouse2human.get(accession.split("\\.")[0]);
						}
						if (uniprot_accession.contains("NP_")) {
							uniprot_accession = "NA";
						}	
						out.write("\tFound\t" + uniprot_accession + "\t" + human_geneName + "\t" + human_entrez);
					} else {
					
						out.write("\tMissed\tNA\tNA\tNA");
					}
					
					human_geneName = "NA";
					human_entrez = "NA";
					
					if (mouse2human_blast.containsKey(accession)) {
						String human_accession = (String)mouse2human_blast.get(accession);
						if (human_uniprot2entrez.containsKey(human_accession)) {
							human_entrez = (String)human_uniprot2entrez.get(human_accession);							
						}
						if (human_uniprot2geneName.containsKey(human_accession)) {
							human_geneName = (String)human_uniprot2geneName.get(human_accession);							
						}
						String reverse_accession = "NA";
						if (human2mouse_blast.containsKey(human_accession)) {
							reverse_accession = (String)human2mouse_blast.get(human_accession);							
						} 
						
						String blast_identity = (String)mouse2human_blast_score.get(accession); 
						String status = "OneWayHit";
						if (reverse_accession.equals(accession)) {
							status = "TwoWayHit";
						}
						String geneNameMatch = "MISMATCH";
						if (official_geneName.toUpperCase().equals(human_geneName.toUpperCase()) && !official_geneName.equals("NA")) {
							geneNameMatch = "MATCH";
						}
						
						double score = 0.0;
						
						if (status.equals("OneWayHit") && geneNameMatch.equals("MISMATCH")) {
							score = 0.1;
						} else if (status.equals("OneWayHit") && geneNameMatch.equals("MATCH")) {
							score = 0.3;
						} else if (status.equals("TwoWayHit") && geneNameMatch.equals("MISMATCH")) {
							score = 0.2;
						} else if (status.equals("TwoWayHit") && geneNameMatch.equals("MATCH")) {
							score = 0.4;
						}
						
						if (final_top_hit_uniprot.equals("NA")) {
							final_top_hit_entrez = human_entrez;
							if (final_top_hit_geneName.toUpperCase().equals(human_geneName.toUpperCase())) {
								final_top_hit_uniprot = human_accession;
								final_top_hit_geneName = human_geneName;
								final_top_hit_score += score;															
							} else if (final_top_hit_geneName.equals("NA")) {
								final_top_hit_uniprot = human_accession;
								final_top_hit_geneName = human_geneName;
								final_top_hit_score = score;								
							}
						} else if (final_top_hit_uniprot.equals(human_accession)) {
							if (final_top_hit_geneName.toUpperCase().equals(human_geneName.toUpperCase())) {
								final_top_hit_score = final_top_hit_score += score;
							}
						}	
						out.write("\t" + status + "\t" + human_accession + "\t" + human_geneName + "\t" + human_entrez + "\t" + blast_identity + "\t" + geneNameMatch);
					} else {						
						out.write("\tMissed\tNA\tNA\tNA\tNA\tNA");
					}
					
					out.write("\t" + final_top_hit_uniprot + "\t" + final_top_hit_geneName + "\t" + final_top_hit_entrez + "\t" + final_top_hit_score);
					out.write("\n");
				}				
			}
			in.close();
			
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
