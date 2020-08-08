package proteomics.phospho.kinaseactivity.pipeline;

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
 * Assign kinase substrate relationship to JUMPq peptide publication file
 * If two site have the same unique identifier, we use the one with the higher PSM if PSM is same we use the highest intensity value
 * @author tshaw
 *
 */
public class AssignKinaseSubstrate2JUMPqPeptide {

	public static String description() {
		return "Assign kinase substrate relationship to JUMPq peptide publication file";
	}
	public static String type() {
		return "KINASEACTIVITY";
	}
	public static String parameter_info() {
		return "[inputJUMPqPeptideFile] [phosphosite_kinsub_file] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String phosphosite_kinsub_file = args[1];
			String outputFile = args[2];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			HashMap kinase_substrate = new HashMap();
			FileInputStream fstream = new FileInputStream(phosphosite_kinsub_file);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String kinase = split[0].toUpperCase();
				String tag = "";
				if (split[3].equals("CONVERT_UNIPROT")) {
					tag = split[5] + "_" + split[8];
				} else {
					tag = split[3] + "_" + split[4];
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
			
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split[1].split("|").length > 2) {
					String accession = split[1].split("|")[1];
					String site = split[1].split(":")[1];
					String kinases = "";
					Iterator itr = kinase_substrate.keySet().iterator();
					while (itr.hasNext()) {
						String kinase = (String)itr.next();
						LinkedList list = (LinkedList)kinase_substrate.get(kinase);
						Iterator itr2 = list.iterator();
						while (itr2.hasNext()) {
							String substrate = (String)itr2.next();
							if (substrate.equals(accession + "_" + site)) {
								if (kinases.equals("")) {
									kinases = kinase;
								} else {
									kinases += "," + kinase;
								}
							}
						}
					}
					if (kinases.equals("")) {
						kinases = "NA";
					}
					out.write(str + "\t" + kinases + "\n");
				} else {
					out.write(str + "\n");
				}
			}
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
