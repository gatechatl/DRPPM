package idconversion.tools;

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
 * For 
 * @author tshaw
 *
 */
public class GenerateConversionTable {

	public static void execute(String[] args) {
		try {
			
			
			String uniprotIDmapFile = args[0];
			String proteinFile = args[1];
			String rnaseqFile = args[2];
			String gtfFile = args[3];
			String ncbiFile = args[4];
			String fastaFile = args[5];
			String outputFile = args[6];
			String outputFileAll = args[7];
			
			NCBIGeneInfo ncbi = new NCBIGeneInfo();
			ncbi.initialize(ncbiFile);
			GTFFile gtf = new GTFFile();
			gtf.initialize(gtfFile);
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileWriter fwriterAll = new FileWriter(outputFileAll);
			BufferedWriter out_all = new BufferedWriter(fwriterAll);
			
			HashMap seq_len = new HashMap();
			HashMap all_seq = new HashMap();
			String geneNameTag = "";
			FileInputStream fstream = new FileInputStream(fastaFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains(">")) {
					String[] split = str.split("\\|");
					if (split.length > 1) {
						//String seq = in.readLine();
						geneNameTag = split[1];
						//seq_len.put(split[1], seq.length());
					}
				} else {
					if (all_seq.containsKey(geneNameTag)) {
						String seq = (String)all_seq.get(geneNameTag);
						seq += str.trim();
						all_seq.put(geneNameTag, seq);
						seq_len.put(geneNameTag, seq.length());
					} else {
						all_seq.put(geneNameTag, str.trim());
						seq_len.put(geneNameTag, str.trim().length());
					}
				}
			}			
			in.close();
			
			
			HashMap protein = new HashMap();
			fstream = new FileInputStream(proteinFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split.length > 1) {
					if (split[1].split("\\|").length > 1) {
						String uniprot = split[1].split("\\|")[1];
						protein.put(uniprot, uniprot);
					}
				}
			}
			in.close();
			
			HashMap geneName_map = new HashMap();
			HashMap protein_map = new HashMap();
			HashMap protein_found = new HashMap();
			
			fstream = new FileInputStream(rnaseqFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneName = split[0];
				if (!geneName_map.containsKey(geneName)) {
					geneName_map.put(geneName, geneName);
				}
			}
			in.close();
			
			HashMap final_list_all = new HashMap();
			HashMap final_list = new HashMap();
			fstream = new FileInputStream(uniprotIDmapFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String orig = split[0];
				for (int i = 0; i < 40; i++) {
					if (i == 0) {
						split[0] = orig;
					} else {
						split[0] = orig + "-" + i;
					}
					if (split[1].equals("Ensembl")) {
						String geneName = "";
						if (gtf.geneid2geneName.containsKey(split[2])) {
							
							 geneName = (String)gtf.geneid2geneName.get(split[2]);
							 
							 final_list_all.put(split[0] + "\t" + geneName + "\n", "");
						} else {
							System.out.println(split[2] + " Not available");
						}
						if (protein.containsKey(split[0])) {
							if (geneName_map.containsKey(geneName)) {
								protein_found.put(split[0], split[0]);
								final_list.put(split[0] + "\t" + geneName + "\n", "");
								//out.write(split[0] + "\t" + geneName + "\n");
							}
							if (protein_map.containsKey(split[0])) {
								LinkedList list = (LinkedList)protein_map.get(split[0]);
								list.add(geneName);							
								protein_map.put(split[0], list);
							} else {
								LinkedList list = new LinkedList();
								list.add(geneName);
								protein_map.put(split[0], list);
							}
						}					
					} else if (split[1].equals("GeneID")) {
						String geneID = split[2];
						String geneName = (String)ncbi.entrez2geneName.get(geneID);
						final_list_all.put(split[0] + "\t" + geneName + "\n", "");
						if (protein.containsKey(split[0])) {
							if (geneName_map.containsKey(geneName)) {
								protein_found.put(split[0], split[0]);
								final_list.put(split[0] + "\t" + geneName + "\n", "");
								//out.write(split[0] + "\t" + geneName + "\n");
							}
							if (protein_map.containsKey(split[0])) {
								LinkedList list = (LinkedList)protein_map.get(split[0]);
								list.add(geneName);							
								protein_map.put(split[0], list);
							} else {
								LinkedList list = new LinkedList();
								list.add(geneName);
								protein_map.put(split[0], list);
							}
						}
					}
					
				}
			}
			in.close();
			
			Iterator itr = protein_map.keySet().iterator();
			while (itr.hasNext()) {
				String protein_id = (String)itr.next();
				if (!protein_found.containsKey(protein_id)) {
					System.out.println(protein_id + " no matching geneID");
				}
				
			}
			
			HashMap uniq_protein = new HashMap();
			HashMap uniq_geneName = new HashMap();
			itr = final_list.keySet().iterator();
			while (itr.hasNext()) {
				String entries = (String)itr.next();
				String stuff = entries.replaceAll("\n", "");
				String uniprot = stuff.split("\t")[0];
				String geneName = stuff.split("\t")[1];
				uniq_protein.put(uniprot, uniprot);
				uniq_geneName.put(geneName, geneName);
				String len = "NA";
				if (seq_len.containsKey(uniprot)) {
					len = (Integer)seq_len.get(uniprot) + "";
				}
				out.write(uniprot + "\t" + geneName + "\t" + len + "\n");
				
			}
			out.close();
			
			itr = final_list_all.keySet().iterator();
			while (itr.hasNext()) {
				String entries = (String)itr.next();
				String stuff = entries.replaceAll("\n", "");
				String uniprot = stuff.split("\t")[0];
				String geneName = stuff.split("\t")[1];
				//uniq_protein.put(uniprot, uniprot);
				//uniq_geneName.put(geneName, geneName);
				String len = "NA";
				if (seq_len.containsKey(uniprot)) {
					len = (Integer)seq_len.get(uniprot) + "";
					out_all.write(uniprot + "\t" + geneName + "\t" + len + "\n");
				}								
			}
			out_all.close();
			System.out.println("Total Protein Matched: " + uniq_protein.size());
			System.out.println("Total Gene Symbol Matched: " + uniq_geneName.size());
			
			System.out.println("Total RNAseq GeneName: " + geneName_map.size());
			System.out.println("Total Protein: " + protein.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
