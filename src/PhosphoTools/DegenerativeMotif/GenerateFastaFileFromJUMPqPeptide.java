package PhosphoTools.DegenerativeMotif;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * Generate a fasta file from the JUMPq id_all_site_quan.txt
 * @author tshaw
 * 
 */
public class GenerateFastaFileFromJUMPqPeptide {

	public static String description() {
		return "Generate a fasta file from the JUMPq id_all_pep_quan.txt";
	}
	public static String type() {
		return "JUMPQ";
	}
	public static String parameter_info() {
		return "[id_all_pep_quan.txt]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String name = split[2];
				String sequence = split[0].replaceAll("S#", "S\\*").replaceAll("T%", "T\\*").replaceAll("M@", "M").replaceAll("-", "").split("\\.")[1];
				System.out.println(">" + name + "\n" + sequence);
			}
			in.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
