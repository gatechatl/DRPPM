package PhosphoTools.PSSM;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import SJPssmMotifTools.Alignment;
import SJPssmMotifTools.ClustalAlign;
import SJPssmMotifTools.PSSM;
import SJPssmMotifTools.RyansPSSMSearch;
import SJPssmMotifTools.SearchResult;

public class AppendPSSMResult2HPRD {


	public static String[] loadMotif(String fileName) {
		String[] seqs = null;
		try {
			LinkedList list = new LinkedList();
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			
			while (in.ready()) {
				String str = in.readLine();
				list.add(str);
			}
			in.close();
			seqs = new String[list.size()];
			int index = 0;
			Iterator itr = list.iterator();
			while (itr.hasNext()) {
				String str = (String)itr.next();
				seqs[index] = str;
				index++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return seqs;
	}
	/*
	public static HashMap findSequence(LinkedList database, String seq) {
		String[] tests = list2String(database);
		Alignment alignment = new Alignment();
		int index = 1;
		for (String seqtest: tests) {
			alignment.addSequence("Name" + index + "\t" + seq);
			index++;
			//System.out.println(seqtest);
		}
		alignment.setAlignmentMethod(new ClustalAlign());
		alignment.noAlign();
		PSSM pssm = new PSSM(alignment);
		//System.out.println(pssm.getProfileFragAlignment());
		RyansPSSMSearch search = new RyansPSSMSearch();
		LinkedList<PSSM> pssms = new LinkedList<PSSM>();
		pssms.add(pssm);
		//String seq = "TMEARHRVPTTELCRPPAGITTIEAVKRKIQVLQQQADDEARHRVPTTELCRPPAEERAERLQREVEGERRAREQAEAEVASLNRRIQLVEEELDRAQERLATALQKLEEAEKAADESERGMKVIENRALKDEEKMELQEIQLKEAKHIAEEADRKYEEVARKLVIIEGDLERTEERAELAESKCSELEEELKNVTNNLKSLEAQAEKYSQKEDKYEEEIKILTDKLKEAETRAEFAERSVAKLEKTIDDLEDTNSTSGDPVEKKDETPFGVSVAVGLAVFACLFLSTLLLVLNKCGRRNKFGINRPAVLAPEDGLAMSLHFMTLGGSSLSPTEGKGSGLQGHIIENPQYFSDACVHHIKRRDIVLKWELGEGAFGKVFLAECHNLLPEQDKMLVAVKALKEASESARQDFQREAELLTMLQHQHIVRFFGVCTEGRPLLMVFEYMRHGDLNRFLRSHGPDAKLLAGGEDVAPGPLGLGQLLAVASQVAAGMVYLAGLHFVHRDLATRNCLVGQGLVVKIGDFGMSRDIYSTDYYRVGGRTMLPIRWMPPESILYRKFTTESDVWSFGVVLWEIFTYGKQPWYQLSNTEAIDCITQGRELERPRACPPEVYAIMRGCWQREPQQRHSIKDVHARLQALAQAPPVYLDVLGDYKDDDDK";
		int xCount = 0;
		HashMap map = new HashMap();
		SearchResult[] searchResults = search.search(pssms, seq, xCount);
		for (SearchResult result: searchResults) {
			//System.out.println("Position: " + result.getIndex());
			//System.out.println("P-value: " + result.getPvalue());
			//System.out.println("Log10(Pval): " + -Math.log10(result.getPvalue()));
			map.put(result.getIndex(), result.getPvalue());
		}
		return map;
	}*/
	public static String[] list2String(LinkedList list) {
		String[] seqs = new String[list.size()];
		int index = 0;
		Iterator itr = list.iterator();
		while (itr.hasNext()) {
			String str = (String)itr.next();
			seqs[index] = str;
			index++;
		}
		return seqs;
	}
	
	public static void execute(String[] args) {
		
		/*String[] tests = loadMotif("C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Paper_Powerpoint\\CEASAR\\AKT.txt");
		
		int index = 1;
		for (String seq: tests) {
			alignment.addSequence("Name" + index + "\t" + seq);
			index++;
			//System.out.println(seq);
		}*/
		// read once the file to see how many kinase are available
		try {
					
			String kinase_fileName = args[0];
			//String fileName = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Phosphorylation\\PSSM\\kinase_motif_caesar.txt";
			String hrpd_file = args[1];			
			String outputFile = args[2];
			String kinase_motif = args[3];
			double pvalue_cutoff = new Double(args[4]);			
			String bufferFile = args[5]; // for faster query in the future

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);

			File f = new File(bufferFile);
			if (!f.exists()) {
				f.createNewFile();
			}
			HashMap preprocessed = new HashMap();
			FileInputStream fstream = new FileInputStream(bufferFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));		
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split.length == 3) {
					preprocessed.put(split[0], split[1] + "\t" + split[2]);
				}
			}
			in.close();

			HashMap motif2gene = new HashMap();
			fstream = new FileInputStream(kinase_motif);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));		
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String motif = split[1];
				String gene = split[2];
				motif2gene.put(motif, gene);
			}
			in.close();
			PrintWriter out_preprocess = new PrintWriter(new BufferedWriter(new FileWriter(bufferFile, true)));
			
			HashMap map_seq_kinase_pval = new HashMap();
			HashMap map_seq = new HashMap();
			LinkedList list_hprd_result = new LinkedList();
			fstream = new FileInputStream(hrpd_file);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));		
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map_seq.put(split[2], split[2]);
				list_hprd_result.add(str);
			}
			in.close();
			
			HashMap map = new HashMap();
			String current_key = "";
			fstream = new FileInputStream(kinase_fileName);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));		
			while (in.ready()) {
				String str = in.readLine();
				
				if (!(str.trim().equals("") || str.trim().equals("#NAME?"))) {
					if (str.contains(">")) {
						current_key = str;
					} else {
						//System.out.println(str);
						if (map.containsKey(current_key)) {
							LinkedList list = (LinkedList)map.get(current_key);
							if (str.substring(7, 8).equals("Y")) {
								list.add(str);
								map.put(current_key, list);
							} else {
								String newseqT = str.trim().substring(0, 7) + "T" + str.substring(8, str.length());
								String newseqS = str.trim().substring(0, 7) + "s" + str.substring(8, str.length());
								list.add(newseqT);
								list.add(newseqS);
								map.put(current_key, list);
							}
						} else {
							LinkedList list = new LinkedList();
							if (str.substring(7, 8).equals("Y")) {
								list.add(str);
								map.put(current_key, list);
							} else {
								String newseqT = str.trim().substring(0, 7) + "T" + str.substring(8, str.length());
								String newseqS = str.trim().substring(0, 7) + "s" + str.substring(8, str.length());
								list.add(newseqT);
								list.add(newseqS);
								map.put(current_key, list);
							}
						}
					}
				}
			}
			in.close();
			int count = 0;
			//System.out.println(map_seq.size());
			HashMap pssm_map = generatePSSMs(map);
			Iterator itr2 = map_seq.keySet().iterator();
			while (itr2.hasNext()) {

				String kinase_name = "";
				String pval = "";
				String seq = (String)itr2.next();
				if (count % (map_seq.size() / 10 + 1) == 0) {
					System.out.println((new Double((new Double(count) / map_seq.size() * 100)).intValue()) + "% Finished");
					//System.out.println(count);
				}
				count++;
				
				int index = seq.indexOf("*") + 1;
				String inputSeq = seq.replaceAll("\\*", "");
				if (index - 9 < 0) {
					for (int i = 0; i < 9 - index; i++) {
						inputSeq = "N" + inputSeq;						
					}
					index = 9;
				}
				if (index + 6 > inputSeq.length()) {
					for (int i = 0; i < (6 + index) - inputSeq.length(); i++) {
						inputSeq = inputSeq + "N";
					}
				}
				String newInputSeq = "";
				if (index - 9 >= 0 && index + 6 < inputSeq.length()) {
					newInputSeq = inputSeq.substring(index - 9, index + 6);
				}
				
				if (preprocessed.containsKey(newInputSeq)) {
					String stuff = (String)preprocessed.get(newInputSeq);
					String[] split3 = stuff.split("\t");
					kinase_name = split3[0];
					pval = split3[1];
					
				} else if (index - 9 >= 0 && index + 6 < inputSeq.length()) {
					
					// maybe need to assume T and S are equal probable
					
					Iterator itr = pssm_map.keySet().iterator();
					while (itr.hasNext()) {
						String kinase_key = (String)itr.next();
						LinkedList<PSSM> pssms = (LinkedList<PSSM>)pssm_map.get(kinase_key);
															
						//String seq = "EFQTNLVPYPRIHFPLAT*YAPVISAEKAYHEQLSVAEI";
						//String seq = "EFQTNLVPYPRIHFPLATYAPVISAEKAYHEQLSVAEI";
	
						//System.out.println(newInputSeq);
						int xCount = 0;
						RyansPSSMSearch search = new RyansPSSMSearch();					
						SearchResult[] searchResults = search.search(pssms, newInputSeq, xCount);
						if (searchResults != null) {
							for (SearchResult result: searchResults) {
								//if (result.getIndex() == index) {
								
								/*System.out.println(newInputSeq);
								System.out.println("Position: " + result.getIndex());
								System.out.println("P-value: " + result.getPvalue());						
								System.out.println("Log10(Pval): " + -Math.log10(result.getPvalue()));*/
								if (result.getIndex() == 0) {
									/*System.out.println(kinase_key);
									System.out.println("Position: " + result.getIndex());
									System.out.println("P-value: " + result.getPvalue());
									
									System.out.println("Log10(Pval): " + -Math.log10(result.getPvalue()));*/
									kinase_name += kinase_key.split(">")[1].split("\t")[0] + ",";
									double new_pvalue = new Double((new Double(result.getPvalue() * 100000)).intValue()) / 100000;
									pval += new_pvalue + ",";
								}
							}
						}
						if (kinase_name.equals("")) {
							kinase_name = "NA";
						}
						if (pval.equals("")) {
							pval = "NA";
						}
					} // pssm_map
					
					preprocessed.put(newInputSeq, kinase_name + "\t" + pval);
					out_preprocess.println(newInputSeq + "\t" + kinase_name + "\t" + pval);
					out_preprocess.flush();
				} // if statement
				
				map_seq_kinase_pval.put(newInputSeq, kinase_name + "\t" + pval);
			} // map_seq
			
			System.out.println("Writing output result");
			Iterator itr = list_hprd_result.iterator();
			while (itr.hasNext()) {
				String str = (String)itr.next();
				String[] split = str.split("\t");
				String seq = split[2];
				String motifName = split[5];
				String geneInMotif = "";
				if (motif2gene.containsKey(motifName)) {
					geneInMotif = (String)motif2gene.get(motifName);
				}
						
				LinkedList kinase_motif_gene_found = new LinkedList();
				LinkedList kinase_motif_gene = new LinkedList();
				for (String gene: geneInMotif.split(",")) {
					kinase_motif_gene.add(gene);
				}
				int index = seq.indexOf("*") + 1;
				String inputSeq = seq.replaceAll("\\*", "");
				String newInputSeq = "";
				if (index - 9 >= 0 && index + 6 < inputSeq.length()) {
					newInputSeq = inputSeq.substring(index - 9, index + 6);
				}
				//System.out.println(seq + "\t" + newInputSeq);
				if (preprocessed.containsKey(newInputSeq) && !newInputSeq.equals("")) {
					String hit = (String)preprocessed.get(newInputSeq);					
					String[] split2 = hit.split("\t");
					
					String motifKinase = "NA";
					String motifPval = "NA";
					String kinase = "NA";
					String pval = "NA";
					if (!split2[0].equals("NA")) {						
						kinase = "";
						pval = "";
						motifKinase = "";
						motifPval = "";
						String[] kinases = split2[0].split(",");
						String[] pvals = split2[1].split(",");
						boolean find = false;
						double lowest_pval = 1;
						for (int i = 0; i < pvals.length - 1; i++) {							
							if (kinase_motif_gene.contains(kinases[i])) {
								motifKinase += kinases[i] + ",";
								motifPval += pvals[i] + ",";
							}
							double kinase_pvalue = new Double(pvals[i]);
							if (kinase_pvalue < pvalue_cutoff) {
								kinase += kinases[i] + ",";
								pval += pvals[i] + ",";
								find = true;								
							}				
							if (lowest_pval > kinase_pvalue) {
								lowest_pval = kinase_pvalue;
							}
						}
						if (!find) {
							for (int i = 0; i < pvals.length - 1; i++) {
								double kinase_pvalue = new Double(pvals[i]);
								if (kinase_pvalue == lowest_pval) {
									kinase += kinases[i] + ",";
									pval += pvals[i] + ",";									
								}
							}
						}
					}
					if (motifKinase.equals("")) {						
						motifKinase = geneInMotif;
						if (geneInMotif.equals("")) {
							motifKinase = "NA";
						}
					}
					if (motifPval.equals("")) {
						motifPval = "NA";
					}
					if (kinase.equals("")) {
						kinase = "NA";
					}
					if (pval.equals("")) {
						pval = "NA";
					}
					out.write(str + "\t" + motifKinase + "\t" + motifPval + "\t" + kinase + "\t" + pval + "\n");
				} else {
					out.write(str + "\tNAX\tNAX\tNAX\tNAX\n");
					//System.out.println("Something Wrong");
				}
			}
			out.close();
			out_preprocess.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static HashMap generatePSSMs(HashMap map) {
		HashMap pssm_map = new HashMap();
		Iterator itr = map.keySet().iterator();
		while (itr.hasNext()) {
			String kinase_key = (String)itr.next();
			int index = 1;
			LinkedList list = (LinkedList)map.get(kinase_key);
			//System.out.println(kinase_key + "\t" + list.size());
			Alignment alignment = new Alignment();
			Iterator itr2 = list.iterator();
			while (itr2.hasNext()) {
				String seq = (String)itr2.next();
				if (!seq.contains("_") && !seq.contains("#")) {
					alignment.addSequence("Name" + index + "\t" + seq);
					//System.out.println(seq);
					index++;
				}
			}
			
			alignment.setAlignmentMethod(new ClustalAlign());
			alignment.noAlign();
			PSSM pssm = new PSSM(alignment);
			//System.out.println(pssm.getProfileFragAlignment());
			
			LinkedList<PSSM> pssms = new LinkedList<PSSM>();
			pssms.add(pssm);
			pssm_map.put(kinase_key, pssms);
		}
		return pssm_map;
	}
}
