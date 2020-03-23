package stjude.projects.jinghuizhang.dexseq.exon.cart.candidate;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class JinghuiZhangIdentify3primeExon {

	
	public static void main(String[] args) {
		
		try {
			
			String inputFile = "";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
