package statistics.comparison;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Based on the identified protein IDs calculate the histogram coverage
 * X axis consist of binning of FPKM and 
 * Y axis consist of the number of protein coding gene
 * @author tshaw
 *
 */
public class ComparingCoverageRNAseqProteome {

	public static void main(String[] args) {
		
		try {
			HashMap map = new HashMap();
			String inputFile = args[0];
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
