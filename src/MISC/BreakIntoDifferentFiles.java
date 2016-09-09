package MISC;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class BreakIntoDifferentFiles {

	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				
			}
			in.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
