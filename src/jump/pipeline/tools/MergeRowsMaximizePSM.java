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
 * Merge rows based on unique genes index based on PSM
 * 
 * @author tshaw
 *
 */
public class MergeRowsMaximizePSM {

	public static String type() {
		return "JUMP";
	}
	public static String description() {
		return "Takes matrix containing psm and geneName and generate a matrix that merges duplicated geneName based on max psm value.";
	}
	public static String parameter_info() {
		return "[id_uni_prot_quan.txt] [geneName_index] [psm_index] [minPSM] [outputMatrix] [tmt number: 10 or 11?] [outputSimplifiedMatrix]";
	}
	public static void execute(String[] args) {
		
		try {
			String inputFile = args[0];
			//String sampleNames = args[1];
			int geneName_index = new Integer(args[1]);
			int psm_index = new Integer(args[2]);			
			int min_psm = new Integer(args[3]);
			String outputFile = args[4]; // output comprehensive table filtered by psm
			int tmt_num = new Integer(args[5]);
			String outputSimpleMatrixFile = args[6]; // geneName \t values
			/*if (sampleNames.split(",").length != 10) {
				System.out.println("sampleNames must contains 10 different tags");
				System.exit(0);
			}*/
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileWriter fwriter_simple = new FileWriter(outputSimpleMatrixFile);
			BufferedWriter out_simple = new BufferedWriter(fwriter_simple);
			
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
			String header = in.readLine();
			String[] split_header = header.split("\t");
			if (split_header.length < 3) {
				// print out the top header (number of protein identified) and column Name which is on the second line
				out.write(header + "\n");
				//out_simple.write(header + "\n");
				header = in.readLine();
				out.write(header + "\n");
				split_header = header.split("\t");
				out_simple.write("GN");
				for (int i = split_header.length - tmt_num; i < split_header.length; i++) {
					out_simple.write("\t" + split_header[i]);
				}
				out_simple.write("\n");
				
			} else {
				out.write(header + "\n");
				out_simple.write("GN");
				for (int i = split_header.length - tmt_num; i < split_header.length; i++) {
					out_simple.write("\t" + split_header[i]);
				}
				out_simple.write("\n");
			}
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneName = split[geneName_index];
				if (!list.contains(geneName)) {
					list.add(geneName);
				}
				int new_psm = new Integer(split[psm_index]);
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
					String[] split_line = line.split("\t");
					out_simple.write(geneName);
					for (int i = split_line.length - tmt_num; i < split_line.length; i++) {
						out_simple.write("\t" + split_line[i]);
					}
					out_simple.write("\n");
				}
			}
			out.close();
			out_simple.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
