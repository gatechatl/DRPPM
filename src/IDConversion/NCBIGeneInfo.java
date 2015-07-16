package IDConversion;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class NCBIGeneInfo {

	public static HashMap entrez2geneName = new HashMap();
	public static void initialize(String fileName) {
		
		try {
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split.length > 2) {
					entrez2geneName.put(split[1], split[2]);
				}
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
