package proteomics.phospho.tools.annotation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Append mTORC1 score to the raptor substrate file
 * @author tshaw
 *
 */
public class AppendKinaseMotif2PeptideTable {

	public static String type() {
		return "PHOSPHO";
	}
	public static String description() {
		return "Append kinase score to the raptor substrate file";
	}
	public static String parameter_info() {
		return "[peptideFile] [motifFolder] [kinase_name] [geneNameIndex: {4}] [accessionIndex: {2}] [siteIndex {14}] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			String peptideFile = args[0];
			String motifFolder = args[1];
			String kinase_name = args[2];
			int geneIndex = new Integer(args[3]);
			int accessionIndex = new Integer(args[4]);
			int siteIndex = new Integer(args[5]);
			String outputFile = args[6];			
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);

			FileInputStream fstream = new FileInputStream(peptideFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();

			out.write(header + "\tNewID\tMotifScore\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				//String geneName = split[4].toUpperCase();
				String geneName = split[geneIndex].toUpperCase();
				//String accession = split[2].split("\\|")[1];
				String accession = split[accessionIndex].split("\\|")[1];
				//String sites = split[14];
				String sites = split[siteIndex];
				for (String site: sites.split(",")) {
					String id = geneName + "_" + accession + "_" + site;
					if (map.containsKey(id)) {
						String clean_site = site.replaceAll("S",  "").replaceAll("T", "").replaceAll("Y", "");
						out.write(str + "\t" + id + "\t" + map.get(id) + "\n");
					} else {
						String clean_site = site.replaceAll("S",  "").replaceAll("T", "").replaceAll("Y", "");
						String motifScore = "NA";
						File f = new File(motifFolder + "/" + accession + ".txt");
						if (f.exists()) {
							FileInputStream fstream2 = new FileInputStream(motifFolder + "/" + accession + ".txt");
							DataInputStream din2 = new DataInputStream(fstream2);
							BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
							String in2_header = in2.readLine();
							String[] split_header = in2_header.split("\t");
							int kinase_index = -1;
							for (int i = 0; i < split_header.length; i++) {
								if (split_header[i].split("_")[0].toUpperCase().equalsIgnoreCase(kinase_name)) {
									kinase_index = i;
									System.out.println("Hit");
								}
							}
							while (in2.ready()) {
								String str2 = in2.readLine();
								String[] split2 = str2.split("\t");
								if (split2[0].equals(accession + "_" + clean_site)) {
									motifScore = split2[kinase_index];
									break;
								}
							}
							in2.close();
							out.write(str + "\t" + id + "\t" + motifScore + "\n");
						} else {
							System.out.println("Still Missing: " + id);
							out.write(str + "\t" + id + "\tNA\n");
						}
					}
				}
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

