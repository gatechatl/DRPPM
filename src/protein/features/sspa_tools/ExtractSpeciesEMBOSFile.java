package protein.features.sspa_tools;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class ExtractSpeciesEMBOSFile {

	public static String parameter_info() {
		return "[inputFile] [Species]";
	}
	public static void execute(String[] args) {
		
		try {
			String inputFile = args[0];
			String species = args[1];
			boolean write = false;
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine().trim();
				if (str.length() >= 2) {
					if (str.substring(0, 2).equals("ID")) {
						if (str.contains("_" + species)) {
							write = true;
						}
					}
					
					if (write) {
						System.out.println(str);
					}
					if (str.substring(0, 2).equals("//")) {
						write = false;
					}
				}
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
