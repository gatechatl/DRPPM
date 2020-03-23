package stjude.projects.jinghuizhang.rnaediting;

public class JinghuiZhangGenerateName {

	
	public static void main(String[] args) {
		for (int i = 1; i < 10; i++) {
			System.out.println("comet/w2f00" + i + "/w2f00" + i + ".1");
		}
		for (int i = 10; i < 100; i++) {
			System.out.println("comet/w2f0" + i + "/w2f0" + i + ".1");
		}
		for (int i = 100; i < 199; i++) {
			System.out.println("comet/w2f" + i + "/w2f" + i + ".1");
		}
	}
}
