package xmlprocessor;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedOutputStream;
import java.io.PushbackInputStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.zip.GZIPInputStream;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

/**
 * This class will process xml files. It handles file IO as well as the xml
 * stream. If output is to be generated, then it will be gzipped. Input is
 * transparently uncompressed if necessary.
 * 
 * This code is licensed under the BSD license.
 * 
 * @author Christof Schulze
 * @version 0.1
 * 
 */
public abstract class XmlProcessor {

	/**
	 * This method will be called when characters are found in the xml
	 * input-stream between any two xml tags.
	 * 
	 * @param chars
	 *            the characters that are found
	 * @throws XmlProcessorException
	 */
	protected abstract void characters(String chars)
			throws XmlProcessorException;

	/**
	 * This method will be called when an opening element is found in the xml
	 * input-stream.
	 * 
	 * @param name
	 *            name of the element
	 * @throws XmlProcessorException
	 */
	protected abstract void startelement(String name)
			throws XmlProcessorException;

	/**
	 * This method will be called when a closing element is found in the xml
	 * input-stream.
	 * 
	 * @param name
	 *            name of the element
	 * @throws XmlProcessorException
	 */
	protected abstract void endelement(String name)
			throws XmlProcessorException;

	/**
	 * This method will be called when an attribute is found in the xmls
	 * input-stream inside an xml element.
	 * 
	 * @param name
	 *            name of the attribute
	 * @param content
	 *            value of the attribute
	 * @throws XmlProcessorException
	 */
	protected abstract void attribut(String name, String content)
			throws XmlProcessorException;


	private PipedOutputStream os = null;
	private XMLStreamWriter writer = null;
	private XMLOutputFactory wfactory = null;
	private XMLStreamReader parser = null;
	private XMLInputFactory factory = null;
	private Boolean readonly = false;
	private GzipThread gzipThread = null;

	public void cleanup() {

		if (writer != null)
			try {
				writer.flush();
			} catch (Exception ignored) {
			}
		try {
			writer.close();
		} catch (Exception ignored) {
		}

		if (wfactory != null)
			wfactory = null;
		if (parser != null)
			parser = null;
		if (factory != null)
			factory = null;
		if (os != null) {
			try {
				os.flush();
			} catch (Exception ignored) {
			}
			try {
				os.close();
			} catch (Exception ignored) {
			}
			os = null;
		}
	}

	private static InputStream decompressStream(InputStream input)
			throws IOException {
		// transparently decompress a potentially gzipped xml stream
		PushbackInputStream pb = new PushbackInputStream(input, 2);

		byte[] signature = new byte[2];
		pb.read(signature); // read the signature
		pb.unread(signature); // push back the signature to the stream
		if (signature[0] == (byte) 0x1f && signature[1] == (byte) 0x8b)
			// check if matches standard gzip magic number
			return new GZIPInputStream(pb);
		else
			return pb;
	}

	public void init(String filename) throws XmlProcessorException {
		readonly = true;
		this.init(filename, null);
	}

	protected boolean threaderror = false;
	protected Exception threadexception = null;

	public void init(String filename, String outfile)
			throws XmlProcessorException {
		InputStream is = null;
		threadexception = new Exception();
		try {
			is = decompressStream(new FileInputStream(filename));

			if (!readonly) {
				if (outfile.substring(outfile.length() - 3, outfile.length())
						.compareTo(".gz") == 1) {
					outfile = outfile + ".gz";
				}

				os = new PipedOutputStream();
				gzipThread = new GzipThread(outfile);
				GzipThread
				.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {

					@Override
					public void uncaughtException(Thread t, Throwable e) {
						System.err
						.println("uncaught exception in Thred GzipOutFile");
						threadexception = (Exception) (e);
						e.printStackTrace();
						threaderror = true;
					}
				});
				gzipThread.init(os);
				wfactory = XMLOutputFactory.newInstance();
				wfactory.setProperty("escapeCharacters", false);
				// wfactory = XMLOutputFactory.newInstance();
				writer = wfactory.createXMLStreamWriter(os);
				writer.writeStartDocument();

				gzipThread.start();
			}

			factory = XMLInputFactory.newInstance();
			parser = factory.createXMLStreamReader(is);

		} catch (Exception e1) {
			// input file not found or io broken
			e1.printStackTrace();
			cleanup();
			throw new XmlProcessorException("cannot initialize XmlProcessor",
					e1);
		}
	}
	public void dowork() throws XmlProcessorException {
		String prefix = new String("");
		
		try {
			while (parser.hasNext() && (threaderror == false)) {
				int event = parser.next();
				switch (event) {
				case XMLStreamConstants.END_DOCUMENT :
					if (!readonly) {
						writer.writeEndDocument();
						os.flush();
						os.close();
						gzipThread.join();
					}
					parser.close();
					break;
				case XMLStreamConstants.START_ELEMENT :
					if (parser.hasName()) {
						boolean attr = false;
						
						if (parser.getPrefix().length() > 0)
						{
							if  ( prefix.isEmpty() ) {
								attr = true;
							}	
							prefix = parser.getPrefix() + ":";
						}
						startelement(prefix + parser.getLocalName().toString());
						if (attr)
							attribut("xmlns:" + parser.getPrefix(),  parser.getNamespaceURI());
						for (int i = 0; i < parser.getAttributeCount(); i++) {
							attribut(parser.getAttributeLocalName(i),
									parser.getAttributeValue(i));
						}
					
					}
					break;
				case XMLStreamConstants.CHARACTERS :
					if (!parser.isWhiteSpace()) {
						characters(parser.getText());
					}
					break;
				case XMLStreamConstants.END_ELEMENT :
					if (parser.hasName())
						endelement(parser.getLocalName());
					break;
				default :
					break;
				}
			}
			if (threaderror) {
				cleanup();
				throw new XmlProcessorException(
						"error when writing output stream", threadexception);
			}
		} catch (XMLStreamException e) {
			gzipThread.stopprocessing();
			cleanup();
			try {
				gzipThread.join();
			} catch (InterruptedException e1) {
				// this thread is never interrupted
				e1.printStackTrace();
			}

			throw new XmlProcessorException("Error when reading input file.", e);
		} catch (IOException e) {
			gzipThread.stopprocessing();
			cleanup();

			throw new XmlProcessorException(
					"Error when writing output stream.", e);
		} catch (InterruptedException ignore) {
			// this never happens
			ignore.printStackTrace();
		} finally {
			cleanup();
		}
	}

	/**
	 * this will close the current xml-element in the output stream. The output
	 * stream will be valid xml.
	 * 
	 * @throws XmlProcessorException
	 */
	protected void writeEndElement() throws XmlProcessorException {
		try {
			writer.writeEndElement();
		} catch (XMLStreamException e) {
			cleanup();
			throw new XmlProcessorException("Could not write to output stream",
					e);
		}
	}

	/**
	 * this will write the characters element from the input stream to the
	 * output stream. The output stream will be valid xml.
	 * 
	 * @param characters
	 *            chars that will be written to the stream
	 * @throws XmlProcessorException
	 */
	protected void writeChars(String characters) throws XmlProcessorException {
		try {
			writer.writeCharacters(characters);
		} catch (XMLStreamException e) {
			cleanup();
			throw new XmlProcessorException("Could not write to output stream",
					e);
		}
	}

	/**
	 * This will create an opening element in the output xml stream.
	 * 
	 * @param name
	 *            Name of the element
	 * @throws XmlProcessorException
	 */
	protected void writeStartElement(String localName)
			throws XmlProcessorException {
		try {
			writer.writeStartElement(localName);
		} catch (XMLStreamException e) {
			cleanup();
			throw new XmlProcessorException("Could not write to output stream",
					e);
		}
	}

	/**
	 * After an element has been opened in the output stream several attributes
	 * may be written to it. wattribute() does exactly that.
	 * 
	 * @param attributeLocalName
	 *            Name of the attribute
	 * @param attval
	 *            value of the attribute
	 * @throws XmlProcessorException
	 */
	protected void writeAttribute(String attributeLocalName, String attval)
			throws XmlProcessorException {
		try {
			writer.writeAttribute(attributeLocalName, attval);
		} catch (XMLStreamException e) {
			cleanup();
			throw new XmlProcessorException("Could not write to output stream",
					e);
		}
	}
}



