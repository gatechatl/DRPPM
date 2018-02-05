package proteinfeatures.lowcomplexitydomain;

import idconversion.tools.Uniprot2GeneID;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class GRPRReplaceAnnotationInformation {

	public static String parameter_info() {
		return "[inputOriginalFile] [replaceAnnotation] [uniprot2geneName]";
	}
	public static void execute(String[] args) {
		try {
			String inputFile = args[0];
			String new_annotationFile = args[1];
			String uniprot2geneNameFile = args[2];
			String outputFile = args[3];
			
			HashMap uniprto2geneName = Uniprot2GeneID.uniprot2geneID(uniprot2geneNameFile);
			HashMap annotation = read_annotation(new_annotationFile);
			
			HashMap checkAll = new HashMap();

			FileWriter fwriter = new FileWriter(outputFile);
            BufferedWriter out = new BufferedWriter(fwriter);
            
            
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String type = split[0];
				String name = split[1];
				String accession = split[2];
				if (uniprto2geneName.containsKey(accession)) {
					String geneName = (String)uniprto2geneName.get(accession);
					checkAll.put(geneName, geneName);
					if (annotation.containsKey(geneName)) {
						type = (String)annotation.get(geneName);
						
					} else {
						type = "Human_Proteome";
					}
				} else {
					String geneName = name.split("_")[0];
					checkAll.put(geneName, geneName);
					if (annotation.containsKey(geneName)) {
						type = (String)annotation.get(geneName);
						
					} else {
						type = "Human_Proteome";
					}
					out.write(type + "\t" + split[1] + "\t" + accession + "\n");
					
				}
				out.write(type + "\t" + split[1] + "\t" + accession + "\n");
			}
			in.close();
			out.close();
			Iterator itr = annotation.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String)itr.next();
				if (!checkAll.containsKey(key)) {
					System.out.println("Problem: " + key);
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static HashMap read_annotation(String inputFile) {
		HashMap map = new HashMap();
		try {
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String type = split[1];
				if (type.equals("GR")) {
					type = "GR_ONLY";
				}
				if (type.equals("PR")) {
					type = "PR_ONLY";
				}
				if (map.containsKey(split[0])) {
					String old_type = (String)map.get(split[0]);
					if (!old_type.equals(type)) {
						map.put(split[0], "GRPR");
					} else {
						System.out.println("Additional problem: " + split[0]);
					}
					
				} else {
					map.put(split[0], type);
				}
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}
