package stjude.projects.junminpeng.proteomics.peptide.peak.iondrawer;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * This class is responsible for searching the ID.txt or IDmod.txt 
 * extract the DTA file information
 * @author tshaw
 *
 */
public class SearchIDModExtractDTA {

	public static void execute(String[] args) {
		
		try {
			String idtxt_file = args[0];
			String jumpsearchPath = args[1];
			String search_peptide = args[2];
			FileInputStream fstream = new FileInputStream(idtxt_file);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split(";");
				String peptide = split[0];
				String protein_name = args[1];
				String path = args[2];
				
			}
			in.close();
						
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
