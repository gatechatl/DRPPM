package proteomics.phospho.tools.misc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class GrabFastaFile {
	public static String type() {
		return "MISC";
	}
	public static String description() {
		return "Grab Fasta File given an index file";
	}
	public static String parameter_info() {
		return "[accessionFile] [tagFlag] [human_fasta] [outputFolder]";
	}
	public static void execute(String[] args) {		
		try {
			
			// step1 input a fasta sequence ; will change to sliding window later
			
			String accession_file = args[0];
			String tag_flag = args[1];
			String human_fasta = args[2];
			String outputFile = args[3];
			
			FileWriter fwriter = new FileWriter(outputFile);
            BufferedWriter out = new BufferedWriter(fwriter);
            
			HashMap map = loadFastaFile(human_fasta);
			HashMap accession2name = loadFastaName(human_fasta);
			
						
			LinkedList list = grabList(accession_file, tag_flag);
			Iterator itr = list.iterator();
			while (itr.hasNext()) {
				String accession = (String)itr.next();
				
				String name = (String)accession2name.get(accession);
				name = name.replaceAll("\\|", "_") + "_" + accession;
				
				//String outputFile = outputFolder + "/" + name + ".txt";
				//String outputImg = outputFolder + "/" + name + ".png";

	            //out.write("AAPosition\tAminoAcid\tChargeScore\tType\n");
				if (map.containsKey(accession)) {
					String inputSeq = (String)map.get(accession);
					out.write(">" + name + "\n" + inputSeq + "\n");
					//System.out.println(inputSeq);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	public static LinkedList grabList(String inputFile, String tag) {
		LinkedList list = new LinkedList();
		try {
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine().trim();
				String[] split = str.split("\t");
				if (split[0].equals(tag)) {
					list.add(split[2]);
				}
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	public static HashMap loadFastaName(String inputFile) {
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
					String geneName = "";
					String[] split = str.split(" ");
					for (String tag: split) {
						if (tag.contains("GN=")) {
							geneName = tag.replaceAll("GN=", "");
						}
					}
					map.put(name, geneName);
				} else {					
					
				}
			}
			in.close();			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}