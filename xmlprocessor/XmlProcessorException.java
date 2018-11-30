package xmlprocessor;

/**
 * @author Christof Schulze
 * 
 */
public class XmlProcessorException extends Exception {

	/**
	 * @param string
	 */
	public XmlProcessorException(String string) {
		// TODO Auto-generated constructor stub
		super(string);
	}

	/**
	 * @param string
	 * @param e
	 */
	public XmlProcessorException(String string, Exception e) {
		super(string, e);
	}
	private static final long serialVersionUID = -6629040594909772710L;

}
