package PhosphoTools.HongBoProject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

import IDConversion.Uniprot2GeneID;

public class GenerateComprehensiveGeneList {

	public static String parameter_info() {
		return "[inputFile] [idmapperFile] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			String inputFile = args[0];
			String idmapperFile = args[1];
			String outputFile = args[2];
			HashMap map = Uniprot2GeneID.uniprot2geneID(idmapperFile);
			
			HashMap sites = new HashMap();
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String accession = split[0].split("\\|")[1];
				String site = split[1].replaceAll("\\*", "").replaceAll(",", "");
				if (site.contains("S")) {
					site = "S" + site.replaceAll("S", "");
				} else if (site.contains("T")) {
					site = "T" + site.replaceAll("T", "");
				} else if (site.contains("Y")) {
					site = "Y" + site.replaceAll("Y", "");
				}
				if (map.containsKey(accession)) {
					String gene = (String)map.get(accession);
					sites.put(gene + "_" + accession + "_" + site, "");
				}				
			}
			in.close();
			

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			Iterator itr = sites.keySet().iterator();
			while (itr.hasNext()) {
				String site = (String)itr.next();
				out.write(site + "\n");
			}
			
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
