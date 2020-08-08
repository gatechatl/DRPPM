package mathtools.expressionanalysis.differentialexpression;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Identify the genes that are overlapping between the two comparison
 * @author tshaw
 *
 */
public class CombineLIMMAResults {

	public static void execute(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			
			String inputFile = args[0];
			String inputFile2 = args[1];
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(split[0], str);
			}
			in.close();
			
			
			HashMap map2 = new HashMap();
			FileInputStream fstream2 = new FileInputStream(inputFile2);
			DataInputStream din2 = new DataInputStream(fstream2);
			BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map2.put(split[0], str);
			}
			in.close();
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String geneName = (String)itr.next();
				if (map2.containsKey(geneName)) {
					String line = (String)map.get(geneName);
					String[] split1 = line.split("\t");
					String line2 = (String)map2.get(geneName);
					String[] split2 = line2.split("\t");
					
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
