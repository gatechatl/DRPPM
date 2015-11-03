package PhosphoTools;

public class SummaryTools {

	public static int[] header_expr_info(String header, String[] tags) {
		int[] index = new int[tags.length];
		String[] split = header.split("\t");
		for (int i = 0; i < split.length; i++) {
			for (int j = 0; j < tags.length; j++) {
				//System.out.println(split[i].split(" ")[0] + "\t" + tags[j] + "\t" + header);
				/*if (split[i].contains(tags[j])) {
					
				}*/
				if (split[i].split(" ")[0].equals(tags[j])) {
					
					index[j] = i;
				}
			}
		}
		return index;
	}
	
}
