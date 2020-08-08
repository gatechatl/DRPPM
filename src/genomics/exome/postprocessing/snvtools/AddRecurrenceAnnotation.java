package genomics.exome.postprocessing.snvtools;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class AddRecurrenceAnnotation {

	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0]; //
			String mutFreqFile = args[1];
			HashMap map = readMutationFreq(mutFreqFile);
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			System.out.println(header + "\tRecurrentFreq\tRecurrentSample");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (map.containsKey(split[0])) {
					String stuff = (String)map.get(split[0]);
					System.out.println(str + "\t" + stuff);
				} else {
					System.out.println(str + "\t0\tNA");
				}
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static HashMap readMutationFreq(String inputFile) {
		HashMap map = new HashMap();
		try {
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(split[0], split[1] + "\t" + split[2]);
				
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}
