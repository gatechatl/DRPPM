package proteomics.phospho.tools.heatmap;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * For TMT based expression profile of phosphosite, 
 * grab expression of specific keywords from file
 * @author tshaw
 *
 */
public class GrabPhosphositeExpression {

	public static String parameter_info() {
		/*
		 * String phosphoAnnotationFile = args[0];			
			String species = args[1].toUpperCase();
			int tag_column = new Integer(args[2]);
			String tag_keyword = args[3];			
			String jumpq_file = args[4];
			int peptide = new Integer(args[5]);
			int accession = new Integer(args[6]);
			int expression_signal_start = new Integer(args[7]);
			int expression_signal_end = new Integer(args[8]);
			int modsite = new Integer(args[9]);
			
		 */
		return "[phosphoAnnotationFile] [species] [tag_index] [tag_keyword] [expression_file] [peptide_index] [accesion_index] [expression_signal_start_index] [expression_signal_end_index] [modsite_index]"; 
	}
	public static void execute(String[] args) {
		
		try {
			String phosphoAnnotationFile = args[0];			
			String species = args[1].toUpperCase();
			int tag_column = new Integer(args[2]);
			String tag_keyword = args[3];			
			String jumpq_file = args[4];
			int peptide = new Integer(args[5]);
			int accession = new Integer(args[6]);
			int expression_signal_start = new Integer(args[7]);
			int expression_signal_end = new Integer(args[8]);
			int modsite = new Integer(args[9]);
			
			HashMap map = new HashMap();
			HashMap geneName_map = new HashMap();
			FileInputStream fstream = new FileInputStream(phosphoAnnotationFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String anno_species = split[2];				
				String anno_accession = split[1]; 
				//System.out.println(anno_accession);
				String anno_geneName = split[0];
				geneName_map.put(anno_accession, anno_geneName);
				String anno_location = split[3].replaceAll("-p", "").toUpperCase();				
				if (split[tag_column].replaceAll(" ", "_").contains(tag_keyword)) {
					if (map.containsKey(anno_accession)) {
						LinkedList list = (LinkedList)map.get(anno_accession);
						list.add(anno_location);
						map.put(anno_accession, list);						
					} else {
						LinkedList list = new LinkedList();
						list.add(anno_location);
						map.put(anno_accession, list);
					}
				}
			
			}
			in.close();
			
			fstream = new FileInputStream(jumpq_file);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");
			String new_header = split_header[peptide] + "\t" + split_header[accession] + "\tgeneName" + "\t" + split_header[modsite];
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
				if (split[accession].contains("|")) {
					split[accession] = split[accession].split("\\|")[1];
					String result = split[peptide] + "\t" + split[accession] + "\t" + geneName_map.get(split[accession]) + "\t" + split[modsite];				
					for (int i = expression_signal_start; i <= expression_signal_end; i++) {
						result += "\t" + split[i];
					}
					if (map.containsKey(split[accession])) {
						LinkedList list = (LinkedList)map.get(split[accession]);
						if (list.contains(split[modsite].replaceAll("-p", "").toUpperCase())) {
							System.out.println(result);
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
