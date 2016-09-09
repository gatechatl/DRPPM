package WholeExonTools.OverlapExternalDB;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class FilterFailedCoordinates {

	public static void main(String[] args) {
		
		try {
			String rawFile = "C:\\Users\\tshaw\\Desktop\\EXONCAP\\McKinnon\\CosmicCounting\\mm9_coordinates.txt";
			String inputFile = "C:\\Users\\tshaw\\Desktop\\EXONCAP\\McKinnon\\CosmicCounting\\bad_mm9_coordinates.txt";
			String outputFile = "C:\\Users\\tshaw\\Desktop\\EXONCAP\\McKinnon\\CosmicCounting\\good_mm9_coordinates.txt";
			HashMap map = new HashMap();
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				map.put(str, str);
				
			}
			in.close();
			

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);

			fstream = new FileInputStream(rawFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (!map.containsKey(str)) {
					out.write(str + "\n");
				}
				
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
