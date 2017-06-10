package TranscriptionFactorTools;

import mathtools.expressionanalysis.differentialexpression.AddAnnotation2DiffFisher;

/**
 * Produce a list of enriched TFs
 * @author tshaw
 *
 */
public class TFGeneEnrichment {

	public static void execute(String[] args) {
		
		try {
			AddAnnotation2DiffFisher.execute(args);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
