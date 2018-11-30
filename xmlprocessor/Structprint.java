package xmlprocessor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * @author Christof Schulze
 * 
 */
public class Structprint extends XmlProcessor {

	private ArrayList<String> hierarchy = null;
	private StringBuffer chars = null;
	private LinkedList<String> attributenames = null;
	private LinkedList<String> attributevals = null;

	public void cleanup() {
		super.cleanup();
		if (hierarchy != null)
			hierarchy = null;
		if (chars != null)
			chars = null;
		if (attributenames != null)
			attributenames = null;
		if (attributevals != null)
			attributevals = null;
	}

	public void init(String filename) throws XmlProcessorException {
		super.init(filename);
		hierarchy = new ArrayList<String>();
		attributenames = new LinkedList<String>();
		attributevals = new LinkedList<String>();
	}

	protected void startelement(String name) {
		if (name.contains(":")) {
			String[] argres = name.split(":");
			name = argres[1];
		}
		hierarchy.add(name);
	}

	protected void attribut(String name, String value) {
		attributenames.add(name);
		attributevals.add(value);
	}

	protected void endelement(String content) {
		ListIterator<String> iter = hierarchy.listIterator();
		while (iter.hasNext()) {
			System.out.print(iter.next());
			if (iter.hasNext())
				System.out.print(".");
		}

		while (!attributenames.isEmpty())
			System.out.print(":" + attributenames.remove() + "="
					+ attributevals.remove());

		if (chars != null)
			System.out.print(chars.toString());

		if (!hierarchy.isEmpty())
			hierarchy.remove(hierarchy.size() - 1);
		System.out.println("");
	}

	protected void characters(String characters) {
		chars = new StringBuffer(characters.length() + 2);
		chars.append(" (").append(characters).append(")");
	}
}
