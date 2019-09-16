package proteomics.phospho.tools.motifs;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Calculate the pvalue
 * @author tshaw
 */
public class CalculateAllMotifPValueFastaFile {

	public static void execute(String[] args) {
		
		try {
			
			HashMap map_motif = MotifTools.grabMotif(args[0]); ////"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Phosphorylation\\all_motif.txt");
			LinkedList query_name_list = MotifTools.grabFastaQueryName(args[1]); //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\Differential_Peptide_Analysis\\AddFastSequence\\extended.fasta");
			LinkedList query_list = MotifTools.grabFastaQuery(args[1]); //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\Differential_Peptide_Analysis\\AddFastSequence\\extended.fasta");
			String outputFile = args[2];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			Iterator motif_itr = map_motif.keySet().iterator();
			while (motif_itr.hasNext()) {
				String motif = (String)motif_itr.next();
				String name = (String)map_motif.get(motif);
				double total = 0;
				double space = 0;
				double narrow = 0;
				Iterator itr_name = query_name_list.iterator();
				Iterator itr = query_list.iterator();
				while (itr.hasNext()) {
					String geneName = (String)itr_name.next();
					String seq = (String)itr.next();
					String type = "";
					
					seq = MotifTools.replaceTag(seq);
					// when counting motifs we disregard the phosphorylation location, or else can't really count
					/*seq = seq.replaceAll("Y", "Y*");
					seq = seq.replaceAll("T", "T*");
					seq = seq.replaceAll("S", "S*");*/
					boolean found = false;
					if (seq.contains("Y#")) {
						type = "Y";
					}
					if (seq.contains("S#")) {
						type = "S";
					}
					if (seq.contains("T#")) {
						type = "T";
					}
					//double count = 0;
					double count = MotifTools.countMotif(seq, motif, false);
					//if (MotifTools.containsMotif(seq, motif, false)) {
					//	count = 1;
					//}
					if (count > 1) {
						count = 1;
					}
					int motif_len = (MotifTools.strMotif2List(motif)).size();
					narrow += MotifTools.countMotifType(seq, type, motif_len);
					int search_space_len = seq.length() - motif_len + 1;
					if (search_space_len < 0) {
						search_space_len = 0;
					}
					space += search_space_len;
					total += count;
				}
				System.out.println(motif + "\t" + name + "\t" + (total / narrow) + "\t" + (total / space) + "\t" + total + "\t" + narrow + "\t" + space);
				out.write(motif + "\t" + name + "\t" + (total / narrow) + "\t" + (total / space) + "\t" + total + "\t" + narrow + "\t" + space + "\n");
				out.flush();
			}
			
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
