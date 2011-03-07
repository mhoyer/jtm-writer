package de.topicmapslab;

import org.codehaus.jackson.JsonNode;
import org.junit.Before;
import org.junit.Test;
import org.tmapi.core.Association;
import org.tmapi.core.Role;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junitx.framework.ComparableAssert.assertGreater;
import static org.junit.Assert.assertTrue;

/**
 * Author:  mhoyer
 * Created: 28.10.2010 00:41
 */
public class when_writing_a_role extends with_JTMWriter {
    private Role role;

    @Before public void given_a_role() {
        Association association = createAssociation(getClass().getSimpleName());
        role = association.createRole(
                createSimpleNamedTopic("role-type"),
                createSimpleNamedTopic("player"));
    }
    
    @Test public void it_should_write_the_version_and_item_type() throws IOException {
        JsonNode node = writeAndRead(role);
        assertEquals(1.0, node.get("version").getValueAsDouble());
        assertItemType(role, node);
    }
    
    @Test public void it_should_write_id() throws IOException {
        JsonNode node = writeAndRead(role);
        assertEquals(role.getId(), node.get("id").getTextValue());
    }

    @Test public void it_should_write_player() throws IOException {
        JsonNode node = writeAndRead(role);
        assertEquals(
                "si:"+ role.getPlayer().getSubjectIdentifiers().iterator().next().getReference(),
                node.get("player").getTextValue());
    }

    @Test public void it_should_write_type() throws IOException {
        JsonNode node = writeAndRead(role);
        assertEquals(
                "si:"+ role.getType().getSubjectIdentifiers().iterator().next().getReference(),
                node.get("type").getTextValue());
    }

    @Test public void it_should_not_write_null_reifier() throws IOException {
        JsonNode node = writeAndRead(role);
        assertNull(node.get("reifier"));
    }

    @Test public void it_should_write_reifier() throws IOException {
        role.setReifier(createSimpleNamedTopic(getClass().getSimpleName()));
        JsonNode node = writeAndRead(role);
        assertEquals(
                "si:"+ role.getReifier().getSubjectIdentifiers().iterator().next().getReference(),
                node.get("reifier").getTextValue());
    }

    @Test public void it_should_not_write_the_empty_item_identifiers() throws IOException {
        JsonNode node = writeAndRead(role);
        assertEquals(0, role.getItemIdentifiers().size());
        assertNull(node.get("item_identifiers"));
    }

    @Test public void it_should_write_the_item_identifiers() throws IOException {
        role.addItemIdentifier(createLocator("it_should_write_the_item_identifiers"));
        JsonNode node = writeAndRead(role);

        assertTrue(node.get("item_identifiers").isArray());
        assertGreater(0, node.get("item_identifiers").size());
        assertEquals(
                role.getItemIdentifiers().iterator().next().getReference(),
                node.get("item_identifiers").get(0).getTextValue());
    }
}
