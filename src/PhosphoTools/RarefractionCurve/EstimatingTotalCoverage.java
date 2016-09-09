package PhosphoTools.RarefractionCurve;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;

/**
 * Perform rarefaction estimation of protein coverage
 * Estimate the number of PSM required for complete coverage
 * @author tshaw
 *
 */
public class EstimatingTotalCoverage {

	public static void execute(String[] args) {
		
		try {
			
			String inputFiles = args[0];
			LinkedList list = new LinkedList();
			FileInputStream fstream = new FileInputStream(inputFiles);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				list.add(str);				
			}
			in.close();
			
			/*Iterator itr = list.iterator();
			fstream = new FileInputStream(fileName);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				
				
			}
			in.close();*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
