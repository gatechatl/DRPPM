package proteomics.phospho.tools.misc;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Program for filtering GR/PR peptide based on hit on proteins
 * @author tshaw
 *
 */
public class PaulTaylorPeptideFiltering {

	public static void execute(String[] args) {
		
		try {
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static HashMap grabPeptide(String inputFile) {
		HashMap map = new HashMap();
		try {
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine().trim();				
				String[] split = str.split("\t");
				if (split.length > 10) {
					map.put(split[0], split[0]);
				}
				
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	public static HashMap getFile(String inputFile) {
		
		HashMap map = new HashMap();
		try {
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine().trim();
				map.put(str, str);
			}
			in.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}
