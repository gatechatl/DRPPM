package MISC;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;

public class FilterColumns {

	public static void main(String[] args) {
		try {
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void execute(String[] args) {
		String fileName = args[0];
		String columnName = args[1];
		String outputFile = args[2];
		filterColumn(fileName, columnName, outputFile);
	}
	public static void filterColumn(String fileName, String columnFile, String outputFile) {
		try {
			LinkedList list = new LinkedList();
			FileInputStream fstream = new FileInputStream(columnFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {					
				String str = in.readLine();
				int num = new Integer(str.trim());
				list.add(num);
			}
			in.close();
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			boolean header = true;
			fstream = new FileInputStream(fileName);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {					
				String str = in.readLine();
				if (header) {
					str = str.replaceAll("\\.", "_");
				} 
				if (!str.trim().equals("")) {
					String[] split = str.split("\t");
					Iterator itr = list.iterator();
					boolean first = true;
					while (itr.hasNext()) {
						int num = (Integer)itr.next();
						if (first) {
							out.write(filter(split[num]));
						} else {
							out.write("\t" + filter(split[num]));
						}
						first = false;
					}
					out.write("\n");
				}
				header = false;
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	public static String filter(String str) {
		return str.trim().replaceAll("-", "").replaceAll(" ", "_");
	}
}
