package misc;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class generateScript {

	public static void main(String[] args) {
		
		try {
			String fileName = "C:\\Users\\tshaw\\Desktop\\RNASEQ\\heatmap3.R";
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				str = str.replaceAll("\t", " ").trim();
				str = str.replaceAll("\"", "\\\\\"");
				str = "script += \"" + str + "\\n\";";
				System.out.println(str);
			}
			in.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
