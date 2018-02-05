package stjude.projects.jpaultaylor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class JPaulTaylorConvertUniprot2UniprotGeneName {
	
	public static String type() {
		return "JPaulTaylor";
	}
	public static String description() {
		return "Convert uniprot id to uniprot geneName.";
	}
	public static String parameter_info() {
		return "[GRPR_list.txt] [fastaFile] [GRPR_outputFile] [PR_outputFile] [GR_outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			String inputFile = args[0];
			String fastaFile = args[1];
			String GRPR_outputFile = args[2];
			String PR_outputFile = args[3];
			String GR_outputFile = args[4];

			FileWriter fwriter_grpr = new FileWriter(GRPR_outputFile);
			BufferedWriter out_grpr = new BufferedWriter(fwriter_grpr);
			FileWriter fwriter_pr = new FileWriter(PR_outputFile);
			BufferedWriter out_pr = new BufferedWriter(fwriter_pr);
			FileWriter fwriter_gr = new FileWriter(GR_outputFile);
			BufferedWriter out_gr = new BufferedWriter(fwriter_gr);
			
			
			HashMap accession2humanGeneName = new HashMap();
			FileInputStream fstream = new FileInputStream(fastaFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains(">")) {
					String accession = str.split("\\|")[1];
					String uniprot_geneName = str.split("\\|")[2].split(" ")[0];
					accession2humanGeneName.put(accession,  uniprot_geneName);
				}				
			}
			in.close();
			
			HashMap GRPR = new HashMap();
			HashMap PR = new HashMap();
			HashMap GR = new HashMap();
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split[0].equals("GRPR")) {
					if (accession2humanGeneName.containsKey(split[2])) {
						String uniprotGeneName = (String)accession2humanGeneName.get(split[2]);
						GRPR.put(uniprotGeneName, uniprotGeneName);
					}
				}
				if (split[0].equals("GR_ONLY")) {
					if (accession2humanGeneName.containsKey(split[2])) {
						String uniprotGeneName = (String)accession2humanGeneName.get(split[2]);
						GR.put(uniprotGeneName, uniprotGeneName);
					}
				}
				if (split[0].equals("PR_ONLY")) {
					if (accession2humanGeneName.containsKey(split[2])) {
						String uniprotGeneName = (String)accession2humanGeneName.get(split[2]);
						PR.put(uniprotGeneName, uniprotGeneName);
					}
				}
			}
			in.close();
			
			Iterator itr = GRPR.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String)itr.next();
				out_grpr.write(key + "\n");
			}
			itr = GR.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String)itr.next();
				out_gr.write(key + "\n");
			}
			itr = PR.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String)itr.next();
				out_pr.write(key + "\n");
			}
			out_grpr.close();
			out_gr.close();
			out_pr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
