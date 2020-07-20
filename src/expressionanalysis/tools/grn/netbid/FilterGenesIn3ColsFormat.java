package expressionanalysis.tools.grn.netbid;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * After the 3col format was generated, filter for the subnetwork
 * @author tshaw
 *
 */
public class FilterGenesIn3ColsFormat {

	public static void execute(String[] args) {
		
		try {
			
			
			String inputGeneList = args[0];
			FileInputStream fstream = new FileInputStream(inputGeneList);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
