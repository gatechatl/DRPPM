package proteomics.phospho.tools.enrichment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class GenerateKinaseSubstrateGMT {

	public static String parameter_info() {
		return "[inputFIle] [outputFolder]";
	}
	public static void execute(String[] args) {
		
		try {
			
			HashMap kinase_substrate = new HashMap();
			String inputFile = args[0];
			String outputFolder = args[1];
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String kinase = split[0].toUpperCase();
				String tag = "";
				if (split[3].equals("CONVERT_UNIPROT")) {
					tag = split[6].toUpperCase() + "_" + split[5] + "_" + split[8];
				} else {
					tag = split[6].toUpperCase() + "_" + split[3] + "_" + split[4];
				}
				if (kinase_substrate.containsKey(kinase)) {
					LinkedList list = (LinkedList)kinase_substrate.get(kinase);
					if (!list.contains(tag)) {
						list.add(tag);
					}
					kinase_substrate.put(kinase, list);					
				} else {
					LinkedList list = new LinkedList();					
					list.add(tag);
					kinase_substrate.put(kinase, list);
				}
			}
			in.close();
			
			Iterator itr = kinase_substrate.keySet().iterator();
			while (itr.hasNext()) {
				String kinase = (String)itr.next();
				if (!kinase.contains("/")) {
					System.out.println(kinase + "\t" + outputFolder + "/" + kinase + "_gmt.txt");
					FileWriter fwriter = new FileWriter(outputFolder + "/" + kinase + "_gmt.txt");
					BufferedWriter out = new BufferedWriter(fwriter);
					out.write(">" + kinase + "\n");
					LinkedList list = (LinkedList)kinase_substrate.get(kinase);
					Iterator itr2 = list.iterator();
					while (itr2.hasNext()) {
						String substrate = (String)itr2.next();
						out.write(substrate + "\n");
					}				
					out.close();
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
