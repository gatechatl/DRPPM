package stjude.projects.peng;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * 
 * @author tshaw
 *
 */
public class GeneNameTableAppendNCBIHomologyTable {

	
	public static void execute(String[] args) {
		
		try {

			
			String inputFile = args[0];
			String inputFile2 = args[1];
			String outputFile = args[2];
			HashMap map = new HashMap();
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String human_geneID = split[0];
				String mouse_geneID = split[1];
				if (split[0].contains("\\|")) {
					human_geneID = split[0].split("\\|")[1];					
				}
				if (split[1].contains("\\|")) {
					mouse_geneID = split[1].split("\\|")[1];					
				}
				map.put(human_geneID, mouse_geneID);
			}
			in.close();
			
			HashMap match = new HashMap();
			

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			fstream = new FileInputStream(inputFile2);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			out.write(header + "\t" + header + "\n");
			while (in.ready()) {
				String mouse_str = in.readLine();
				String[] split_mouse = mouse_str.split("\t");
				String mouse_uniprot_accession = split_mouse[12];
				String human_str = in.readLine();
				String[] split_human = human_str.split("\t");
				String human_uniprot_accession = split_human[12];
				if (map.containsKey(human_uniprot_accession)) {
					String mouse_ortholog = (String)map.get(human_uniprot_accession);
					
				}
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
