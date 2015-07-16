package GenerateNUC2PROT;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class RescueFragments {

	public static void execute(String[] args) {
		
		try {
			
			String inputFileUNIPROT = args[0];
			String outputFile_lookup = args[1];
			String outputFile_found = args[2];
			String outputFile_notfound = args[3];
			HashMap uniprot = new HashMap();
			String geneName = "";
			FileInputStream fstream = new FileInputStream(inputFileUNIPROT);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains(">")) {
					geneName = str.replaceAll(">", "");
				} else {
					if (uniprot.containsKey(geneName)) {
						String prev_str = (String)uniprot.get(geneName);
						uniprot.put(geneName, prev_str + str.trim());
					} else {
						uniprot.put(geneName, str.trim());
					}
					
				}
			}
			in.close();

			System.out.println(uniprot.size());
			int count = 0;
			HashMap uniprotcopy = (HashMap)uniprot.clone();
			HashMap found = new HashMap();
			HashMap map = new HashMap();
			Iterator itr = uniprot.keySet().iterator();
			while (itr.hasNext()) {
				String geneName1 = (String)itr.next();
				String seq1 = (String)uniprot.get(geneName1);
				Iterator itr2 = uniprotcopy.keySet().iterator();
				while (itr2.hasNext()) {
					String geneName2 = (String)itr2.next();
					String seq2 = (String)uniprot.get(geneName2);
					if (!geneName1.equals(geneName2) && seq1.contains(seq2)) {
						//found.put(geneName1, geneName1);
						found.put(geneName2, geneName2);
						if (map.containsKey(geneName1)) {
							String gNS = (String)map.get(geneName1);
							map.put(geneName1, geneName2 + "\t" + gNS);
							
						} else {
							map.put(geneName1, geneName2);
						}
					}
				}
				if (count % 1000 == 0) {
					System.out.println(count);
				}
				count++;
			}
			
			FileWriter fwriter = new FileWriter(outputFile_lookup);
			BufferedWriter out = new BufferedWriter(fwriter);	
			
			FileWriter fwriter_found = new FileWriter(outputFile_found);
			BufferedWriter out_found = new BufferedWriter(fwriter_found);	
			
			FileWriter fwriter_notfound = new FileWriter(outputFile_notfound);
			BufferedWriter out_notfound = new BufferedWriter(fwriter_notfound);	
			
			itr = uniprot.keySet().iterator();
			while (itr.hasNext()) {
				String geneName1 = (String)itr.next();
				String seq = (String)uniprot.get(geneName1);
				if (found.containsKey(geneName1)) {
					out_found.write(">" + geneName1 + "\n" + seq + "\n");
				} else {
					out_notfound.write(">" + geneName1 + "\n" + seq + "\n");
				}
			}
			
			itr = map.keySet().iterator();
			while (itr.hasNext()) {
				geneName = (String)itr.next();
				String matches = (String)map.get(geneName);
				out.write(geneName + "\t" + matches + "\n");
			}
			
			out_found.close();
			out_notfound.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
