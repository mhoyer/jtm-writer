package de.topicmapslab;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.junit.Before;
import org.junit.Test;
import org.tmapi.core.Association;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junitx.framework.ComparableAssert.assertGreater;
import static org.junit.Assert.assertTrue;

/**
 * Author:  mhoyer
 * Created: 28.10.2010 00:56
 */
public class when_writing_an_association extends with_JTMWriter {
    private Association association;

    @Before public void given_an_association() {
        association = createAssociation(getClass().getSimpleName(), "simple-role:player");
    }
    
    @Test public void it_should_write_the_version_and_item_type() throws IOException {
        JsonNode node = writeAndRead(association);
        assertEquals(1.0, node.get("version").getValueAsDouble());
        assertItemType(association, node);
    }
    
    @Test public void it_should_write_id() throws IOException {
        JsonNode node = writeAndRead(association);
        assertEquals(association.getId(), node.get("id").getTextValue());
    }

    @Test public void it_should_write_type() throws IOException {
        JsonNode node = writeAndRead(association);
        assertEquals(
                "si:"+ association.getType().getSubjectIdentifiers().iterator().next().getReference(),
                node.get("type").getTextValue());
    }

    @Test public void it_should_not_write_the_empty_scope_array() throws IOException {
        JsonNode node = writeAndRead(association);

        assertNull(node.get("scope"));
    }

    @Test public void it_should_write_a_non_empty_scope_array() throws IOException {
        association.addTheme(createSimpleNamedTopic("it_should_write_a_non_empty_scope_array"));
        JsonNode node = writeAndRead(association);

        assertTrue(node.get("scope").isArray());
        assertGreater(0, node.get("scope").size());
    }

    @Test(expected = JsonGenerationException.class)
    public void it_should_throw_exception_if_no_role_given() throws IOException {
        association.getRoles().iterator().next().remove();
        JsonNode node = writeAndRead(association);

        assertEquals(0, association.getRoles().size());
        assertNull(node.get("roles"));
    }

    @Test public void it_should_write_a_non_empty_roles_array() throws IOException {
        JsonNode node = writeAndRead(association);

        assertTrue(node.get("roles").isArray());
        assertGreater(0, node.get("roles").size());
        assertNull(node.get("roles").get(0).get("version"));
    }

    @Test public void it_should_not_write_null_reifier() throws IOException {
        JsonNode node = writeAndRead(association);
        assertNull(node.get("reifier"));
    }

    @Test public void it_should_write_reifier() throws IOException {
        association.setReifier(createSimpleNamedTopic(getClass().getSimpleName()));
        JsonNode node = writeAndRead(association);
        assertEquals(
                "si:"+ association.getReifier().getSubjectIdentifiers().iterator().next().getReference(),
                node.get("reifier").getTextValue());
    }

    @Test public void it_should_not_write_the_empty_item_identifiers() throws IOException {
        JsonNode node = writeAndRead(association);
        assertEquals(0, association.getItemIdentifiers().size());
        assertNull(node.get("item_identifiers"));
    }

    @Test public void it_should_write_the_item_identifiers() throws IOException {
        association.addItemIdentifier(createLocator("it_should_write_the_item_identifiers"));
        JsonNode node = writeAndRead(association);

        assertTrue(node.get("item_identifiers").isArray());
        assertGreater(0, node.get("item_identifiers").size());
        assertEquals(
                association.getItemIdentifiers().iterator().next().getReference(),
                node.get("item_identifiers").get(0).getTextValue());
    }
}
