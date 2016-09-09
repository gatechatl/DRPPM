package PhosphoTools.Network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class FilterKinaseSubstrate2KinaseOnly {

	public static String parameter_info() {
		return "[kinaseSubstrateFileName] [kinaseListFile] [activeSiteFile] [pathwayGeneList] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			String kinaseSubstrateFileName = args[0];
			String kinaseListFile = args[1];
			String activeSiteFile = args[2];
			String pathwayGeneListFile = args[3];
			String outputFile = args[4];
						
			HashMap kinase = new HashMap();
			
			FileInputStream fstream = new FileInputStream(kinaseListFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
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
				activeSite.put(accession + "\t" + site, "");
			}
			in.close();
			
			HashMap pathwayGeneList = new HashMap();
			if (!pathwayGeneListFile.equals("ALL")) {
				fstream = new FileInputStream(pathwayGeneListFile);
				din = new DataInputStream(fstream);
				in = new BufferedReader(new InputStreamReader(din));			
				while (in.ready()) {
					String str = in.readLine().toUpperCase();
					pathwayGeneList.put(str, str);
				}
				in.close();
			}
						
			HashMap relationship = new HashMap();
			fstream = new FileInputStream(kinaseSubstrateFileName);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));			
			header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split.length > 10) {
					String kinase1 = split[2].toUpperCase();
					String substrate = split[7].toUpperCase();
					String accession = split[6];
					String site = split[9];
					String key = accession + "\t" + site;
					if (((pathwayGeneList.containsKey(kinase1) && pathwayGeneList.containsKey(substrate)) || pathwayGeneList.size() == 0) && kinase.containsKey(substrate) && activeSite.containsKey(key)) {						
						String tag = kinase1 + "\tphoshorylate\t" + substrate + "\t" + accession + "_" + site;;
						relationship.put(tag, tag);
					}
				}
			}
			in.close();
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			Iterator itr = relationship.keySet().iterator();
			while (itr.hasNext()) {
				String tag = (String)itr.next();
				out.write(tag + "\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
