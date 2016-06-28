package nameGenerator;

import java.util.Random;
import java.util.ArrayList;
import org.w3c.dom.*;
import utils.FileUtils;

public class NameGenerator {
	private ArrayList<ArrayList<String>> syllables;
	
	/*public static void main(String[] args) {
		NameGenerator gen = new NameGenerator("VikingCity");
		for (int i = 0; i < 10; i ++)
			System.out.println(gen.nextName());
	}*/
	
	public NameGenerator(String fname) {
		fname = "/nameGenerator/" + fname + ".xml";
		Document doc = FileUtils.openXML(fname);
		NodeList lists = doc.getFirstChild().getChildNodes();
		
		syllables = new ArrayList<ArrayList<String>>();
		for (int i = 0; i < lists.getLength(); i ++) {
			Node list = lists.item(i);
			String content = list.getTextContent();
			String[] lines = content.split("\n");
			syllables.add(new ArrayList<String>());
			
			for (int j = 0; j < lines.length; j ++) {
				String line = lines[j].trim();
				if (line.length() > 0) {
					syllables.get(i).add(line);
				}
			}
		}
		
		for (int i = syllables.size() - 1; i >= 0; i --) {
			if (syllables.get(i).size() == 0) {
				syllables.remove(i);
			}
		}
	}
	
	public String nextName() {
		String name = new String();
		Random rand = new Random();
		
		for (int i = 0; i < syllables.size(); i ++) {
			int randIndex = rand.nextInt(syllables.get(i).size());
			name += syllables.get(i).get(randIndex);
		}
		
		return name;
	}
	
	protected void setSyllables(ArrayList<ArrayList<String>> syllables) {
		this.syllables = syllables;
	}
}
