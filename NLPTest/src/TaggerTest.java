import java.io.IOException;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;


public class TaggerTest {
	static MaxentTagger tagger;
	public TaggerTest() {
		try {
			tagger = new MaxentTagger("/home/devashishshankar/reviews/libraries/stanford-postagger-2012-11-11/models/wsj-0-18-bidirectional-distsim.tagger");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void main(String args[]) {
		try {
			tagger = new MaxentTagger("/home/devashishshankar/reviews/libraries/stanford-postagger-2012-11-11/models/wsj-0-18-bidirectional-distsim.tagger");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		String sentence = "galaxy price battery value display screen for money pictures very bad htc samsung galaxy";
		System.out.println(tagLine(sentence));
	}
	public static String tagLine(String line) {
		String tagged = tagger.tagString(line);
		return tagged;
	}

}
