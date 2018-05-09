package stjude.projects.peng;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class CompareModule0ToOthers {


	public static String description() {
		return "Compare module 0 to other modules (must have one module called MODULE_0";
	}
	public static String type() {
		return "MISC";
	}
	public static String parameter_info() {
		return "[inputModules]";
	}
	public static void execute(String[] args) {
		
		try {
			
			HashMap module0 = new HashMap();
			String inputFile = args[0];
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));					
			while (in.ready()) {
				String str = in.readLine().trim().toUpperCase();
				String[] split = str.split("\t");
				if (split[0].equals("MODULE_0")) {
					String[] split2 = split[1].split(",");
					for (String genes: split2) {
						module0.put(genes, genes);
					}
				}						
			}
			in.close();
			
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));					
			while (in.ready()) {
				String str = in.readLine().trim().toUpperCase();
				String[] split = str.split("\t");
				String hits = "";
				String[] split2 = split[1].split(",");
				for (String genes: split2) {
					
					if (module0.containsKey(genes)) {
						hits += genes + ",";
					}
				}
				int len = hits.split(",").length;
				if (!hits.contains(",")) {
					len = 0;
				}
				System.out.println(str + "\t" + len + "\t" + hits);					
			}
			in.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();;
		}
	}
}
