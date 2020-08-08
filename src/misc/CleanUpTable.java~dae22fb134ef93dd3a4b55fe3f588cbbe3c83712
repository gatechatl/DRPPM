package misc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class CleanUpTable {

	public static void main(String[] args) {
		
		try {
			
			String outputFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\REFERENCE\\Phosphositeplus\\Mouse_Kinase_Substrate_Cleaned.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);						
			
			
			String inputFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\REFERENCE\\Phosphositeplus\\Mouse_Kinase_Substrate.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] header_split = header.split("\t");
			out.write("KinaseGeneName\tKinaseUniprot\tKinaseSpecies\tSubstrateGeneName\tSubstrateUniprot\tSubstrateSite\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split[3].equals("CONVERT_UNIPROT")) {
					out.write(split[0].toUpperCase() + "\t" + split[1] + "\t" + split[2] + "\t" + split[6].toUpperCase() + "\t" + split[5] + "\t" + split[8] + "\n");
				} else {
					out.write(split[0].toUpperCase() + "\t" + split[1] + "\t" + split[2] + "\t" + split[6].toUpperCase() + "\t" + split[3] + "\t" + split[4] + "\n");
				}
			}
			in.close();
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
