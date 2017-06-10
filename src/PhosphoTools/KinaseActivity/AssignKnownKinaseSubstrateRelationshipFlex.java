package PhosphoTools.KinaseActivity;

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
 * @author tshaw
 *
 */
public class AssignKnownKinaseSubstrateRelationshipFlex {

	public static String description() {
		return "Assign kinase substrate relationship to JUMPq peptide publication file";
	}
	public static String type() {
		return "KINASEACTIVITY";
	}
	public static String parameter_info() {
		return "[inputJUMPqSiteFile] [phosphosite_kinsub_file] [numSamples] [outputJUMPqFile] [outputMatrixFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String phosphosite_kinsub_file = args[1];
			int numSamples = new Integer(args[2]);
			String outputFile = args[3];
			String outputMatrix = args[4];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileWriter fwriter2 = new FileWriter(outputMatrix);
			BufferedWriter out2 = new BufferedWriter(fwriter2);
			
			out2.write("Phosphosite");
			for (int i = 0; i < numSamples; i++) {
				out2.write("\tData" + (i + 1));
			}
			out2.write("\tKINASE\n");
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
			//LinkedList list1 = (LinkedList)kinase_substrate.get("GSK3B");
			//System.out.println(kinase_substrate.size() + "\tGSK3B:" + list1.size());
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			out.write(in.readLine() + "\tKinase\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split[1].split("|").length > 2) {
					String accession = split[1].split("\\|")[1];
					String site = split[1].split(":")[1];
					String geneName = split[4];
					//System.out.println(accession + "\t" + site);
					if (accession.equals("P01108") && site.equals("T58")) { 
						//System.out.println(str);
					}
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
					out2.write(geneName.toUpperCase() + "_" + accession + "_" + site);
					for (int i = split.length - numSamples; i < split.length; i++) {
						out2.write("\t" + split[i]);
					}
					
					out2.write("\t" + kinases + "\n");
				} else {
					out.write(str + "\n");
				}
			}
			out.close();
			out2.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

