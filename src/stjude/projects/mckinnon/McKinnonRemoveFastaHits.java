package stjude.projects.mckinnon;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class McKinnonRemoveFastaHits {


	public static String description() {
		return "McKinnon output fasta without blat hits.";
	}
	public static String type() {
		return "MCKINNON";
	}
	public static String parameter_info() {
		return "[inputFile] [fastaFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			String inputFile = args[0];
			String fastaFile = args[1];
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split.length > 9) {
					map.put(split[9], split[9]);
				}
			}
			in.close();			
			
			fstream = new FileInputStream(fastaFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String title = in.readLine();
				String fasta = in.readLine();
				if (!map.containsKey(title.replaceAll(">", ""))) {
					System.out.println(title + "\n" + fasta);
									
				}
			}
			in.close();			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
