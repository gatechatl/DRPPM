package PeptidePeaksIonDrawerPipeline;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class JUMPGenerateDTAFileScript {

	public static void execute(String[] args) {
		
		try {
			
			String fileName = args[0];
			int name_idx = new Integer(args[1]);
			int scan_number_idx = new Integer(args[2]);
			int charge_idx = new Integer(args[3]);
			String program = args[4];
			String path = args[5];
			String outputFolder = args[6];
						
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String name = split[name_idx];
				String scan_number = split[scan_number_idx];
				String charge = split[charge_idx];
				String id = name + "." + scan_number + "." + 1 + "." + charge;
					System.out.println("perl " + program + " " + path + " " + outputFolder + " " + id);
			}
			in.close();						
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
