package PhosphoTools.KinaseCentricReport;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class ActivityPhosphositeForAll {

	public static String parameter_info() {
		return "[inputFile] [Active]";
	}
	public static void execute(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			String inputFile = args[0];
			String active_flag = args[1];
			boolean active = false;
			if (active_flag.equals("TRUE")) {
				active = true;
			} else {
				active = false;
			}
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (active) {
					if (split[4].contains("activity") && split[4].contains("induced") && !split[4].contains("inhibited")) {
						System.out.println(str);
					}			
				} else {
					if (split[4].contains("activity") && !split[4].contains("induced") && split[4].contains("inhibited")) {
						System.out.println(str);
					}	
				}
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
