package stjude.projects.hongbochi.phosphoanalysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class WGCNAModifyShape {

	public static String parameter_info() {
		return "[inputFile] [kinaseListFile] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			HashMap kinase = new HashMap();
			String inputFile = args[0];
			String kinaseListFile = args[1];
			String outputFile = args[2];
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);	
			FileInputStream fstream = new FileInputStream(kinaseListFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				kinase.put(split[0], split[0]);
				
			}
			in.close();
			
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write(header + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (kinase.containsKey(split[0])) {
					out.write(split[0] + "\t" + split[1] + "\t" + split[2] + "\t" + split[3] + "\t" + split[4] + "\t" + split[5] + "\thexagon\t" + split[7] + "\n");
				} else {
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
