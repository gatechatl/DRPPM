package WholeExonTool.circos;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class Indel2CircosInput {
	public static String description() {
		return "Generate circos input based on SNV file";
	}
	public static String type() {
		return "CIRCOS";
	}
	public static String parameter_info() {
		return "[snvInputFile] [sampleName]";
	}
	public static void execute(String[] args) {
		
		try {
			HashMap map = new HashMap();
			String inputFile = args[0];
			String sampleName = args[1];
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			//String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (!split[0].equals("GeneName")) {
					String geneName = split[0];
					String chr = split[3].replaceAll("chr", "");
					String pos = split[4];
					String type = split[5];
					String color = "";
					if (type.equals("frameshift") || type.equals("proteinDel") || type.equals("proteinIns")) {
						color = "red";
					}
					double tumorReads = new Integer(split[9]);
					double normReads = new Integer(split[10]);
					if (split[1].equals("SJHQ") && split[2].equals(sampleName) && tumorReads >= 5 && (tumorReads / normReads) >= 0.1 && color.equals("red")) {
						if (!map.containsKey(geneName + "\t" + color)) {
							System.out.println("mm" + chr + "\t" + pos + "\t" + pos + "\t" + geneName + "\tcolor=" + color);
						}
						map.put(geneName + "\t" + color, "");
					}
				}
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
