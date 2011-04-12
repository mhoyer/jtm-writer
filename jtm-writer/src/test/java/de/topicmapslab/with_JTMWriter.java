package de.topicmapslab;

import de.topicmapslab.jtm.writer.JTMVersion;
import de.topicmapslab.jtm.writer.JTMWriter;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.tmapi.core.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;

import static junit.framework.Assert.assertEquals;

/**
 * Author: mhoyer Created: 27.10.2010 17:39:06
 */
public class with_JTMWriter extends with_TopicMap {
	protected ByteArrayOutputStream out;
	protected JTMWriter jtmwriter;
	protected ObjectMapper jsonReader;

	@Before
	public void given_JTMWriter() {
		out = new ByteArrayOutputStream();
		jtmwriter = new JTMWriter(out);
	}

	@Before
	public void given_JSON_reader() {
		jsonReader = new ObjectMapper();
	}

	protected void assertItemType(Construct construct, JsonNode node) {
		if (construct instanceof Variant) {
			assertEquals("variant", node.get("item_type").getValueAsText());
		}
		if (construct instanceof Name) {
			assertEquals("name", node.get("item_type").getValueAsText());
		}
		if (construct instanceof Occurrence) {
			assertEquals("occurrence", node.get("item_type").getValueAsText());
		}
		if (construct instanceof Topic) {
			assertEquals("topic", node.get("item_type").getValueAsText());
		}
		if (construct instanceof TopicMap) {
			assertEquals("topicmap", node.get("item_type").getValueAsText());
		}
		if (construct instanceof Association) {
			assertEquals("association", node.get("item_type").getValueAsText());
		}
		if (construct instanceof Role) {
			assertEquals("role", node.get("item_type").getValueAsText());
		}
	}

	protected JsonNode writeAndRead(Construct construct, JTMVersion version) throws IOException {
		jtmwriter.write(construct, version).flush();
		return jsonReader.readTree(out.toString("UTF-8"));
	}

	protected JsonNode writeAndRead(Construct construct) throws IOException {
		return writeAndRead(construct, JTMVersion.JTM_1_0);
	}

	protected JsonNode writeAndRead(Collection<Construct> constructs) throws IOException {
		jtmwriter.write(constructs, JTMVersion.JTM_1_0).flush();
		return jsonReader.readTree(out.toString("UTF-8"));
	}
}
