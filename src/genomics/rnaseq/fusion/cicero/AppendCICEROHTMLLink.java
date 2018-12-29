package genomics.rnaseq.fusion.cicero;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class AppendCICEROHTMLLink {

	public static void execute(String[] args) {
		try {
			
			String originalFile = args[0];
			String path = args[1];
			String organism = args[2];
			FileInputStream fstream = new FileInputStream(originalFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String sampleName = split[0] + ".bam";
				String left_chr = split[4].replaceAll("chr", "");
				String left_location = split[5];
				String left_link = generateLink(sampleName, path, left_chr, left_location, organism);
				
				String right_chr = split[9].replaceAll("chr", "");
				String right_location = split[10];
				String right_link = generateLink(sampleName, path, right_chr, right_location, organism);
				System.out.println("" + left_link + "\t" + "" + right_link + "");
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String generateLink(String sampleName, String path, String chr, String location, String organism) {
		String fileName = path + "/" + sampleName;
		//return "http://pallasbfv01:8080/BAMViewer/aceview/aceView.jnlp;jsessionid=ADF91C9285FE690528FF39ADF7B26DC9?normalname=" + fileName + "&tumorname=" + fileName + "&region=" + chr + "&center=" + location + "&Xmx=&Xms=&noMx=false&ref=" + organism;
		return "http://pallasbfv01:8080/BAMViewer/aceview/aceView.jnlp;jsessionid=ADF91C9285FE690528FF39ADF7B26DC9?tumorname=" + fileName + "&region=" + chr + "&center=" + location + "&Xmx=&Xms=&noMx=false&ref=" + organism;
	}
}
