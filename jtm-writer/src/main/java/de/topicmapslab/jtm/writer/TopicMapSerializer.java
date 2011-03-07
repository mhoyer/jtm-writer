package de.topicmapslab.jtm.writer;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.tmapi.core.TopicMap;

/**
 * Class to convert the topic map to JTM
 * 
 * Author: mhoyer Created: 28.10.2010 01:19
 */
public class TopicMapSerializer extends ConstructSerializer<TopicMap> {
	/**
	 * Jackson Mapper
	 */
	@JsonSerialize(using = TopicMapSerializer.class)
	public class TopicMapMixIn {
		// VOID
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(TopicMap value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
		writeStart(jgen);
		{
			writeList(jgen, value.getTopics(), IJTMConstants.TOPICS);
			writeList(jgen, value.getAssociations(), IJTMConstants.ASSOCIATIONS);
			writeLocators(jgen, value.getItemIdentifiers(), IJTMConstants.ITEM_IDENTIFIERS);
			writeReifier(jgen, value);
		}
		jgen.writeEndObject();
	}

}
