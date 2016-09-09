package RNAseqTools.CICERO;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class SummaryCICEROResult {

	public static String parameter_info() {
		return "";
	}
	public static void execute(String[] args) {
		
		try {
			
			
			HashMap map = new HashMap();
			String inputFile = args[0];
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				
			}
			in.close();
						
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
