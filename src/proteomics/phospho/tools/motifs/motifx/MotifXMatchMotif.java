package proteomics.phospho.tools.motifs.motifx;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import proteomics.phospho.tools.motifs.AminoAcid;
import proteomics.phospho.tools.motifs.MotifTools;

public class MotifXMatchMotif {


		public static void execute(String[] args) {
			
			
			String query_input_file = args[0];
			String all_motif_file = args[1];
			String completeMatch = args[2];
			boolean complete_match_flag = false;
			if (completeMatch.equals("yes")) {
				complete_match_flag = true;
			}
			//HashMap map_motif = grabMotif("C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Phosphorylation\\all_motif.txt");
			//LinkedList query_list = grabQuery("C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Phosphorylation\\wong_hong_query_long.txt");
			LinkedList query_list = grabQuery(query_input_file);
			HashMap map_motif = MotifTools.grabMotif(all_motif_file);
			
			Iterator itr = query_list.iterator();
			while (itr.hasNext()) {
				String query = (String)itr.next();
				boolean found = false;
				Iterator itr2 = map_motif.keySet().iterator();
				while (itr2.hasNext()) {
					String motif = (String)itr2.next();
					//String motif = "RXRXX[pS/pT][F/L]";				
					//motif = "pSPX[R/K]X";
					String proteomic = query;
					int comparison = MotifTools.getMotifIndex(proteomic, motif, complete_match_flag);
					if (comparison > 0) {
						found = true;
						System.out.println(query + "\t" + motif + "\t" + (comparison + 1) + "\t" + map_motif.get(motif));
					}
				}
				if (!found) {
					System.out.println(query + "\tNA");
				}
			}
		}
		
		
		public static void main(String[] args) {
			
			
			String all_motif_file = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Phosphorylation\\all_motif.txt";
			String query_inputFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Phosphorylation\\wong_hong_query_long.txt";
			String[] args_input = {query_inputFile, all_motif_file};
			execute(args_input);
			
			String input = "......Y..S...";
			int len = input.length() / 2;
			String test = input.substring(0, len - 1) + input.substring(len, len + 1).toLowerCase() + input.substring(len + 1, input.length());
			
			System.out.println(test);
		}
		public static LinkedList grabQuery(String fileName) {
			LinkedList list = new LinkedList();
			try {
				
				FileInputStream fstream = new FileInputStream(fileName);
				DataInputStream din = new DataInputStream(fstream);
				BufferedReader in = new BufferedReader(new InputStreamReader(din));
				while (in.ready()) {
					String str = in.readLine();
					String[] split = str.split("\t");
					int len = split[1].length() / 2;
					String addPhospho = split[1].substring(0, len - 1) + split[1].substring(len, len + 1).toLowerCase() + split[1].substring(len + 1, split[1].length());										
					list.add(addPhospho);
					//list.add(split[0]);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return list;
		}
		/*public static HashMap grabMotif(String fileName) {
			HashMap map = new HashMap();
			try {
				
				FileInputStream fstream = new FileInputStream(fileName);
				DataInputStream din = new DataInputStream(fstream);
				BufferedReader in = new BufferedReader(new InputStreamReader(din));
				while (in.ready()) {
					String str = in.readLine();
					String[] split = str.split("\t");
					if (split.length > 1) {
						map.put(split[0], split[1]);
					}
				}
				in.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return map;
		}*/

	}


