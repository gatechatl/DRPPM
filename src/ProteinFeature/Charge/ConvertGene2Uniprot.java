package ProteinFeature.Charge;

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

public class ConvertGene2Uniprot {

	public static String type() {
		return "PROTEINFEATURE";
	}
	public static String description() {
		return "Generate list file for charge input";
	}
	public static String parameter_info() {
		return "[inputFile] [uniprotFile] [homoFasta] [type]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String uniprotFile = args[1];
			String homoFasta = args[2];
			String type = args[3];
			String outputFile = args[4];
			HashMap gene2uniprot = Uniprot2GeneID.geneID2uniprot(uniprotFile);
			HashMap accession2tag = loadAccession2Tag(homoFasta);
			HashMap seq = loadFastaFile(homoFasta);
			

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String geneName = in.readLine();
				if (gene2uniprot.containsKey(geneName)) {
					LinkedList list = (LinkedList)gene2uniprot.get(geneName);
					Iterator itr = list.iterator();
					while (itr.hasNext()) {
						String accession = (String)itr.next();
						if (accession2tag.containsKey(accession)) {
							String tag = (String)accession2tag.get(accession);
							System.out.println(type + "\t" + tag + "\t" + accession);
							out.write(">" + accession + "\n" + seq.get(accession) + "\n");
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
	public static HashMap loadAccession2Tag(String inputFile) {
		HashMap map = new HashMap();
		try {
			String name = "";			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine().trim();
				if (str.contains(">")) {
					String accession = str.split("\\|")[1];
					String tag = str.split(" ")[0].split("\\|")[2];
					map.put(accession, tag);
				} else {					
					
				}
			}
			in.close();			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	public static HashMap loadFastaFile(String inputFile) {
		HashMap map = new HashMap();
		try {
			String name = "";			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine().trim();
				if (str.contains(">")) {
					name = str.split("\\|")[1];					
				} else {					
					if (map.containsKey(name)) {
						String seq = (String)map.get(name);
						seq += str;
						map.put(name, seq);
					} else {
						map.put(name, str);
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
