package mathtools.expressionanalysis.differentialexpression;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class GrabSampleNameWithKeyword {
	public static String type() {
		return "DE";
	}
	public static String description() {
		return "Extract samples that matches the input terms";
	}
	public static String parameter_info() {
		return "[inputMatrixFile] [terms split by ',']";
	}
	public static void execute(String[] args) {
		try {
			
			String inputFile = args[0];
			
			String[] terms = args[1].split(",");
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String[] header = in.readLine().split("\t");
			for (int i = 1; i < header.length; i++) {
				boolean find = false;
				for (String term: terms) {
					if (header[i].contains(term)) {
						find = true;
					}
				}
				if (find) {
					System.out.println(header[i]);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
