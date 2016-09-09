package PhosphoTools.HongBoProject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class ConvertWGCNAPeptide2Site {

	public static String parameter_info() {
		return "[inputFile] [fastaFile] [clusterFile] [tag] [outputFile]";
	}
	public static void execute(String[] args) {
		try {
			String inputFile = args[0];
			String fastaFile = args[1];
			String clusterFile = args[2];
			String tag = args[3];
			String outputFile = args[4];
			
			HashMap motif_map = new HashMap();
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String name = split[0].split("\\|")[1] + "_" + split[1].split(",")[0].replaceAll("\\*", "");
				String motif = split[5];
				if (motif_map.containsKey(motif)) {
					LinkedList list = (LinkedList)motif_map.get(motif);
					if (!list.contains(name)) {
						list.add(name);
					}
					motif_map.put(motif, list);
				} else {
					LinkedList list = new LinkedList();
					list.add(name);
					motif_map.put(motif, list);
				}
			}
			in.close();
			
			HashMap total_phosphosite = new HashMap();
			HashMap peptide2accession = new HashMap();
			
			fstream = new FileInputStream(fastaFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains(">")) {
					String[] split = str.split("\t");
					String accession = split[0].split("\\|")[1];
					String positions = split[1];
					String seq = in.readLine();
					String[] split2 = seq.split("\t");
					String peptide = split2[1];
					
					if (peptide2accession.containsKey(peptide)) {
						LinkedList list = (LinkedList)peptide2accession.get(peptide);
						for (String position: positions.split(",")) {
							position = position.replaceAll("\\*", "");
							if (position.contains("S")) {
								position = "S" + position.replaceAll("S", "");
							} else if (position.contains("T")) {
								position = "T" + position.replaceAll("T", "");
							} else if (position.contains("Y")) {
								position = "Y" + position.replaceAll("Y", "");
							}
							total_phosphosite.put(accession + "_" + position, accession + "_" + position);
							list.add(accession + "_" + position);
							//System.out.println(accession + "\t" + position);
						}
						peptide2accession.put(peptide, list);		
						
					} else {
						LinkedList list = new LinkedList();
						for (String position: positions.split(",")) {
							position = position.replaceAll("\\*", "");
							if (position.contains("S")) {
								position = "S" + position.replaceAll("S", "");
							} else if (position.contains("T")) {
								position = "T" + position.replaceAll("T", "");
							} else if (position.contains("Y")) {
								position = "Y" + position.replaceAll("Y", "");
							}
							total_phosphosite.put(accession + "_" + position, accession + "_" + position);
							list.add(accession + "_" + position);
							//System.out.println(accession + "\t" + position);
						}
						peptide2accession.put(peptide, list);						
					}
				}
			}
			in.close();
			
			HashMap phosphosite = new HashMap();
			HashMap clusterList = new HashMap();									
			fstream = new FileInputStream(clusterFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String gene = split[2].toUpperCase();
				String peptide = split[0].replaceAll("M@", "M");
				peptide = peptide.replaceAll("\\@", "*");
				peptide = peptide.replaceAll("\\#", "*");
				peptide = peptide.replaceAll("\\%", "*");
				
				LinkedList combination = generateCombination(peptide);				
				Iterator itr2 = combination.iterator();
				while (itr2.hasNext()) {						
					String new_peptide = (String)itr2.next();
					//System.out.println(new_peptide);
					if (peptide2accession.containsKey(new_peptide)) {						
						//System.out.println("Hit?");
						LinkedList list = (LinkedList)peptide2accession.get(new_peptide);
						Iterator itr = list.iterator();
						while (itr.hasNext()) {
							String accession = (String)itr.next();
							phosphosite.put(gene + "_" + accession, gene + "_" + accession);
							
							/*String stuff = accession2geneName.get(accession.split("\t")[0]) + "\t" + accession + "\n";
							if (!outputGene.containsKey(stuff)) {
								outputGene.put(stuff, "");
								out.write(clusterFile + "\t" + accession2geneName.get(accession.split("\t")[0]) + "\t" + accession + "\n");
								count++;
							}*/							
						
						}										
					}
				}				
			}
			in.close();
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			out.write(tag + "\t");
			String result = "";
			Iterator itr = phosphosite.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String)itr.next();
				out.write(key + ",");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static LinkedList generateCombination(String peptide) {
		
		peptide = peptide.replaceAll("M@", "M");
		peptide = peptide.replaceAll("\\@", "*");
		peptide = peptide.replaceAll("\\#", "*");
		peptide = peptide.replaceAll("\\%", "*");
		
		LinkedList list = new LinkedList();
		String[] split = peptide.split("\\*");
		
		for (int i = 0; i < split.length - 1; i++) {
			String result = "";
			for (int j = 0; j <= i; j++) {
				result += split[j];
			}
			result += "*";
			for (int k = i + 1; k < split.length; k++) {
				result += split[k];
			}
			list.add(result);
		}
		return list;
	}
}
