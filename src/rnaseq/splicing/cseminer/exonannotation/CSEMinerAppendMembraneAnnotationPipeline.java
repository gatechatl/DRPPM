package rnaseq.splicing.cseminer.exonannotation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class CSEMinerAppendMembraneAnnotationPipeline {

	
	public static String type() {
		return "CSIMINER";
	}
	public static String description() {
		return "Add annotation information. GeneName that is contained in the row name is considered as a match.";
	}
	public static String parameter_info() {
		return "[inputFile] [geneListFile] [outputFile]";
	}
	public static void execute(String[] args) {
		try {
				
			String inputFile = args[0];
			
			HashMap map = generateHashMapList(args[1]);
			LinkedList list = generateOrder(args[1]);
			String outputFile = args[2]; //"/Users/4472414/References/genome/Exon_Annotation/exon_annotation_updated_withblat.txt"; //args[0]; 
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			
			
			Iterator itr = list.iterator();
			String additional_header = "";
			LinkedList tags = new LinkedList();
			while (itr.hasNext()) {
				String tag = (String)itr.next();
				tags.add(tag);
				additional_header += "\t" + tag;
			}
			additional_header += "\tMETAINFOHIT";
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine() + additional_header;
			//System.out.println(header);
			out.write(header + "\n");
			while (in.ready()) {
				String str = in.readLine();
				boolean isTRUE = false;
				String[] split = str.split("\t");
				String geneName = split[0].replaceAll("\"", "").split("_")[0].split(":")[0].toUpperCase();
				String annotation = "";
				itr = list.iterator();
				while (itr.hasNext()) {
					String tag = (String)itr.next();
					HashMap geneList = (HashMap)map.get(tag);
					boolean found = false;
					Iterator itr2 = geneList.keySet().iterator();
					while (itr2.hasNext()) {
						String gene = (String)itr2.next();
						gene = gene.toUpperCase();
						if (geneName.equals(gene)) {
							found = true;
						}
					}
					if (found) {
						annotation += "\tTRUE";
						isTRUE = true;
					} else {
						annotation += "\tFALSE";	
					}
				}
				if (isTRUE) {
					annotation += "\tTRUE";
				} else {
					annotation += "\tFALSE";
				}
				//System.out.println(str + annotation);
				out.write(str + annotation + "\n");
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static HashMap generateHashMapList(String inputFile) {
		HashMap map = new HashMap();
		try {
			
			FileInputStream fstream2 = new FileInputStream(inputFile);
			DataInputStream din2 = new DataInputStream(fstream2);
			BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
			while (in2.ready()) {
				String str = in2.readLine();
				String[] split = str.split("\t");
				String name = split[0];
				String fileName = split[1];
				HashMap geneList = createGeneList(fileName);
				map.put(name, geneList);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	public static LinkedList generateOrder(String inputFile) {
		LinkedList list = new LinkedList();
		try {
			
			FileInputStream fstream2 = new FileInputStream(inputFile);
			DataInputStream din2 = new DataInputStream(fstream2);
			BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
			while (in2.ready()) {
				String str = in2.readLine();
				String[] split = str.split("\t");
				String name = split[0];
				String fileName = split[1];
				//HashMap geneList = createGeneList(fileName);
				list.add(name);
			}
			in2.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	public static HashMap createGeneList(String inputFile) {
		HashMap map = new HashMap();
		try {
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				map.put(str, str);
			}
			in.close();			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}
