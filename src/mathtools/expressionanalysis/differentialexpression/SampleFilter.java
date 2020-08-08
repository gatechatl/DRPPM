package mathtools.expressionanalysis.differentialexpression;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * This will remove certain sample from the marix
 * Function assumes first col is gene name and first row is header
 * 
 * @author tshaw
 *
 */
public class SampleFilter {

	public static void execute(String[] args) {
		try {
			
			String inputFile = args[0];
			String[] terms = args[1].split(",");
			String outputFile = args[2];
			String contains = args[3];
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);						
			
			LinkedList list = new LinkedList();
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String[] header = in.readLine().split("\t");
			for (int i = 0; i < header.length; i++) {
				if (i == 0) {
					out.write(header[i]);
				} else {
					boolean find = false;
					for (String term: terms) {
						if (header[i].contains(term)) {
							find = true;
							
						}
					}
					if (contains.equals("yes")) {											
						if (find) {
							list.add(i);
							out.write("\t" + header[i]);
							System.out.println(header[i]);
						}
					} else {
						if (!find) {
							list.add(i);
							out.write("\t" + header[i]);
							System.out.println(header[i]);
						}
					}
				}
			}
			out.write("\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				out.write(split[0]);
				Iterator itr = list.iterator();
				while (itr.hasNext()) {
					int key = (Integer)itr.next();
					out.write("\t" + split[key]);
				}
				out.write("\n");
			}
			out.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
