package jump.pipeline.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Merge rows based on unique genes
 * Assumes that sample is a 10plex
 * @author tshaw
 *
 */
public class MergeRowsMaximizePSM {

	public static String type() {
		return "JUMP";
	}
	public static String description() {
		return "Takes input from peptide and generate a matrix\nAssumes that sample is a 10plex";
	}
	public static String parameter_info() {
		return "[id_uni_prot_quan.txt] [minPSM] [outputMatrix]";
	}
	public static void execute(String[] args) {
		
		try {
			String inputFile = args[0];
			//String sampleNames = args[1];
			int min_psm = new Integer(args[1]);
			String outputFile = args[2];			
			
			/*if (sampleNames.split(",").length != 10) {
				System.out.println("sampleNames must contains 10 different tags");
				System.exit(0);
			}*/
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			//out.write("Gene");
			/*String[] names = sampleNames.split(",");
			for (int i = 0; i < names.length; i++) {
				out.write("\t" + names[i]);
			}*/
			//out.write("\n");
			
			HashMap map = new HashMap();
			LinkedList list = new LinkedList();
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			out.write(in.readLine() + "\n");
			out.write(in.readLine() + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneName = split[3];
				if (!list.contains(geneName)) {
					list.add(geneName);
				}
				int new_psm = new Integer(split[4]);
				if (new_psm >= min_psm) {
					if (!geneName.equals("NA")) {
						if (map.containsKey(geneName)) {
							String line = (String)map.get(geneName);
							String[] split_line = line.split("\t");
							int old_psm = new Integer(split_line[4]);
							if (new_psm > old_psm) {
								map.put(geneName, str);
							}
						} else {
							map.put(geneName, str);
						}
					}
				}
				/*if (!geneName.equals("NA")) {
					out.write(geneName);
					for (int i = split.length - 10; i < split.length; i++) {
						out.write("\t" + split[i]);
					}
					out.write("\n");
				}*/
			}
			in.close();
			Iterator itr = list.iterator();
			while (itr.hasNext()) {
				String geneName = (String)itr.next();
				if (map.containsKey(geneName)) {
					String line = (String)map.get(geneName);
					out.write(line + "\n");
				}
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
