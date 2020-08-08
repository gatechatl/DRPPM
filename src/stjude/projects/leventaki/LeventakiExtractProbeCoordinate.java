package stjude.projects.leventaki;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class LeventakiExtractProbeCoordinate {

	public static String description() {
		return "Extract probe coordinates";
	}
	public static String type() {
		return "LEVENTAKI";
	}
	public static String parameter_info() {
		return "[inputFolder] [refFile] [query_chr] [query_start_position] [query_end_position]";
	}
	public static void execute(String[] args) {
		
		
		try {
			
			HashMap map = new HashMap();
			String inputMatrixFile = args[0];
			String refFile = args[1];
			String query_chr = args[2];
			int query_start_position = new Integer(args[3]);
			int query_end_position = new Integer(args[4]);
			FileInputStream fstream = new FileInputStream(refFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din)); 			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split(",");
				if (split.length > 30) {
					String id = split[0];
					String type = split[8];
					String chr = split[11];
					if (!chr.contains("chr")) {
						chr = "chr" + chr;
					}
					String loc = split[12];
					if (query_chr.equals(chr)) {
						if (query_start_position <= new Integer(loc) && new Integer(loc) <= query_end_position) {
							map.put(id, id);
						}
					}										
				}
			}
			in.close();
			
			fstream = new FileInputStream(inputMatrixFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			System.out.println(in.readLine());
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (map.containsKey(split[0])) {
					System.out.println(str);
				}
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
