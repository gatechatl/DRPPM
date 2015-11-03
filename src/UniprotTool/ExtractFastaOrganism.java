package UniprotTool;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class ExtractFastaOrganism {

	public static String parameter_info() {
		return "[inputFasta] [organism]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String organism = args[1];
			boolean write = false;
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine().trim();
				if (str.contains(">")) {
					if (str.replaceAll(" ", "_").contains("OS=" + organism)) {
						write = true;
					} else {
						write = false;
					}
				}
				if (write) {
					System.out.println(str);
				}
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
