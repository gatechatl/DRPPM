package ProteinFeature.LowComplexityDomain;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class GenerateSEGSampleGroup {

	public static String parameter_info() {
		return "[list.txt] [allprotein]";
	}
	public static void execute(String[] args) {
		
		try {
			HashMap map = new HashMap();
			String inputFile = args[0];
			String inputFile2 = args[1];
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(split[2], split[0]);
			}
			in.close();
			
			
			fstream = new FileInputStream(inputFile2);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (map.containsKey(split[2])) {
					System.out.println(map.get(split[2]) + "\t" + split[1] + "\t" + split[2]);
				} else {
					System.out.println(str);
				}
				
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
