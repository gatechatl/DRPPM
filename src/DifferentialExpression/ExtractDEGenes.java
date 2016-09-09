package DifferentialExpression;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class ExtractDEGenes {

	public static String type() {
		return "DE";
	}
	public static String description() {
		return "Extract differentially expressed list from LIMMA (UP.txt, DN.txt, or DE.txt)";
	}
	public static String parameter_info() {
		return "[inputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));	
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				System.out.println(split[0].replaceAll("\"", ""));
			}
			in.close();
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
