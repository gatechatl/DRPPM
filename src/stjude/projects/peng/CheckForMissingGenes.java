package stjude.projects.peng;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class CheckForMissingGenes {

	
	public static String description() {
		return "Compare two gene lists.";
	}
	public static String type() {
		return "MISC";
	}
	public static String parameter_info() {
		return "[inputMatrixFile] [geneListFile: from PingChung]";
	}
	
	public static void execute(String[] args) {
		
		
		try {
			
			String inputMatrixFile = args[0];
			String geneListFile = args[1];
			HashMap map = new HashMap();
			FileInputStream fstream = new FileInputStream(inputMatrixFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));					
			while (in.ready()) {
				String str = in.readLine().trim();
				String[] split = str.split("\t");
				map.put(split[0].toUpperCase(), split[0]);
			}
			in.close();						
			
			HashMap exprList = new HashMap();
			fstream = new FileInputStream(geneListFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));					
			while (in.ready()) {
				String str = in.readLine().trim();
				String[] split = str.split("\t");
				if (!map.containsKey(split[0].toUpperCase())) {
					System.out.println(str);
				}
			}
			in.close();						
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
