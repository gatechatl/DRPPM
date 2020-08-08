package proteomics.phospho.tools.misc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Convert the coordinates and append to the original phosphositefile
 * @author tshaw
 *
 */
public class OrganismConversion2PhosphositeFile {

	public static void execute(String[] args) {
		
		try {
			
			String mouse_kinase_file = args[1];

			
			String orig_phosphosite_file = args[0];
			String outputFile = args[2];
			String organism = args[3];
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
						
			HashMap map = new HashMap();
			FileInputStream fstream = new FileInputStream(mouse_kinase_file);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String gene_symb = split[0];
				String kin_acc_id = split[1];								
				String kin_org = split[2];
				String new_uniprot = split[3];
				String new_location = split[4];
				
				
				String sub_acc_id = split[5]; // general name
				String sub_gene_id = split[6];
				String sub_org = split[7];
				String sub_mod_rsd = split[8];
				if (!new_uniprot.equals("CONVERT_UNIPROT")) {
					String key = gene_symb + "\t" + kin_acc_id + "\t" + kin_org + "\t" + sub_acc_id + "\t" + sub_mod_rsd;
					if (map.containsKey(key)) {
						String orig = (String)map.get(key);
						orig += "," + new_uniprot + "\t" + new_location;
						map.put(key, orig);
					} else {
						map.put(key, new_uniprot + "\t" + new_location);
					}
				}
				
			}
			in.close();
			
			fstream = new FileInputStream(orig_phosphosite_file);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split.length > 10) {
					String kinase = split[0];
					
					String kin_acc_id = split[1];
					String gene_symb = split[2];
					String chr_loc_hum = split[3];
					String kin_org = split[4];
					String substrate = split[5]; // general name
					String sub_gene_id = split[6];
					String sub_acc_id = split[7];
	
					String sub_gene_symb = split[8];
					String sub_chr_loc_hum = split[9];
					String sub_org = split[10];
					String sub_mod_rsd = split[11];
					String site_grp_id = split[12];
					String modsite_seq = split[13];
					String in_vivo_rxn = split[14];
					String in_vitro_rxn = split[15];
					
					//String last = split[16];
					String key = gene_symb + "\t" + kin_acc_id + "\t" + kin_org + "\t" + sub_acc_id + "\t" + sub_mod_rsd;
					if (map.containsKey(key)) {
						String result = (String)map.get(key);
						String[] split3 = result.split(",");
						for (String stuff: split3) {
							String[] split2 = stuff.split("\t");
							String new_uniprot = split2[0];
							String new_location = split2[1];
							out.write(kinase + "\t" + kin_acc_id + "\t" + gene_symb + "\t" + chr_loc_hum + "\t" + kin_org + "\t" 
									+ substrate + "\t" +  sub_gene_id + "\t" + new_uniprot + "\t" + sub_gene_symb + "\t"
									+ sub_chr_loc_hum + "\t" + organism + "\t" + new_location + "\t" + site_grp_id + "\t"
									+ modsite_seq + "\t" + in_vivo_rxn + "\t" + in_vitro_rxn + "\n");
						
						}
					} else {
						out.write(str + "\n");
					}
				}
			}
			in.close();			
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
