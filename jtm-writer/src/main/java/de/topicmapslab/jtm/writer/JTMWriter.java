package de.topicmapslab.jtm.writer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

import org.tmapi.core.Construct;

/**
 * Author: mhoyer Created: 27.10.2010 17:25:20
 */
public class JTMWriter {

	private final OutputStream out;
	private final JTMMapper mapper;

	/**
	 * constructor
	 * 
	 * @param outputStream
	 *            the output stream
	 */
	public JTMWriter(OutputStream outputStream) {
		out = outputStream;

		mapper = new JTMMapper();
	}

	/**
	 * Writes the given construct to JSON
	 * 
	 * @param construct
	 *            the construct
	 * @throws IOException
	 *             thrown if an I/O error occur
	 * @deprecated will be removed use {@link #write(Construct, JTMVersion)}
	 */
	@Deprecated
	public JTMWriter write(Construct construct) throws IOException {
		mapper.writeValue(out, construct);

		return this;
	}

	/**
	 * Writes the given constructs to JSON
	 * 
	 * @param constructs
	 *            the constructs
	 * @throws IOException
	 *             thrown if an I/O error occur
	 * @deprecated will be removed use {@link #write(Collection, JTMVersion)}
	 */
	@Deprecated
	public JTMWriter write(Collection<? extends Construct> constructs) throws IOException {
		mapper.writeValue(out, constructs.toArray(new Construct[constructs.size()]));

		return this;
	}

	/**
	 * Writes the given construct to JSON
	 * 
	 * @param construct
	 *            the construct
	 * @param version
	 *            the JTM version
	 * @throws IOException
	 *             thrown if an I/O error occur
	 */
	public JTMWriter write(Construct construct, JTMVersion version) throws IOException {
		mapper.setVersion(version);
		mapper.writeValue(out, construct);

		return this;
	}

	/**
	 * Writes the given constructs to JSON
	 * 
	 * @param constructs
	 *            the constructs
	 * @param version
	 *            the JTM version
	 * @throws IOException
	 *             thrown if an I/O error occur
	 */
	public JTMWriter write(Collection<? extends Construct> constructs, JTMVersion version) throws IOException {
		mapper.setVersion(version);
		mapper.writeValue(out, constructs.toArray(new Construct[constructs.size()]));

		return this;
	}

	/**
	 * Flush the internal writer
	 */
	public JTMWriter flush() {
		try {
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return this;
	}

	/**
	 * Returns the given construct as JSON string
	 * 
	 * @param construct
	 *            the construct
	 * @return the JSON string
	 * @throws IOException
	 *             thrown if an I/O error occur
	 * @deprecated will be removed, use {@link #getJson(Construct, JTMVersion)}
	 */
	@Deprecated
	public static String getJson(Construct construct) throws IOException {
		return getJsonAsStream(construct).toString();
	}

	/**
	 * Returns the given construct as JSON byte array
	 * 
	 * @param construct
	 *            the construct
	 * @return the JSON byte array
	 * @throws IOException
	 *             thrown if an I/O error occur
	 * @deprecated will be removed, use {@link #getJsonAsByteArray(Construct, JTMVersion)}
	 */
	@Deprecated
	public static byte[] getJsonAsByteArray(Construct construct) throws IOException {
		return getJsonAsStream(construct).toByteArray();
	}

	/**
	 * Returns the given construct as JSON byte array stream
	 * 
	 * @param construct
	 *            the construct
	 * @return the JSON byte array stream
	 * @throws IOException
	 *             thrown if an I/O error occur
	 * @deprecated will be removed, use {@link #getJsonAsStream(Construct, JTMVersion)}
	 */
	@Deprecated
	public static ByteArrayOutputStream getJsonAsStream(Construct construct) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		new JTMWriter(buffer).write(construct).flush();

		return buffer;
	}

	/**
	 * Returns the given construct as JSON string
	 * 
	 * @param construct
	 *            the construct
	 * @param version
	 *            the JTM version
	 * @return the JSON string
	 * @throws IOException
	 *             thrown if an I/O error occur
	 */
	public static String getJson(Construct construct, JTMVersion version) throws IOException {
		return getJsonAsStream(construct, version).toString();
	}

	/**
	 * Returns the given construct as JSON byte array
	 * 
	 * @param construct
	 *            the construct
	 * @param version
	 *            the JTM version
	 * @return the JSON byte array
	 * @throws IOException
	 *             thrown if an I/O error occur
	 */
	public static byte[] getJsonAsByteArray(Construct construct, JTMVersion version) throws IOException {
		return getJsonAsStream(construct, version).toByteArray();
	}

	/**
	 * Returns the given construct as JSON byte array stream
	 * 
	 * @param construct
	 *            the construct
	 * @param version
	 *            the JTM version
	 * @return the JSON byte array stream
	 * @throws IOException
	 *             thrown if an I/O error occur
	 */
	public static ByteArrayOutputStream getJsonAsStream(Construct construct, JTMVersion version) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		new JTMWriter(buffer).write(construct, version).flush();

		return buffer;
	}
}
