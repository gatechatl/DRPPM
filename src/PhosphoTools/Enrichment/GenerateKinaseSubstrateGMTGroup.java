package PhosphoTools.Enrichment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class GenerateKinaseSubstrateGMTGroup {

	public static String parameter_info() {
		return "[inputFile] [otherAlias] [kinaseFamily] [kinaseFamily2] [outputGroupFolder] [missed]";
	}
	public static void execute(String[] args) {
		
		try {
			
			HashMap kinase_substrate = new HashMap();
			
			String inputFile = args[0];			
			String otherSynonym = args[1];
			String kinaseFamily = args[2];
			String kinaseFamily2 = args[3];
			String outputFolderGroup = args[4];
			String missedFile = args[5];

			FileWriter fwriter2 = new FileWriter(missedFile);
			BufferedWriter out2 = new BufferedWriter(fwriter2);
			
			HashMap synonym = new HashMap();
			FileInputStream fstream = new FileInputStream(otherSynonym);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine().toUpperCase();
				String[] split = str.split("\t");
				if (split.length > 1) {
					split[1] = split[1].replaceAll(" ", "");
					String[] split2 = split[1].split(",");
					for (String name: split2) {
						if (!name.equals("")) {
							synonym.put(name, split[0]);
						}
					}
				}
			}
			in.close();
			
			HashMap kinase_group = new HashMap();
			HashMap kinase_family = new HashMap();
			fstream = new FileInputStream(kinaseFamily);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			while (in.ready()) {
				String str = in.readLine().toUpperCase();
				String[] split = str.split("\t");
				if (split.length > 2) {
					split[2] = split[2].replaceAll(" ", "");
					String[] split2 = split[2].split(",");
					for (String name: split2) {
						String geneName = name;
						kinase_group.put(geneName, split[0]);
						kinase_family.put(geneName, split[1]);
						
						if (synonym.containsKey(name)) {
							geneName = (String)synonym.get(name);
						}
						kinase_group.put(geneName, split[0]);
						kinase_family.put(geneName, split[1]);
						
					}
				}
			}
			in.close();
			
			fstream = new FileInputStream(kinaseFamily2);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			while (in.ready()) {
				String str = in.readLine().toUpperCase();
				String[] split = str.split("\t");
				if (split.length > 2) {
					split[2] = split[2].replaceAll(" ", "");
					String[] split2 = split[2].split(",");
					for (String name: split2) {
						String geneName = name;
						kinase_group.put(geneName, split[6]);
						kinase_family.put(geneName, split[7]);
						if (synonym.containsKey(name)) {
							geneName = (String)synonym.get(name);
						}
						kinase_group.put(geneName, split[6]);
						kinase_family.put(geneName, split[7]);
						
					}
				}
			}
			in.close();
			
			
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String kinase = split[0].toUpperCase();
				String group = "";
				if (kinase_group.containsKey(kinase)) {
					group = (String)kinase_group.get(kinase);
				} else {
					out2.write(kinase + "\n");
				}
				String tag = "";
				if (split[3].equals("CONVERT_UNIPROT")) {
					tag = split[6].toUpperCase() + "_" + split[5] + "_" + split[8];
				} else {
					tag = split[6].toUpperCase() + "_" + split[3] + "_" + split[4];
				}
				if (kinase_substrate.containsKey(group)) {
					LinkedList list = (LinkedList)kinase_substrate.get(group);
					if (!list.contains(tag)) {
						list.add(tag);
					}
					kinase_substrate.put(group, list);					
				} else {
					LinkedList list = new LinkedList();					
					list.add(tag);
					kinase_substrate.put(group, list);
				}
			}
			in.close();

			Iterator itr = kinase_substrate.keySet().iterator();
			while (itr.hasNext()) {
				String kinase = (String)itr.next();
				if (!kinase.equals("") && !kinase.contains("/")) {
					System.out.println(kinase + "\t" + outputFolderGroup + "/" + kinase + "_gmt.txt");
					FileWriter fwriter = new FileWriter(outputFolderGroup + "/" + kinase + "_gmt.txt");
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
			
			out2.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

