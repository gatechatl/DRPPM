package expressionanalysis.tools.genename;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Append the entrez id based on 
 * @author tshaw
 *
 */
public class GeneSymbol2EntrezIDAppend {

	
	
	public static void execute(String[] args) {
		
		try {
			HashMap geneSymbol2Entrez = new HashMap();
			String id_conversion_table = args[0];
			FileInputStream fstream = new FileInputStream(id_conversion_table);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));					
			while (in.ready()) {
				String str = in.readLine().trim();
				String[] split = str.split("\t");
				geneSymbol2Entrez.put(split[4], split[1]);
			}
			in.close();

			
			
			
			fstream = new FileInputStream(id_conversion_table);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));					
			while (in.ready()) {
				String str = in.readLine().trim();
				
			}
			in.close();
					
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
