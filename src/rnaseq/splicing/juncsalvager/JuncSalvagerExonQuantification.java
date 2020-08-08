package rnaseq.splicing.juncsalvager;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class JuncSalvagerExonQuantification {

	
	public static void execute(String[] args) {
		
		try {
			
			String fileList = args[0];
			String geneName = args[1];
			
			FileInputStream fstream = new FileInputStream(fileList);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				
				FileInputStream fstream2 = new FileInputStream(str);
				DataInputStream din2 = new DataInputStream(fstream2);
				BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
				while (in2.ready()) {
					String str2 = in2.readLine();
					
					
				}
				in2.close();
				
			}
			in.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
