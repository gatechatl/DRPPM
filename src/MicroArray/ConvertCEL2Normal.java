package MicroArray;

public class ConvertCEL2Normal {

	public static void main(String[] args) {
		try {
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * This haven't quite worked yet...
	 * @return
	 */
	public static String convertCELFiles() {
		String script = "";
		script += "library(GEOquery)\n";
		script += "library(affy)\n";
		script += "library(gcrma)\n";
		script += "library(hugene10stv1cdf)\n";
		script += "library(hugene10stv1probe)\n";
		script += "library(hugene10stprobeset.db)\n";
		script += "library(hugene10sttranscriptcluster.db)\n";
		return script;
	}
}
