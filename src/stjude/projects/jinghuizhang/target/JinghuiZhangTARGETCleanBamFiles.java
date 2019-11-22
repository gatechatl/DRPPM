package stjude.projects.jinghuizhang.target;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;

public class JinghuiZhangTARGETCleanBamFiles {

	
	public static void main(String[] args) {
		
		
		try {

			LinkedList list = new LinkedList();
			String inputFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\step3_star_mapping.sh";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				list.add(str);
			}
			in.close();
			
			LinkedList bad = new LinkedList();
			
			String killed_jobs_file = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\killed_jobs.txt";
			fstream = new FileInputStream(killed_jobs_file);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains("sh[")) {
					int number = new Integer(str.split("sh\\[")[1].split("\\]")[0]);
					bad.add(number);
					String line = (String)list.get((number - 1));
					//System.out.println(line);
					
				}
			}
			
			for (int i = 0; i < list.size(); i++) {
				if (!bad.contains(i + 1)) {
					String line = ((String)list.get(i)).replaceAll(".sh", "").replaceAll("sh ", "");
					System.out.println("ls -lhtr " + line + "/" + line + ".STAR.Aligned.sortedByCoord.out.bam");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
