package de.topicmapslab;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junitx.framework.ComparableAssert.assertGreater;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.codehaus.jackson.JsonNode;
import org.junit.Test;
import org.tmapi.core.Topic;

import de.topicmapslab.jtm.writer.IJTMConstants;
import de.topicmapslab.jtm.writer.JTMVersion;

/**
 * Author: mhoyer Created: 28.10.2010 01:14
 */
public class when_writing_a_topic_map extends with_JTMWriter {

	@Test
	public void it_should_write_the_version_and_item_type() throws IOException {
		JsonNode node = writeAndRead(topicMap);
		assertEquals(1.0, node.get("version").getValueAsDouble());
		assertItemType(topicMap, node);
	}

	@Test
	public void it_should_not_write_the_empty_topics_array() throws IOException {
		JsonNode node = writeAndRead(topicMap);

		assertNull(node.get("topics"));
	}

	@Test
	public void it_should_write_a_version() throws IOException {
		topicMap.createTopic();
		JsonNode node = writeAndRead(topicMap, JTMVersion.JTM_1_1);
		assertEquals(IJTMConstants.VERSION_11, node.get(IJTMConstants.VERSION).getTextValue());
	}

	@Test
	public void it_should_write_a_non_empty_instance_of_array() throws IOException {
		Topic t = topicMap.createTopicBySubjectIdentifier(topicMap.createLocator("http://topic-with-si"));
		Topic type = topicMap.createTopicBySubjectIdentifier(topicMap.createLocator("http://type-with-si"));
		t.addType(type);
		JsonNode node = writeAndRead(topicMap, JTMVersion.JTM_1_1);
		assertTrue(node.get(IJTMConstants.TOPICS).isArray());
		assertGreater(2, node.get(IJTMConstants.TOPICS).size());
		boolean found = false;
		for (JsonNode n : node.get(IJTMConstants.TOPICS)) {
			if (n.has(IJTMConstants.INSTANCE_OF)) {
				found = true;
				assertTrue(n.get(IJTMConstants.INSTANCE_OF).isArray());
				assertEquals(1, n.get(IJTMConstants.INSTANCE_OF).size());
				assertEquals("si:http://type-with-si", n.get(IJTMConstants.INSTANCE_OF).get(0).getTextValue());
			}
		}
		assertTrue("At least one topic should contain an instance_of topic array", found);
	}

	@Test
	public void it_should_write_a_non_empty_topics_array() throws IOException {
		topicMap.createTopic();
		JsonNode node = writeAndRead(topicMap);

		assertTrue(node.get("topics").isArray());
		assertGreater(0, node.get("topics").size());
		assertNull(node.get("topics").get(0).get("version"));
	}

	@Test
	public void it_should_not_write_the_empty_associations_array() throws IOException {
		JsonNode node = writeAndRead(topicMap);

		assertNull(node.get("associations"));
	}

	@Test
	public void it_should_write_a_non_empty_associations_array() throws IOException {
		topicMap.createAssociation(topicMap.createTopic()).createRole(topicMap.createTopic(), topicMap.createTopic());
		JsonNode node = writeAndRead(topicMap);

		assertTrue(node.get("associations").isArray());
		assertGreater(0, node.get("associations").size());
		assertNull(node.get("associations").get(0).get("version"));
	}

	@Test
	public void it_should_not_write_the_empty_item_identifiers() throws IOException {
		JsonNode node = writeAndRead(topicMap);
		assertEquals(0, topicMap.getItemIdentifiers().size());
		assertNull(node.get("item_identifiers"));
	}

	@Test
	public void it_should_write_the_item_identifiers() throws IOException {
		topicMap.addItemIdentifier(tms.createLocator("fubar://it_should_write_the_item_identifiers"));
		JsonNode node = writeAndRead(topicMap);

		assertTrue(node.get("item_identifiers").isArray());
		assertGreater(0, node.get("item_identifiers").size());
		assertEquals(topicMap.getItemIdentifiers().iterator().next().getReference(), node.get("item_identifiers").get(0).getTextValue());
	}

	@Test
	public void it_should_not_write_null_reifier() throws IOException {
		JsonNode node = writeAndRead(topicMap);
		assertNull(node.get("reifier"));
	}

	@Test
	public void it_should_write_reifier() throws IOException {
		topicMap.setReifier(createSimpleNamedTopic(getClass().getSimpleName()));
		JsonNode node = writeAndRead(topicMap);
		assertEquals("si:" + topicMap.getReifier().getSubjectIdentifiers().iterator().next().getReference(), node.get("reifier").getTextValue());
	}

}
