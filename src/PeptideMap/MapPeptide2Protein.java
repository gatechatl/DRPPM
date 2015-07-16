package PeptideMap;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class MapPeptide2Protein {

	public static void main(String[] args) {
		
		try {
			String inputFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\COMPLETE_FINAL_ANALYSIS\\phospho_coverage\\IDSUM_YUXIN_SEARCHED\\2.2%\\publications\\id_all_pep_simple.txt";
			//String inputFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\COMPLETE_FINAL_ANALYSIS\\phospho_coverage\\pdgfra_idsum_peptides.txt";
			//String inputFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\Differential_Peptide_Analysis\\AddFastSequence\\TTest.txt";
			String mouseFasta = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\Differential_Peptide_Analysis\\AddFastSequence\\MOUSE.fasta";
			//String outputFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\COMPLETE_FINAL_ANALYSIS\\phospho_coverage\\pdgfra_idsum_peptides.out";
			String outputFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\COMPLETE_FINAL_ANALYSIS\\phospho_coverage\\IDSUM_YUXIN_SEARCHED\\2.2%\\publications\\id_all_pep_simple.out";
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			HashMap seq = loadFasta(mouseFasta);			
			HashMap gene2uniprot = generateUniprot2Gene(inputFile);
			HashMap substrates_map = compileAllModLocation(inputFile, seq, gene2uniprot);
			//System.out.println(substrates_map.size());
			Iterator itr = substrates_map.keySet().iterator();
			while (itr.hasNext()) {
				String uniprot_name = (String)itr.next();
				KINASESUBSTRATE substrate = (KINASESUBSTRATE)substrates_map.get(uniprot_name);
				
				String ext = "";
				Iterator itr2 = substrate.EXT_PEPTIDE.iterator();
				while (itr2.hasNext()) {
					ext += (String)itr2.next() + ",";
				}
				
				String orig = "";
				itr2 = substrate.ORIG_PEPTIDE.iterator();
				while (itr2.hasNext()) {
					orig += (String)itr2.next() + ",";
				}
				
				
				String mod = "";
				itr2 = substrate.MODSITE.keySet().iterator();
				while (itr2.hasNext()) {
					mod += (String)itr2.next() + ",";
				}
				if (!substrate.UNIPROTNAME.contains("-")) {
					out.write(substrate.GENENAME + "\t" + substrate.UNIPROTNAME + "\t" + ext + "\t" + orig + "\t" + mod + "\n");
					System.out.println(substrate.GENENAME + "\t" + substrate.UNIPROTNAME + "\t" + ext + "\t" + orig + "\t" + mod);
				}
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void pdgfra_example(String[] args) {
		
		try {
			String inputFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\COMPLETE_FINAL_ANALYSIS\\phospho_coverage\\pdgfra_idsum_peptides.txt";
			//String inputFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\Differential_Peptide_Analysis\\AddFastSequence\\TTest.txt";
			String mouseFasta = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\Differential_Peptide_Analysis\\AddFastSequence\\MOUSE.fasta";
			String outputFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\COMPLETE_FINAL_ANALYSIS\\phospho_coverage\\pdgfra_idsum_peptides.out";
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			HashMap seq = loadFasta(mouseFasta);			
			HashMap gene2uniprot = generateUniprot2Gene(inputFile);
			HashMap substrates_map = compileAllModLocation(inputFile, seq, gene2uniprot);
			//System.out.println(substrates_map.size());
			Iterator itr = substrates_map.keySet().iterator();
			while (itr.hasNext()) {
				String uniprot_name = (String)itr.next();
				KINASESUBSTRATE substrate = (KINASESUBSTRATE)substrates_map.get(uniprot_name);
				
				String ext = "";
				Iterator itr2 = substrate.EXT_PEPTIDE.iterator();
				while (itr2.hasNext()) {
					ext += (String)itr2.next() + ",";
				}
				
				String orig = "";
				itr2 = substrate.ORIG_PEPTIDE.iterator();
				while (itr2.hasNext()) {
					orig += (String)itr2.next() + ",";
				}
				
				
				String mod = "";
				itr2 = substrate.MODSITE.keySet().iterator();
				while (itr2.hasNext()) {
					mod += (String)itr2.next() + ",";
				}
				out.write(substrate.GENENAME + "\t" + substrate.UNIPROTNAME + "\t" + ext + "\t" + orig + "\t" + mod + "\n");
				System.out.println(substrate.GENENAME + "\t" + substrate.UNIPROTNAME + "\t" + ext + "\t" + orig + "\t" + mod);
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void OLD_PEPTIDE_MAPPING(String[] args) {
		try {
			String inputFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\Differential_Peptide_Analysis\\AddFastSequence\\TTest.txt";
			String mouseFasta = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\Differential_Peptide_Analysis\\AddFastSequence\\MOUSE.fasta";
			String outputFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\Differential_Peptide_Analysis\\AddFastSequence\\MOUSE_TTEST_COORD.txt";
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			HashMap seq = loadFasta(mouseFasta);			
			HashMap gene2uniprot = generateUniprot2Gene(inputFile);
			HashMap substrates_map = compileAllModLocation(inputFile, seq, gene2uniprot);
			//System.out.println(substrates_map.size());
			Iterator itr = substrates_map.keySet().iterator();
			while (itr.hasNext()) {
				String uniprot_name = (String)itr.next();
				KINASESUBSTRATE substrate = (KINASESUBSTRATE)substrates_map.get(uniprot_name);
				
				String ext = "";
				Iterator itr2 = substrate.EXT_PEPTIDE.iterator();
				while (itr2.hasNext()) {
					ext += (String)itr2.next() + ",";
				}
				
				String orig = "";
				itr2 = substrate.ORIG_PEPTIDE.iterator();
				while (itr2.hasNext()) {
					orig += (String)itr2.next() + ",";
				}
				
				
				String mod = "";
				itr2 = substrate.MODSITE.keySet().iterator();
				while (itr2.hasNext()) {
					mod += (String)itr2.next() + ",";
				}
				out.write(substrate.GENENAME + "\t" + substrate.UNIPROTNAME + "\t" + ext + "\t" + orig + "\t" + mod + "\n");
				System.out.println(substrate.GENENAME + "\t" + substrate.UNIPROTNAME + "\t" + ext + "\t" + orig + "\t" + mod);
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public static void executeMe(String[] args) {
		try {
			String inputFile = args[0]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\Differential_Peptide_Analysis\\AddFastSequence\\TTest.txt";
			String mouseFasta = args[1]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\Differential_Peptide_Analysis\\AddFastSequence\\MOUSE.fasta";
			String outputFile = args[2]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\Differential_Peptide_Analysis\\AddFastSequence\\MOUSE_TTEST_COORD.txt";
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			HashMap seq = loadFasta(mouseFasta);			
			HashMap gene2uniprot = generateUniprot2Gene(inputFile);
			HashMap substrates_map = compileAllModLocation(inputFile, seq, gene2uniprot);
			//System.out.println(substrates_map.size());
			Iterator itr = substrates_map.keySet().iterator();
			while (itr.hasNext()) {
				String uniprot_name = (String)itr.next();
				KINASESUBSTRATE substrate = (KINASESUBSTRATE)substrates_map.get(uniprot_name);
				
				String ext = "";
				Iterator itr2 = substrate.EXT_PEPTIDE.iterator();
				while (itr2.hasNext()) {
					ext += (String)itr2.next() + ",";
				}
				
				String orig = "";
				itr2 = substrate.ORIG_PEPTIDE.iterator();
				while (itr2.hasNext()) {
					orig += (String)itr2.next() + ",";
				}
				
				
				String mod = "";
				itr2 = substrate.MODSITE.keySet().iterator();
				while (itr2.hasNext()) {
					mod += (String)itr2.next() + ",";
				}
				out.write(substrate.GENENAME + "\t" + substrate.UNIPROTNAME + "\t" + ext + "\t" + orig + "\t" + mod + "\n");
				System.out.println(substrate.GENENAME + "\t" + substrate.UNIPROTNAME + "\t" + ext + "\t" + orig + "\t" + mod);
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static HashMap generateUniprot2Gene(String inputFile) {
		
		HashMap map = new HashMap();
		try {
			
			String fileName = inputFile;
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String orig = split[0];
				String geneName = split[1];
				String geneSymbol = split[2];
				if (map.containsKey(geneSymbol)) {
					String name = (String)map.get(geneSymbol);
					String[] split2 = name.split(",");
					boolean match = false;
					for (String str2: split2) {
						if (str2.equals(geneName)) {
							match = true;
						}
					}
					if (!match) {
						name += "," + geneName;
					}
					map.put(geneSymbol, name);
				} else {
					
					map.put(geneSymbol, geneName);
				}
				
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	public static HashMap compileAllModLocation(String inputFile, HashMap seq, HashMap gene2uniprot) {
		HashMap map = new HashMap();
		try {

			
			int total = 0;
			String fileName = inputFile;
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			in.readLine();
			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				//String geneName = split[1];
				String geneSymbol = split[2];
				String[] geneNames = ((String)gene2uniprot.get(geneSymbol)).split(",");
				//System.out.println(geneNames.length);
				for (String geneName: geneNames) {	
					
				
					KINASESUBSTRATE substrate = new KINASESUBSTRATE(); 
					String orig = split[0];
					String newSeq = orig.replaceAll("\\.", "").replaceAll("\\^", "").replaceAll("\\#", "").replaceAll("\\*", "").replaceAll("\\@", "").replaceAll("\\%", "");
					//System.out.println(orig + "\t" + newSeq);
					String geneSeq = (String)seq.get(geneName);
					
					String finalSeq = "";
					if (seq.containsKey(geneName)) {
						
						if (map.containsKey(geneName)) {
							substrate = (KINASESUBSTRATE)map.get(geneName);					
						} else {
							substrate.UNIPROTNAME = geneName;
							substrate.GENENAME = geneSymbol;
						}
						//System.out.println(geneName);
						int count = 0;
						int index = 0;
						for (int i = 0; i < geneSeq.length() - newSeq.length(); i++) {
							if (geneSeq.substring(i, i + newSeq.length()).equals(newSeq)) {
								count++;
								//System.out.println("found");
								index = i;
							}
						}
						if (count == 1) {
							//if (index >= 5 && index + newSeq.length() < geneSeq.length() - 5) {
							int buffer = 5;
							int end_buffer = 5;
							if (index < 5) {
								buffer = index;
							}
							if (geneSeq.length() - index - newSeq.length() < 5) {
								end_buffer = geneSeq.length() - index - newSeq.length(); 
							}
							HashMap coord_map = new HashMap();
							//System.out.println(buffer + "\t" + end_buffer);
							String temp = geneSeq.substring(index - buffer, index + newSeq.length() + end_buffer);
							finalSeq = temp.substring(0, buffer);
							int oi = 0;
							for (int j = buffer; j < temp.length() - end_buffer; j++) {
								String p = orig.substring(oi, oi + 1);
								
								if (p.equals(".") || p.equals("*")) {
									oi++;
								} else {								
									if (p.equals("#")) {
										finalSeq += "#";
										if (orig.substring(oi - 1, oi).equals("S")) {
											coord_map.put(index - buffer + j, "S#");
											substrate.MODSITE.put((index - buffer + j) + "S", "");
										}
										oi++;																				
									}
									if (p.equals("^")) {
										finalSeq += "^";
										coord_map.put(index - buffer + j, "Y^");
										substrate.MODSITE.put((index - buffer + j) + "Y", "");
										oi++;
									}
									if (p.equals("@")) {
										finalSeq += "@";
										coord_map.put(index - buffer + j, "T@");
										substrate.MODSITE.put((index - buffer + j) + "T", "");
										oi++;
									}		
									if (p.equals("%")) {
										finalSeq += "%";
										coord_map.put(index - buffer + j, "T%");
										substrate.MODSITE.put((index - buffer + j) + "T", "");
										oi++;
									}	
								}
								finalSeq += temp.substring(j, j + 1);
								oi++;
							}
							total++;
							
							finalSeq += geneSeq.substring(index + newSeq.length(), index + newSeq.length() + end_buffer);
						
							String mod = "";
							Iterator itr = coord_map.keySet().iterator();
							while (itr.hasNext()) {
								int coord = (Integer)itr.next();
								String type = (String)coord_map.get(coord);
								mod += coord + type + ",";
							}
							//out.write(">" + geneName + "\n" + finalSeq + "\n");
							substrate.ORIG_PEPTIDE.add(orig);
							substrate.EXT_PEPTIDE.add(finalSeq);
							//System.out.println("found: " + geneName + "\t" + finalSeq + "\t" + orig + "\t" + geneSeq + "\t" + mod);
							map.put(geneName, substrate);
						}
						
					} // for loop of genename
				}
			} 
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	public static class KINASESUBSTRATE {
		public String GENENAME = "";
		public String UNIPROTNAME = "";
		public LinkedList ORIG_PEPTIDE = new LinkedList();
		public LinkedList EXT_PEPTIDE = new LinkedList();
		public HashMap MODSITE = new HashMap();
	}
	public static HashMap loadFasta(String inputFile) {
		HashMap seq = new HashMap();
		try {
			String fileName = inputFile;
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String name = "";
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains(">")) {
					name = str.split("\\|")[1];
					//System.out.println(name);
					seq.put(name, "");
				} else {
					String orig = (String)seq.get(name);
					orig += str.trim();
					seq.put(name, orig);
				}
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return seq;
	}
}
