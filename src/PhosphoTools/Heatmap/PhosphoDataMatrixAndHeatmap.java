package PhosphoTools.Heatmap;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class PhosphoDataMatrixAndHeatmap {
	public static String parameter_info() {
		return "[KinaseSub-reference] [inputFile] [accession_index] [modsiteIndex] [numeric_start] [numeric_end] [outputPathFolder]";
	}
	public static void execute(String[] args) {
		
		try {
			HashMap map = new HashMap();
			String reference = args[0];
			String inputFile = args[1];
			int accession_index = new Integer(args[2]);
			int modsite_index = new Integer(args[3]);
			int numeric_start = new Integer(args[4]);
			int numeric_end = new Integer(args[5]);
			String outputFilePath = args[6];
			
			HashMap uniprot2geneName = new HashMap();
			FileInputStream fstream = new FileInputStream(reference);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String kinase = split[2].replaceAll("\\/", "-");
				String sub_name = split[8];
				String sub_acc = split[7];
				uniprot2geneName.put(sub_acc, sub_name);
				String site = split[11];
				if (map.containsKey(kinase)) {
					HashMap substrate = (HashMap)map.get(kinase);
					substrate.put(sub_acc + "\t" + site, sub_acc + "\t" + site);
					map.put(kinase, substrate);
				} else {
					HashMap substrate = new HashMap();
					substrate.put(sub_acc + "\t" + site, sub_acc + "\t" + site);
					map.put(kinase, substrate);
				}
			}
			in.close();
			
			
			String gene = "";
			HashMap expression = new HashMap();
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");
			String header_final = "";;
			for (int i = numeric_start; i <= numeric_end; i++) {
				if (i == numeric_start) {
					header_final = split_header[i];
				} else {
					header_final += "\t" + split_header[i];
				}
			}
			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				gene = split[accession_index].split("\\|")[1] + "\t" + split[modsite_index];				
				String result = "";;
				for (int i = numeric_start; i <= numeric_end; i++) {
					if (i == numeric_start) {
						result = split[i];
					} else {
						result += "\t" + split[i];
					}
				}
				expression.put(gene, result);
			}
			in.close();
			
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String)itr.next();
				
				FileWriter fwriter = new FileWriter(outputFilePath + "/" + key + "_matrix.txt");
				BufferedWriter out = new BufferedWriter(fwriter);
				out.write("GeneName\tAccession\tMODSITE\t" + header_final + "\n");
				HashMap substrate = (HashMap)map.get(key);
				Iterator itr2 = substrate.keySet().iterator();
				while (itr2.hasNext()) {
					String substrate_name = (String)itr2.next();
					if (expression.containsKey(substrate_name)) {
						String result = (String)expression.get(substrate_name);
						String uniprot_accession = substrate_name.split("\t")[0];
						String orig_geneName = (String)uniprot2geneName.get(uniprot_accession);
						out.write(orig_geneName + "\t" + substrate_name + "\t" + result + "\n");
					}					
				}
				out.close();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
