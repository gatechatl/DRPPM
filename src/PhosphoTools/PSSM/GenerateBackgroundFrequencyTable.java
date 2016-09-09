package PhosphoTools.PSSM;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class GenerateBackgroundFrequencyTable {

	
	public static String type() {
		return "PSSM";
	}
	public static String description() {
		return "Go through the entire UNIPROT database and grab all the S/T/Y site and generate the frequency background for S/T and Y motifs";
	}
	public static String parameter_info() {
		return "[fastaFile] [outputFileST] [outputFileY]";
	}
	public static void execute(String[] args) {
		
		try {
			String fastaFile = args[0];
			String outputFileST = args[1];
			String outputFileY = args[2];
			
			HashMap map = readFastaFile(fastaFile);
			
			HashMap[] background_ST = new HashMap[15];
			HashMap[] background_Y = new HashMap[15];
			HashMap allbackground = new HashMap();
			for (int i = 0; i < 15; i++) {
				background_ST[i] = new HashMap();
				background_Y[i] = new HashMap();
			}
			int totalST = 0;
			int totalY = 0;
			double totalAll = 0;
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String geneName = (String)itr.next();
				String fasta = (String)map.get(geneName);
				for (int i = 0; i < fasta.length() - 15; i++) {
					String aminoacid = fasta.substring(i, i + 1);
					totalAll++;
					if (allbackground.containsKey(aminoacid)) {
						double count = (Double)allbackground.get(aminoacid);
						count++;
						allbackground.put(aminoacid, count);
					} else {
						allbackground.put(aminoacid, 1.0);
					}
					String site = fasta.substring(i + 7, i + 7 + 1);
					if (site.equals("S") || site.equals("T")) {
						totalST++;
						for (int j = 0; j < 15; j++) {
							String aa = fasta.substring(i + j, i + j + 1);
							if (background_ST[j].containsKey(aa)) {
								int count = (Integer)background_ST[j].get(aa);
								count++;
								background_ST[j].put(aa, count);
							} else {
								int count = 0;
								count++;
								background_ST[j].put(aa, count);								
							}
						}
					}
					if (site.equals("Y")) {
						totalY++;
						for (int j = 0; j < 15; j++) {
							String aa = fasta.substring(i + j, i + j + 1);
							if (background_Y[j].containsKey(aa)) {
								int count = (Integer)background_Y[j].get(aa);
								count++;
								background_Y[j].put(aa, count);
							} else {
								int count = 0;
								count++;
								background_Y[j].put(aa, count);								
							}
						}
					}
				}
			}

			FileWriter fwriter = new FileWriter(outputFileST);
            BufferedWriter out = new BufferedWriter(fwriter);

			FileWriter fwriter2 = new FileWriter(outputFileY);
            BufferedWriter out2 = new BufferedWriter(fwriter2);

			String[] amino_acids = {"A", "C", "D", "E", "F", "G", "H", "I", "K", "L", "M", "N", "P", "Q", "R", "S", "T", "V", "W", "Y"};
			out.write("Location");
			out2.write("Location");
			for (String aa: amino_acids) {
				out.write("\t" + aa);
				out2.write("\t" + aa);
			}
			out.write("\n");
			out2.write("\n");
			for (int i = 0; i < 15; i++) {
				out.write((i - 7) + "");
				out2.write((i - 7) + "");
				for (String aa: amino_acids) {			
					
					if (background_ST[i].containsKey(aa)) {
						double count = (Integer)background_ST[i].get(aa);
						double frequency = count / totalST;
						if (i == 7) {
							if (aa.equals("T") || aa.equals("S")) {
								count = (Double)allbackground.get(aa);
								frequency = count / totalAll;
								out.write("\t" + frequency);
							}
						} else {
							out.write("\t" + frequency);
						}
					} else {
						double count = 0;
						double frequency = count / totalST;						
						out.write("\t" + frequency);
					}
					
					
					if (background_Y[i].containsKey(aa)) {
						double count = (Integer)background_Y[i].get(aa);
						double frequency = count / totalY;
						
						if (i == 7) {
							if (aa.equals("Y")) {
								count = (Double)allbackground.get(aa);
								frequency = count / totalAll;
								out2.write("\t" + frequency);
							}
						} else {
							out2.write("\t" + frequency);
						}
						//out2.write("\t" + frequency);
					} else {
						double count = 0;
						double frequency = count / totalY;
						out2.write("\t" + frequency);
					}
					
				}
				out.write("\n");
				out2.write("\n");
			}
			out.close();
			out2.close();
						
		} catch (Exception e) {
			e.printStackTrace();
		}
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
