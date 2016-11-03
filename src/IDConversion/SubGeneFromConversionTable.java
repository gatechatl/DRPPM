package IDConversion;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Given a gene list, extract the conversion table rows
 * @author tshaw
 *
 */
public class SubGeneFromConversionTable {

	public static String type() {
		return "IDCONVERSION";
	}
	public static String description() {
		return "Given a gene list, extract the conversion table rows";
	}
	public static String parameter_info() {
		return "[inputFile] [conversionTable] [id_type:GENENAME/UNIPROT/ENTREZ/REFSEQPROTEIN/REFSEQNUCLEOTIDE] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String conversionTable = args[1];
			String id_type = args[2];
			String outputFile = args[3];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			HashMap map = new HashMap();
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String id = split[0].replaceAll("\"",  "").split("\\.")[0];
				map.put(id, id);
			}
			in.close();			
			
			HashMap hit = new HashMap();
			fstream = new FileInputStream(conversionTable);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write(header + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String uniprot = split[0].split("\\.")[0];
				String entrez = split[1];
				String refseq_protein = split[2].split("\\.")[0];
				String refseq_nuc = split[3].split("\\.")[0];
				String geneName = split[4];
				if (id_type.equals("GENENAME") && map.containsKey(geneName)) {
					hit.put(geneName, geneName);
					out.write(str + "\n");
				}
				if (id_type.equals("UNIPROT") && map.containsKey(uniprot)) {
					hit.put(uniprot, uniprot);
					out.write(str + "\n");
				}
				if (id_type.equals("ENTREZ") && map.containsKey(entrez)) {
					hit.put(entrez, entrez);
					out.write(str + "\n");
				}
				if (id_type.equals("REFSEQPROTEIN") && map.containsKey(refseq_protein)) {
					hit.put(refseq_protein, refseq_protein);
					out.write(str + "\n");
				}
				if (id_type.equals("REFSEQNUCLEOTIDE") && map.containsKey(refseq_nuc)) {
					hit.put(refseq_nuc, refseq_nuc);
					out.write(str + "\n");
				}
				
			}
			in.close();	
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String id = (String)itr.next();
				if (!hit.containsKey(id)) {
					System.out.println("The output table failed to find: " + id);
				}
				//if (hit.)
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
