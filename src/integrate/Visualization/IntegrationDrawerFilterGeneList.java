package integrate.Visualization;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class IntegrationDrawerFilterGeneList {

	public static String type() {
		return "INTEGRATION";
	}
	public static String description() {
		return "Filter the gene from the input file";
	}
	public static String parameter_info() {
		return "[inputFile] [geneListFile] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {

			String inputFile = args[0];
			String geneListFile = args[1];
			String outputFile = args[2];
			
			File f = new File(outputFile);
			if (f.exists()) {
				System.out.println(outputFile + " already exists.");
				System.exit(0);
			}
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			HashMap map = new HashMap();
			FileInputStream fstream = new FileInputStream(geneListFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneName = split[0];
				map.put(geneName, geneName);
			}
			in.close();
			
			
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			out.write(in.readLine() + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneName = split[0].split("_")[0];
				if (map.containsKey(geneName)) {
					out.write(str + "\n");
				}
			}			
			in.close();
			out.close();
						
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
