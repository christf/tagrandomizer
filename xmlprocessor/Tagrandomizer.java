/**
 * This class will replace the content of a given xml tag in a given xml file with a random number of a given length.
 * This is by factor 300 faster than the corresponding bash implementation
 */
package xmlprocessor;

import java.util.ArrayList;

/**
 * @author Christof Schulze
 */
public class Tagrandomizer extends XmlProcessor {
	private String attribut = null;
	private int matchindex = 0;
	private ArrayList<String> matchlist = null;
	private ArrayList<String> position = null;

	/**
	 * initialize a read-write xmlprocessor that will do randomization of
	 * content (attributes and characters) when there is a match on pxpath
	 * 
	 * @param filename
	 * @param pxpath
	 * @param outfile
	 * @throws XmlProcessorException
	 */
	public void init(String filename, String pxpath, String outfile)
			throws XmlProcessorException {
		super.init(filename, outfile);

		position = new ArrayList<String>();
		matchlist = new ArrayList<String>();
		String[] result = pxpath.split("\\.");

		for (int x = 0; x < result.length; x++) {
			String element = result[x];

			if (result[x].contains(":")) {
				String[] argres = result[x].split(":");
				attribut = argres[0];
				element = argres[1];
			}
			matchlist.add(element);
		}
	}

	private String grandom(int length) {
		/**
		 * This function will return a random string of a given length
		 * containing numbers exclusively
		 */
		int exp;
		int intervall = 15;
		StringBuilder random = new StringBuilder(length);

		while (random.length() < length) {
			if (length - random.length() > intervall)
				exp = intervall;
			else
				exp = Math.min(length - random.length() - 1, intervall - 1);

			random.append((long) ((Math.random() * 10) * Math.pow(10, exp)));
		}

		return random.toString();
	}
	protected void attribut(String name, String value)
			throws XmlProcessorException {
		String attval = new String();
		if ((attribut != null) && (matchindex == matchlist.size())
				&& (attribut.equals(name))) {
			attval = grandom(value.length());
			writeAttribute(name, attval);
		} else
			writeAttribute(name, value);
	}

	protected void startelement(String name) throws XmlProcessorException {
		
		String element = new String();
		if (name.contains(":")) {
			String[] argres = name.split(":");
			element = argres[1];
		}
		
		position.add(element);
		System.out.println(name + " " + element);
		//if (matchlist.size() > matchindex) {
			if (element.equals(matchlist.get(matchindex))) {
				matchindex++;
			//}
		}

		writeStartElement(name);
	}

	protected void characters(String content) throws XmlProcessorException {
		String characters = null;
		
		System.out.println(matchlist.toString() + matchindex);
		
		
		if ((attribut == null) && (matchindex > 0)
				&& (matchindex == matchlist.size())) {
			characters = new String("");
			matchindex--;
			characters = grandom(content.length());
			writeChars(characters);
		} else
			writeChars(content);
	}

	protected void endelement(String content) throws XmlProcessorException {
		position.remove(position.size() - 1);
		writeEndElement();
	}
}
