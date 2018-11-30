package xmlprocessor;

/**
 * @author Christof Schulze
 * 
 */
interface XmlProcessorIF {

	/**
	 * always call this when done with xml processing
	 */
	public void cleanup();

	/**
	 * initialize xml processor for reading xml files only
	 * 
	 * @param filename
	 *            input xml file
	 * @throws XmlProcessorException
	 */
	public void init(String filename) throws XmlProcessorException;

	/**
	 * initialize xml processor for reading an xml file and writing another xml
	 * file
	 * 
	 * @param filename
	 *            input xml file
	 * @param outfile
	 *            output xml file (will be gzipped)
	 * @throws XmlProcessorException
	 */
	public void init(String filename, String outfile)
			throws XmlProcessorException;

	/**
	 * call this to process the xml stream(s) using the defined methods
	 * startelement(), endelement(), attribut(), character()
	 * 
	 * @throws XmlProcessorException
	 */
	public void dowork() throws XmlProcessorException;

}