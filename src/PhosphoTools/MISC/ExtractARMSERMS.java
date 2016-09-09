package PhosphoTools.MISC;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class ExtractARMSERMS {

	public static void main(String[] args) {
		try {

			boolean start = false;
			String inputFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Projects\\ARMS_ERMS\\Current\\Combine\\combined_norm_uni_prot.txt";
			String outputFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Projects\\ARMS_ERMS\\Current\\Combine\\combined_norm_uni_prot_filter.txt";
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);		
			
			int[] index = {14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53};
			
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine().trim();
				String[] split = str.split("\t");
				if (start) {
					String accession = split[1].split("\\|")[1];
					String site = split[1].split(":")[1];
					String gene = split[3];
					String name = gene + "_" + accession + "_" + site;
					boolean flag = true;
					for (int i: index) {
						if (split[i].equals("NA")) {
							flag = false;
						}
					}
					if (flag) {
						out.write(name);
						
						for (int i: index) {
							out.write("\t" + split[i]);
						}
						out.write("\n");
					}
				}
				if (split.length > 10) {
					if (split[0].equals("Protein Group#")) {
						start = true;
						out.write("Name");
						for (int i: index) {
							out.write("\t" + split[i]);
						}
						out.write("\n");
					}
				}				
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
