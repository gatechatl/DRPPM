package PhosphoTools.Heatmap;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;

import IDConversion.Uniprot2GeneID;

/**
 * For TMT based expression profile of phosphosite, 
 * grab expression of specific keywords from file
 * @author tshaw
 *
 */
public class GrabPhosphositeExpressionAll {

	public static String type() {
		return "PHOSPHOPROTEOME";
	}
	public static String description() {
		return "Grab expression information from TMT based expression profile of phosphosite";
	}
	public static String parameter_info() {
		return "[expression_file] [uniprot2geneName] [peptide_index] [accesion_index] [expression_signal_start_index] [expression_signal_end_index] [modsite_index] [separate flag true/false]"; 
	}
	public static void execute(String[] args) {
		
		try {
			String jumpq_file = args[0];
			String uniprot2geneNameFile = args[1];
			int peptide = new Integer(args[2]);
			int accession = new Integer(args[3]);
			int expression_signal_start = new Integer(args[4]);
			int expression_signal_end = new Integer(args[5]);
			int modsite = new Integer(args[6]);
			boolean separate_flag = new Boolean (args[7]);
			
			HashMap geneName_map = new HashMap();
			HashMap uniprot2geneName = Uniprot2GeneID.uniprot2geneID(uniprot2geneNameFile);
			
			FileInputStream fstream = new FileInputStream(jumpq_file);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");
			
			//String new_header = "geneName_" + split_header[accession] + "_" + split_header[modsite];
			String new_header = "geneName";
			for (int i = expression_signal_start; i <= expression_signal_end; i++) {
				new_header += "\t" + split_header[i];
			}
			System.out.println(new_header);
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				/*			int peptide = new Integer(args[5]);
			int accession = new Integer(args[6]);
			int expression_signal_start = new Integer(args[7]);
			int expression_signal_end = new Integer(args[8]);
			int modsite = new Integer(args[9]);
			*/
				if (separate_flag) {
					if (split[accession].contains("|")) {
						split[accession] = split[accession].split("\\|")[1];
						//String result = split[peptide] + "\t" + split[accession] + "\t" + uniprot2geneName.get(split[accession]) + "\t" + split[modsite];
						for (String mod: split[modsite].split(",")) {
							if (uniprot2geneName.containsKey(split[accession])) {
								String result = ((String)uniprot2geneName.get(split[accession])).toUpperCase() + "_" + split[accession] + "_" + mod;
								for (int i = expression_signal_start; i <= expression_signal_end; i++) {
									result += "\t" + split[i];
								}
								System.out.println(result);
							}
						}
						/*if (map.containsKey(split[accession])) {
							LinkedList list = (LinkedList)map.get(split[accession]);
							if (list.contains(split[modsite].replaceAll("-p", "").toUpperCase())) {
								System.out.println(result);
							}					
						}*/				  
					}
				} else {
					if (split[accession].contains("|")) {
						split[accession] = split[accession].split("\\|")[1];
						//String result = split[peptide] + "\t" + split[accession] + "\t" + uniprot2geneName.get(split[accession]) + "\t" + split[modsite];				
						String result = ((String)uniprot2geneName.get(split[accession])).toUpperCase() + "_" + split[accession] + "_" + split[modsite].replaceAll(",", "_");
						for (int i = expression_signal_start; i <= expression_signal_end; i++) {
							result += "\t" + split[i];
						}
						System.out.println(result);
						/*if (map.containsKey(split[accession])) {
							LinkedList list = (LinkedList)map.get(split[accession]);
							if (list.contains(split[modsite].replaceAll("-p", "").toUpperCase())) {
								System.out.println(result);
							}					
						}*/				  
					}
				}
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
