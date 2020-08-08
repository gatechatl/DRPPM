package stjude.projects.jpaultaylor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class SplitFastaFile {

	public static String type() {
		return "FASTA";
	}
	public static String description() {
		return "Split the original fasta file";
	}
	public static String parameter_info() {
		return "[fastaFile] [num_files_split]";
	}
	public static void execute(String[] args) {
		try {
			HashMap map = new HashMap();
			

			
			String header = "";
			String fastaFile = args[0];
			int num = new Integer(args[1]);
			FileWriter[] fwriter = new FileWriter[num];
			BufferedWriter[] out = new BufferedWriter[num];
			
			for (int i = 0; i < num; i++) {
				fwriter[i] = new FileWriter(fastaFile + "_" + i);
				out[i] = new BufferedWriter(fwriter[i]);
			}
			
			FileInputStream fstream = new FileInputStream(fastaFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains(">")) {
					header = str;
				} else {
					if (map.containsKey(header)) {
						String line = (String)map.get(header);
						line += str.trim();
						map.put(header, line);
					} else {
						map.put(header, str.trim());
					}
				}				
			}
			in.close();
			int i = 0;
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String name = (String)itr.next();
				String seq = (String)map.get(name);
				out[i].write(name + "\n" + seq + "\n");
				i++;
				if (i >= num) {
					i = 0;
				}
			}
			for (i = 0; i < num; i++) {
				out[i].close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
