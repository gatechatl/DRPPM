package WholeExonTools.Special.MouseGermlineAnalysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class ExtractSingletonAndRecurrent {

	public static String parameter_info() {
		return "[inputFile] [outputRecurrent] [outputBadSingleton] [outputGoodSingleton]";
	}
	public static void execute(String[] args) {
		
		try {
			String inputFile = args[0];
			String outputRecurrent = args[1];
			String outputBadSingleton = args[2];
			String outputGoodSingleton = args[3];
			String outputAll = args[4];

			FileWriter fwriter4 = new FileWriter(outputAll);
			BufferedWriter out_all = new BufferedWriter(fwriter4);

			FileWriter fwriter = new FileWriter(outputRecurrent);
			BufferedWriter out_r = new BufferedWriter(fwriter);

			FileWriter fwriter2 = new FileWriter(outputBadSingleton);
			BufferedWriter out_b = new BufferedWriter(fwriter2);

			FileWriter fwriter3 = new FileWriter(outputGoodSingleton);
			BufferedWriter out_g = new BufferedWriter(fwriter3);

			LinkedList recurrent = new LinkedList();
			LinkedList all = new LinkedList();
			LinkedList good = new LinkedList();
			HashMap map = new HashMap();
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out_g.write("Note\t" + header + "\n");
			out_b.write("Note\t" + header + "\n");
			out_r.write("Note\t" + header + "\n");
			out_all.write(header + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split[1].equals("SJHQ")) {
					if (!all.contains(str)) {
						all.add(str);
					}
					
					boolean multi_germline = false;
					int hit = 0;
					for (int i = 16; i < split.length; i++) {
						String value = split[i].replaceAll("=", "");
						double mut = new Integer(value.split("/")[0]);
						double total = new Integer(value.split("/")[1]);
						if (mut / total > 0.05) {
							hit++;
						}
					}
					if (hit == 0) {
						good.add(str);
					}
					String key = split[0] + "\t" + split[3] + "\t" + split[4];
					String sample = split[2];
					if (map.containsKey(key)) {
						String line = (String)map.get(key);
						String[] split2 = line.split("\t");
						if (split2[2].equals(sample)) {
							if (!recurrent.contains(line)) {
								recurrent.add(line);
							}
							if (!recurrent.contains(str)) {
								recurrent.add(str);
							}							
						}
					} else {
						map.put(key, str);
					}
				}
			}
			in.close();
			
			Iterator itr = all.iterator();
			while (itr.hasNext()) {
				String str = (String)itr.next();
				if (good.contains(str)) {
					out_g.write(str + "\n");
					out_all.write("singleton_good\t" + str + "\n");
				} else if (recurrent.contains(str)) {
					out_r.write(str + "\n");
					out_all.write("recurrent\t" + str + "\n");
				} else {
					out_b.write(str + "\n");
					out_all.write("singleton_bad\t" + str + "\n");
				}
			}
			out_g.close();
			out_r.close();
			out_b.close();
			out_all.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
