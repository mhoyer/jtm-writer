package de.topicmapslab;

import org.codehaus.jackson.JsonNode;
import org.junit.Before;
import org.junit.Test;
import org.tmapi.core.Topic;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junitx.framework.ComparableAssert.assertGreater;
import static org.junit.Assert.assertTrue;

/**
 * Author:  mhoyer
 * Created: 27.10.2010 00:05:04
 */
public class when_writing_a_topic extends with_JTMWriter {
    private Topic topic;

    @Before public void given_a_topic() {
        topic = topicMap.createTopic();
    }
    
    @Test public void it_should_write_the_version_and_item_type() throws IOException {
        JsonNode node = writeAndRead(topic);
        assertEquals(1.0, node.get("version").getValueAsDouble());
        assertItemType(topic, node);
    }

    @Test public void it_should_write_id() throws IOException {
        JsonNode node = writeAndRead(topic);
        assertEquals(topic.getId(), node.get("id").getTextValue());
    }
    
    @Test public void it_should_not_write_the_empty_names_array() throws IOException {
        JsonNode node = writeAndRead(topic);

        assertNull(node.get("names"));
    }

    @Test public void it_should_write_a_non_empty_names_array() throws IOException {
        topic.createName("it_should_write_a_non_empty_names_array");
        JsonNode node = writeAndRead(topic);

        assertTrue(node.get("names").isArray());
        assertGreater(0, node.get("names").size());
        assertNull(node.get("names").get(0).get("version"));
    }

    @Test public void it_should_not_write_the_empty_occurrences_array() throws IOException {
        JsonNode node = writeAndRead(topic);

        assertNull(node.get("names"));
    }

    @Test public void it_should_write_a_non_empty_occurrences_array() throws IOException {
        topic.createOccurrence(topicMap.createTopic(), "it_should_write_a_non_empty_occurrencess_array");
        JsonNode node = writeAndRead(topic);

        assertTrue(node.get("occurrences").isArray());
        assertGreater(0, node.get("occurrences").size());
        assertNull(node.get("occurrences").get(0).get("version"));
    }

    @Test public void it_should_not_write_the_empty_item_identifiers() throws IOException {
        topic.addSubjectIdentifier(tms.createLocator("fubar://it_should_not_write_the_empty_item_identifiers"));
        topic.removeItemIdentifier(topic.getItemIdentifiers().iterator().next());
        JsonNode node = writeAndRead(topic);
        assertEquals(0, topic.getItemIdentifiers().size());
        assertNull(node.get("item_identifiers"));
    }

    @Test public void it_should_write_the_item_identifiers() throws IOException {
        JsonNode node = writeAndRead(topic);

        assertTrue(node.get("item_identifiers").isArray());
        assertGreater(0, node.get("item_identifiers").size());
        assertEquals(
                topic.getItemIdentifiers().iterator().next().getReference(),
                node.get("item_identifiers").get(0).getTextValue());
    }

    @Test public void it_should_not_write_the_empty_subject_identifiers() throws IOException {
        JsonNode node = writeAndRead(topic);
        assertEquals(0, topic.getSubjectIdentifiers().size());
        assertNull(node.get("subject_identifiers"));
    }

    @Test public void it_should_write_the_subject_identifiers() throws IOException {
        topic.addSubjectIdentifier(tms.createLocator("fubar://it_should_write_the_subject_identifiers"));
        JsonNode node = writeAndRead(topic);

        assertTrue(node.get("subject_identifiers").isArray());
        assertGreater(0, node.get("subject_identifiers").size());
        assertEquals(
                topic.getSubjectIdentifiers().iterator().next().getReference(),
                node.get("subject_identifiers").get(0).getTextValue());
    }

    @Test public void it_should_not_write_the_empty_subject_locators() throws IOException {
        JsonNode node = writeAndRead(topic);
        assertEquals(0, topic.getSubjectLocators().size());
        assertNull(node.get("subject_locators"));
    }

    @Test public void it_should_write_the_subject_locators() throws IOException {
        topic.addSubjectLocator(tms.createLocator("fubar://it_should_write_the_subject_locators"));
        JsonNode node = writeAndRead(topic);

        assertTrue(node.get("subject_locators").isArray());
        assertGreater(0, node.get("subject_locators").size());
        assertEquals(
                topic.getSubjectLocators().iterator().next().getReference(),
                node.get("subject_locators").get(0).getTextValue());
    }

}
