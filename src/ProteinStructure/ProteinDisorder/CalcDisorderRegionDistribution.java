package ProteinStructure.ProteinDisorder;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Given the disorder region calculate the 
 * @author tshaw
 *
 */
public class CalcDisorderRegionDistribution {

	public static String parameter_info() {
		return "[folderPath]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String folderPath = args[0];
			File f = new File(folderPath);
			
			File[] files = f.listFiles();
			LinkedList list = new LinkedList();
			for (File file: files) {
				int count = 0;
				FileInputStream fstream = new FileInputStream(file.getPath());
				DataInputStream din = new DataInputStream(fstream);
				BufferedReader in = new BufferedReader(new InputStreamReader(din));
				while (in.ready()) {
					String str = in.readLine();
					String[] split = str.split("\t");
					if (split[4].equals("Disorder_Predicted")) {
						count++;
					} else {
						if (count > 0) {
							list.add(count);
						}
						count = 0;
					}
				}
				if (count > 0) {
					list.add(count);
				}
				in.close();
			}
			
			System.out.println("Type\tDomainLength");
			Iterator itr = list.iterator();
			while (itr.hasNext()) {
				int value = (Integer)itr.next();
				System.out.println("Domain\t" + value);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
