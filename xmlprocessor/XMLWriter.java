// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   XMLWriter.java

//package com.sun.xml.internal.stream.writers;
package xmlprocessor;

import java.io.IOException;
import java.io.Writer;

import com.sun.org.apache.xerces.internal.util.XMLStringBuffer;

@SuppressWarnings({"javadoc", "restriction"})
public class XMLWriter extends Writer {

	public XMLWriter(Writer writer) {
		this(writer, 4096);
	}

	public XMLWriter(Writer writer, int size) {
		buffer = new XMLStringBuffer(12288);
		this.writer = writer;
		this.size = size;
	}

	public void write(int c) throws IOException {
		ensureOpen();
		buffer.append((char) c);
		conditionalWrite();
	}

	public void write(char cbuf[]) throws IOException {
		write(cbuf, 0, cbuf.length);
	}

	public void write(char cbuf[], int off, int len) throws IOException {
		ensureOpen();
		if (len > size) {
			writeBufferedData();
			writer.write(cbuf, off, len);
		} else {
			buffer.append(cbuf, off, len);
			conditionalWrite();
		}
	}

	public void write(String str, int off, int len) throws IOException {
		write(str.toCharArray(), off, len);
	}

	public void write(String str) throws IOException {
		if (str.length() > size) {
			writeBufferedData();
			writer.write(str);
		} else {
			buffer.append(str);
			conditionalWrite();
		}
	}

	public void close() throws IOException {
		if (writer == null) {
			return;
		} else {
			flush();
			writer.close();
			writer = null;
			return;
		}
	}

	public void flush() throws IOException {
		ensureOpen();
		writeBufferedData();
		writer.flush();
	}

	public void reset() {
		writer = null;
		buffer.clear();
		size = 4096;
	}

	public void setWriter(Writer writer) {
		this.writer = writer;
		buffer.clear();
		size = 4096;
	}

	public void setWriter(Writer writer, int size) {
		this.writer = writer;
		this.size = size;
	}

	protected Writer getWriter() {
		return writer;
	}

	private void conditionalWrite() throws IOException {
		if (buffer.length > size)
			writeBufferedData();
	}

	private void writeBufferedData() throws IOException {
		writer.write(buffer.ch, buffer.offset, buffer.length);
		buffer.clear();
	}

	private void ensureOpen() throws IOException {
		if (writer == null)
			throw new IOException("Stream closed");
		else
			return;
	}

	private Writer writer;
	private int size;
	private XMLStringBuffer buffer;
	@SuppressWarnings("unused")
	private static final int THRESHHOLD_LENGTH = 4096;
	@SuppressWarnings("unused")
	private static final boolean DEBUG = false;
}
