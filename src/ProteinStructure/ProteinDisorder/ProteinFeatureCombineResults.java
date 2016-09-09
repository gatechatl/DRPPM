package ProteinStructure.ProteinDisorder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class ProteinFeatureCombineResults {

	public static String parameter_info() {
		return "[aminoacid_file] [pepstats_file] [protparam_file] [GR_PR_Index] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String aminoacid_file = args[0];
			String pepstats_file = args[1];
			String protparam_file = args[2];
			String grpr_index_file = args[3];
			String outputFile = args[4];
			HashMap aminoacid = grabResult(aminoacid_file);
			HashMap pepstats = grabResult(pepstats_file);
			HashMap protparam = grabResult(protparam_file);
			String aminoacid_header = grabHeader(aminoacid_file);
			String pepstats_header = grabHeader(pepstats_file);
			String protparam_header = grabHeader(protparam_file);			
			HashMap grpr_index = grab_grpr_index(grpr_index_file);
			
			FileWriter fwriter = new FileWriter(outputFile);
            BufferedWriter out = new BufferedWriter(fwriter);
                   
            String header = "Type" + "\t" + aminoacid_header + "\t" + pepstats_header + "\t" + protparam_header + "\n";
            out.write(header);
			Iterator itr = aminoacid.keySet().iterator();
			while (itr.hasNext()) {
				String uniprot = (String)itr.next();				
				if (pepstats.containsKey(uniprot) && protparam.containsKey(uniprot)) {
					
					String[] short_uniprot = uniprot.split("_");
					if (uniprot.contains("_HUMAN_")) {
						String result = grpr_index.get(short_uniprot[0] + "_" + short_uniprot[1] + "_" + short_uniprot[2]) + "\t" + aminoacid.get(uniprot) + "\t" + pepstats.get(uniprot) + "\t" + protparam.get(uniprot) + "\n";
						if (result.split("\t").length == header.split("\t").length) {
							out.write(result);
						}
					} else {
						String result = grpr_index.get(short_uniprot[0] + "_" + short_uniprot[1]) + "\t" + aminoacid.get(uniprot) + "\t" + pepstats.get(uniprot) + "\t" + protparam.get(uniprot) + "\n";
						if (result.split("\t").length == header.split("\t").length) {
							out.write(result);
						}
					}
				}
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static HashMap grab_grpr_index(String inputFile) {
		HashMap map = new HashMap();
		try {
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(split[1] + "_" + split[2], split[0]);
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	public static String grabHeader(String inputFile) {
		String header = "";
		try {
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return header;
	}
	public static HashMap grabResult(String inputFile) {
		HashMap map = new HashMap();
		try {
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(split[0], str);
			}
			in.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}
