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
public class JUMPqPhoProteome2Matrix {

	public static String description() {
		return "Convert phospho jumpq result. JUMPq peptide publication file";
	}
	public static String type() {
		return "KINASEACTIVITY";
	}
	public static String parameter_info() {
		return "[inputJUMPqSiteFile] [numberOfSamples] [outputMatrixFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			int numSamples = new Integer(args[1]);
			String outputMatrix = args[2];
		
			FileWriter fwriter2 = new FileWriter(outputMatrix);
			BufferedWriter out2 = new BufferedWriter(fwriter2);
			
			out2.write("Phosphosite");
			for (int i = 0; i < numSamples; i++) {
				out2.write("\tData" + (i + 1));
			}
			out2.write("\tKinase\n");
			//LinkedList list1 = (LinkedList)kinase_substrate.get("GSK3B");
			//System.out.println(kinase_substrate.size() + "\tGSK3B:" + list1.size());
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
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
					
					out2.write(geneName.toUpperCase() + "_" + accession + "_" + site);
					for (int i = split.length - numSamples; i < split.length; i++) {
						out2.write("\t" + split[i]);
					}
					out2.write("\n");
					//out2.write("\tNA\n");
				}
			}

			out2.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

