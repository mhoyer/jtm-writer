package de.topicmapslab.jtm.writer;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.tmapi.core.Variant;

/**
 * Class to convert a variant to JTM
 * 
 * Author: mhoyer Created: 27.10.2010 22:05:41
 */
public class VariantSerializer extends ConstructSerializer<Variant> {
	/**
	 * Jackson Mapper
	 */
	@JsonSerialize(using = VariantSerializer.class)
	public class VariantMixIn {
		// VOID
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(Variant value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
		writeStart(jgen);
		{
			writeId(jgen, value.getId());
			if (!value.getDatatype().getReference().endsWith(IJTMConstants.STRING)) {
				jgen.writeStringField(IJTMConstants.DATATYPE, value.getDatatype().getReference());
			}

			writeScope(jgen, value);
			jgen.writeStringField(IJTMConstants.VALUE, value.getValue());
			writeReifier(jgen, value);
			writeLocators(jgen, value.getItemIdentifiers(), IJTMConstants.ITEM_IDENTIFIERS);
		}
		jgen.writeEndObject();
	}
}
