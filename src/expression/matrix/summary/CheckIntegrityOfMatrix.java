package expression.matrix.summary;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * 
 * @author tshaw
 *
 */
public class CheckIntegrityOfMatrix {

	public static String description() {
		return "Check the integrity of a matrix file.";
	}
	public static String type() {
		return "DATAMATRIX";
	}
	public static String parameter_info() {
		return "[inputMatrixFile]";
	}
	public static void execute(String[] args) {
		
		
		try {
			
			boolean inconsisten_num = false;
			String inputFile = args[0];
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");
			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split_header.length != split.length) {
					inconsisten_num = true;
					System.out.println("The number of samples is inconsistent to the number of columns with values");
					System.out.println("Number of splits in the header by tab:" + split_header.length);
					System.out.println("Number of splits in the row by tab:" + split.length);
					System.exit(0);;
				}
				
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
