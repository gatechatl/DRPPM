package PhosphoTools.Network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class KinaseSubstrateAll {

	public static String type() {
		return "KINASENETWORK";
	}
	public static String description() {
		return "Generate kinase substrate relationship regardless of activity site";
	}
	public static String parameter_info() {
		return "[kinaseSubstrateFileName] [activeSiteFile] [inhibitSiteFile] [allSiteFile] [pathwayGeneList] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			String kinaseSubstrateFileName = args[0];
			//String kinaseListFile = args[1];
			String activeSiteFile = args[1];
			String inhibitSiteFile = args[2];
			String allSiteFile = args[3];
			String pathwayGeneListFile = args[4];
			String outputFile = args[5];
						
			HashMap kinase = new HashMap();
			/*
			FileInputStream fstream = new FileInputStream(kinaseListFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				kinase.put(str, str);
			}
			*/

			HashMap activeSite = new HashMap();			
			FileInputStream fstream = new FileInputStream(activeSiteFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String accession = split[1];
				String site = split[3].split("-")[0];
				activeSite.put(accession + "\t" + site, "");
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
				inhibitSite.put(accession + "\t" + site, "");
			}
			in.close();
			
			HashMap allSite = new HashMap();			
			fstream = new FileInputStream(allSiteFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String accession = split[1];
				String site = split[3].split("-")[0];
				allSite.put(accession + "\t" + site, "");
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
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split.length > 10) {
					String kinase1 = split[2].toUpperCase();
					String substrate = split[7].toUpperCase();
					String accession = split[6];
					String site = split[9];
					String key = accession + "\t" + site;
					//
					boolean found = false;
					if (((pathwayGeneList.containsKey(kinase1) && pathwayGeneList.containsKey(substrate)) || pathwayGeneList.size() == 0) && activeSite.containsKey(key)) {
						found = true;
						String tag = kinase1 + "\tactivates\t" + substrate + "\t" + accession + "_" + site;;
						relationship.put(tag, tag);
					}
					if (((pathwayGeneList.containsKey(kinase1) && pathwayGeneList.containsKey(substrate)) || pathwayGeneList.size() == 0) && inhibitSite.containsKey(key)) {
						found = true;
						String tag = kinase1 + "\tinhibits\t" + substrate + "\t" + accession + "_" + site;;
						relationship.put(tag, tag);
					}
					if (!found) {
						if (((pathwayGeneList.containsKey(kinase1) && pathwayGeneList.containsKey(substrate)) || pathwayGeneList.size() == 0) && allSite.containsKey(key)) {
							found = true;
							String tag = kinase1 + "\tphosphorylates\t" + substrate + "\t" + accession + "_" + site;;
							relationship.put(tag, tag);
						}
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
