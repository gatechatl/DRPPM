package WholeExonTools;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class GenerateSNVTableFromMutationTable {

	public static String description() {
		return "Generate SNV Table from Mutation Table";
	}
	public static String type() {
		return "SNV";
	}
	public static String parameter_info() {
		return "[inputSNVFile]";
	}
	
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String file1Header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String chr = split[3];
				String pos = split[4];
				String allele1 = split[13];
				String allele2 = split[14];
				System.out.println(chr + "." + pos + "." + allele1 + "." + allele2);
				
			}
			in.close();
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
