package stjude.projects.jpaultaylor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class MatchUniprotGeneName2GeneLCDLength {

	public static String type() {
		return "MISC";
	}
	public static String description() {
		return "Match UniprotGeneName2GeneLCDLength";
	}
	public static String parameter_info() {
		return "[proteinListFile] [geneLCDMaxLengthFile] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String proteinListFile = args[0];
			String geneLCDMaxLengthFile = args[1];
			String outputFile = args[2];
			
        	FileWriter fwriter = new FileWriter(outputFile);
            BufferedWriter out = new BufferedWriter(fwriter);
            
			HashMap map = new HashMap();
			FileInputStream fstream = new FileInputStream(geneLCDMaxLengthFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(split[2], split[0] + "\t" + split[1] + "\t" + split[2] + "\t" + split[3]);
			}
			in.close();
			out.write("UniprotID\tGeneName\tUniprotGeneName\tMaxLCDLength\tType\n");
			
			HashMap hit = new HashMap();
			fstream = new FileInputStream(proteinListFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();			
				hit.put(str, str);
				if (!map.containsKey(str)) {
					System.out.println("Missing: " + str);
				}
			}
			in.close();
			
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String uniprotGeneName = (String)itr.next();
				if (hit.containsKey(uniprotGeneName)) {
					String line = (String)map.get(uniprotGeneName);
					out.write(line + "\tUbiquitome\n");
				} else {
					String line = (String)map.get(uniprotGeneName);
					out.write(line + "\tOtherHumanProteome\n");
				}
			}
			out.close();
			//					String line = (String)map.get(str);
			//String[] split_line = line.split("\t");
			//out.write(split_line[0] + "\t" + split_line[1] + "\t" + split_line[2] + "\t" + split_line[3] + "\t" + "Ubiquitinome\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
