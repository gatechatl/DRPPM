package PhosphoTools.KinaseSubstrateInference;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Read the Phospho Site and 
 * @author tshaw
 *
 */
public class PhoFilterKinaseFunctionalRole {


	public static String description() {
		return "Obtain Kinase Activity Site";
	}
	public static String type() {
		return "KINASESUBSTRATE";
	}
	public static String parameter_info() {
		return "[phoMatrixFile] [kinaseListFile] [activeSiteFile] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String phoMatrixFile = args[0];
			String kinaseListFile = args[1];
			String activeSiteFile = args[2];
			String outputFile = args[3];
						
			HashMap kinase = new HashMap();
			
			FileInputStream fstream = new FileInputStream(kinaseListFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine().toUpperCase();
				kinase.put(str, str);
			}
			

			HashMap activeSite = new HashMap();
			
			fstream = new FileInputStream(activeSiteFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String accession = split[1];
				String site = split[3].split("-")[0];
				activeSite.put(accession + "_" + site, "");
			}
			in.close();
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			fstream = new FileInputStream(phoMatrixFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));			
			String header2 = in.readLine();
			out.write(header2 + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String kinaseName = split[0].split("_")[0];
				String name = split[0].split("_")[1] + "_" + split[0].split("_")[2];
				if (kinase.containsKey(kinaseName) && activeSite.containsKey(name)) {
					out.write(kinaseName);
					for (int i = 1; i < split.length; i++) {
						out.write("\t" + split[i]);
					}
					out.write("\n");
				}
			}
			in.close();
			out.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
