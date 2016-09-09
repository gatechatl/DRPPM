package PhosphoTools.Heatmap;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class JUMPqDataMatrixGeneration {

	public static int[] header_expr_info(String header, String[] tags) {
		int[] index = new int[tags.length];
		String[] split = header.split("\t");
		for (int i = 0; i < split.length; i++) {
			for (int j = 0; j < tags.length; j++) {
				//System.out.println(split[i].split(" ")[0] + "\t" + tags[j] + "\t" + header);
				/*if (split[i].contains(tags[j])) {
					
				}*/
				if (split[i].split(" ")[0].equals(tags[j])) {
					
					index[j] = i;
				}
			}
		}
		return index;
	}
	public static String parameter_info() {
		return "[KinaseSub-reference] [inputFile] [accession_index] [modsiteIndex] [type] [kinase_name] [index_str] [alias_str] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			HashMap map = new HashMap();
			String reference = args[0];
			String inputFile = args[1];
			int accession_index = new Integer(args[2]);
			String input_type = args[3];
			String term = args[4];
			//int modsite_index = new Integer(args[3]);
			String index_str = args[5];
			String alias_str = args[6];
			String outputFile = args[7];
			
			HashMap uniprot2geneName = new HashMap();
			FileInputStream fstream = new FileInputStream(reference);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			//String header = in.readLine();			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String kinase = split[2].replaceAll("\\/", "-");
				String sub_name = "";
				String sub_acc = "";
				//uniprot2geneName.put(sub_acc, sub_name);
				String site = "";
				if (input_type.equals("kinase_substrate_ref")) {
					sub_name = split[8];
					sub_acc = split[7];
					uniprot2geneName.put(sub_acc, sub_name);
					site = split[11];
					
					if (term.equals("NA")) {
						if (map.containsKey(kinase)) {
							HashMap substrate = (HashMap)map.get(kinase);
							substrate.put(sub_acc + "\t" + site, sub_acc + "\t" + site);
							map.put(kinase, substrate);
						} else {
							HashMap substrate = new HashMap();
							substrate.put(sub_acc + "\t" + site, sub_acc + "\t" + site);
							map.put(kinase, substrate);
						}
					} else {
						if (term.toUpperCase().equals(kinase.toUpperCase())) {
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
					}
					
				} else if (input_type.equals("substrate_function_ref")) {
					sub_name = split[0];
					sub_acc = split[1];
					uniprot2geneName.put(sub_acc, sub_name);
					site = split[3].split("-")[0];
					String function_type = split[4];
					if (term.equals("NA")) {
						if (map.containsKey(sub_acc)) {
							HashMap substrate = (HashMap)map.get(sub_acc);
							substrate.put(sub_acc + "\t" + site, sub_acc + "\t" + site);
							map.put(sub_acc, substrate);
						} else {
							HashMap substrate = new HashMap();
							substrate.put(sub_acc + "\t" + site, sub_acc + "\t" + site);
							map.put(sub_acc, substrate);
						}
					} else {
						if (function_type.contains(term)) {
							if (map.containsKey(sub_acc)) {
								HashMap substrate = (HashMap)map.get(sub_acc);
								substrate.put(sub_acc + "\t" + site, sub_acc + "\t" + site);
								map.put(sub_acc, substrate);
							} else {
								HashMap substrate = new HashMap();
								substrate.put(sub_acc + "\t" + site, sub_acc + "\t" + site);
								map.put(sub_acc, substrate);
							}
						}
					}
				} else {
					System.out.println("Bad Input Type");
					System.exit(0);
				}

				
				
			}
			in.close();
			
			
			String gene = "";
			HashMap expression = new HashMap();
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));			
			String header = in.readLine();
			if (header.split("\t").length < 4) {
				header = in.readLine();
			}
			String[] split_header = header.split("\t");
			String header_final = "";;
			String[] sample_strs = index_str.split(",");
			int[] samples = header_expr_info(header, sample_strs);
			for (int i: samples) {
				if (header_final.equals("")) {
					header_final = split_header[i];
				} else {
					header_final += "\t" + split_header[i];
				}
			}			
			if (alias_str.split(",").length == samples.length) {
				header_final = alias_str.replaceAll(",", "\t");
			}
			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				gene = split[accession_index].split("\\|")[1] + "\t" + split[accession_index].split(":")[1];				
				String result = "";;
				/*for (int i = numeric_start; i <= numeric_end; i++) {
					if (i == numeric_start) {
						result = split[i];
					} else {
						result += "\t" + split[i];
					}
				}*/
				for (int i: samples) {
					if (result.equals("")) {
						result = split[i];
					} else {
						result += "\t" + split[i];
					}
				}
				expression.put(gene, result);
			}
			in.close();

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			out.write("GeneName_Accession_MODSITE\t" + header_final + "\n");
			
			HashMap writeOnce = new HashMap();
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String)itr.next();				

				HashMap substrate = (HashMap)map.get(key);
				Iterator itr2 = substrate.keySet().iterator();
				while (itr2.hasNext()) {
					String substrate_name = (String)itr2.next();
					if (expression.containsKey(substrate_name)) {										
						String result = (String)expression.get(substrate_name);
						String uniprot_accession = substrate_name.split("\t")[0];
						String orig_geneName = (String)uniprot2geneName.get(uniprot_accession);
						String name = orig_geneName + "_" + substrate_name.replaceAll("\t", "_"); 
						if (!writeOnce.containsKey(name)) {
							out.write(name + "\t" + result + "\n");
						}
						writeOnce.put(name, name);
					}					
				}				
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

