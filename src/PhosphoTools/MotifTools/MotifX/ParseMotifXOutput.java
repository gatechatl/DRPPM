package PhosphoTools.MotifTools.MotifX;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Output the motif result
 * @author tshaw
 *
 */
public class ParseMotifXOutput {
	public static void execute(String[] args) {
		
		try {
			String inputFileList = args[0];
			String outputFile = args[1];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			LinkedList list = getFileList(inputFileList);
			Iterator itr = list.iterator();
			while (itr.hasNext()) {
				String inputFile = (String)itr.next();			
				FileInputStream fstream = new FileInputStream(inputFile);
				DataInputStream din = new DataInputStream(fstream);
				BufferedReader in = new BufferedReader(new InputStreamReader(din));
				while (in.ready()) {
					String str = in.readLine().trim();
					if (str.equals("Increase")) {
						boolean continue_flag = true;
						while (in.ready() && continue_flag) {
							str = in.readLine();
							String[] split = str.split("\t");													
							if (str.equals("")) {
								continue_flag = false;
							} else {
								
								out.write(str + "\n");
							}
						}
					}
				}
				in.close();
							
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static LinkedList getFileList(String fileName) {
		LinkedList list = new LinkedList();
		
		try {
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine().trim();
				if (!str.equals("")) {
					list.add(str.trim());
				}
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}
