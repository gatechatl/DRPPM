package enrichment.tool.go;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class ParseGeneOntology {

	public static String type() {
		return "GENESET";
	}
	public static String description() {
		return "Generate Gene ontology database format";
	}
	public static String parameter_info() {
		return "[inputFile] [go_descriptionFile] [outputFolder] [outputIndex_BP] [outputIndex_MF] [outputIndex_CC]";
	}
	public static void execute(String[] args) {
		
		try {
			String inputFile = args[0];
			String go_descriptionFile = args[1];
			String outputFolder = args[2];
			String outputIndex_BP = args[3];
			String outputIndex_MF = args[4];
			String outputIndex_CC = args[5];
			
			HashMap geneontology = new HashMap();
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (!str.substring(0, 1).equals("!")) {
					if (geneontology.containsKey(split[4])) {
						LinkedList list = (LinkedList)geneontology.get(split[4]);
						if (!list.contains(split[2])) {
							list.add(split[2]);
							geneontology.put(split[4], list);
						}
					} else {
						LinkedList list = new LinkedList();
						list.add(split[2]);
						geneontology.put(split[4], list);
					}
				}
			}
			in.close();
			
			HashMap alias = new HashMap();
			
			HashMap alias_BP = new HashMap();
			HashMap alias_MF = new HashMap();
			HashMap alias_CC = new HashMap();
			fstream = new FileInputStream(go_descriptionFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains("id: ") && !str.contains("alt_id")) {
					String go = str.replaceAll("id: " , "");
					String name = in.readLine().replaceAll("name: ", "");
					String namespace = in.readLine().replaceAll("namespace: ", "");
					alias.put(go, name);
					if (namespace.equals("biological_process")) {
						alias_BP.put(go, namespace);
					} else if (namespace.equals("molecular_function")) {
						alias_MF.put(go, namespace);
					} else if (namespace.equals("cellular_component")) {
						alias_CC.put(go, namespace);
					}
				}
			}
			in.close();
			
			
			FileWriter fwriter_bp = new FileWriter(outputIndex_BP);
			BufferedWriter out_bp = new BufferedWriter(fwriter_bp);
			
			FileWriter fwriter_cc = new FileWriter(outputIndex_CC);
			BufferedWriter out_cc = new BufferedWriter(fwriter_cc);
			
			FileWriter fwriter_mf = new FileWriter(outputIndex_MF);
			BufferedWriter out_mf = new BufferedWriter(fwriter_mf);
			
			Iterator itr = geneontology.keySet().iterator();
			while (itr.hasNext()) {
				String go = (String)itr.next();
				String name = (String)alias.get(go);
				if (alias_BP.containsKey(go)) {
					out_bp.write(name + "\t" + outputFolder + "/" + go + ".txt" + "\n");
				} else if (alias_MF.containsKey(go)) {
					out_mf.write(name + "\t" + outputFolder + "/" + go + ".txt" + "\n");
				} else if (alias_CC.containsKey(go)) {
					out_cc.write(name + "\t" + outputFolder + "/" + go + ".txt" + "\n");
				}
				FileWriter fwriter3 = new FileWriter(outputFolder + "/" + go + ".txt");
				BufferedWriter out3 = new BufferedWriter(fwriter3);
				out3.write(go + " " + name + "\n");
				LinkedList list = (LinkedList)geneontology.get(go);
				Iterator itr2 = list.iterator();
				while (itr2.hasNext()) {
					String gene = (String)itr2.next();
					out3.write(gene + "\n");
				}
				out3.close();
			}
			out_bp.close();
			out_cc.close();
			out_mf.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
