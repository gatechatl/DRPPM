package stjude.projects.mckinnon;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class McKinnonGenerateBlatBEDFile {

	public static String description() {
		return "McKinnon generate a blat based BED file.";
	}
	public static String type() {
		return "MCKINNON";
	}
	public static String parameter_info() {
		return "[inputLeftFile] [inputRightFile]";
	}
	public static void execute(String[] args) {
				
		try {
			
			HashMap map = new HashMap();
			String inputLeftFile = args[0];
			FileInputStream fstream = new FileInputStream(inputLeftFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			in.readLine();
			in.readLine();
			in.readLine();
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String name = split[9];
				String direction = split[8];
				String chr = split[13];
				int start = new Integer(split[15]);
				int end = new Integer(split[16]);
				map.put(name, direction + "\t" + chr + "\t" + start + "\t" + end);
				
			}
			in.close();
			
			int count_match = 0;
			String inputRightFile = args[1];
			fstream = new FileInputStream(inputRightFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			in.readLine();
			in.readLine();
			in.readLine();
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String name = split[9];
				String direction = split[8];
				String chr = split[13];
				int start = new Integer(split[15]);
				int end = new Integer(split[16]);
				int site = 0;
				if (direction.equals("+")) {
					site = start + 6;
				} else {
					site = end - 6;
				}
				if (map.containsKey(name)) {
					String prev = (String)map.get(name);
					String[] split_prev = prev.split("\t");
					String prev_direction = split_prev[0];
					String prev_chr = split_prev[1];
					int prev_start = new Integer(split_prev[2]);
					int prev_end = new Integer(split_prev[3]);
					int prev_site = 0;
					if (prev_direction.equals("+")) {
						prev_site = prev_start + 11;
					} else {
						prev_site = prev_end + 11;
					}
					if (prev_direction.equals(direction) && prev_chr.equals(chr)) {
						if (prev_site > site) {
							System.out.println(chr + "\t" + site + "\t" + prev_site + "\t" + name + "\t" + 0 + "\t" + direction);
						} else {
							System.out.println(chr + "\t" + prev_site + "\t" + site + "\t" + name + "\t" + 0 + "\t" + direction);
						}
					}
				}								
			}
			in.close();
			
			System.out.println(count_match);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
