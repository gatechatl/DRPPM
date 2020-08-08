package protein.features.aminoacidresidue;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Given the motif region calculate the 
 * @author tshaw
 *
 */
public class CountGeneWithResidueRegionPlot {

	public static String type() {
		return "PROTEINFEATURE";
	}
	public static String description() {
		return "Count the region that is considered as significant";
	}
	public static String parameter_info() {
		return "[folderPath1] [folderPath2] [folderPath3] [folderPath4] [title1] [title2] [title3] [title4] [cutoff]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String folderPath1 = args[0];
			String folderPath2 = args[1];
			String folderPath3 = args[2];
			String folderPath4 = args[3];
			String title1 = args[4];
			String title2 = args[5];
			String title3 = args[6];
			String title4 = args[7];
			int maxcutoff = new Integer(args[8]);
			String term = args[9];
			System.out.println("Cutoff\t" + title1 + "\t" + title2 + "\t" + title3 + "\t" + title4);
			//int cutoff = new Integer(args[1]);
			for (int cutoff = 0; cutoff <= maxcutoff; cutoff = cutoff + 1) {
				File f = new File(folderPath1);
				double total1 = 0;
				double satisfy1 = 0;
				double total2 = 0;
				double satisfy2 = 0;
				double total3 = 0;
				double satisfy3 = 0;
				double total4 = 0;
				double satisfy4 = 0;
				File[] files = f.listFiles();
				LinkedList list = new LinkedList();
				for (File file: files) {
					int max = 0;
					int count = 0;
					FileInputStream fstream = new FileInputStream(file.getPath());
					DataInputStream din = new DataInputStream(fstream);
					BufferedReader in = new BufferedReader(new InputStreamReader(din));
					while (in.ready()) {
						String str = in.readLine();
						String[] split = str.split("\t");
						if (split.length > 4) {
							if (split[4].equals(term)) {
								count++;
							} else {
								if (count > 0) {
									list.add(count);
									if (max < count) {
										max = count;
									}
								}
								count = 0;
							}
						}
					}
					if (count > 0) {
						if (max < count) {
							max = count;
						}
						list.add(count);
					}
					in.close();
					total1++;
					boolean satisfy_flag = false;
					if (max >= cutoff) {
						satisfy1++;
						satisfy_flag = true;
					}
					//System.out.println(file.getName() + "\t" + satisfy_flag + "\t" + max);
				}
				
				f = new File(folderPath2);			
				files = f.listFiles();
				
				for (File file: files) {
					int max = 0;
					int count = 0;
					FileInputStream fstream = new FileInputStream(file.getPath());
					DataInputStream din = new DataInputStream(fstream);
					BufferedReader in = new BufferedReader(new InputStreamReader(din));
					while (in.ready()) {
						String str = in.readLine();
						String[] split = str.split("\t");
						if (split.length > 4) {
							if (split[4].equals(term)) {
								count++;
							} else {
								if (count > 0) {
									list.add(count);
									if (max < count) {
										max = count;
									}
								}
								count = 0;
							}
						}
					}
					if (count > 0) {
						if (max < count) {
							max = count;
						}
						list.add(count);
					}
					in.close();
					total2++;
					boolean satisfy_flag = false;
					if (max >= cutoff) {
						satisfy2++;
						satisfy_flag = true;
					}
					//System.out.println(file.getName() + "\t" + satisfy_flag + "\t" + max);
				}
				
				f = new File(folderPath3);			
				files = f.listFiles();
				
				for (File file: files) {
					int max = 0;
					int count = 0;
					//System.out.println(file.getPath());
					FileInputStream fstream = new FileInputStream(file.getPath());
					DataInputStream din = new DataInputStream(fstream);
					BufferedReader in = new BufferedReader(new InputStreamReader(din));
					while (in.ready()) {
						String str = in.readLine();
						String[] split = str.split("\t");
						if (split.length > 4) {
							if (split[4].equals(term)) {
								count++;
							} else {
								if (count > 0) {
									list.add(count);
									if (max < count) {
										max = count;
									}
								}
								count = 0;
							}
						}
					}
					if (count > 0) {
						if (max < count) {
							max = count;
						}
						list.add(count);
					}
					in.close();
					total3++;
					boolean satisfy_flag = false;
					if (max >= cutoff) {
						satisfy3++;
						satisfy_flag = true;
					}
					
					
					//System.out.println(file.getName() + "\t" + satisfy_flag + "\t" + max);
				}
				
				f = new File(folderPath4);			
				files = f.listFiles();
				
				for (File file: files) {
					int max = 0;
					int count = 0;
					FileInputStream fstream = new FileInputStream(file.getPath());
					DataInputStream din = new DataInputStream(fstream);
					BufferedReader in = new BufferedReader(new InputStreamReader(din));
					while (in.ready()) {
						String str = in.readLine();
						String[] split = str.split("\t");
						if (split.length > 4) {
							if (split[4].equals(term)) {
								count++;
							} else {
								if (count > 0) {
									list.add(count);
									if (max < count) {
										max = count;
									}
								}
								count = 0;
							}
						}
					}
					if (count > 0) {
						if (max < count) {
							max = count;
						}
						list.add(count);
					}
					in.close();
					total4++;
					boolean satisfy_flag = false;
					if (max >= cutoff) {
						satisfy4++;
						satisfy_flag = true;
					}
					//System.out.println(file.getName() + "\t" + satisfy_flag + "\t" + max);
				}
				System.out.println(cutoff + "\t" + (satisfy1 / total1) + "\t" + (satisfy2 / total2) + "\t" + (satisfy3 / total3) + "\t" + (satisfy4 / total4));			
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

