package de.topicmapslab.jtm.writer;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.tmapi.core.Name;

/**
 * Class to convert a topic name to JTM
 * 
 * Author: mhoyer Created: 27.10.2010 23:25:00
 */
public class NameSerializer extends ConstructSerializer<Name> {

	/**
	 * Jackson Mapper
	 */
	@JsonSerialize(using = NameSerializer.class)
	public class NameMixIn {
		// VOID
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(Name value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
		writeStart(jgen);
		{
			writeId(jgen, value.getId());
			jgen.writeStringField(IJTMConstants.VALUE, value.getValue());
			writeType(jgen, value);
			writeScope(jgen, value);
			writeList(jgen, value.getVariants(), IJTMConstants.VARIANTS);

			writeReifier(jgen, value);
			writeLocators(jgen, value.getItemIdentifiers(), IJTMConstants.ITEM_IDENTIFIERS);
		}
		jgen.writeEndObject();
	}
}
