package MISC;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

//
public class HumanMouseGeneNameConversion {

	public static void main(String[] args) {
		
		try {
			
			String hs2mmFile = "C:\\Users\\tshaw\\Desktop\\RNASEQ\\hs_mm_homo_r66.txt";
			HashMap human2mouse = human2mouse(hs2mmFile);
			String inputFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\GangGeneList\\GangGeneListAnalysis.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				System.out.println(str + "\t" + human2mouse.get(str));
				
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void convertM2HOneColumn(String[] args) {
		try {
			
			String hs2mmFile = args[1]; //"C:\\Users\\tshaw\\Desktop\\RNASEQ\\hs_mm_homo_r66.txt";
			HashMap human2mouse = human2mouse(hs2mmFile);
			String inputFile = args[0]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\GangGeneList\\GangGeneListAnalysis.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String name = split[0];
				if (human2mouse.containsKey(split[0])) {
					name = (String)human2mouse.get(split[0]);
					System.out.println(human2mouse.get(str));
				}
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void convertM2HTwoColumn(String[] args) {
		try {
			
			String hs2mmFile = args[1]; //"C:\\Users\\tshaw\\Desktop\\RNASEQ\\hs_mm_homo_r66.txt";
			HashMap human2mouse = human2mouse(hs2mmFile);
			String inputFile = args[0]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\GangGeneList\\GangGeneListAnalysis.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String name = split[0];
				if (human2mouse.containsKey(split[0])) {
					name = (String)human2mouse.get(split[0]);
					//System.out.println(human2mouse.get(str));
				}
				String target = split[1];
				if (human2mouse.containsKey(split[1])) {
					target = (String)human2mouse.get(split[1]);
					//System.out.println(human2mouse.get(str));
				}
				System.out.println(name + "\t" + target);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void executeHuman2MouseMatrix(String[] args) {				
		try {
			
			String hs2mmFile = args[1]; //"C:\\Users\\tshaw\\Desktop\\RNASEQ\\hs_mm_homo_r66.txt";
			HashMap human2mouse = human2mouse(hs2mmFile);
			String inputFile = args[0]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\GangGeneList\\GangGeneListAnalysis.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			System.out.println(in.readLine());
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String data = "";
				for (int i = 1; i < split.length; i++) {
					data += "\t" + split[i];
				}
				if (human2mouse.containsKey(split[0])) {
					System.out.println(human2mouse.get(split[0]) + data);
				}				
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void executeMouse2HumanMatrix(String[] args) {				
		try {
			
			String hs2mmFile = args[1]; //"C:\\Users\\tshaw\\Desktop\\RNASEQ\\hs_mm_homo_r66.txt";
			HashMap mouse2human = mouse2human(hs2mmFile);
			String inputFile = args[0]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\GangGeneList\\GangGeneListAnalysis.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			System.out.println(in.readLine());
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String data = "";
				for (int i = 1; i < split.length; i++) {
					data += "\t" + split[i];
				}
				if (mouse2human.containsKey(split[0])) {
					System.out.println(mouse2human.get(split[0]) + data);
				}				
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void executeHuman2Mouse(String[] args) {				
		try {
			
			String hs2mmFile = args[1]; //"C:\\Users\\tshaw\\Desktop\\RNASEQ\\hs_mm_homo_r66.txt";
			HashMap human2mouse = human2mouse(hs2mmFile);
			String inputFile = args[0]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\GangGeneList\\GangGeneListAnalysis.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (human2mouse.containsKey(str)) {
					System.out.println(human2mouse.get(str));
				}				
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void executeMouse2Human(String[] args) {				
		try {
			
			String hs2mmFile = args[1]; //"C:\\Users\\tshaw\\Desktop\\RNASEQ\\hs_mm_homo_r66.txt";
			HashMap mouse2human = mouse2human(hs2mmFile);
			String inputFile = args[0]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\GangGeneList\\GangGeneListAnalysis.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (mouse2human.containsKey(str)) {
					System.out.println(mouse2human.get(str));
				}				
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void executeMouse2HumanCapitalize(String[] args) {				
		try {
			
			String hs2mmFile = args[1]; //"C:\\Users\\tshaw\\Desktop\\RNASEQ\\hs_mm_homo_r66.txt";
			HashMap mouse2human = mouse2human(hs2mmFile);
			String inputFile = args[0]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\GangGeneList\\GangGeneListAnalysis.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (mouse2human.containsKey(str)) {
					System.out.println(mouse2human.get(str));
				} else {
					System.out.println(str.toUpperCase());
				}
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static HashMap human2mouse(String hs_mm_homo_r66) {
		HashMap human2mouse = new HashMap();
		try {
			
			String fileName = hs_mm_homo_r66;
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				human2mouse.put(split[0], split[1]);
			}
			in.close();
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return human2mouse;
	}
	public static HashMap mouse2human(String hs_mm_homo_r66) {
		HashMap mouse2human = new HashMap();
		try {
			
			String fileName = hs_mm_homo_r66;
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				mouse2human.put(split[1], split[0]);
			}
			in.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return mouse2human;
	}
}
