package stjude.projects.mckinnon;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Random;

public class McKinnonGenerateRandomBEDFile {

	public static String description() {
		return "Generate a random BED file for calculating the background";
	}
	public static String type() {
		return "MCKINNON";
	}
	public static String parameter_info() {
		return "[inputBEDFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String bedFile = args[0];
			
			HashMap chr_size = new HashMap();
			FileInputStream fstream = new FileInputStream(bedFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));		
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				int current_size = new Integer(split[2]);
				if (chr_size.containsKey(split[0])) {
					int size = (Integer)chr_size.get(split[0]);
					if (size < current_size) {
						chr_size.put(split[0], current_size);
					}
				} else {
					chr_size.put(split[0], new Integer(split[2]));
				}
			}
			in.close();

			fstream = new FileInputStream(bedFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));		
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				int len = new Integer(split[2]) - new Integer(split[1]);
				int max_size = (Integer)chr_size.get(split[0]);
				Random rand = new Random();
				int rand_num = rand.nextInt(max_size);
				if (rand_num < len) {
					rand_num = len + 100;
				}
				System.out.println(split[0] + "\t" + (rand_num - len) + "\t" + (rand_num + 1));
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
