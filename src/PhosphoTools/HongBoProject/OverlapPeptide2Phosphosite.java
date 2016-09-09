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

/**
 * Overlap Peptide against funcitonal Phosphosite
 * @author tshaw
 *
 */
public class OverlapPeptide2Phosphosite {
	public static String parameter_info() {
		return "[inputFile] [clusterFile] [functionListFile] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			HashMap peptide2accession = new HashMap();
			String inputFile = args[0];
			String clusterFile = args[1];
			String functionListFile = args[2]; // check substrate function
			String outputFile = args[3];
			
			HashMap accession2geneName = new HashMap();
			HashMap function = new HashMap();
			FileInputStream fstream = new FileInputStream(functionListFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String site = split[3].split("-")[0];
				if (site.contains("S")) {
					site = site.substring(1, site.length()) + "S";
				}
				if (site.contains("T")) {
					site = site.substring(1, site.length()) + "T";
				}
				if (site.contains("Y")) {
					site = site.substring(1, site.length()) + "Y";
				}
				//System.out.println(split[1] + "\t" + site);
				accession2geneName.put(split[1], split[0]);
				function.put(split[1] + "\t" + site, split[1] + "\t" + site);
			}
			in.close();
			
			fstream = new FileInputStream(inputFile);
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
							list.add(accession + "\t" + position);
							//System.out.println(accession + "\t" + position);
						}
						peptide2accession.put(peptide, list);		
						
					} else {
						LinkedList list = new LinkedList();
						for (String position: positions.split(",")) {
							position = position.replaceAll("\\*", "");
							list.add(accession + "\t" + positions);
							//System.out.println(accession + "\t" + position);
						}
						peptide2accession.put(peptide, list);
						
					}
				}
			}
			in.close();
								
			int count = 0;
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);			
			HashMap outputGene = new HashMap();
			
			HashMap phosphosite = new HashMap();
			HashMap clusterList = new HashMap();									
			fstream = new FileInputStream(clusterFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				
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
							phosphosite.put(accession, accession);
							if (function.containsKey(accession)) {
								String stuff = accession2geneName.get(accession.split("\t")[0]) + "\t" + accession + "\n";
								if (!outputGene.containsKey(stuff)) {
									outputGene.put(stuff, "");
									out.write(clusterFile + "\t" + accession2geneName.get(accession.split("\t")[0]) + "\t" + accession + "\n");
									count++;
								}							
							}
						}										
					}
				}				
			}
			in.close();
			out.close();
			System.out.println("Total Functional Sites: " + function.size());			
			System.out.println("TotalPhosphosite: " + phosphosite.size());
			System.out.println("TotalHit: " + count);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		LinkedList list = generateCombination("R.LQTTDNLLPM@S@PEEFDEM@SR.I");
		Iterator itr = list.iterator();
		while (itr.hasNext()) {
			String seq = (String)itr.next();
			System.out.println(seq);
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
