package proteomics.phospho.tools.summary;

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
 * Generate a kinase summary based on the gene list specified.
 * @author tshaw
 *
 */
public class ExtractLineBasedOnList {

	public static String parameter_info() {
		return "[inputFile] [geneListFile] [index] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];			
			String geneListFile = args[1];
			int accession_index = new Integer(args[2]);
			int site_index = new Integer(args[3]);
			String outputFile = args[4];
			HashMap map = grabPhosphoPrediction(inputFile);
			
			HashMap total = new HashMap();
			HashMap found = new HashMap();			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileInputStream fstream = new FileInputStream(geneListFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));	
			String header = in.readLine();
			out.write(header + "\tReference\tPredicted" + "\n");			
			while (in.ready()) {
				String str = in.readLine().trim();
				String[] split = str.split("\t");
				String accession = split[accession_index];
				String sites = split[site_index];
				for (String site: sites.split(",")) {
					String key = accession + "\t" + site;
					total.put(key, key);
					if (map.containsKey(key)) {
						
						found.put(key, key);
						String Ref = "NA";
						String Pred = "NA";
						LinkedList list = (LinkedList)map.get(key);
						Iterator itr = list.iterator();						
						while (itr.hasNext()) {
							String kinase = (String)itr.next();
							if (kinase.contains("Ref:")) {
								if (Ref.equals("NA")) {
									Ref = kinase.replaceAll("Ref:", "");
								} else {
									Ref += "," + kinase.replaceAll("Ref:", "");
								}
							}
							if (kinase.contains("Pred:")) {
								if (Pred.equals("NA")) {
									Pred = kinase.replaceAll("Pred:", "");
								} else {
									Pred += "," + kinase.replaceAll("Pred:", "");
								}
							}
						}
						out.write(str + "\t" + Ref + "\t" + Pred + "\n");
						
					}
				}
			}
			in.close();
			out.close();
			System.out.println("Numnber of item in query: " + total.size());
			System.out.println("Number found: " + found.size());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String grabHeader(String inputFile) {
		String header = "";
		try {
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return header;
	}
	public static HashMap grabPhosphoPrediction(String inputFile) {
		HashMap map = new HashMap();
		try {

			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine().trim();
				String[] split = str.split("\t");
				String accession = split[0];
				String site = split[1];
				site = cleanSite(site);
				String phosphositeRefGeneName = split[11];
				String neighbor= split[18];
				if (!phosphositeRefGeneName.equals("NA")) {
					if (map.containsKey(accession + "\t" + site)) {
						LinkedList list = (LinkedList)map.get(accession + "\t" + site);
						if (!list.contains("Ref:" + phosphositeRefGeneName)) {
							list.add("Ref:" + phosphositeRefGeneName);
						}
						if (neighbor.equals("found_neighbor") || neighbor.equals("found_predicted_neighbor")) {
							String pred_gene = split[17];
							if (!list.contains("Pred:" + pred_gene)) {
								list.add("Pred:" + pred_gene);
							}
						}
						map.put(accession + "\t" + site, list);
					} else {
						LinkedList list = new LinkedList();
						if (!list.contains("Ref:" + phosphositeRefGeneName)) {
							list.add("Ref:" + phosphositeRefGeneName);
						}
						if (neighbor.equals("found_neighbor") || neighbor.equals("found_predicted_neighbor")) {
							String pred_gene = split[17];
							if (!list.contains("Pred:" + pred_gene)) {
								list.add("Pred:" + pred_gene);
							}
						}
						map.put(accession + "\t" + site, list);
					}
				} else {
					if (map.containsKey(accession + "\t" + site)) {
						LinkedList list = (LinkedList)map.get(accession + "\t" + site);
						if (neighbor.equals("found_neighbor") || neighbor.equals("found_predicted_neighbor")) {
							String pred_gene = split[17];
							if (!list.contains("Pred:" + pred_gene)) {
								list.add("Pred:" + pred_gene);
							}
						}
						map.put(accession + "\t" + site, list);
					} else {
						LinkedList list = new LinkedList();						
						if (neighbor.equals("found_neighbor") || neighbor.equals("found_predicted_neighbor")) {
							String pred_gene = split[17];
							if (!list.contains("Pred:" + pred_gene)) {
								list.add("Pred:" + pred_gene);
							}
						}
						map.put(accession + "\t" + site, list);
					}				
				}
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	public static String cleanSite(String site) {
		String msite = site.split(",")[0];
		String newSite = site;
		if (msite.contains("S")) {
			newSite = "S" + msite.replaceAll("S", "").replaceAll("\\*", "");
		} else if (msite.contains("T")) {
			newSite = "T" + msite.replaceAll("T", "").replaceAll("\\*", "");
		} else if (msite.contains("Y")) {
			newSite = "Y" + msite.replaceAll("Y", "").replaceAll("\\*", "");
		}
		//System.out.println(newSite);
		return newSite;
	}
}
