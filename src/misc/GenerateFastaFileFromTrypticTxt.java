package misc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class GenerateFastaFileFromTrypticTxt {

	public static void execute(String[] args) {
		
		HashMap map = new HashMap();
		try {
			
			String outputFile = args[1];
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			String inputFile = args[0];
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine().trim();
				if (!str.equals("")) {
					map.put(str, str);
				}
				
			}
			in.close();
			
			int count = 1;
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String str = (String)itr.next();
				out.write(">" + count + "\n" + str + "\n");
				count++;
			}			
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
