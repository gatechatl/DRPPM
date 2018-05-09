package protein.features.lowcomplexitydomain;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class GrabGRPRFasta {

	public static String parameter_info() {
		return "[inputFile] [geneListFile] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			

			String inputFile = args[0];
			String geneListFile = args[1];
			String outputFile = args[2];
			
						
			HashMap map = new HashMap();
			HashMap geneName = new HashMap();
			FileInputStream fstream = new FileInputStream(geneListFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(split[2], split[0]);		
				geneName.put(split[2], split[1]);
			}
			in.close();
			
			String accession = "";
			HashMap seq_map = new HashMap();
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains(">")) {
					accession = str.split("\\|")[1];
					//System.out.println(accession);
					
				} else {
					String seq = str;
					
					if (seq.length() > 0) {
						
						if (map.containsKey(accession)) {
							String type = (String)map.get(accession);
							if (seq_map.containsKey(accession)) {
								seq_map.put(accession, seq_map.get(accession) + seq);
							} else {
								seq_map.put(accession, seq);
							}							
						}
					}
				}
			}
			in.close();
			

			FileWriter fwriter = new FileWriter(outputFile);
            BufferedWriter out = new BufferedWriter(fwriter);
            
			Iterator itr = seq_map.keySet().iterator();
			while (itr.hasNext()) {
				accession = (String)itr.next();
				String seq = (String)seq_map.get(accession);
				String gene = (String)geneName.get(accession);
				String type = (String)map.get(accession);
				if (type.equals("GRPR")) {
					out.write(">" + gene + "_" + accession + "_" + type + "_Complete\n" + seq.toUpperCase() + "\n");
				}
			}
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
