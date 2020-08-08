package genomics.exome.circos;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class FromSV2CircosInput {

	public static String description() {
		return "Structural Variation to Circos Input File";
	}
	public static String type() {
		return "CIRCOS";
	}
	public static String parameter_info() {
		return "[inputFile] [sampleName] [organism]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String sampleName = args[1];
			String organism = args[2];
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				
				if (!split[0].equals("sample") && split.length >= 30) {
					String tag = "";
					if (organism.toUpperCase().equals("MOUSE")) {
						tag = "mm";
					} else {
						tag = "hs";
					}
					String chrA = split[2].replaceAll("chr", tag);
					int posA = new Integer(split[3]);
					String chrB = split[7].replaceAll("chr", tag);
					int posB = new Integer(split[8]);
					String type = split[29];
					String type2 = type;
					if (split.length > 30) {
						type2 = split[30];
					}
					String eventA = split[5];
					String eventB = split[10];
					if (split[0].contains(sampleName) && (type.equals("HQ") || type2.equals("HQ")) && !eventA.equals("intergenic") && !eventB.equals("intergenic")) {
						if (chrA.equals(chrB)) {
							System.out.println(chrA + " " + (posA) + " " + (posA + 10) + " " + chrB + " " + (posB) + " " + (posB + 10) + " color=green,thickness=15p");
						} else {
							System.out.println(chrA + " " + (posA) + " " + (posA + 10) + " " + chrB + " " + (posB) + " " + (posB + 10) + " color=purple,thickness=15p");
						}
					}
				}
			}
			in.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
