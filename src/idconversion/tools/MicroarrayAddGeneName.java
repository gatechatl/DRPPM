package idconversion.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;


public class MicroarrayAddGeneName {

	public static String parameter_info() {
		return "[refseqFlatFile] [inputFile] [outputFile]";
	}
	public static void execute(String[] args) {
		try {
									
			String refseqFile = args[0];
			String inputFile = args[1];
			String outputFile = args[2];
			
			
			HashMap refseq = new HashMap();
			FileInputStream fstream = new FileInputStream(refseqFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				refseq.put(split[1], split[0]);
			}
			in.close();
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			HashMap map = new HashMap();
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write(header + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String gene = "";
				for (int i = 0; i < split[0].split("_").length - 1; i++) {
					if (gene.equals("")) {
						gene = split[0].split("_")[i];
					} else {
						gene += "_" + split[0].split("_")[i];
					}
				}
				split[0] = gene;
				if (refseq.containsKey(split[0]) && !map.containsKey(split[0])) {
					String geneName = (String)refseq.get(split[0]);
					out.write(geneName);
					for (int i = 1; i < split.length; i++) {
						out.write("\t" + split[i]);
					}
					out.write("\n");
					map.put(split[0], split[0]);
				}
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
