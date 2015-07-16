package Preprocessing;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class ExtractRawData {

	public static void main(String[] args) {
		
		try {
			String fileName = args[0];
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
