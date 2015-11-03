package PhosphoTools.MISC;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class OverlapMtorPhoshporylationSeq {

	public static void main(String[] args) {
		
		try {
			
			HashMap geneName_map = new HashMap();
			HashMap map = new HashMap();
			String geneName = "";
			FileInputStream fstream = new FileInputStream("C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Phosphorylation\\REFERENCE\\Phosphorylation_seq.txt");
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();

				if (str.contains(">")) {
					str = str.replaceAll(">", "");
					if (str.contains("human")) {
						String[] split2 = str.split("\\|");
						String geneName2 = split2[0].split(" ")[0];
						String accession = split2[2];												
						//geneName2 = str;
						//if (!accession.contains("-")) {
						if (true) {
							if (geneName_map.containsKey(geneName2)) {
								LinkedList list = (LinkedList)geneName_map.get(geneName2);
								list.add(accession);
								geneName_map.put(geneName2, list);							
							} else {
								LinkedList list = new LinkedList();
								list.add(accession);
								geneName_map.put(geneName2, list);
							}						
							map.put(geneName2 + "\t" + accession, "");
							geneName = geneName2 + "\t" + accession;
							//System.out.println(geneName2 + "\t" + accession);
						}
					} else {
						geneName = "";
					}

				} else {
					if (!geneName.equals("")) {
						if (map.containsKey(geneName)) {
							String seq = (String)map.get(geneName);
							seq += str.trim();
							map.put(geneName, seq);
							
						}
					}
				}				
			}
			in.close();
			
			fstream = new FileInputStream("C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Phosphorylation\\REFERENCE\\mtor_reference_updated.txt");
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split[1].equals("bad") || split[1].equals("")) {
					geneName = split[0];
					String aaloc = split[4].replaceAll("-p", "");
					String aa = aaloc.substring(0, 1);
					int loc = new Integer(aaloc.substring(1, aaloc.length()));
					if (geneName_map.containsKey(geneName)) {
						LinkedList list = (LinkedList)geneName_map.get(geneName);
						Iterator itr = list.iterator();
						while (itr.hasNext()) {
							String qaccession = (String)itr.next();
							if (map.containsKey(geneName + "\t" + qaccession)) {						
								String[] seq_split = ((String)map.get(geneName + "\t" + qaccession)).split("\t");								
								String seq = seq_split[0];
								//System.out.println("Hit?");
								if (seq.length() > loc) {
									if (aa.equals(seq.substring(loc -1, loc))) {
										
										System.out.println(geneName + "\t" + qaccession + "\t" + split[4] + "\t" + aa);
									}
								}
							
							}
						}
					}
				}
			}
			in.close();
				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
