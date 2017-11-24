package stjude.projects.jpaultaylor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class ChangeFastaIDUniprot {

	public static String type() {
		return "Change fasta id to refmRNA";
	}
	public static String description() {
		return "Change the fasta id to refseq";
	}
	public static String parameter_info() {
		return "[conversionFile] [lowComplexityFile] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String conversionFile = args[0];
			String lowComplexityFile = args[1];
			String outputFile = args[2];
			HashMap map = new HashMap();
			HashMap score = new HashMap();
			FileInputStream fstream = new FileInputStream(conversionFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split(",");
				split[1] = split[1].split("\\|")[1];
				//if (split[0].contains("NM_")) {
				if (map.containsKey(split[1])) {		
					if (new Double(split[2]) > 90) {
						String line = (String)map.get(split[1]);
						String[] split2 = line.split(" ");
						if (new Double(split[11]) > new Double(split2[2])) {
							map.put(split[1], split[0] + " " + split[2] + " " + split[11]);
						}
					}
					//map.put(split[1], split[0] + "\t" + split[2]);
				} else {
					if (new Double(split[2]) > 90) {
						map.put(split[1], split[0] + " " + split[2] + " " + split[11]);
					}
				}
				//}
			}
			in.close();
			

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			boolean found = false;
			fstream = new FileInputStream(lowComplexityFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains(">")) {
					String accession = str.split(" ")[0].replaceAll(">", "");
					if (map.containsKey(accession)) {
						found = true;
						String replace = (String)map.get(accession);
						out.write(str.replaceAll(accession, replace) + "\n");
					} else {
						found = false;
					}
				} else {
					if (found) {
						out.write(str + "\n");
					}
				}				
			}
			in.close();
			out.close();
			
			System.out.println(map.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
