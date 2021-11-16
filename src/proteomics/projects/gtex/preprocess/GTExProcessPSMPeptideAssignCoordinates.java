package proteomics.projects.gtex.preprocess;

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
 * Search for the PSM coordinates in the Ensembl Protein fasta file
 * @author 4472414
 *
 */
public class GTExProcessPSMPeptideAssignCoordinates {

	
	public static void main(String[] args) {
		
		try {
			HashMap map = new HashMap();
			HashMap gene2protein = new HashMap();
			String protein_id = "";
			String inputFileFasta = "/Users/4472414/References/genome/Exon_Annotation/Homo_sapiens.GRCh38.pep.all.fa";
			FileInputStream fstream = new FileInputStream(inputFileFasta);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains(">")) {
					protein_id = str.split("_")[0].replaceAll(">", "");		
					if (str.contains("gene:")) {
						String geneID = str.split("gene:")[1].split(" ")[0].split("\\.")[0];
						System.out.println(geneID);
						if (gene2protein.containsKey(geneID)) {
							
							LinkedList list = (LinkedList)gene2protein.get(geneID);
							list.add(protein_id);
							gene2protein.put(geneID, list);
						} else {
							LinkedList list = new LinkedList();
							list.add(protein_id);
							gene2protein.put(geneID, list);
						}
					}
				} else {
					if (map.containsKey(protein_id)) {
						String seq = (String)map.get(protein_id);
						seq += str.trim();
						map.put(protein_id, seq);
					} else {
						map.put(protein_id, str.trim());
					}
				}
			}
			in.close();
			

			String outputFile = "/Users/4472414/Projects/GTEx_Proteomics_Search/PSM/outputTable_annotation.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			String inputGtexPeptideFile = "/Users/4472414/Projects/GTEx_Proteomics_Search/PSM/outputTable.txt";
			FileInputStream fstream2 = new FileInputStream(inputGtexPeptideFile);
			DataInputStream din2 = new DataInputStream(fstream2);
			BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
			while (in2.ready()) {
				String str = in2.readLine();
				String[] split = str.split("\t");
				String geneID = split[2].split("\\.")[0];
				String found_str = "";
				if (gene2protein.containsKey(geneID)) {
					LinkedList list = (LinkedList)gene2protein.get(geneID);
					Iterator itr = list.iterator();
					while (itr.hasNext()) {
						String proteinID = (String)itr.next();
						String seq = (String)map.get(proteinID);
						int index = seq.indexOf(split[0]);
						if (index >= 0) {
							found_str += proteinID + ":" + (index + 1) + ":" + (index + split[0].length() + 1) + ",";
						}
					}
				} else {
					System.out.println("Missing" + geneID);
				}
				if (found_str.equals("")) {
					found_str = "NA";
				}
				out.write(str + "\t" + found_str + "\n");
			}
			in2.close();
			out.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
