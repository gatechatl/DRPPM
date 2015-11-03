package UniprotAnnotation;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class ExtractUniprotInfo {

	public static String parameter_info() {
		return "[inputFile] [geneIndex] [uniprot_refFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			String inputFile = args[0];
			int geneIndex = new Integer(args[1]);
			String refFile = args[2];
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));		
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(split[geneIndex], "");				
			}
			in.close();
			
			
			fstream = new FileInputStream(refFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));		
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				
				if (map.containsKey(split[split.length - 1])) {
					System.out.println(str);
				}
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
