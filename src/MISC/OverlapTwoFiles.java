package MISC;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class OverlapTwoFiles {

	public static String parameter_info() {
		return "[file Name List] [file1]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile1 = args[0];	
			String inputFile2 = args[1];
			HashMap map = new HashMap();
			FileInputStream fstream = new FileInputStream(inputFile1);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				File f = new File(str);				
				map.put(f.getName(), "Not Found");				
			}
			in.close();			
				
			
			fstream = new FileInputStream(inputFile2);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));						
			while (in.ready()) {
				String str = in.readLine();
				Iterator itr = map.keySet().iterator();
				boolean found = false;
				while (itr.hasNext()) {
					String key = (String)itr.next();					
					if (str.contains(key)) {
						map.put(key, "Found");
					}
				}
				
			}
			in.close();			
			
			Iterator itr = map.keySet().iterator();			
			while (itr.hasNext()) {			
				String key = (String)itr.next();
				String found = (String)map.get(key);
				System.out.println(found + "\t" + key);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
