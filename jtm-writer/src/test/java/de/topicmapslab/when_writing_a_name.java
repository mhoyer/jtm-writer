package de.topicmapslab;

import org.codehaus.jackson.JsonNode;
import org.junit.Before;
import org.junit.Test;
import org.tmapi.core.Name;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junitx.framework.ComparableAssert.assertGreater;
import static org.junit.Assert.assertTrue;

/**
 * Author:  mhoyer
 * Created: 27.10.2010 23:39:36
 */
public class when_writing_a_name extends with_JTMWriter {
    private Name name;

    @Before public void given_a_string_name() {
        name = createName(getClass().getSimpleName());
    }
    
    @Test public void it_should_write_the_version_and_item_type() throws IOException {
        JsonNode node = writeAndRead(name);
        assertEquals(1.0, node.get("version").getValueAsDouble());
        assertItemType(name, node);
    }

    @Test public void it_should_write_value() throws IOException {
        JsonNode node = writeAndRead(name);
        assertEquals(name.getValue(), node.get("value").getTextValue());
    }

    @Test public void it_should_write_values_with_special_chars() throws IOException {
        name = name.getParent().createName("צהü");
        JsonNode node = writeAndRead(name);
        assertEquals(name.getValue(), node.get("value").getTextValue());
    }

    @Test public void it_should_write_type() throws IOException {
        JsonNode node = writeAndRead(name);
        assertEquals(
                "si:"+ name.getType().getSubjectIdentifiers().iterator().next().getReference(),
                node.get("type").getTextValue());
    }

    @Test public void it_should_not_write_the_empty_scope_array() throws IOException {
        JsonNode node = writeAndRead(name);

        assertNull(node.get("scope"));
    }

    @Test public void it_should_write_a_non_empty_scope_array() throws IOException {
        name.addTheme(createSimpleNamedTopic("it_should_write_a_non_empty_scope_array"));
        JsonNode node = writeAndRead(name);

        assertTrue(node.get("scope").isArray());
        assertGreater(0, node.get("scope").size());
    }

    @Test public void it_should_not_write_the_empty_variants_array() throws IOException {
        JsonNode node = writeAndRead(name);

        assertNull(node.get("variants"));
    }

    @Test public void it_should_write_a_non_empty_variants_array() throws IOException {
        Name nameWithVariant = createVariant("it_should_write_a_non_empty_variants_array").getParent();
        JsonNode node = writeAndRead(nameWithVariant);

        assertTrue(node.get("variants").isArray());
        assertGreater(0, node.get("variants").size());
        assertNull(node.get("variants").get(0).get("version"));
    }

    @Test public void it_should_not_write_null_reifier() throws IOException {
        JsonNode node = writeAndRead(name);
        assertNull(node.get("reifier"));
    }

    @Test public void it_should_write_reifier() throws IOException {
        name.setReifier(createSimpleNamedTopic(getClass().getSimpleName()));
        JsonNode node = writeAndRead(name);
        assertEquals(
                "si:"+ name.getReifier().getSubjectIdentifiers().iterator().next().getReference(),
                node.get("reifier").getTextValue());
    }

    @Test public void it_should_not_write_the_empty_item_identifiers() throws IOException {
        JsonNode node = writeAndRead(name);
        assertEquals(0, name.getItemIdentifiers().size());
        assertNull(node.get("item_identifiers"));
    }

    @Test public void it_should_write_the_item_identifiers() throws IOException {
        name.addItemIdentifier(createLocator("it_should_write_the_item_identifiers"));
        JsonNode node = writeAndRead(name);

        assertTrue(node.get("item_identifiers").isArray());
        assertGreater(0, node.get("item_identifiers").size());
        assertEquals(
                name.getItemIdentifiers().iterator().next().getReference(),
                node.get("item_identifiers").get(0).getTextValue());
    }
}
