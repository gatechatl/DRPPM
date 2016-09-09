package PhosphoTools.PSSM;

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
 * Read in the PSSM Scores and for each position calculate the score
 * @author tshaw
 *
 */
public class GeneratePSSMUniprotDatabase {

	public static String type() {
		return "PSSM";
	}
	public static String description() {
		return "Generate PSSM Uniprot Database";
	}
	public static String parameter_info() {
		return "[inputFile] [fastaFile] [outputPath]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0]; // index of PSSM files
			String fastaFile = args[1];
			String outputPath = args[2];
			
			
			HashMap map = readFastaFile(fastaFile);
			
			HashMap pssms = new HashMap();
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));					
			while (in.ready()) {
				String str = in.readLine().trim();
				String[] split = str.split("\t");
				HashMap[] maps = readPSSMFile(split[1]);
				pssms.put(split[0], maps);
			}
			in.close();

			
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String accession = (String)itr.next();
				String fasta = (String)map.get(accession);
				
				FileWriter fwriter = new FileWriter(outputPath + "/" + accession + ".txt");
				BufferedWriter out = new BufferedWriter(fwriter);
				
				out.write("Location\tPeptide");
				Iterator itr2 = pssms.keySet().iterator();
				while (itr2.hasNext()) {
					String kinase_motif = (String)itr2.next();
					out.write("\t" + kinase_motif);
				}
				out.write("\n");
				
				for (int i = 0; i < fasta.length(); i++) {
					String site = fasta.substring(i, i + 1);
					String site_fasta = "";
					if (site.equals("S") || site.equals("T") || site.equals("Y")) {
						
						int upstream = 7 - i;						
						int downstream = 7 - (fasta.length() - i - 1);						
						if (upstream > 0) {
							for (int j = 0; j < upstream; j++) {
								site_fasta += "X";
							}
						}
						
						for (int j = i - 7; j < i + 8; j++) {
							if (j >= 0 && j < fasta.length()) {
								site_fasta += fasta.substring(j, j + 1);
							}
						}
						if (downstream > 0) {
							for (int j = 0; j < downstream; j++) {
								site_fasta += "X";
							}
						}
						
						out.write(accession + "_" + (i + 1) + "\t" + site_fasta);
						Iterator itr3 = pssms.keySet().iterator();
						while (itr3.hasNext()) {
							String kinase_motif = (String)itr3.next();
							HashMap[] pssm = (HashMap[])pssms.get(kinase_motif);
							double score = calculateScore(site_fasta, pssm);
							out.write("\t" + score);
						}		
						out.write("\n");
						
					} // each site
					
				} // scan fasta
				out.close();		
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static double calculateScore(String fasta, HashMap[] map) {
		if (fasta.length() != 15) {
			return -999;
		}
		double score = 0;
		for (int i = 0; i < 15; i++) {
			String aa = fasta.substring(i, i + 1);
			if (map[i].containsKey(aa)) {
				double value = (Double)map[i].get(aa);
				score += value;
			}
		}
		return score;
	}
	public static HashMap[] readPSSMFile(String inputFile) {
		HashMap[] maps = new HashMap[15];
		for (int i = 0; i < 15; i++) {
			maps[i] = new HashMap();
		}
		try {
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String[] header = in.readLine().split("\t");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				int index = new Integer(split[0]) + 7;
				for (int i = 1; i < split.length; i++) {
					maps[index].put(header[i], new Double(split[i]));
				}				
			}
			in.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return maps;
	}
	public static HashMap readFastaFile(String inputFile) {
		HashMap map = new HashMap();
		try {
			String uniprotName = "";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains(">")) {
					String[] split = str.split("\\|");
					uniprotName = split[1];					
					
				} else {
					if (map.containsKey(uniprotName)) {
						String seq = (String)map.get(uniprotName);
						seq += str.trim();
						map.put(uniprotName, seq);
					} else {
						map.put(uniprotName, str.trim());
					}
				}				
			}
			in.close();			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}
