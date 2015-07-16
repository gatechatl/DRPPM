package WholeExonTools;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class CheckRecurrentGenes {

	public static void main(String[] args) {
		try {
			
			HashMap map = new HashMap();
			String inputFile = "C:\\Users\\tshaw\\Desktop\\EXONCAP\\Leventaki\\filter\\gene_list.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				int num = new Integer(split[1]);
				if (num > 1) {
					map.put(split[0], str);
				}
			}
			in.close();
			
			inputFile = "C:\\Users\\tshaw\\Desktop\\EXONCAP\\Leventaki\\filter\\SJALCL014728_D1_G1.txt";
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (map.containsKey(str)) {
					System.out.println(str + "\tfound\t" + map.get(str));
				} else {
					System.out.println(str + "\tnot_found\t" + map.get(str));
				}
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
