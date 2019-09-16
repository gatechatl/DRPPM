package proteomics.phospho.tools.kinase.report;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class DegradationPhosphositeRegForAll {

	public static String description() {
		return "Extract phosphosites with degradation or stabilization annotation";
	}
	public static String type() {
		return "PHOSPHOSITE ANNOTATION";
	}
	
	public static String parameter_info() {
		return "[inputFile] [degradation flag TRUE/FALSE]";
	}
	public static void execute(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			String inputFile = args[0];
			String active_flag = args[1];
			boolean active = false;
			if (active_flag.equals("TRUE")) {
				active = true;
			} else {
				active = false;
			}
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (active) {
					if (split[4].contains("protein degradation") && !split[4].contains("protein stabilization")) {
						System.out.println(str);
					}			
				} else {
					if (split[4].contains("protein stabilization") && !split[4].contains("protein degradation")) {
						System.out.println(str);
					}	
				}
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
