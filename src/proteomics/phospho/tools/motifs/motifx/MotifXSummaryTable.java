package proteomics.phospho.tools.motifs.motifx;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class MotifXSummaryTable {

	public static void execute(String[] args) {
		
		try {
			HashMap map = new HashMap();
			HashMap map_T = new HashMap();
			HashMap map_S = new HashMap();
			HashMap map_Y = new HashMap();
			HashMap motif = new HashMap();
			HashMap no_matching_motif = new HashMap();
			String fileName = args[0];
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (!str.equals("")) {
					String[] split = str.split("\t");
					String seq = split[0];
					map.put(seq, seq);
					if (split.length >= 4) {
						
						if (seq.contains("s")) {
							map_S.put(seq, seq);
						}
						if (seq.contains("t")) {
							map_T.put(seq, seq);
						}
						if (seq.contains("y")) {
							map_Y.put(seq, seq);
						}
						
						String kinase_motif = split[3];	
						if (motif.containsKey(kinase_motif)) {
							int count = (Integer)motif.get(kinase_motif);
							motif.put(kinase_motif, (count + 1));
						} else {
							motif.put(kinase_motif, 1);
						}
					} else {
						if (seq.contains("s")) {
							map_S.put(seq, seq);
						}
						if (seq.contains("t")) {
							map_T.put(seq, seq);
						}
						if (seq.contains("y")) {
							map_Y.put(seq, seq);
						}
						if (split[1].equals("NA")) {
							no_matching_motif.put(seq, seq);
						}
					}
				}
			}
			in.close();
						
			Iterator itr = motif.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String)itr.next();
				System.out.println(key + "\t" + motif.get(key));
			}
			
			System.out.println("Total number of Substrate motif: " + map.size());
			System.out.println("Total number of Serine motif: " + map_S.size());
			System.out.println("Total number of Threonine motif: " + map_T.size());
			System.out.println("Total number of Tyrosine motif: " + map_Y.size());
			System.out.println("Total number of Identified motif: " + motif.size());
			System.out.println("Total number of Unidentified motif: " + no_matching_motif.size());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
