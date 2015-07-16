package PhosphoTools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class CreateNewIDSUM {

	public static void main(String[] args) {
		
		try {
			
			String outputFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\COMPLETE_FINAL_ANALYSIS\\phospho_coverage\\IDSUM_YUXIN_SEARCHED\\2.2%\\publications\\id_all_pep_simple.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			
			String inputFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\COMPLETE_FINAL_ANALYSIS\\phospho_coverage\\IDSUM_YUXIN_SEARCHED\\2.2%\\publications\\id_all_pep.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));		
			while (in.ready()) {
				String str = in.readLine();	
				String[] split = str.split("\t");
				if (split.length > 5) {
					if (split[2].contains("|")) {
						String uniprot = split[2].split("\\|")[1];
						String geneName = split[4];
						
						out.write(split[0] + "\t" + uniprot + "\t" + geneName + "\n");
						System.out.println(split[0] + "\t" + uniprot + "\t" + geneName);
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
