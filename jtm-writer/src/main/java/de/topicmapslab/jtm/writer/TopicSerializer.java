package de.topicmapslab.jtm.writer;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.tmapi.core.Topic;

/**
 * Class to convert a topic to JTM
 * 
 * Author: mhoyer Created: 28.10.2010 00:21:53
 */
public class TopicSerializer extends ConstructSerializer<Topic> {
	/**
	 * Jackson Mapper
	 */
	@JsonSerialize(using = TopicSerializer.class)
	public class TopicMixIn {
		// VOID
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(Topic value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
		writeStart(jgen);
		{
			writeId(jgen, value.getId());
			/*
			 * get version
			 */
			ObjectCodec codec = jgen.getCodec();
			JTMVersion version = JTMVersion.JTM_1_0;
			if (codec instanceof JTMMapper) {
				version = ((JTMMapper) codec).getVersion();
			}
			/*
			 * write instance_of field if version is 1.1
			 */
			if (version == JTMVersion.JTM_1_1) {
				writeListOfReferences(jgen, value.getTypes(), IJTMConstants.INSTANCE_OF);
			}
			writeList(jgen, value.getNames(), IJTMConstants.NAMES);
			writeList(jgen, value.getOccurrences(), IJTMConstants.OCCURRENCES);
			writeLocators(jgen, value.getSubjectIdentifiers(), IJTMConstants.SUBJECT_IDENTIFIERS);
			writeLocators(jgen, value.getSubjectLocators(), IJTMConstants.SUBJECT_LOCATORS);
			writeLocators(jgen, value.getItemIdentifiers(), IJTMConstants.ITEM_IDENTIFIERS);
		}
		jgen.writeEndObject();
	}

}
