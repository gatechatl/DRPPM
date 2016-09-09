package Integration.summarytable;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class FilterSNVSamples {

	public static String description() {
		return "Filter the SNV table based on a sample list";
	}
	public static String type() {
		return "MISC";
	}
	public static String parameter_info() {
		return "[sampleList] [inputFile]";
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
				map.put(str, str);
			}
			in.close();			
			
			
			fstream = new FileInputStream(inputFile2);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String sample = split[2];
				if (map.containsKey(sample)) {
					System.out.println(str);
				}
			}
			in.close();			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
