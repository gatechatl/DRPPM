package RNAseqTools.Summary;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Based on the LIMMA Output calculate the genes that are present into two dataset
 * @author tshaw
 *
 */
public class CalculateIntersectingGenes {

	public static String type() {
		return "PostDE";
	}
	public static String description() {
		return "Overlap two differential expression gene set";
	}
	public static String parameter_info() {
		return "[upFile1] [dnFile1] [upFile2] [dnFile2]";
	}
	public static void execute(String[] args) {
		try {
			String upFile1 = args[0];
			String dnFile1 = args[1];
			String upFile2 = args[2];
			String dnFile2 = args[3];
			HashMap map1 = new HashMap();
			HashMap map2 = new HashMap();
			map1 = grabGeneName(map1, upFile1);
			map1 = grabGeneName(map1, dnFile1);
			map2 = grabGeneName(map2, upFile2);
			map2 = grabGeneName(map2, dnFile2);
			HashMap intersect = intersectReads(map1, map2);
			System.out.println("Intersect\t(File1 - Intersect)\t(File2 - Intersect)\tFile1\tFile2");
			System.out.println(intersect.size() + "\t" + (map1.size() - intersect.size()) + "\t" + (map2.size() - intersect.size()) + "\t" + map1.size() + "\t" + map2.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static HashMap intersectReads(HashMap map1, HashMap map2) {
		HashMap map = new HashMap();
		Iterator itr = map1.keySet().iterator();
		while (itr.hasNext()) {
			String key = (String)itr.next();
			if (map2.containsKey(key)) {
				map.put(key, key);
			}
		}
		return map;
	}
	public static HashMap grabGeneName(HashMap map, String matrixFile) {
		try {
			
			FileInputStream fstream = new FileInputStream(matrixFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(split[0], split[0]);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}
