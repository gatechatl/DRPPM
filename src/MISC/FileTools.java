package MISC;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;

public class FileTools {

	public static LinkedList readFileList(String inputFile) {
		
		LinkedList listFile = new LinkedList();
		try {
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (!str.equals("")) {
					listFile.add(str.trim());
				}
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listFile;
	}
}
