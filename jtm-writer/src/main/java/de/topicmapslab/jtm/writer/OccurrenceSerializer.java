package de.topicmapslab.jtm.writer;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.tmapi.core.Occurrence;

/**
 * Class to convert an occurrence to JTM
 * 
 * Author: mhoyer Created: 28.10.2010 00:01:02
 */
public class OccurrenceSerializer extends ConstructSerializer<Occurrence> {
	/**
	 * Jackson Mapper
	 */
	@JsonSerialize(using = OccurrenceSerializer.class)
	public class OccurrenceMixIn {
		// VOID
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(Occurrence value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
		writeStart(jgen);
		{
			writeId(jgen, value.getId());
			if (!value.getDatatype().getReference().endsWith(IJTMConstants.STRING)) {
				jgen.writeStringField(IJTMConstants.DATATYPE, value.getDatatype().getReference());
			}

			writeScope(jgen, value);
			jgen.writeStringField(IJTMConstants.VALUE, value.getValue());
			writeType(jgen, value);
			writeReifier(jgen, value);
			writeLocators(jgen, value.getItemIdentifiers(), IJTMConstants.ITEM_IDENTIFIERS);
		}
		jgen.writeEndObject();
	}
}
