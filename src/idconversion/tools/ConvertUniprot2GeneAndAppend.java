package idconversion.tools;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Reads the human or mouse's ID conversion table and 
 * @author tshaw
 *
 */
public class ConvertUniprot2GeneAndAppend {
	public static String type() {
		return "IDCONVERT";
	}
	public static String description() {
		return "Reads the human or mouse's ID conversion table and";
	}
	public static String parameter_info() {
		return "[mapID_reference] [inputFile] [uniprot_index]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String reference = args[0];
			HashMap uniprot2gene = Uniprot2GeneID.uniprot2geneID(reference);
			
			String inputFile = args[1];
			int uniprot_index = new Integer(args[2]);
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (uniprot2gene.containsKey(split[uniprot_index])) {
					System.out.println(str + "\t" + uniprot2gene.get(split[uniprot_index]));
				} else {
					System.out.println(str + "\tNA");
				}								
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
