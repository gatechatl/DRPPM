package stjude.projects.peng;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class JunminPengRemoveModuleHighlightiPSDConnections {
	
	public static String description() {
		return "Remove Modules highlighting iPSD connections";
	}
	public static String type() {
		return "EXPRESSION";
	}
	public static String parameter_info() {
		return "[inputEdgeFile] [inputNodeFile] [nodeName] [newColor]";
	}
	
	public static void execute(String[] args) {
		
		try {
			
			String inputEdgeFile = args[0];
			String inputNodeFile = args[1];
			String nodeName = args[2];
			String newColor = args[3];
			HashMap map = new HashMap();
			FileInputStream fstream = new FileInputStream(inputEdgeFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			//String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split[0].equals(nodeName)) {
					map.put(split[2], split[2]);
				}
				if (split[2].equals(nodeName)) {
					map.put(split[0], split[0]);
				}
			}
			in.close();
			
			fstream = new FileInputStream(inputNodeFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			//String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (map.containsKey(split[0])) {
					String newStr = "";
					for (int i = 0; i < split.length; i++) {
						if (newStr.equals("")) {
							newStr = split[0];
						} else {
							newStr += "\t" + split[i];
						}
					}
					System.out.println(split[0] + "\t" + split[1] + "\t" + split[2] + "\t" + newColor + "\t" + split[4] + "\t" + split[5] + "\t" + split[6] + "\t" + split[7] + "\t" + split[8]);
				} else {
					System.out.println(str);
				}				
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
