package de.topicmapslab;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junitx.framework.ComparableAssert.assertGreater;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.codehaus.jackson.JsonNode;
import org.junit.Before;
import org.junit.Test;
import org.tmapi.core.Topic;
import org.tmapi.core.Variant;

import de.topicmapslab.majortom.model.namespace.Namespaces;

/**
 * Author:  mhoyer
 * Created: 27.10.2010 17:39:06
 */
public class when_writing_a_variant extends with_JTMWriter {
    private Variant stringVariant;
    private Variant locatorVariant;

    @Before public void given_a_string_variant() {
        stringVariant = createVariant(getClass().getSimpleName());
    }
    
    @Before public void given_a_locator_variant() {
        locatorVariant = createVariant("f");
        locatorVariant.setValue(topicMap.createLocator(Namespaces.XSD.ANY));
    }
    
    @Test public void it_should_write_id() throws IOException {
        JsonNode node = writeAndRead(stringVariant);
        assertEquals(stringVariant.getId(), node.get("id").getTextValue());
    }

    @Test public void it_should_write_the_version_and_item_type() throws IOException {
        JsonNode node = writeAndRead(stringVariant);
        assertEquals(1.0, node.get("version").getValueAsDouble());
        assertItemType(stringVariant, node);
    }

    @Test public void it_should_not_write_the_datatype_for_string_variants() throws IOException {
        JsonNode node = writeAndRead(stringVariant);
        assertNull(node.get("datatype"));
    }

    @Test public void it_should_write_the_datatype_for_non_string_variants() throws IOException {
        JsonNode node = writeAndRead(locatorVariant);
        assertEquals(Namespaces.XSD.ANYURI, node.get("datatype").getTextValue());
    }

    @Test public void it_should_write_the_empty_scope_array() throws IOException {
        Topic theme = stringVariant.getScope().iterator().next();
        stringVariant.removeTheme(theme);
        JsonNode node = writeAndRead(stringVariant);

        assertTrue(node.get("scope").isArray());
        assertEquals(0, node.get("scope").size());
    }

    @Test public void it_should_write_a_non_empty_scope_array() throws IOException {
        JsonNode node = writeAndRead(stringVariant);

        assertTrue(node.get("scope").isArray());
        assertGreater(0, node.get("scope").size());
    }

    @Test public void it_should_write_value() throws IOException {
        JsonNode node = writeAndRead(stringVariant);
        assertEquals(stringVariant.getValue(), node.get("value").getTextValue());
    }

    @Test public void it_should_not_write_null_reifier() throws IOException {
        JsonNode node = writeAndRead(stringVariant);
        assertNull(node.get("reifier"));
    }

    @Test public void it_should_write_reifier() throws IOException {
        stringVariant.setReifier(createSimpleNamedTopic(getClass().getSimpleName()));
        JsonNode node = writeAndRead(stringVariant);
        assertEquals(
                "si:"+stringVariant.getReifier().getSubjectIdentifiers().iterator().next().getReference(), 
                node.get("reifier").getTextValue());
    }

    @Test public void it_should_not_write_the_empty_item_identifiers() throws IOException {
        JsonNode node = writeAndRead(stringVariant);
        assertEquals(0, stringVariant.getItemIdentifiers().size());
        assertNull(node.get("item_identifiers"));
    }

    @Test public void it_should_write_the_item_identifiers() throws IOException {
        stringVariant.addItemIdentifier(createLocator("it_should_write_the_item_identifiers"));
        JsonNode node = writeAndRead(stringVariant);

        assertTrue(node.get("item_identifiers").isArray());
        assertGreater(0, node.get("item_identifiers").size());
        assertEquals(
                stringVariant.getItemIdentifiers().iterator().next().getReference(), 
                node.get("item_identifiers").get(0).getTextValue());
    }
}
