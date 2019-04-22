package rnaseq.tools.ercc;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class GenerateERCCgtffile {

	public static String type() {
		return "MISC";
	}
	public static String description() {
		return "Generate GTF File for ERCC";
	}
	public static String parameter_info() {
		return "[inputERCCFasta]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String ercc_fasta = args[0];
			HashMap map_length = new HashMap();
			String name = "";
			FileInputStream fstream = new FileInputStream(ercc_fasta);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains(">")) {
					name = str.replaceAll(">", "");
					map_length.put(name, 0);
				
				} else {
					if (map_length.containsKey(name)) {
						int length = (Integer)map_length.get(name);
						length += str.length();
						map_length.put(name, length);
					}
				}
				
			}
			in.close();
			
			Iterator itr = map_length.keySet().iterator();
			while (itr.hasNext()) {
				name = (String)itr.next();
				int length = (Integer)map_length.get(name);
				String attribute = "gene_id \"" + name + "\"; transcript_id \"" + name + "\"; exon_number \"1\"; gene_name \"" + name + "\"; gene_biotype \"" + name + "\"; transcript_name \"" + name + "\";";
				String line = name + "\tprotein_coding\tgene\t1\t" + length + "\t.\t+\t.\t " + attribute;
				System.out.println(line);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
