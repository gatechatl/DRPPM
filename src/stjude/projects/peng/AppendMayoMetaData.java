package stjude.projects.peng;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class AppendMayoMetaData {

	public static String description() {
		return "Append the mayo clinic metainformation.";
	}
	public static String type() {
		return "MISC";
	}
	public static String parameter_info() {
		return "[IRFile] [metaInfoFile]";
	}
	
	public static void execute(String[] args) {
		
		try {
			String IRFile = args[0];
			String metaInfoFile = args[1];
			HashMap map = new HashMap();
			FileInputStream fstream = new FileInputStream(metaInfoFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));					
			while (in.ready()) {
				String str = in.readLine().trim();
				String[] split = str.split("\t");
				map.put(split[0], str);				
			}
			in.close();						
			
			fstream = new FileInputStream(IRFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			System.out.println(header + "ID	Source	Tissue	RIN	Diagnosis	Gender	AgeAtDeath	ApoE	FLOWCELL	PMI");
			while (in.ready()) {
				String str = in.readLine().trim();
				String[] split = str.split("\t");
				String name = split[0].split("\\.")[0];
				if (map.containsKey(name)) {
					String meta_info = (String)map.get(name);
					String[] split_meta = meta_info.split("\t");
					System.out.println(str + "\t" + meta_info);
				} else {
					System.out.println("Missing: " + name);
				}
			}
			in.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
