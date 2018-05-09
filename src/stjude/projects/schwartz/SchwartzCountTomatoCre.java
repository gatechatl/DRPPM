package stjude.projects.schwartz;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class SchwartzCountTomatoCre {

	public static String description() {
		return "Count Tomato and Cre";
	}
	public static String type() {
		return "SCHWARTZ";
	}
	public static String parameter_info() {
		return "[inputSAMFile]";
	}
	
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			
			HashMap tomato = new HashMap();
			HashMap Dbh = new HashMap();
			HashMap TH = new HashMap();
			HashMap all = new HashMap();
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));									
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String tag = split[2];
				if (split[0].contains("CB:Z:")) {
					String name = split[0].split("CB:Z:")[1];
					if (tag.contains("tdTomato.dna") || tag.contains("CRE")) {
						all.put(name, name);
						if (tomato.containsKey(name)) {
							int count = (Integer)tomato.get(name);
							count = count + 1;
							tomato.put(name, count);
						} else {
							tomato.put(name, 1);
						}
					}
					if (tag.contains("TH")) {
						all.put(name, name);
						if (TH.containsKey(name)) {
							int count = (Integer)TH.get(name);
							count = count + 1;
							TH.put(name, count);
						} else {
							TH.put(name, 1);
						}
					}
					if (tag.contains("Dbh")) {
						all.put(name, name);
						if (Dbh.containsKey(name)) {
							int count = (Integer)Dbh.get(name);
							count = count + 1;
							Dbh.put(name, count);
						} else {
							Dbh.put(name, 1);
						}
					}
				}
				
			} 
			in.close();
			
			System.out.println("Sample\tTomato\tDbh\tTh");
			
			Iterator itr = all.keySet().iterator();
			while (itr.hasNext()) {
				String name = (String)itr.next();
				int tomato_count = 0;
				int dbh_count = 0;
				int th_count = 0;
				if (tomato.containsKey(name)) {
					tomato_count = (Integer)tomato.get(name);
				}
				if (Dbh.containsKey(name)) {
					dbh_count = (Integer)Dbh.get(name);
				}
				if (TH.containsKey(name)) {
					th_count = (Integer)TH.get(name);
				}
				
				System.out.println(name + "\t" + tomato_count+ "\t" + dbh_count + "\t" + th_count);
			}
		} catch (Exception e) {
			e.printStackTrace();;
		}
	}
}
