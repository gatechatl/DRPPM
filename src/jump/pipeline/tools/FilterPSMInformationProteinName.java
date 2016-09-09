package jump.pipeline.tools;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class FilterPSMInformationProteinName {

	public static String description() {
		return "Filter PSM raw table with ProteinName";
	}
	public static String type() {
		return "JUMP";
	}
	public static String parameter_info() {
		return "[inputFile] [proteinName]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String proteinName = args[1];
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split(";");				
				if (split[1].equals(proteinName)) {
					System.out.println(str.replaceAll(";","\t"));
				}
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
