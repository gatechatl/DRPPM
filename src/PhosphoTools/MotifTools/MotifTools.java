package PhosphoTools.MotifTools;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class MotifTools {
	
	/**
	 * Grabs the HPRD motif information
	 * @param fileName
	 * @return
	 */
	public static HashMap grabMotifKinaseGroup(String fileName) {
		HashMap map = new HashMap();
		try {
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split.length > 1) {
					if (split[3].equals("no")) {
						map.put(split[0] + "\t" + split[1], split[6]);
					}
				}
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return map;
	}
	/**
	 * Grabs the HPRD motif information
	 * @param fileName
	 * @return
	 */
	public static HashMap grabMotifKinaseFamily(String fileName) {
		HashMap map = new HashMap();
		try {
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split.length > 1) {
					if (split[3].equals("no")) {
						map.put(split[0] + "\t" + split[1], split[7]);
					}
				}
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return map;
	}
	/**
	 * Grabs the HPRD motif information
	 * @param fileName
	 * @return
	 */
	public static HashMap grabMotifName(String fileName) {
		HashMap map = new HashMap();
		try {
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split.length > 1) {
					if (split[3].equals("no")) {
						map.put(split[0] + "\t" + split[1], split[1]);
					}
				}
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return map;
	}
	/**
	 * Grabs the HPRD motif information
	 * @param fileName
	 * @return
	 */
	public static HashMap grabMotif(String fileName) {
		HashMap map = new HashMap();
		try {
			
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split.length > 1) {
					map.put(split[0], split[1]);
				}
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return map;
	}
	
	public static void main(String[] args) {
		
		String proteomic = "M@S%G$GG^PS@GGG*P";
		System.out.println(replaceTag(proteomic));
		//String proteomic = "MSGGGPSGGGPGGSGRARTSSFAEPGGGGGGGGGGPGGSASGPGGTGGGKASVGAMGGGVGASSSGGGPSGSGGGGSGGPGAGTSFPPPGVKLGRDSGKVTTVVATVGQGPERSQEVAYTDIKVIGNGSFGVVYQARLAETRELVAIKKVLQDKRFKNRELQIMRKLDHCNIVRLRYFFYSSGEKKDELYLNLVLEYVPETVYRVARHFTKAKLITPIIYIKVYMYQLFRSLAYIHSQGVCHRDIKPQNLLVDPDTAVLKLCDFGSAKQLVRGEPNVSYICSRYYRAPELIFGATDYTSSIDVWSAGCVLAELLLGQPIFPGDSGVDQLVEIIKVLGTPTREQIREMNPNYTEFKFPQIKAHPWTKVFKSSKTPPEAIALCSSLLEYTPSSRLSPLEACAHSFFDELRRLGAQLPNDRPLPPLFNFSPGELSIQPSLNAILIPPHLRSPAGPASPLTTSYNPSSQALTEAQTGQDWQPSDATTATLASSS";
		//String motif = "GRART[S/T]pSFAE";
		//System.out.println(countMotif(proteomic, motif, false));
		
	}
	public static int countMotifType(String proteomic, String motifType, int len) {
		int count = 0;
		for (int i = 0; i < proteomic.length() - len; i++) {
			
			String query = proteomic.substring(i, i + len);
			if (query.contains(motifType)) {
				count++;
			}
		}
		//System.out.println(proteomic);
		//System.out.println(convertMotif);
		//return false;
		return count;	
	}
	public static LinkedList strMotif2List(String motif) {
		String convertMotif = motif.replaceAll("X", ".");
		LinkedList complete_motif = new LinkedList();
		for (int i = 0; i < convertMotif.length(); i++) {
			String a = convertMotif.substring(i, i + 1);
			
			if (a.equals("p")) {
				i++;
				String phospho = convertMotif.substring(i, i + 1);
				AminoAcid aa = new AminoAcid();
				aa.add(phospho.toLowerCase());
				complete_motif.add(aa);
			} else if (a.equals("[")) {
				AminoAcid aa = new AminoAcid();
				i++;
				while (!convertMotif.substring(i, i + 1).equals("]")) {
					a = convertMotif.substring(i, i + 1);
					if (i == convertMotif.length()) {
						break;
					}
					if (a.equals("/")) {
						// do nothing
					} else if (a.equals("p")) {
						i++;
						String phospho = motif.substring(i, i + 1);
						aa.add(phospho.toLowerCase());
					} else {
						
						aa.add(a.toLowerCase());
					}
					i++;
				} // end while loop
				complete_motif.add(aa);
								
			} else {
				AminoAcid aa = new AminoAcid();
				aa.add(a.toLowerCase());
				complete_motif.add(aa);				
			}
		} // for loop
		return complete_motif;
	}
	/**
	 * disregard for the phosphorylation site
	 * @param proteomic
	 * @param motif
	 * @param complete_match_flag
	 * @return
	 */
	public static int countMotif(String proteomic, String motif, boolean complete_match_flag) {
		int count = 0;
		
		
		LinkedList complete_motif = strMotif2List(motif);
		
		
		
		/*Iterator itr = complete_motif.iterator();
		while (itr.hasNext()) {
			AminoAcid key = (AminoAcid)itr.next();
			
			key.print();
			//System.out.println(key);
		}*/
		boolean match = false;
		String new_proteomic = convertFormat(proteomic);
		
		
		//System.out.println(proteomic + "\t" + new_proteomic);
		
		proteomic = new_proteomic;
		//System.out.println("check: " + proteomic);
		for (int i = 0; i < proteomic.length() - complete_motif.size() + 1; i++) {
			int j = 0;
			Iterator itr = complete_motif.iterator();
			while (itr.hasNext()) {
				AminoAcid key = (AminoAcid)itr.next();
				String query = proteomic.substring(i + j, i + j + 1);
				//System.out.print(query);
				//System.out.println(query);
				if (key.match(query)) {
					match = true;
				} else if (key.match(".")) {
					match = true;
				} else {
					match = false;
					break;
				}				
				j++;
				//System.out.println(key);
			}
			//System.out.println();
			if (match) {
				count++;
			}
		}
		//System.out.println(proteomic);
		//System.out.println(convertMotif);
		//return false;
		return count;
	}
	public static boolean containsMotif(String proteomic, String motif, boolean complete_match_flag) {
		String convertMotif = motif.replaceAll("X", ".");
		LinkedList complete_motif = new LinkedList();
		for (int i = 0; i < convertMotif.length(); i++) {
			String a = convertMotif.substring(i, i + 1);
			if (a.equals("p")) {
				i++;
				String phospho = convertMotif.substring(i, i + 1);
				AminoAcid aa = new AminoAcid();
				aa.add(phospho.toUpperCase());
				complete_motif.add(aa);
			} else if (a.equals("[")) {
				AminoAcid aa = new AminoAcid();
				i++;
				while (!convertMotif.substring(i, i + 1).equals("]")) {
					a = convertMotif.substring(i, i + 1);
					if (i == convertMotif.length()) {
						break;
					}
					if (a.equals("/")) {
						// do nothing
					} else if (a.equals("p")) {
						i++;
						String phospho = motif.substring(i, i + 1);
						aa.add(phospho.toUpperCase());
					} else {
						
						aa.add(a.toLowerCase());
					}
					i++;
				} // end while loop
				complete_motif.add(aa);
								
			} else {
				AminoAcid aa = new AminoAcid();
				aa.add(a.toLowerCase());
				complete_motif.add(aa);				
			}
		} // for loop
		
		
		/*Iterator itr = complete_motif.iterator();
		while (itr.hasNext()) {
			AminoAcid key = (AminoAcid)itr.next();
			
			key.print();
			//System.out.println(key);
		}*/
		boolean match = false;
		String new_proteomic = convertFormat(proteomic);
		
		
		//System.out.println(proteomic + "\t" + new_proteomic);
		
		proteomic = new_proteomic;
		//System.out.println("check: " + proteomic);
		for (int i = 0; i < proteomic.length() - complete_motif.size() + 1; i++) {
			int j = 0;
			Iterator itr = complete_motif.iterator();
			while (itr.hasNext()) {
				AminoAcid key = (AminoAcid)itr.next();
				String query = proteomic.substring(i + j, i + j + 1);
				//System.out.print(query);
				
				if (key.match(query)) {
					match = true;
				} else if (key.match(".")) {
					match = true;
				} else {
					match = false;
					break;
				}				
				j++;
				//System.out.println(key);
			}
			//System.out.println();
			if (match) {
				return true;
				//System.out.println(i);
				/*boolean foundOther = false;
				for (int k = 0; k < i; k++) {
					if (!proteomic.substring(k, k + 1).equals(".")) {
						foundOther = true;
					}
				}
				for (int k = i + complete_motif.size(); k < proteomic.length(); k++) {
					if (!proteomic.substring(k, k + 1).equals(".")) {
						foundOther = true;
					}
				}
				if (!(foundOther && complete_match_flag)) {
					return i;
					//return -1;
					//System.out.println("diff: " + proteomic);
					//System.out.println("diff: " + motif);
				}
				//return true;*/
			}
		}
		//System.out.println(proteomic);
		//System.out.println(convertMotif);
		//return false;
		return false;
	}
	public static String convertFormat(String proteomic) {
		// convert all phosphorylation site into uppercase and rest lower case
		String new_proteomic = "";
		for (int i = 0; i < proteomic.length(); i++) {
			String p = proteomic.substring(i, i + 1);
			if (p.equals("@") || p.equals("^") || p.equals("#") || p.equals("*")) {
				String prev = new_proteomic.substring(new_proteomic.length() - 1, new_proteomic.length());
				new_proteomic = new_proteomic.substring(0, new_proteomic.length() - 1) + prev.toUpperCase();
			} else {
				new_proteomic += p.toLowerCase();
			}
		}
		return new_proteomic;
	}
	public static int getMotifIndex(String proteomic, String motif, boolean complete_match_flag) {
		String convertMotif = motif.replaceAll("X", ".");
		LinkedList complete_motif = new LinkedList();
		for (int i = 0; i < convertMotif.length(); i++) {
			String a = convertMotif.substring(i, i + 1);
			if (a.equals("p")) {
				i++;
				String phospho = convertMotif.substring(i, i + 1);
				AminoAcid aa = new AminoAcid();
				aa.add(phospho.toLowerCase());
				complete_motif.add(aa);
			} else if (a.equals("[")) {
				AminoAcid aa = new AminoAcid();
				i++;
				while (!convertMotif.substring(i, i + 1).equals("]")) {
					a = convertMotif.substring(i, i + 1);
					if (i == convertMotif.length()) {
						break;
					}
					if (a.equals("/")) {
						// do nothing
					} else if (a.equals("p")) {
						i++;
						String phospho = motif.substring(i, i + 1);
						aa.add(phospho.toLowerCase());
					} else {
						aa.add(a);
					}
					i++;
				} // end while loop
				complete_motif.add(aa);
								
			} else {
				AminoAcid aa = new AminoAcid();
				aa.add(a);
				complete_motif.add(aa);				
			}
		} // for loop
		
		
		/*Iterator itr = complete_motif.iterator();
		while (itr.hasNext()) {
			AminoAcid key = (AminoAcid)itr.next();
			
			key.print();
			//System.out.println(key);
		}*/
		boolean match = false;
		for (int i = 0; i < proteomic.length() - complete_motif.size() + 1; i++) {
			int j = 0;
			Iterator itr = complete_motif.iterator();
			while (itr.hasNext()) {
				AminoAcid key = (AminoAcid)itr.next();
				String query = proteomic.substring(i + j, i + j + 1);
				//System.out.print(query);
				if (key.match(query)) {
					match = true;
				} else {
					match = false;
					break;
				}				
				j++;
				//System.out.println(key);
			}
			//System.out.println();
			if (match) {
				//System.out.println(i);
				boolean foundOther = false;
				for (int k = 0; k < i; k++) {
					if (!proteomic.substring(k, k + 1).equals(".")) {
						foundOther = true;
					}
				}
				for (int k = i + complete_motif.size(); k < proteomic.length(); k++) {
					if (!proteomic.substring(k, k + 1).equals(".")) {
						foundOther = true;
					}
				}
				if (!(foundOther && complete_match_flag)) {
					return i;
					//return -1;
					//System.out.println("diff: " + proteomic);
					//System.out.println("diff: " + motif);
				}
				//return true;
			}
		}
		//System.out.println(proteomic);
		//System.out.println(convertMotif);
		//return false;
		return -1;
	}
	public static LinkedList grabFastaQueryName(String fileName) {
		LinkedList list = new LinkedList();
		try {
			
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				list.add(str.trim().replaceAll(">", "").split("\t")[0]);
				str = in.readLine();				
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	public static LinkedList convert(String str) {
		LinkedList list = new LinkedList();
		String clean = str.replaceAll("#", "");
		//System.out.println(str);
		for (int i = 0; i < str.length(); i++) {
			String substring = str.substring(i, i + 1);
			if (substring.equals("#")) {
				String newStr = str.substring(0, i) + "%" + str.substring(i, str.length());
				newStr = newStr.replaceAll("#", "");
				//System.out.println("Before: " + newStr);
				newStr = newStr.replaceAll("%", "\\*");
				//System.out.println("After: " + newStr);
				list.add(newStr);
			}
		}
		return list;
	}
	public static LinkedList grabFastaQueryNameMeta(String fileName) {
		LinkedList list = new LinkedList();
		try {
			
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.trim().replaceAll(">", "").split("\t");
				if (split.length > 1) {
					list.add(split[1]);
				} else {
					System.out.println("Failed: ");
					System.out.println(str);
					
					System.exit(0);
				}
				str = in.readLine();				
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	public static LinkedList grabFastaQuery(String fileName) {
		LinkedList list = new LinkedList();
		try {
			
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				str = in.readLine().split("\t")[0];				
				list.add(str.trim());
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	public static LinkedList grabFastaQueryOrig(String fileName) {
		LinkedList list = new LinkedList();
		try {
			
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				str = in.readLine().split("\t")[1];				
				list.add(str.trim());
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	public static String cleanAll(String input) {
		return replaceTag(input).replaceAll("#", "");
	}
	
	public static HashMap grabMotifAndName(String fileName) {
		HashMap map = new HashMap();
		try {						
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String motif_str = split[1];
				String motif = split[0];
				map.put(motif_str + "\t" + motif, 0);
				//if (split[5].equals("yes")) {
					
				//}
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	public static HashMap grabKinase2Motif(String fileName) {
		HashMap map = new HashMap();
		try {						
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String motif_str = split[1];
				if (split[5].equals("yes")) {
					String[] genes = split[2].split(",");
					for (String gene: genes) {
						if (map.containsKey(gene)) {
							HashMap motif = (HashMap)map.get(gene);
							motif.put(motif_str, motif_str);
							map.put(gene, motif);
						} else {
							HashMap motif = new HashMap();
							motif.put(motif_str, motif_str);
							map.put(gene, motif);
						}
					}
				}
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public static String replaceTag(String input) {
		String result = "";
		result = input.replaceAll("M@", "M");
		result = result.replaceAll("M#", "M");
		result = result.replaceAll("M%", "M");
		result = result.replaceAll("M\\*", "M");		
		result = result.replaceAll("M^", "M");
		result = result.replaceAll("M$", "M");
		//result = input.replaceAll("~", "#");
		//result = result = result.replaceAll("!", "#");
		
		//result = result.replaceAll("$", "#");
		result = result.replaceAll("\\%", "#");
		result = result.replaceAll("\\@", "#");
		result = result.replaceAll("\\^", "#");
		result = result.replaceAll("\\$", "#");
		
		//result = result.replaceAll("^", "#");
		//result = result.replaceAll("&", "#");
		result = result.replaceAll("\\*", "#");
		return result;
	}
	public static boolean contains(String input) {
		String result = "";
		if (result.equals("~")) {
			return true;
		} else if (result.equals("!")) {
			return true;
		} else if (result.equals("@")) {
			return true;
		} else if (result.equals("#")) {
			return true;
		} else if (result.equals("$")) {
			return true;
		} else if (result.equals("%")) {
			return true;
		} else if (result.equals("^")) {
			return true;
		} else if (result.equals("&")) {
			return true;
		} else if (result.equals("*")) {
			return true;
		} else if (result.equals("*")) {
			return true;
		} else if (result.equals("(")) {
			return true;
		} else if (result.equals(")")) {
			return true;
		} else if (result.equals("-")) {
			return true;
		} else if (result.equals("+")) {
			return true;
		} else if (result.equals("_")) {
			return true;
		} else if (result.equals("=")) {
			return true;
		}
		return false;
	}
}
