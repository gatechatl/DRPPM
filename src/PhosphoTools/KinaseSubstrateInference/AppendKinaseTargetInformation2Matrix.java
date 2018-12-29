package PhosphoTools.KinaseSubstrateInference;

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
public class AppendKinaseTargetInformation2Matrix {

	public static String description() {
		return "Assign kinase substrate relationship to matrix";
	}
	public static String type() {
		return "KinaseSubstrate";
	}
	public static String parameter_info() {
		return "[inputJUMPqPeptideFile] [phosphosite_kinsub_file] [limit annotated kinase flag yes/no] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String phosphosite_kinsub_file = args[1];
			String flag = args[2];
			String outputFile = args[3];
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			HashMap kinase_substrate = new HashMap(); 
			HashMap substrate_kinase = new HashMap();
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
				if (substrate_kinase.containsKey(tag)) {
					String kinases = (String)substrate_kinase.get(tag);
					substrate_kinase.put(tag, kinases + "," + kinase);
				} else {
					substrate_kinase.put(tag, kinase.toUpperCase());
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
			String header2 = in.readLine();
			out.write(header2 + "\tAnnotatedKinase\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");

				String site = split[0].split("_")[1] + "_" + split[0].split("_")[2];
				String kinases = "";
				if (substrate_kinase.containsKey(site)) {
					kinases = (String)substrate_kinase.get(site);
				}
				
				if (kinases.equals("")) {
					kinases = "NA";
				} else {
					
					// sometimes there are kinases that are duplicated in the list, the following code removes those duplicates
					String new_kinases = "";
					HashMap kinase_map = new HashMap();
					for (String kinase: kinases.split(",")) {
						if (!kinase.equals("")) {
							kinase_map.put(kinase, kinase);
						}
					}
					Iterator itr = kinase_map.keySet().iterator();
					while (itr.hasNext()) {
						String geneName = (String)itr.next();
						new_kinases += geneName + ",";
					}
					kinases = new_kinases;
					if (kinases.equals("")) {
						kinases = "NA";
					}
				}
				if (flag.toUpperCase().equals("YES")) {
					if (!kinases.equals("NA")) {
						out.write(str + "\t" + kinases + "\n");
					}
				} else {
					out.write(str + "\t" + kinases + "\n");
				}
			}
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
