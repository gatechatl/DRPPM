package protein.features.combineresults;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;


public class CombineProteinFeatures {

	public static String parameter_info() {
		return "[protParamFile] [pepStatFile] [SSPAFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String protParamFile = args[0];			
			String pepstatFile = args[1];
			String sspaFile = args[2];
			String protParamHeader = getHeader(protParamFile);
			String pepstatHeader = getHeader(pepstatFile);
			String sspaHeader = getHeader(sspaFile);
			HashMap protParamData = grabData(protParamFile, true);
			HashMap pepstatData = grabData(pepstatFile, false);
			HashMap sspaData = grabData(sspaFile, false);
			HashMap geneName2accession = key2accession(protParamFile);			
			System.out.println("GeneName\tAccession\t" + protParamHeader + "\t" + pepstatHeader + "\t" + sspaHeader);
			
			Iterator itr = protParamData.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String)itr.next();
				if (pepstatData.containsKey(key) && sspaData.containsKey(key)) {
					String pepStatStr = (String)pepstatData.get(key);
					String protParamStr = (String)protParamData.get(key);
					String sspaStr = (String)sspaData.get(key);
					System.out.println(key + "\t" + geneName2accession.get(key) + "\t" + protParamStr + "\t" + pepStatStr  + "\t" + sspaStr);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static HashMap key2accession(String inputFile) {
		HashMap map = new HashMap();
		try {
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String[] split2 = split[0].split("\\|");
				String geneName = split2[2];
				String accession = split2[1];
				map.put(geneName, accession);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	public static HashMap grabData(String inputFile, boolean uniprot_flag) {
		HashMap map = new HashMap();
		try {
	
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String data = "";
				for (int i = 1; i < split.length; i++) {
					if (i == 1) {
						data = split[i];
					} else {
						data += "\t" + split[i];
					}
				}
				String geneName = "";
				if (uniprot_flag) {
					String[] split2 = split[0].split("\\|");
					geneName = split2[2];
					//System.out.println(geneName);
				} else {
					geneName = split[0];
				}
				
				map.put(geneName, data);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;		
	}
	public static String getHeader(String inputFile) {
		String new_header = "";
		try {
	
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split = header.split("\t");
			
			for (int i = 1; i < split.length; i++) {
				if (i == 1) {
					new_header = split[i];
				} else {
					new_header += "\t" + split[i];
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new_header;
	}
	
}
