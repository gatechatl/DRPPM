package protein.structure.domain.disorder;

import idconversion.tools.Uniprot2GeneID;

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
 * Kyung Ha sent a new list of GR-PR list
 * We need to regenerate the GRPR protein list to examine the disorder region
 * @author tshaw
 *
 */
public class GenerateD2P2Input {

	public static String parameter_info() {
		return "[uniprot2geneIDFile] [GRFile] [PRFile] [outputFile]";
	}
	public static void execute(String[] args) {
		
		
		try {
			
			String inputFile = args[0];
			String GRFile = args[1];
			String PRFile = args[2];
			String outputFile = args[3];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);	
			HashMap gene2uniprot = geneName2uniprot(inputFile);
			HashMap GR = grabFile(GRFile);
			HashMap PR = grabFile(PRFile);			
			HashMap overlap = overlap(GR, PR);
			HashMap GR_Only = exclude(GR, overlap);
			HashMap PR_Only = exclude(PR, overlap);
			//HashMap gene2uniprot = Uniprot2GeneID.geneID2uniprot(inputFile);
			Iterator itr = GR_Only.keySet().iterator();
			while (itr.hasNext()) {
				String gene = (String)itr.next();
				if (gene2uniprot.containsKey(gene)) {
					
					/*LinkedList uniprot = (LinkedList)gene2uniprot.get(gene);
					String uniprot_name = ""; 
					Iterator itr2 = uniprot.iterator();
					while (itr2.hasNext()) {
						String name = (String)itr2.next();
						if (name.length() == 6) {
							if (uniprot_name.equals("")) {
								uniprot_name = name;
							}							
						}
					}*/
					
					out.write("GR_ONLY\t" + gene + "\t" + gene2uniprot.get(gene) + "\n");
					//System.out.println("GR_ONLY\t" + gene + "\t" + uniprot.get(0));
				} else {
					System.out.println("Missing: " + gene);
				}
			}
			itr = PR_Only.keySet().iterator();
			while (itr.hasNext()) {
				String gene = (String)itr.next();
				if (gene2uniprot.containsKey(gene)) {
					/*LinkedList uniprot = (LinkedList)gene2uniprot.get(gene);
					String uniprot_name = ""; 
					Iterator itr2 = uniprot.iterator();
					while (itr2.hasNext()) {
						String name = (String)itr2.next();
						if (name.length() == 6) {
							if (uniprot_name.equals("")) {
								uniprot_name = name;
							}							
						}
					}*/
					out.write("PR_ONLY\t" + gene + "\t" + gene2uniprot.get(gene) + "\n");
					//System.out.println("PR_ONLY\t" + gene + "\t" + uniprot.get(0));
				} else {
					System.out.println("Missing: " + gene);
				}
			}
			itr = overlap.keySet().iterator();
			while (itr.hasNext()) {
				String gene = (String)itr.next();
				if (gene2uniprot.containsKey(gene)) {
					/*LinkedList uniprot = (LinkedList)gene2uniprot.get(gene);
					String uniprot_name = ""; 
					Iterator itr2 = uniprot.iterator();
					while (itr2.hasNext()) {
						String name = (String)itr2.next();
						if (name.length() == 6) {
							if (uniprot_name.equals("")) {
								uniprot_name = name;
							}							
						}
					}*/
					out.write("GRPR\t" + gene + "\t" + gene2uniprot.get(gene) + "\n");
					//System.out.println("GRPR\t" + gene + "\t" + uniprot.get(0));
				} else {
					System.out.println("Missing: " + gene);
				}
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static HashMap geneName2uniprot(String inputFile) {
		HashMap map = new HashMap();
		try {
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains(">")) {
					String accession = str.split("\\|")[1];
					String geneName = "";
					String[] split = str.split(" ");
					for (String tag: split) {
						if (tag.contains("GN=")) {
							geneName = tag.replaceAll("GN=", "");
						}
					}
					map.put(geneName, accession);
				}
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	public static HashMap exclude(HashMap map1, HashMap overlap) {
		HashMap exclude = new HashMap();
		Iterator itr = map1.keySet().iterator();
		while (itr.hasNext()) {
			String gene = (String)itr.next();
			if (!overlap.containsKey(gene)) {
				exclude.put(gene, gene);
			}
		}
		return exclude;
	}
	public static HashMap overlap(HashMap map1, HashMap map2) {
		HashMap overlap = new HashMap();
		Iterator itr = map1.keySet().iterator();
		while (itr.hasNext()) {
			String gene = (String)itr.next();
			if (map2.containsKey(gene)) {
				overlap.put(gene, gene);
			}
		}
		return overlap;
	}
	public static HashMap grabFile(String inputFile) {
		HashMap map = new HashMap();
		try {
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				map.put(str, str);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}
