package PhosphoTools.MISC;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class ExtractHGGWGCNAResult {

	public static void main(String[] args) {
		
		try {
			String inputFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Projects\\HGG_KinaseActivity_Manuscript\\WGCNA\\hgg_phos.txt";
			String outputFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Projects\\HGG_KinaseActivity_Manuscript\\WGCNA\\hgg_phos_input.txt";
			FileWriter fwriter2 = new FileWriter(outputFile);
			BufferedWriter out2 = new BufferedWriter(fwriter2);						
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();				
			while (in.ready()) {
				String str = in.readLine().trim();
				if (!str.equals("")) {
					String[] split = str.split("\t");					
					String accession = split[1].split("\\|")[2].replaceAll(":", ".");
					//String site = split[1].split("\\|")[2].split(":")[1];
					//site = site.replaceAll("S", "");
					//site = site.replaceAll("Y", "");
					//site = site.replaceAll("T", "");
					//String name = accession + "." + site;
					String name = accession;
					System.out.println(name + "\t" + split[split.length - 1]);
					out2.write(name + "\t" + split[split.length - 1] + "\n");
				}
			}
			in.close();
			out2.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
