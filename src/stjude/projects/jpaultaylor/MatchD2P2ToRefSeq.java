package stjude.projects.jpaultaylor;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class MatchD2P2ToRefSeq {

	
	public static void execute(String[] args) {
		
		try {
			
			String refSeqFasta = args[0];
			
			HashMap sequence_map = new HashMap();
			String header = "";
			FileInputStream fstream = new FileInputStream(refSeqFasta);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains(">")) {
					header = str;
				} else {
					if (sequence_map.containsKey(header)) {
						String line = (String)sequence_map.get(header);
						line += str.trim();
						sequence_map.put(header, line);
					} else {
						sequence_map.put(header, str.trim());
					}
				}
			}
			in.close();

			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
