package sequencing.tools.bedmanupulation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * For running bedgraphs
 * @author tshaw
 *
 */
public class BedGraphFilterChromosomeName {

	public static String type() {
		return "MISC";
	}
	public static String description() {
		return "Specialized class for filtering chromosome name";
	}
	public static String parameter_info() {
		return "[inputBedGraphFile] [chromSize]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputBedgraphFile = args[0];
			String chromosomeFile = args[1];
			HashMap chr = new HashMap();
			FileInputStream fstream = new FileInputStream(chromosomeFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				chr.put(split[0], split[0]);				
			}
			in.close();
			
			fstream = new FileInputStream(inputBedgraphFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (chr.containsKey(split[0])) {
					System.out.println(str);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
