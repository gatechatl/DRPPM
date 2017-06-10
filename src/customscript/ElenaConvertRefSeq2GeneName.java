package customScript;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class ElenaConvertRefSeq2GeneName {

	public static String type() {
		return "CUSTOM";
	}
	public static String description() {
		return "Elena's special gene list that needed to be converted to generic geneName";
	}
	public static String parameter_info() {
		return "[refseqList] [id_conversion_table]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String idConversionTable = args[1];
			
			HashMap map = new HashMap();
			FileInputStream fstream = new FileInputStream(idConversionTable);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String[] split3 = split[3].split(",");
				for (String split3_str: split3) {
					map.put(split3_str.split("\\.")[0], split[4]);
				}
			}
			in.close();
			
			
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split(",");
				if (map.containsKey(split[0])) {
					String geneName = (String)map.get(split[0]);
					System.out.println(str + "\t" + geneName);
				} else {
					System.out.println(str + "\t" + "null");
				}
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
