package ProteinStructure.ProteinDisorder;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class GrabUniqProteinMEME {

	public static void main(String[] args) {
		
		try {
			HashMap map = new HashMap();
			//String inputFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Projects\\PaulTaylor\\MotifDiscovery\\MEME\\Hit_In_GR_PR_DisorderRegion.txt";
			//String inputFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Projects\\PaulTaylor\\MotifDiscovery\\MEME\\Hit_In_All_GR_PR_DisorderRegion.txt";
			//String inputFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Projects\\PaulTaylor\\MotifDiscovery\\MEME\\Hit_In_GRPR_LCR.txt";
			//String inputFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Projects\\PaulTaylor\\MotifDiscovery\\MEME\\Hit_In_GRPR_ALL.txt";
			String inputFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Projects\\PaulTaylor\\MotifDiscovery\\MEME\\Hit_In_ALL_LCR.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(split[1], split[1]);
			}
			in.close();
			System.out.println(map.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
