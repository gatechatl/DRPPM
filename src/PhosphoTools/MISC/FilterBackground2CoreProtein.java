package PhosphoTools.MISC;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class FilterBackground2CoreProtein {

	public static String type() {
		return "PSSM";
	}
	public static String description() {
		return "Filter the PSSM scores with core protein set";
	}
	public static String parameter_info() {
		return "[coreGeneSetFasta] [indexFile] [outputFolder]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String coreGeneSetFasta = args[0];
			String indexFile = args[1];
			String outputFolder = args[2];
			HashMap accessions = new HashMap();
			FileInputStream fstream = new FileInputStream(coreGeneSetFasta);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));				
			while (in.ready()) {
				String str = in.readLine().trim();
				if (str.contains(">")) {
					String[] split = str.split("\\|");
					String accession = split[1];
					accessions.put(accession , accessions);
				}
			}
			in.close();
			
			fstream = new FileInputStream(indexFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();				
			while (in.ready()) {
				String str = in.readLine().trim();
				String[] split = str.split("\t");
				File f = new File(split[0]);
				String newFileName = outputFolder + "/" + f.getName();
				
				FileWriter fwriter2 = new FileWriter(newFileName);
				BufferedWriter out2 = new BufferedWriter(fwriter2);			
				
				
				FileInputStream fstream2 = new FileInputStream(split[0]);
				DataInputStream din2 = new DataInputStream(fstream2);
				BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));				
				while (in2.ready()) {
					String str2 = in2.readLine().trim();
					String[] split2 = str2.split("\t");
					String accession = split2[0].split("_")[0];
					if (accessions.containsKey(accession)) {
						out2.write(str2 + "\n");
					}
				}
				in2.close();
				out2.close();
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
