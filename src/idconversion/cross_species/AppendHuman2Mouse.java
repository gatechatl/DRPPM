package idconversion.cross_species;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class AppendHuman2Mouse {

	public static String type() {
		return "IDCONVERT";
	}
	public static String description() {
		return "Convert human 2 mouse";
	}
	public static String parameter_info() {
		return "[inputFile] [hs2mmFile]";
	}
	public static void execute(String[] args) {				
		try {
			
			String hs2mmFile = args[1]; //"C:\\Users\\tshaw\\Desktop\\RNASEQ\\hs_mm_homo_r66.txt";
			HashMap human2mouse = HumanMouseGeneNameConversion.human2mouse(hs2mmFile);
			String inputFile = args[0]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\GangGeneList\\GangGeneListAnalysis.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			System.out.println(in.readLine());
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				/*String data = "";
				for (int i = 1; i < split.length; i++) {
					data += "\t" + split[i];
				}*/
				if (human2mouse.containsKey(split[0])) {
					System.out.println(human2mouse.get(split[0]) + "\t" + str);
				} else {
					System.out.println("NA\t" + str);
				}
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
