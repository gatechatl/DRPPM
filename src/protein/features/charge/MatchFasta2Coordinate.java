package protein.features.charge;

import idconversion.tools.Uniprot2GeneID;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Match the fasta sequence to human reference, must be of uniprot id
 * @author tshaw
 *
 */
public class MatchFasta2Coordinate {

	public static String type() {
		return "PROTEINFEATURE";
	}
	public static String description() {
		return "Match the fasta sequence to human reference, must be of uniprot id";
	}
	public static String parameter_info() {
		return "[queryFasta] [refFasta]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String queryFasta = args[0];
			String refFasta = args[1];
			String idmapFile = args[2];
			HashMap query = loadFastaFile(queryFasta);
			HashMap ref = loadFastaFile(refFasta);
			HashMap uniprot2geneID = Uniprot2GeneID.uniprot2geneID(idmapFile);
			Iterator itr = query.keySet().iterator();
			while (itr.hasNext()) {
				String accession = (String)itr.next();
				String query_seq = (String)query.get(accession);
				String[] split = accession.split(" ")[0].split("\\|");
				accession = split[0] + "|" + split[1] + "|" + split[2];
				boolean found = false;
				int start = -1;
				int end = -1;
				if (ref.containsKey(accession)) {
					String ref_seq = (String)ref.get(accession);
					//System.out.println(query_seq + "\t" + ref_seq);
					for (int i = 0; i < ref_seq.length() - query_seq.length() + 1; i++) {
						String subseq = ref_seq.substring(i, i + query_seq.length() - 1);
						if (query_seq.substring(0, query_seq.length() - 1).equals(subseq)) {
							found = true;
							start = i + 1;
							end = i + query_seq.length() - 1;
							break;
						}
					}
				}
				if (found) {
					String geneID = (String)uniprot2geneID.get(split[1]);
					System.out.println("MATCH\t" + geneID + "\t" + split[1] + "\t" + start + "\t" + end);
					//System.out.println(accession + "\t" + "Found");
				} else {
					//System.out.println(accession + "\t" + "NotFound");
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static HashMap loadFastaFile(String inputFile) {
		HashMap map = new HashMap();
		try {
			String name = "";			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine().trim();
				if (str.contains(">")) {
					name = str.split(" ")[0];					
				} else {					
					if (map.containsKey(name)) {
						String seq = (String)map.get(name);
						seq += str;
						map.put(name, seq);
					} else {
						map.put(name, str);
					}
				}
			}
			in.close();			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}
