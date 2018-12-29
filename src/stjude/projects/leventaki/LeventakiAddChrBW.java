package stjude.projects.leventaki;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class LeventakiAddChrBW {


	public static String description() {
		return "Append chr to bigwig";
	}
	public static String type() {
		return "LEVENTAKI";
	}
	public static String parameter_info() {
		return "[inputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String chrSizeFile = args[1];
			HashMap chrName = new HashMap();
			FileInputStream fstream = new FileInputStream(chrSizeFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din)); 			
			while (in.ready()) {
				String str = in.readLine().replaceAll("\"", "");
				String[] split = str.split("\t");
				chrName.put(split[0], split[0]);
				
			}
			in.close();			
			FileWriter fwriter = new FileWriter(inputFile + ".addchr.bedgraph");
			BufferedWriter out = new BufferedWriter(fwriter);
			
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din)); 			
			while (in.ready()) {
				String str = in.readLine().replaceAll("\"", "");
				String[] split = str.split("\t");
				if (chrName.containsKey("chr" + split[0])) {
					out.write("chr" + str + "\n");
				}
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
