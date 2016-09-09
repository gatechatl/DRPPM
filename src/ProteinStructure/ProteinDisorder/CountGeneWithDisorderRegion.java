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
public class CountGeneWithDisorderRegion {

	public static String parameter_info() {
		return "[folderPath] [cutoff]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String folderPath = args[0];
			int cutoff = new Integer(args[1]);
			File f = new File(folderPath);
			double total = 0;
			double satisfy = 0;
			File[] files = f.listFiles();
			LinkedList list = new LinkedList();
			for (File file: files) {
				int max = 0;
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
							if (max < count) {
								max = count;
							}
						}
						count = 0;
					}
				}
				if (count > 0) {
					if (max < count) {
						max = count;
					}
					list.add(count);
				}
				in.close();
				total++;
				boolean satisfy_flag = false;
				if (max > cutoff) {
					satisfy++;
					satisfy_flag = true;
				}
				System.out.println(file.getName() + "\t" + satisfy_flag + "\t" + max);
			}
			
			System.out.println("PercentageOfGene:" + (satisfy / total));			
			/*Iterator itr = list.iterator();
			while (itr.hasNext()) {
				int value = (Integer)itr.next();
				System.out.println("Domain\t" + value);
			}*/
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

