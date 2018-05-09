package protein.features.sequenceconservation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * AminoAcid Conservation Statistics
 * @author tshaw
 *
 */
public class ConservationSurvey {

	public static String type() {
		return "PROTEINFEATURE";
	}
	public static String description() {
		return "Generate Amino Acid Conservation Statistics based on muscle aligned sequences.";
	}
	public static String parameter_info() {
		return "[folderPath] [tag] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String folderPath = args[0];
			String tag = args[1];
			String outputFile = args[2];
			
			HashMap aa_combination = combination_1();
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("File");
			Iterator itr = aa_combination.keySet().iterator();
			while (itr.hasNext()) {
				String aa = (String)itr.next();
				out.write("\t" + aa);
			}
			out.write("\t" + "Sequences" + "\n");
			File files = new File(folderPath);
			for (File f: files.listFiles()) {				
				if (f.getName().contains(tag)) {
					
					HashMap aa_freq_pos = new HashMap();
					HashMap aa_freq_all = new HashMap();
					
					HashMap map = new HashMap();
					String name = "";
					FileInputStream fstream = new FileInputStream(f.getPath());
					DataInputStream din = new DataInputStream(fstream);
					BufferedReader in = new BufferedReader(new InputStreamReader(din));
					while (in.ready()) {
						String str = in.readLine();
						if (str.contains(">")) {
							name = str;
						} else {
							if (map.containsKey(name)) {
								String seq = (String)map.get(name);
								map.put(name, str.trim() + seq);
							} else {
								map.put(name, str.trim());
							}
						}
					}
					in.close();
					
					int seq_length = 0;
					HashMap seq_map = new HashMap();
					itr = map.keySet().iterator();
					while (itr.hasNext()) {
						name = (String)itr.next();
						String seq = (String)map.get(name);
						seq_length = seq.length();
						for (int i = 0; i < seq.length(); i++) {
							String aa = seq.substring(i, i + 1);
							if (seq_map.containsKey(i)) {
								HashMap aa_map = (HashMap)seq_map.get(i);
								if (aa_map.containsKey(aa)) {
									int count = (Integer)aa_map.get(aa);										
									aa_map.put(aa, count + 1);										
								} else {
									aa_map.put(aa, 1);
								}
								seq_map.put(i, aa_map);
							} else {
								HashMap aa_map = new HashMap();
								aa_map.put(aa, 1);
								seq_map.put(i, aa_map);
							}																
						}
					} // end itr
					
					// single aa frequency
					for (int i = 0; i < seq_length; i++) {
						HashMap aa_map = (HashMap)seq_map.get(i);
						Iterator itr2 = aa_map.keySet().iterator();
						while (itr2.hasNext()) {
							String aa = (String)itr2.next();
							int absolute_conserved = (Integer)aa_map.get(aa);
							//System.out.println(aa + "\t" + absolute_conserved + "\t" + map.size());
							if (absolute_conserved == map.size()) {
								if (aa_freq_pos.containsKey(aa)) {
									int count = (Integer)aa_freq_pos.get(aa);
									aa_freq_pos.put(aa, count + 1);
								} else {
									aa_freq_pos.put(aa, 1);
								}
							}
						
							if (aa_freq_all.containsKey(aa)) {
								int count = (Integer)aa_freq_all.get(aa);
								aa_freq_all.put(aa, count + 1);
							} else {
								aa_freq_all.put(aa, 1);
							}
						
						}
					}
				
					out.write(f.getName());
					
					itr = aa_combination.keySet().iterator();
					while (itr.hasNext()) {
						String aa = (String)itr.next();
						double total_count = 0.0;
						double conserved_count = 0.0;
						if (aa_freq_all.containsKey(aa)) {
							total_count = (Integer)aa_freq_all.get(aa);
						}
						if (aa_freq_pos.containsKey(aa)) {
							conserved_count = (Integer)aa_freq_pos.get(aa);
						}		
						//System.out.println(aa + "\t" + conserved_count + "\t" + total_count);
						double ratio = conserved_count / total_count;
						if (conserved_count == 0) {
							ratio = 0.0;
						}
						out.write("\t" + ratio);
					}
					out.write("\t" + map.size() + "\n");
				} // if statement to restrict to alignment file
			} // iterate all file
			
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public static HashMap combination_1() {
		
		HashMap map = new HashMap();
		
		String[] aas = {"R", "H", "K", "D", "E", "S", "T", "N", "Q", "C", "U", "G", "P", "A", "V", "I", "L", "M", "F", "Y", "W"};
		for (String aa: aas) {
			map.put(aa, aa);
		}
		
		return map;
	}
}
