package protein.features.motif.meme;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Remove redudnat fasta sequences
 * @author tshaw
 *
 */
public class GenerateUniqFastaFile {

	public static String type() {
		return "MISC";
	}
	public static String description() {
		return "provide a uniq fasta file and remove redundant fasta sequences";
	}
	public static String parameter_info() {
		return "[inputFastaFile] [outputFile] [use numerical id flag true/false]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String outputFile = args[1];
			boolean id_flag = new Boolean(args[2]);
			HashMap name = new HashMap();
			HashMap map = new HashMap();
			String tag = "";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains(">")) {
					tag = str.replaceAll(">", "");
				} else {
					String seq = str.replaceAll("\\.", "");
					seq = seq.replaceAll("\\*", "");
					seq = seq.replaceAll("-", "");
					map.put(seq, seq);
					name.put(seq, tag);
				}
			}
			in.close();
			map = combine(map);
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			Iterator itr = map.keySet().iterator();
			int id = 0;
			while (itr.hasNext()) {
				String seq = (String)itr.next();
				
				if (id_flag) {
					id++;
					out.write(">" + id + "\n" + seq + "\n");
				} else {
					id++;
					tag = (String)name.get(seq);
					out.write(">" + tag + "|" + id + "\n" + seq + "\n");
				}
			}
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static HashMap combine(HashMap map) {
		HashMap result = new HashMap();
		HashMap pickone = new HashMap();
		Iterator itr = map.keySet().iterator();
		while (itr.hasNext()) {
			String seq1 = (String)itr.next();
			
			Iterator itr2 = result.keySet().iterator();			
			while (itr2.hasNext()) {
				String seq2 = (String)itr2.next();
				
				for (int i = 0; i < seq1.length(); i++) {
					double overlap = 0;
					for (int j = 0; j < seq2.length(); j++) {
						String l2 = seq2.substring(j, j + 1);
						if (i + j < seq1.length()) {
							String l1 = seq1.substring(i + j, i + j + 1);
							if (l1.equals(l2)) {
								overlap++;
							}
						}												
					}
					if (overlap / seq1.length() > 0.8) {
						if (seq1.length() > seq2.length()) {
							pickone.put(seq2, seq1);
						} else {
							pickone.put(seq1, seq2);
						}
					}
				}
			}
		}
		itr = map.keySet().iterator();
		while (itr.hasNext()) {
			String seq = (String)itr.next();
			if (!pickone.containsKey(seq)) {
				result.put(seq, seq);
			}
		}
		return result;
	}
}
