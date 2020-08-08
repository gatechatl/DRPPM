package stjude.projects.jinghuizhang.altsplice.annotation;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * Merge the bed files from Liqing's pipeline to get the peptide sequence.
 * @author tshaw
 *
 */
public class JinghuiZhangMergeBEDFiles {

	public static void main(String[] args) {
		
		try {
			

			String inputFile = args[0];
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine().trim();								
			}
			in.close();			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
