package genomics.exome;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class AppendBamReviewFile {

	public static void execute(String[] args) {
		
		try {
			
			
			String fileName = args[0];
			String path = args[1];
			String file = args[2];
			String organism = args[3];
			
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			System.out.println(in.readLine() + "\t" + "BamViewer");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String chr = split[3];
				String location = split[4];
				System.out.println(str + "\t" + generateBamViewer(path, file, chr, location, organism));
			}
			in.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// example path: projects/EXCAP/LeventakiSpliceCell/BucketRaw/SJALCL/
	public static String generateBamViewer(String path, String file, String chr, String location, String organism) {
		String sameFile = path + "/" + file;
		String bamViewer = "http://pallasbfv01:8080/BAMViewer/aceview/splash?tumorname=" + sameFile + "&normalname=" + sameFile + "&ref=" + organism + "&region=" + chr + "&center=" + location;
		return bamViewer;
	}
}
