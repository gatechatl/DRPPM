package stjude.projects.suzannebaker;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class CheckGMTCoverage {

	public static String type() {
		return "GMT";
	}
	public static String description() {
		return "Check gmt coverage for the geneset";
	}
	public static String parameter_info() {
		return "[inputMatrixFile] [gmtFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String gmtFile = args[1];
			HashMap map = new HashMap();
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(split[0], split[0]);
			}

			fstream = new FileInputStream(gmtFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				HashMap total = new HashMap();
				HashMap countHit = new HashMap();
				for (int i = 2; i < split.length; i++) {
					total.put(split[i], split[i]);
					if (map.containsKey(split[i])) {
						countHit.put(split[i], split[i]);
					}
				}
				System.out.println(split[0] + "\t" + countHit.size() + "\t" + total.size());
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
