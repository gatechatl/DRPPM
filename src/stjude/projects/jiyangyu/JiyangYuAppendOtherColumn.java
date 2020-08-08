package stjude.projects.jiyangyu;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class JiyangYuAppendOtherColumn {

	public static String type() {
		return "JIYANGYU";
	}
	public static String description() {
		return "For one of the project from Dr. Yu, we need to append the gene info";
	}
	public static String parameter_info() {
		return "[limmaFile] [miscInfo] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			HashMap geneName2proteinName = new HashMap();
			HashMap geneName2proteinDesc = new HashMap();
			HashMap geneName2peptide = new HashMap();
			
			String limmaFile = args[0];
			String miscInfo = args[1];
			String outputFile = args[2];
			FileInputStream fstream = new FileInputStream(miscInfo);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				split[1] = split[1].replaceAll(" ", "");
				geneName2proteinName.put(split[1], split[0]);
				geneName2proteinDesc.put(split[1], split[2]);
				geneName2peptide.put(split[1], split[3]);
				
			}
			in.close();
			

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);

			fstream = new FileInputStream(limmaFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));		
			String header = in.readLine();
			out.write(header + "\tproteinName\tproteinDescription\tnbPeptides\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				split[0] = split[0].replaceAll(" ", "");
				if (geneName2proteinName.containsKey(split[0])) {
					out.write(str + "\t" + geneName2proteinName.get(split[0]) + "\t" + geneName2proteinDesc.get(split[0]) + "\t" + geneName2peptide.get(split[0]) + "\n");
				} else {
					System.out.println(split[0]);
				}
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
