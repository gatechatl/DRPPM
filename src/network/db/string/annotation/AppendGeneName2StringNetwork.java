package network.db.string.annotation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class AppendGeneName2StringNetwork {

	public static String parameter_info() {
		return "[inputFile] [aliasFile] [uniprotFile] [hg18_flatfile] [ensembl2geneName] [outputFile]";
	}
	
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String aliasFile = args[1];
			String referenceFastaFile = args[2];
			String referenceFile2 = args[3];
			String ensemblprotein2geneNameFile = args[4];
			String outputFile = args[5];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);

			HashMap ensemblprotein2geneName = ensemblprotein2geneName(ensemblprotein2geneNameFile);
			HashMap ref_geneName_map = grabReferenceGeneName(referenceFile2);
			HashMap ref_map = grabReference(referenceFastaFile, ref_geneName_map);
			
			HashMap stringID2geneName = grabGeneName(aliasFile, ref_map, ensemblprotein2geneName);
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write(header + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String protein_A = split[0];
				String protein_B = split[1];	
				if (stringID2geneName.containsKey(protein_A) && stringID2geneName.containsKey(protein_B)) {
					String geneA = (String)stringID2geneName.get(protein_A);
					String geneB = (String)stringID2geneName.get(protein_B);
					out.write(geneA + "\t" + geneB);
					for (int i = 2; i < split.length; i++) {
						out.write("\t" + split[i]);
					}
					out.write("\n");
				}
				
				
			}
			in.close();
			out.close();
		} catch (Exception e) {
			
		}
	}
	
	public static HashMap ensemblprotein2geneName(String inputFile) {
		HashMap map = new HashMap();
		try {
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(split[0], split[2]);
								
			}
			in.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public static HashMap grabReferenceGeneName(String inputFile) {
		HashMap map = new HashMap();
		try {
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(split[0], split[0]);
								
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	public static HashMap grabReference(String inputFile, HashMap refGeneName) {
		HashMap map = new HashMap();
		try {
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains(">")) {
					String name = str.split(" ")[0].split("\\|")[2];
					if (str.contains("GN=")) {
						String geneName = str.split("GN=")[1].split(" ")[0];
						if (refGeneName.containsKey(geneName)) {
							map.put(name, geneName);
						}
					} else {
						String geneName = name.replaceAll("_HUMAN", "");
						if (refGeneName.containsKey(geneName)) {
							map.put(name, geneName);
						}
					}
				}
								
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	public static HashMap grabGeneName(String aliasinputFile, HashMap reference, HashMap ensembl2geneName) {
		HashMap map = new HashMap();
		try {
			FileInputStream fstream = new FileInputStream(aliasinputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String proteinID = split[0].split("\\.")[1];
				if (ensembl2geneName.containsKey(proteinID)) {
					String geneName = (String)ensembl2geneName.get(proteinID);
					map.put(split[0], geneName);
				} else if (reference.containsKey(split[1])) {
					String geneName = (String)reference.get(split[1]);
					if (map.containsKey(split[0])) {
						String prev = (String)map.get(split[0]);
						if (!prev.equals(geneName)) {
							System.out.println(split[0] + "\t" + prev + "\t" + geneName);
							map.put(split[0], geneName + "_" + prev);
						}
					} else {
						map.put(split[0], geneName);
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
