package rnaseq.splicing.alternativejuncpipeline.intronretention;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class CountNumberOfUniqReads {

	public static void execute(String[] args) {
		try {
			
			String fileName = args[0];
			String flag = args[1];
			//HashMap map = new HashMap();
			double count = 0;
			String last_tag = "";
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (!split[3].equals(last_tag)) {
					double start = new Double(split[1]);
					double end = new Double(split[2]);
					if (flag.equals("true") || end - start <= 101) {
						count++;
					}
				}
				last_tag = split[3];
				//map.put(split[3], "");
			}
			in.close();
			System.out.println(fileName + "\t" + count);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
