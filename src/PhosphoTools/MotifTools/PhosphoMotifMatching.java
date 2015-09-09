package PhosphoTools.MotifTools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class PhosphoMotifMatching {

	public static void execute(String[] args) {
		
		try {
			HashMap map_motif = MotifTools.grabMotif(args[0]); ////"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Phosphorylation\\all_motif.txt");			
			String inputFastaFile = args[1];
			String completeMatch = args[2];
			String outputFile = args[3]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\Differential_Peptide_Analysis\\IdentifyKinase\\Kinase_09192014.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			
			
			boolean complete_match_flag = false;
			if (completeMatch.toUpperCase().equals("YES")) {
				complete_match_flag = true;
			}
			LinkedList query_name_list = MotifTools.grabFastaQueryName(inputFastaFile); //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\Differential_Peptide_Analysis\\AddFastSequence\\extended.fasta");
			LinkedList query_name_list_meta = MotifTools.grabFastaQueryNameMeta(inputFastaFile); //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\Differential_Peptide_Analysis\\AddFastSequence\\extended.fasta");
			LinkedList query_list = MotifTools.grabFastaQuery(inputFastaFile); //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\Differential_Peptide_Analysis\\AddFastSequence\\extended.fasta");
			LinkedList query_list_orig = MotifTools.grabFastaQueryOrig(inputFastaFile);
			
			if (query_name_list.size() == query_name_list_meta.size() 
					&& query_name_list.size() == query_list.size() 
					&& query_name_list.size() == query_list_orig.size()) {
				
			
				Iterator itr_name = query_name_list.iterator();
				Iterator itr_meta = query_name_list_meta.iterator();
				Iterator itr = query_list.iterator();
				Iterator itr_orig = query_list_orig.iterator();
				while (itr.hasNext()) {
					String geneName = (String)itr_name.next();
					String geneNameMeta = (String)itr_meta.next();
					String query = (String)itr.next();
					String query_orig = (String)itr_orig.next();
					boolean found = false;
					Iterator itr2 = map_motif.keySet().iterator();
					while (itr2.hasNext()) {
						String motif = (String)itr2.next();
						//String motif = "RXRXX[pS/pT][F/L]";				
						//motif = "pSPX[R/K]X";
						String proteomic = query;
						//int comparison = compareMotif(proteomic, motif, true);
						if (MotifTools.containsMotif(proteomic, motif, complete_match_flag)) {
							found = true;
							out.write(geneName + "\t" + geneNameMeta + "\t" + query + "\t" + query_orig + "\t" + motif + "\t" + map_motif.get(motif) + "\n");
							//System.out.println(geneName + "\t" + geneNameMeta + "\t" + query + "\t" + query_orig + "\t" + motif + "\t" + map_motif.get(motif));
						}
					}
					if (!found) {
						out.write(geneName + "\t" + geneNameMeta + "\t" + query + "\t" + query_orig + "\tNA\tNA\n");
						//System.out.println(query + "\tNA");
					}
				}
				out.close();
			} else {
				/*System.out.println(query_name_list.size());
				System.out.println(query_name_list_meta.size());
				System.out.println(query_list.size());
				System.out.println(query_list_orig.size());*/
				
				System.out.println("LinkedList Size is different, something is wrong with the input file");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		String motif = "X[D/E]pYX";
		//String motif = "EYX";
		String proteomic = "QEEY*DEHFSSTHR";
		System.out.println(MotifTools.containsMotif(proteomic, motif, false));
		
	}
}

