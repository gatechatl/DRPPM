package misc;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class GrabUniqValuesFromColumn {

	public static String type() {
		return "MISC";
	}
	public static String description() {
		return "From a tab-separated file, grab unique entries of the selected columns.";
	}
	public static String parameter_info() {
		return "[inputMatrixFile] [Columns 0,1,2]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String[] columnNumStr =args[1].split(",");
			int[] columns = new int[columnNumStr.length];
			for (int i = 0; i < columns.length; i++) {
				columns[i] = new Integer(columnNumStr[i]);
			}
			HashMap map = new HashMap();
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String line = split[columns[0]];
				for (int i = 1; i < columns.length; i++) {
					line += "\t" + split[i];
					
				}
				map.put(line, line);
			}
			in.close();
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String str = (String)itr.next();
				System.out.println(str);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
