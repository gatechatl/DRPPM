package GSEATools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class ConvertGSEAHuman2Mouse {

	public static void main(String[] args) {
		
		try {
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void execute(String[] args) {
		
		try {
			
			String hs2mmFile = args[0];
			HashMap human2mouse = human2mouse(hs2mmFile);
			String inputFile = args[1];
			String outputFile = args[2];
        	FileWriter fwriter = new FileWriter(outputFile);
            BufferedWriter out = new BufferedWriter(fwriter);
            
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				out.write(split[0] + "\t" + split[1]);
				for (int i = 2; i < split.length; i++) {
					if (human2mouse.containsKey(split[i])) {
						out.write("\t" + (String)human2mouse.get(split[i]));
					}
				}
				out.write("\n");
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static HashMap human2mouse(String hs_mm_homo_r66) {
		HashMap human2mouse = new HashMap();
		try {
			
			String fileName = hs_mm_homo_r66;
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				human2mouse.put(split[0], split[1]);
			}
			in.close();
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return human2mouse;
	}
	public static HashMap mouse2human(String hs_mm_homo_r66) {
		HashMap mouse2human = new HashMap();
		try {
			
			String fileName = hs_mm_homo_r66;
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				mouse2human.put(split[1], split[0]);
			}
			in.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return mouse2human;
	}
}
