package proteomics.phospho.tools.motifs;

import java.util.Iterator;
import java.util.LinkedList;

public class AminoAcid {
	public LinkedList AA = new LinkedList();
	public void add(String aa) {
		AA.add(aa);
	}
	public boolean match(String query) {
		Iterator itr = AA.iterator();
		while (itr.hasNext()) {
			String key = (String)itr.next();
			if (key.equals(query)) {
				return true;
			}
		}
		return false;
	}
	public void print() {
		Iterator itr = AA.iterator();
		while (itr.hasNext()) {
			String key = (String)itr.next();
			System.out.println(key);
		}
	}
}
