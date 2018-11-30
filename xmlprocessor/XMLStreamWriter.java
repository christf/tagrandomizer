package xmlprocessor;

//Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
//Jad home page: http://www.geocities.com/kpdus/jad.html
//Decompiler options: packimports(3) 
//Source File Name:   XMLStreamWriter.java

import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLStreamException;

//Referenced classes of package javax.xml.stream:
//         XMLStreamException

public interface XMLStreamWriter {

	public abstract void writeStartElement(String s) throws XMLStreamException;

	public abstract void writeStartElement(String s, String s1)
			throws XMLStreamException;

	public abstract void writeStartElement(String s, String s1, String s2)
			throws XMLStreamException;

	public abstract void writeEmptyElement(String s, String s1)
			throws XMLStreamException;

	public abstract void writeEmptyElement(String s, String s1, String s2)
			throws XMLStreamException;

	public abstract void writeEmptyElement(String s) throws XMLStreamException;

	public abstract void writeEndElement() throws XMLStreamException;

	public abstract void writeEndDocument() throws XMLStreamException;

	public abstract void close() throws XMLStreamException;

	public abstract void flush() throws XMLStreamException;

	public abstract void writeAttribute(String s, String s1)
			throws XMLStreamException;

	public abstract void writeAttribute(String s, String s1, String s2,
			String s3) throws XMLStreamException;

	public abstract void writeAttribute(String s, String s1, String s2)
			throws XMLStreamException;

	public abstract void writeNamespace(String s, String s1)
			throws XMLStreamException;

	public abstract void writeDefaultNamespace(String s)
			throws XMLStreamException;

	public abstract void writeComment(String s) throws XMLStreamException;

	public abstract void writeProcessingInstruction(String s)
			throws XMLStreamException;

	public abstract void writeProcessingInstruction(String s, String s1)
			throws XMLStreamException;

	public abstract void writeCData(String s) throws XMLStreamException;

	public abstract void writeDTD(String s) throws XMLStreamException;

	public abstract void writeEntityRef(String s) throws XMLStreamException;

	public abstract void writeStartDocument() throws XMLStreamException;

	public abstract void writeStartDocument(String s) throws XMLStreamException;

	public abstract void writeStartDocument(String s, String s1)
			throws XMLStreamException;

	public abstract void writeCharacters(String s) throws XMLStreamException;

	public abstract void writeCharacters(char ac[], int i, int j)
			throws XMLStreamException;

	public abstract String getPrefix(String s) throws XMLStreamException;

	public abstract void setPrefix(String s, String s1)
			throws XMLStreamException;

	public abstract void setDefaultNamespace(String s)
			throws XMLStreamException;

	public abstract void setNamespaceContext(NamespaceContext namespacecontext)
			throws XMLStreamException;

	public abstract NamespaceContext getNamespaceContext();

	public abstract Object getProperty(String s)
			throws IllegalArgumentException;
}
