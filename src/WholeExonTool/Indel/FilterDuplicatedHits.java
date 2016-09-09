package WholeExonTool.Indel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Filter duplicated hits in the indel result
 * @author tshaw
 *
 */
public class FilterDuplicatedHits {

	public static String type() {
		return "INDEL";
	}
	public static String description() {
		return "Filter duplicated hits in the indel result";
	}
	public static String parameter_info() {
		return "[indelFile] [outputCleanFile] [outputFilteredHits]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String indelFile = args[0];
			String outputCleanFile = args[1];
			String outputFilteredHits = args[2];
			
			FileWriter fwriter = new FileWriter(outputCleanFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileWriter fwriter2 = new FileWriter(outputFilteredHits);
			BufferedWriter out2 = new BufferedWriter(fwriter2);
			
			boolean doOnce = false;;
			HashMap duplicateMap = new HashMap();
			FileInputStream fstream = new FileInputStream(indelFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split.length >= 11) {
					if (!split[0].equals("GeneName")) {
						String geneName = split[0];
						String quality = split[1];
						String chr = split[3];
						String pos = split[4];
						String sampleName = split[2];
						String mutationType = split[5];
						double tumorMut = new Double(split[9]);
						double tumorGerm = new Double(split[10]);
						if (duplicateMap.containsKey(chr + pos)) {
							LinkedList list = (LinkedList)duplicateMap.get(chr + pos);
							if (!list.contains(sampleName)) {
								list.add(sampleName);
							}
							duplicateMap.put(chr + pos, list);
						} else {
							LinkedList list = new LinkedList();
							if (!list.contains(sampleName)) {
								list.add(sampleName);
							}
							duplicateMap.put(chr + pos, list);
						}
					}
				}
			}
			in.close();


			fstream = new FileInputStream(indelFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split.length >= 11) {
					if (split[0].equals("GeneName")) {
						if (!doOnce) {
							out.write(str + "\n");
							out2.write(str + "\n");
							doOnce = true;
						}
					}
					if (!split[0].equals("GeneName")) {
						String geneName = split[0];
						String quality = split[1];
						String chr = split[3];
						String pos = split[4];
						String sampleName = split[2];
						String mutationType = split[5];
						double tumorMut = new Double(split[9]);
						double tumorGerm = new Double(split[10]);
						if (duplicateMap.containsKey(chr + pos)) {
							LinkedList list = (LinkedList)duplicateMap.get(chr + pos);
							if (list.size() == 1) {
								out.write(str + "\n");
							} else {
								out2.write(str + "\n");
							}
						}
					}
				}
			}
			in.close();

			out.close();
			out2.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
