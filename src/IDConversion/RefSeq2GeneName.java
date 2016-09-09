package IDConversion;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class RefSeq2GeneName {

	public static String description() {
		return "RefSeq to geneName conversion";
	}
	public static String type() {
		return "IDCONVERSION";
	}
	public static String parameter_info() {
		return "[refFlatFile] [cosmicFile]";
	}
	public static void execute(String[] args) {
		
		try {

			String refFlatFile = args[0];
			String cosmicFile = args[1];
			HashMap map = new HashMap();
			FileInputStream fstream = new FileInputStream(refFlatFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(split[1], split[0]);
			}
			in.close();
			
			HashMap geneList = new HashMap();
			fstream = new FileInputStream(cosmicFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (map.containsKey(split[0])) {
					String geneName = (String)map.get(split[0]);
					geneList.put(geneName, geneName);
				}
			}
			in.close();
			
			Iterator itr = geneList.keySet().iterator();
			while (itr.hasNext()) {
				String geneName = (String)itr.next();
				System.out.println(geneName);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
