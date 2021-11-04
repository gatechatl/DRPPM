package proteomics.projects.gtex.integrate;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Integrate the different peptide search result to examine which peptides are enriched.
 * @author 4472414
 *
 */
public class ProteomicsGTExCPTACRhabdoCombinedTable {

	
	public static void main(String[] args) {
		
		try {
			
			HashMap gtex_map = new HashMap();
			HashMap cptac_map = new HashMap();
			HashMap rms_map = new HashMap();
			HashMap all_map = new HashMap();
			HashMap annotation = new HashMap();
			String outputFile = "/Users/4472414/Projects/GTEx_Proteomics_Search/Integrate/Combined.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("Peptide\tGTEx_SC_count\tCPTAC_SC_count\tRMS_SC_count\tAnnotation\n");
			String inputGTExFile = "/Users/4472414/Projects/GTEx_Proteomics_Search/PSM/outputTable.txt"; 
			FileInputStream fstream = new FileInputStream(inputGTExFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				gtex_map.put(split[0], split[1]);
				all_map.put(split[0], split[0]);
				annotation.put(split[0], split[3] + ";" + split[2]);
			}
			in.close();
			
			String inputCPTACFile = "/Users/4472414/Projects/CPTAC_Brain_Proteomics/outputTable.txt";
			FileInputStream fstream2 = new FileInputStream(inputCPTACFile);
			DataInputStream din2 = new DataInputStream(fstream2);
			BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
			while (in2.ready()) {
				String str = in2.readLine();
				String[] split = str.split("\t");
				cptac_map.put(split[0], split[1]);
				all_map.put(split[0], split[0]);
				if (!annotation.containsKey(split[0])) {
					annotation.put(split[0],  split[2]);
				}
			}
			in.close();
			
			String inputRMSFile = "/Users/4472414/Projects/Rhabdo_Proteomics/COMET/outputTable.txt";
			FileInputStream fstream3 = new FileInputStream(inputRMSFile);
			DataInputStream din3 = new DataInputStream(fstream3);
			BufferedReader in3 = new BufferedReader(new InputStreamReader(din3));
			while (in3.ready()) {
				String str = in3.readLine();
				String[] split = str.split("\t");
				rms_map.put(split[0], split[1]);
				all_map.put(split[0], split[0]);
				if (!annotation.containsKey(split[0])) {
					annotation.put(split[0],  split[2]);
				}
			}
			in.close();
			
			Iterator itr = all_map.keySet().iterator();
			while (itr.hasNext()) {
				String peptide = (String)itr.next();
				double count_gtex = 0.0;
				if (gtex_map.containsKey(peptide)) {
					count_gtex = new Double((String)gtex_map.get(peptide));
				}
				double count_cptac = 0.0;
				if (cptac_map.containsKey(peptide)) {
					count_cptac = new Double( (String)cptac_map.get(peptide));
				}
				double count_rms = 0.0;
				if (rms_map.containsKey(peptide)) {
					count_rms = new Double((String)rms_map.get(peptide));
				}
				String annotation_str = (String)annotation.get(peptide);
				out.write(peptide + "\t" + count_gtex + "\t" + count_cptac + "\t" + count_rms + "\t" + annotation_str + "\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
