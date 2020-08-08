package rnaseq.pcpa;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;


public class PLA2BEDFile {

	public static void execute(String[] args) {
		
		try {
			String fileName = args[0];
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String chr = split[1];
				int start = new Integer(split[3]) - 100;
				int end = new Integer(split[3]) + 100;
				String gene = split[4];
				String direction = split[2];
				if (!gene.equals("n/a")) {
					System.out.println(chr.replaceAll("chr", "") + "\t" + start + "\t" + end + "\t" + gene + "\t0\t" + direction + "\t" + start + "\t" + end + "\t0\t1\t100,\t0,");
				}
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
