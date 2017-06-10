package idconversion.protein2genome;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

/**
 * 
 * @author tshaw
 *
 */
public class QuerySpeciesID {

	
	public static void main(String[] args) {
		try {
			
			
			String fileName = args[0];
			//String knownGene = args[1];
			String toEnsembl = args[1];
			String outputFile = args[2];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			//String outputFile = args[1];
			//HashMap U2kg = grabUniprot2KnownGene(knownGene);
			HashMap kg2ET = grabKnownGene2EnsemblTranscript(toEnsembl);
			
			HashMap map = new HashMap();

			HashMap scanGene = new HashMap();
			HashMap scanCount = new HashMap();
			
            FileInputStream fstream = new FileInputStream(fileName);
            DataInputStream din = new DataInputStream(fstream);
            BufferedReader in = new BufferedReader(new InputStreamReader(din));

            // assuming that the first two line consist of stuff
            in.readLine();
            in.readLine();
            while (in.ready()) {
                    String str = in.readLine();
                    String[] split = str.split(";");
                    String seq = split[0].replaceAll("\\.", "");
                    //String name = split[1].split("\\|")[1];
                    String name = split[1];
                    String IDsumOutfileName = split[2];
                    /*String xcorr = split[6];
                    String pos = split[14].replaceAll("AA", "");
                    String start = pos.split("to")[0];
                    String end = pos.split("to")[1];*/
	
				String tag = name + IDsumOutfileName + seq;

				if (!scanGene.containsKey(tag)) {
					scanGene.put(tag, tag);
					if (!scanCount.containsKey(name + seq)) {
						scanCount.put(name + seq, 1);
					} else {
						int num = (Integer)scanCount.get(name + seq);
						num++;
						scanCount.put(name + seq, num);
					}
				}
			}			
			in.close();

			fstream = new FileInputStream(fileName);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			
			// assuming that the first two line consist of stuff
			in.readLine();
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split(";");
				String seq = split[0].replaceAll("\\.", "");
				//String name = split[1].split("\\|")[1];
				String name = split[1];
				String xcorr = split[6];
				String pos = split[14].replaceAll("AA", "");
				String start = pos.split("to")[0];
				String end = pos.split("to")[1];
				String IDsumOutfileName = split[2];
				String tag = name + IDsumOutfileName + seq;
				//if (U2kg.containsKey(name)) {
					String kg = name;
					if (kg2ET.containsKey(kg)) {
						String et = (String)kg2ET.get(kg);
						if (!map.containsKey((et + seq))) {
							out.write(name + "\t" + seq + "\t" + kg + "\t" + et + "\t" + start + "\t" + end + "\t" + scanCount.get(name + seq) + "\t" + xcorr + "\n");
							map.put(et + seq, et + seq);
						} 
					} else {
						System.out.println("Not found: " + kg);
					}
				//} else {
				//	System.out.println("Not Found: " + name);
				//}
				//System.out.println(name);
			}
			in.close();
			
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static HashMap grabKnownGene2EnsemblTranscript(String fileName) {
		HashMap map = new HashMap();
		try {
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (!split[0].equals("")){
					map.put(split[0], split[1]);
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	public static HashMap grabUniprot2KnownGene(String fileName) {
		HashMap map = new HashMap();
		try {
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (!split[10].equals("")){
					map.put(split[10], split[0]);
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
}

