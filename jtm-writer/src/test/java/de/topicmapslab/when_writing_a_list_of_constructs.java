package de.topicmapslab;

import org.codehaus.jackson.JsonNode;
import org.junit.Before;
import org.junit.Test;
import org.tmapi.core.Construct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import static junit.framework.Assert.assertEquals;

/**
 * Author:  mhoyer
 * Created: 27.10.2010 00:05:04
 */
public class when_writing_a_list_of_constructs extends with_JTMWriter {
    private Collection<Construct> constructs;

    @Before public void given_a_list_of_constructs() {
        constructs = new ArrayList<Construct>();
        constructs.add(topicMap.createTopic());
        constructs.add(topicMap.createTopic());
        constructs.add(createName(getClass().getName()));
    }
    
    @Test public void it_should_write_all_entries() throws IOException {
        JsonNode node = writeAndRead(constructs);
        assertEquals(constructs.size(), node.size());
    }

    @Test public void it_should_write_item_type_and_version_for_all_entries() throws IOException {
        JsonNode node = writeAndRead(constructs);

        int i=0;
        for(Construct construct : constructs)
        {
            assertEquals(1.0, node.get(i).get("version").getValueAsDouble());
            assertItemType(construct, node.get(i));
            i++;
        }
    }
}
