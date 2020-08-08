package stjude.projects.peng;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class JunminPengColoriPSDConnections {

	public static String description() {
		return "Remove Modules highlighting iPSD connections";
	}
	public static String type() {
		return "EXPRESSION";
	}
	public static String parameter_info() {
		return "[inputEdgeFile] [nodeName] [newColor]";
	}
	
	public static void execute(String[] args) {
		
		try {
			
			String inputEdgeFile = args[0];
			String nodeName = args[1];
			String newColor = args[2];
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
			
			fstream = new FileInputStream(inputEdgeFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			//String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (!(split[0].equals(nodeName) || split[2].equals(nodeName))) {
					if (map.containsKey(split[0]) && map.containsKey(split[2])) {
						System.out.println(split[0] + "\t" + split[1] + "\t" + split[2] + "\t" + 20 + "\t" + newColor + "\t" + split[5] + "\t" + split[6] + "\t" + split[7]);
						
					} else {
						System.out.println(str);
					}
				}
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
