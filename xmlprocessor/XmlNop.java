package xmlprocessor;

public class XmlNop extends XmlProcessor {

	@Override
	protected void characters(String chars) throws XmlProcessorException {
		// TODO Auto-generated method stub
		writeChars(chars);
	}

	@Override
	protected void startelement(String name) throws XmlProcessorException {
		// TODO Auto-generated method stub
		writeStartElement(name);
	}

	@Override
	protected void endelement(String name) throws XmlProcessorException {
		// TODO Auto-generated method stub
		writeEndElement();
	}

	@Override
	protected void attribut(String name, String content)
			throws XmlProcessorException {
		writeAttribute(name, content);
		// TODO Auto-generated method stub

	}
	// private String attribut = null;
	// private int matchindex = 0;
	// private ArrayList<String> matchlist = null;
	// private ArrayList<String> position = null;

	public void init(String filename, String outfile)
			throws XmlProcessorException {
		super.init(filename, outfile);

		// position = new ArrayList<String>();
		// matchlist = new ArrayList<String>();

		// matchlist.add(element);
	}

	/**
	 * @param args
	 */

}
