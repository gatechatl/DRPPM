package BlastTool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class GenerateBlastFile {

	public static String description() {
		return "Split file";
	}
	public static String type() {
		return "BLAST";
	}
	public static String parameter_info() {
		return "[inputFile] [outputFolder]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String outputFolder = args[1];
			
			File f = new File(inputFile);
			String outputFile = f.getName();
			HashMap map = new HashMap();
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String name = "";
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains(">")) {
					name = str;
				} else {
					if (map.containsKey(name)) {
						String seq = (String)map.get(name);
						seq += str;
						map.put(name, seq);
					} else {
						map.put(name, str);
					}
				}
			}
			in.close();
			int index = 0;
			int count = 0;
			FileWriter fwriter = fwriter = new FileWriter("fake");;
			BufferedWriter out = new BufferedWriter(fwriter);
			
			out = new BufferedWriter(fwriter);
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				name = (String)itr.next();
				String seq = (String)map.get(name);
				if (count % 1000 == 0) {
					index++;
					out.close();
					System.out.println("blastx -db ../human_protein_Prot_RefSeq_v1_20160715.fna -query " + outputFolder + "/" + outputFile + "_" + index + " -out " + outputFolder + "/" + outputFile + "_" + index + ".output -max_target_seqs 1");
					fwriter = new FileWriter(outputFolder + "/" + outputFile + "_" + index);
					out = new BufferedWriter(fwriter);					
				}
				out.write(name + "\n" + seq + "\n");
				out.flush();
				count++;
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
