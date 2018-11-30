package xmlprocessor;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author Christof Schulze
 * 
 */

// TODO exception für gzipoutputstream behandeln.
public class GzipThread extends Thread {

	final String outfilename;
	private boolean stopthread = false;
	private PipedInputStream pis = null;

	/**
	 * @param outfilename
	 */
	public GzipThread(String outfilename) {
		this.outfilename = outfilename;
		super.setName("GzipOutFile");
	}

	/**
	 * @param pos
	 * @throws IOException
	 */
	public void init(PipedOutputStream pos) throws IOException {
		pis = new PipedInputStream(pos, 1024 * 1024);
	}

	/**
	 * This will tell the thread to stop processing even if there has not been
	 * an EOF on the inputstream
	 */
	public synchronized void stopprocessing() {
		stopthread = true;
	}

	private void cleanup() {
		if (pis != null) {
			try {
				pis.close();
			} catch (Exception ignore) {
			}
			pis = null;
		}
	}

	public void run() {
		GZIPOutputStream gos = null;
		// FileOutputStream gos = null;
		try {
			gos = new GZIPOutputStream(new FileOutputStream(outfilename));
			// gos = new FileOutputStream(outfilename);
			final int bsize = 1024 * 1024;
			final int rsize = bsize / 4 * 3;
			byte[] bytes = new byte[bsize];
			int len = 0;
			while (stopthread == false && len != -1) {
				len = pis.read(bytes, 0, rsize);
				if (len > 0)
					gos.write(bytes, 0, len);
			}
		} catch (IOException e) {
			stopthread = true;
			try {
				gos.flush();
			} catch (IOException e1) {
			}
			try {
				gos.close();
			} catch (IOException e1) {
			}

			cleanup();
			throw new RuntimeException("error while writing to output file "
					+ outfilename, e);
		} finally {

			try {
				gos.finish();
			} catch (IOException e) {
				cleanup();
				throw new RuntimeException(
						"error while writing to output stream " + outfilename,
						e);
			}
			try {
				gos.close();
			} catch (IOException e) {
				// TODO thread cleanup, close outputstreams, throw
				// exception upwards
				cleanup();
				e.printStackTrace();
				throw new RuntimeException(
						"error while writing to output stream " + outfilename,
						e);
			}
			cleanup();
		}
	}
}
