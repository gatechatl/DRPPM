package ProteinFeature.LowComplexityDomain;

import idconversion.tools.Uniprot2GeneID;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

/**
 * This is designed specifically for Dr. Paul Taylor's lab's work
 * @author tshaw
 *
 */
public class SEGPostProcessing {

	public static String category() {
		return "PROTEOMICS";
	}
	public static String description() {
		return "Generate ";
	}
	public static String parameter_info() {
		return "[inputFile] [geneMetaInfoFile] [maxcutoff] [accession2geneNameFile] [outputFile] [outputLengthSummary]";
	}
	public static void execute(String[] args) {
				
		try {
			
			String inputFile = args[0];
			String geneListFile = args[1];
			int maxcutoff = new Integer(args[2]);
			String keytype = args[3];
			String accession2geneNameFile = args[4];
			String outputFile = args[5];
			String outputLengthSummary = args[6];

			HashMap accession2geneName = Uniprot2GeneID.uniprot2geneID(accession2geneNameFile);
			/*System.out.println("# Proteins With LCR: " + new Double(totalGene.size()) / total);			
			System.out.println("HIT_PR_ONLY: " + new Double(HIT_PR_ONLY.size()) / 66);
			System.out.println("HIT_GR_ONLY: " + new Double(HIT_GR_ONLY.size()) / 54);
			System.out.println("HIT_GRPR: " + new Double(HIT_GRPR.size()) / 81);*/
			System.out.println("LCRLength\tHumanProteome\tPR_ONLY\tGR_ONLY\tGRPR");
			

			FileWriter fwriter_len = new FileWriter(outputLengthSummary);
            BufferedWriter out_len = new BufferedWriter(fwriter_len);
            out_len.write("Accession\tGeneName\tUnipriotGeneName\tMaxLCDLengthPerProtein\tDataType\n");
			HashMap map = new HashMap();
			HashMap map2 = new HashMap();
			HashMap geneName = new HashMap();
			int total = 0;
			int pr_only = 0;
			int gr_only = 0;
			int grpr = 0;
			FileInputStream fstream = new FileInputStream(geneListFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(split[2], split[0]);
				map2.put(split[1], split[0]);
				geneName.put(split[2], split[1]);
				if (split[0].equals("GRPR")) {
					grpr++;
				}
				if (split[0].equals("GR_ONLY")) {
					gr_only++;
				}
				if (split[0].equals("PR_ONLY")) {
					pr_only++;
				}
				total++;
			}
			in.close();
			
			
			for (int cutoff = 0; cutoff <= maxcutoff; cutoff++) {
				
				
				HashMap totalGene = new HashMap();
				HashMap PR_ONLY = new HashMap();
				HashMap GR_ONLY = new HashMap();
				HashMap GRPR = new HashMap();
				
				HashMap HIT_PR_ONLY = new HashMap();
				HashMap HIT_GR_ONLY = new HashMap();
				HashMap HIT_GRPR = new HashMap();
				
				
				fstream = new FileInputStream(inputFile);
				din = new DataInputStream(fstream);
				in = new BufferedReader(new InputStreamReader(din));
				while (in.ready()) {
					String str = in.readLine();
					if (str.contains(">")) {
						String accession = str.split("\\|")[1];
						
						//System.out.println(accession);
						String seq = in.readLine();
						
						
						if (seq.length() > cutoff) {
							
							totalGene.put(accession, accession);
							
							if (map.containsKey(accession)) {
								String type = (String)map.get(accession);
								if (type.equals("PR_ONLY")) {
									HIT_PR_ONLY.put(accession, accession);
								} else if (type.equals("GR_ONLY")) {
									HIT_GR_ONLY.put(accession, accession);							
								} else if (type.equals("GRPR")) {
									HIT_GRPR.put(accession, accession);
								}
							}
						}
					}
				}
				in.close();
				
				double hit_pro_lcr = new Double(totalGene.size()) / total;
				double hit_pr_only = new Double(HIT_PR_ONLY.size()) / pr_only;
				double hit_gr_only = new Double(HIT_GR_ONLY.size()) / gr_only;
				double hit_grpr = new Double(HIT_GRPR.size()) / grpr;
				/*System.out.println("# Proteins With LCR: " + new Double(totalGene.size()) / total);			
				System.out.println("HIT_PR_ONLY: " + new Double(HIT_PR_ONLY.size()) / 66);
				System.out.println("HIT_GR_ONLY: " + new Double(HIT_GR_ONLY.size()) / 54);
				System.out.println("HIT_GRPR: " + new Double(HIT_GRPR.size()) / 81);*/
				System.out.println(cutoff + "\t" + hit_pro_lcr + "\t" + hit_pr_only + "\t" + hit_gr_only + "\t" + hit_grpr);
				
			}
			
			HashMap geneList = new HashMap();
			
			HashMap seq_map = new HashMap();
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains(">")) {
					String accession = str.split("\\|")[1];
					//System.out.println(accession);
					String seq = in.readLine();
					//String gene = (String)geneName.get(accession);									
					if (seq.length() > 0) {
						
						
						if (geneList.containsKey(accession)) {
							int len = (Integer)(geneList.get(accession));
							if (len < seq.length()) {
								geneList.put(accession, seq.length());
							}
						} else {
							geneList.put(accession, seq.length());
						}
						
						
						if (map.containsKey(accession)) {
							String type = (String)map.get(accession);
							if (seq_map.containsKey(accession)) {
								seq_map.put(accession, seq_map.get(accession) + seq);
							} else {
								seq_map.put(accession, seq);
							}							
						}
					}
				}
			}
			in.close();
			
			Iterator itr = geneList.keySet().iterator();
			while (itr.hasNext()) {
				
				String accession = (String)itr.next();
				String gene = (String)accession2geneName.get(accession);
				int len = (Integer)geneList.get(accession);
				String othergene = (String)geneName.get(accession);
				if (map.containsKey(accession)) {
					String type = (String)map.get(accession);
					if (type.equals("PR_ONLY")) {
						out_len.write(accession + "\t" + gene + "\t" + othergene + "\t" + len + "\tPR_ONLY\n");
					} else if (type.equals("GR_ONLY")) {
						out_len.write(accession + "\t" + gene + "\t" + othergene + "\t" + len + "\tGR_ONLY\n");				
					} else if (type.equals("GRPR")) {
						out_len.write(accession + "\t" + gene + "\t" + othergene + "\t" + len + "\tGRPR\n");
					} else {
						out_len.write(accession + "\t" + gene + "\t" + othergene + "\t" + len + "\tBACKGROUND\n");
					}																				
				}
			}
			FileWriter fwriter = new FileWriter(outputFile);
            BufferedWriter out = new BufferedWriter(fwriter);
            
			itr = seq_map.keySet().iterator();
			while (itr.hasNext()) {
				String accession = (String)itr.next();
				String seq = (String)seq_map.get(accession);
				String gene = (String)geneName.get(accession);
				String type = (String)map.get(accession);
				if (type.equals(keytype) && checkAA(seq.toUpperCase())) {
					out.write(">" + gene + "_" + accession + "_" + type + "_LCR\n" + seq.toUpperCase() + "\n");
				} else if (keytype.equals("ALL") && checkAA(seq.toUpperCase())) {
					out.write(">" + gene + "_" + accession + "_" + type + "_ALL\n" + seq.toUpperCase() + "\n");
				}
			}
			out.close();
			out_len.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static boolean checkAA(String seq) {
		boolean good = true;
		for (int i = 0; i < seq.length(); i++) {
			String c = seq.substring(i, i + 1);
			boolean find = false;
			if (c.equals("A")) {
				find = true;
			}
			if (c.equals("C")) {
				find = true;
			}
			if (c.equals("D")) {
				find = true;
			}
			if (c.equals("E")) {
				find = true;
			}
			if (c.equals("F")) {
				find = true;
			}
			if (c.equals("G")) {
				find = true;
			}
			if (c.equals("H")) {
				find = true;
			}
			if (c.equals("I")) {
				find = true;
			}
			if (c.equals("K")) {
				find = true;
			}
			if (c.equals("L")) {
				find = true;
			}
			if (c.equals("M")) {
				find = true;
			}
			if (c.equals("N")) {
				find = true;
			}
			if (c.equals("P")) {
				find = true;
			}
			if (c.equals("Q")) {
				find = true;
			}
			if (c.equals("R")) {
				find = true;
			}
			if (c.equals("S")) {
				find = true;
			}
			if (c.equals("T")) {
				find = true;
			}
			if (c.equals("V")) {
				find = true;
			}
			if (c.equals("W")) {
				find = true;
			}
			if (c.equals("Y")) {
				find = true;
			}
			
			if (!find) {
				good = false;
			}
		} // for loop
		return good;
	}
}
