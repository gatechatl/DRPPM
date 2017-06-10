package PhosphoTools.KinaseSubstrateInference;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Assign substrate annotation information to the matrix
 * @author tshaw
 *
 */
public class AppendFunctionalInformation2Matrix {


	public static String description() {
		return "Assign Substrate Annotation to matrix";
	}
	public static String type() {
		return "KinaseSubstrate";
	}
	public static String parameter_info() {
		return "[phoMatrixFile] [inducedSiteFile] [inhibitSiteFile] [degradationFile] [stabilizationFile] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String phoMatrixFile = args[0];
			String inducedSiteFile = args[1];
			String inhibitSiteFile = args[2];
			String degradationFile = args[3];
			String stabilizationFile = args[4];
			String outputFile = args[5];
			HashMap activeSite = new HashMap();
			
			FileInputStream fstream = new FileInputStream(inducedSiteFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String accession = split[1];
				String site = split[3].split("-")[0];
				activeSite.put(accession + "_" + site, "");
			}
			in.close();
			
			HashMap inhibitSite = new HashMap();
			
			fstream = new FileInputStream(inhibitSiteFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String accession = split[1];
				String site = split[3].split("-")[0];
				inhibitSite.put(accession + "_" + site, "");
			}
			in.close();
			
			HashMap degradation = new HashMap();
			fstream = new FileInputStream(degradationFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String accession = split[1];
				String site = split[3].split("-")[0];
				degradation.put(accession + "_" + site, "");
			}
			in.close();
			
			HashMap stabilization = new HashMap();
			
			fstream = new FileInputStream(stabilizationFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String accession = split[1];
				String site = split[3].split("-")[0];
				stabilization.put(accession + "_" + site, "");
			}
			in.close();
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			fstream = new FileInputStream(phoMatrixFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));			
			String header2 = in.readLine();
			out.write(header2 + "\tActivate\tInhibit\tDegradation\tStabilization\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String kinaseName = split[0].split("_")[0];
				String name = split[0].split("_")[1] + "_" + split[0].split("_")[2];
				String induceActivityFlag = "false";
				String inhibitActivityFlag = "false";
				String degradationFlag = "false";
				String stabilizationFlag = "false";
				if (activeSite.containsKey(name)) {
					induceActivityFlag = "true";
				}
				if (inhibitSite.containsKey(name)) {
					inhibitActivityFlag = "true";
				}
				if (degradation.containsKey(name)) {
					degradationFlag = "true";
				}
				if (stabilization.containsKey(name)) {
					stabilizationFlag = "true";
				}
				out.write(str + "\t" + induceActivityFlag + "\t" + inhibitActivityFlag + "\t" + degradationFlag + "\t" + stabilizationFlag + "\n");
				
			}
			in.close();
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
