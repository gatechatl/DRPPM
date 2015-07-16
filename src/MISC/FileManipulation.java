package MISC;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * Collection of tools for reading and counting lines in the file
 * @author tshaw
 *
 */
public class FileManipulation {

	public static int countLines(String inputFile) {
		int count = 0;
		try {
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();	
				count++;				
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}
}
