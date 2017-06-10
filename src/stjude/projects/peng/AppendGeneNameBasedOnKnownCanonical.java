package stjude.projects.peng;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class AppendGeneNameBasedOnKnownCanonical {

	
	public static String description() {
		return "Append the gene name based on the known canonical gene list.";
	}
	public static String type() {
		return "MISC";
	}
	public static String parameter_info() {
		return "[inputMatrixFile] [canonicalGeneList]";
	}
	
	public static void execute(String[] args) {
		
		
		try {
			
			String inputMatrixFile = args[0];
			String canonicalGeneList = args[1];
			HashMap map = new HashMap();
			FileInputStream fstream = new FileInputStream(canonicalGeneList);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));					
			while (in.ready()) {
				String str = in.readLine().trim();
				String[] split = str.split("\t");
				map.put(split[1], split[0]);
			}
			in.close();						
			
			fstream = new FileInputStream(inputMatrixFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			System.out.println("GeneSymbol\t" + header);
			while (in.ready()) {
				String str = in.readLine().trim();
				String[] split = str.split("\t");
				String geneName = (String)map.get(split[0]);
				System.out.println(geneName + "\t" + str);
			}
			in.close();						
			
					
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
