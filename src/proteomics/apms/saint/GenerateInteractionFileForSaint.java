package proteomics.apms.saint;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class GenerateInteractionFileForSaint {


	public static String parameter_info() {
		return "[inputMatrix] [baitFile] [proteinName_index] [begin_index: index where the values start]";
	}
	public static String type() {
		return "PPI";
	}
	public static String description() {		
		return "Generate the interaction file for SAINT";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputMatrix = args[0];
			String baitFile = args[1];
			int accession_index = new Integer(args[2]);
			HashMap map = new HashMap();
			//int geneName_index = new Integer(args[2]);
			int start_index = new Integer(args[3]);
			FileInputStream fstream = new FileInputStream(baitFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(split[0], split[1]);
			}
			in.close();
			fstream = new FileInputStream(inputMatrix);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				for (int i = start_index; i < split.length; i++) {
					if (map.containsKey(split_header[i])) {
						System.out.println(split_header[i] + "\t" + map.get(split_header[i]) + "\t" + split[accession_index] + "\t" + split[i]);
					}
				
				}
			}
			in.close();			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
