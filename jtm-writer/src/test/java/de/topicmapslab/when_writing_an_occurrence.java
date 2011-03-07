package de.topicmapslab;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junitx.framework.ComparableAssert.assertGreater;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.codehaus.jackson.JsonNode;
import org.junit.Before;
import org.junit.Test;
import org.tmapi.core.Occurrence;

import de.topicmapslab.majortom.model.namespace.Namespaces;

/**
 * Author:  mhoyer
 * Created: 27.10.2010 23:56:33
 */
public class when_writing_an_occurrence extends with_JTMWriter {
    private Occurrence stringOccurrence;
    private Occurrence locatorOccurrence;

    @Before public void given_a_string_occurrence() {
        stringOccurrence = createOccurrence(getClass().getSimpleName());
    }
    
    @Before public void given_a_locator_occurrence() {
        locatorOccurrence = createOccurrence("f");
        locatorOccurrence.setValue( topicMap.createLocator(Namespaces.XSD.ANY));
    }

    @Test public void it_should_write_the_version_and_item_type() throws IOException {
        JsonNode node = writeAndRead(stringOccurrence);
        assertEquals(1.0, node.get("version").getValueAsDouble());
        assertItemType(stringOccurrence, node);
    }

    @Test public void it_should_not_write_the_datatype_for_string_occurrences() throws IOException {
        JsonNode node = writeAndRead(stringOccurrence);
        assertNull(node.get("datatype"));
    }

    @Test public void it_should_write_the_datatype_for_non_string_occurrences() throws IOException {
        JsonNode node = writeAndRead(locatorOccurrence);
        assertEquals(Namespaces.XSD.ANYURI, node.get("datatype").getTextValue());
    }

    @Test public void it_should_not_write_the_empty_scope_array() throws IOException {
        JsonNode node = writeAndRead(stringOccurrence);

        assertNull(node.get("scope"));
    }

    @Test public void it_should_write_a_non_empty_scope_array() throws IOException {
        stringOccurrence.addTheme(createSimpleNamedTopic("it_should_write_a_non_empty_scope_array"));
        JsonNode node = writeAndRead(stringOccurrence);

        assertTrue(node.get("scope").isArray());
        assertGreater(0, node.get("scope").size());
    }

    @Test public void it_should_write_value() throws IOException {
        JsonNode node = writeAndRead(stringOccurrence);
        assertEquals(stringOccurrence.getValue(), node.get("value").getTextValue());
    }
    
    @Test public void it_should_write_id() throws IOException {
        JsonNode node = writeAndRead(stringOccurrence);
        assertEquals(stringOccurrence.getId(), node.get("id").getTextValue());
    }

    @Test public void it_should_write_type() throws IOException {
        JsonNode node = writeAndRead(stringOccurrence);
        assertEquals(
                "si:"+ stringOccurrence.getType().getSubjectIdentifiers().iterator().next().getReference(),
                node.get("type").getTextValue());
    }

    @Test public void it_should_not_write_null_reifier() throws IOException {
        JsonNode node = writeAndRead(stringOccurrence);
        assertNull(node.get("reifier"));
    }

    @Test public void it_should_write_reifier() throws IOException {
        stringOccurrence.setReifier(createSimpleNamedTopic(getClass().getSimpleName()));
        JsonNode node = writeAndRead(stringOccurrence);
        assertEquals(
                "si:"+ stringOccurrence.getReifier().getSubjectIdentifiers().iterator().next().getReference(),
                node.get("reifier").getTextValue());
    }

    @Test public void it_should_not_write_the_empty_item_identifiers() throws IOException {
        JsonNode node = writeAndRead(stringOccurrence);
        assertEquals(0, stringOccurrence.getItemIdentifiers().size());
        assertNull(node.get("item_identifiers"));
    }

    @Test public void it_should_write_the_item_identifiers() throws IOException {
        stringOccurrence.addItemIdentifier(createLocator("it_should_write_the_item_identifiers"));
        JsonNode node = writeAndRead(stringOccurrence);

        assertTrue(node.get("item_identifiers").isArray());
        assertGreater(0, node.get("item_identifiers").size());
        assertEquals(
                stringOccurrence.getItemIdentifiers().iterator().next().getReference(),
                node.get("item_identifiers").get(0).getTextValue());
    }
}
