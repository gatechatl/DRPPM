package proteomics.apms.saint;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Generate the gene length file for SAINT input
 * @author tshaw
 *
 */
public class CalculateGeneLengthSaintInputFile {

	public static String parameter_info() {
		return "[inputMatrix: second column is accession] [fastaFile]";
	}
	public static String type() {
		return "PPI";
	}
	public static String description() {		
		return "Calculate the gene length for the SAINT input file";
	}
	public static void execute(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			String inputMatrix = args[0];
			FileInputStream fstream = new FileInputStream(inputMatrix);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String accession = split[1];
				map.put(accession, -1);
			}
			in.close();
			
			String current_accession = "";
			int length = 0;
			String fastaFile = args[1];
			fstream = new FileInputStream(fastaFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains(">")) {
					if (map.containsKey(current_accession)) {
						map.put(current_accession, length);
					}
					current_accession = str.split(" ")[0].replaceAll(">", "");					
					length = 0;
				} else {
					length += str.trim().length();
				}				
			}
			in.close();
			if (map.containsKey(current_accession)) {
				map.put(current_accession, length);
			}
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String accession = (String)itr.next();
				length = (Integer)map.get(accession);
				//String name = accession.split("\\|")[1];
				System.out.println(accession + "\t" + length);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
