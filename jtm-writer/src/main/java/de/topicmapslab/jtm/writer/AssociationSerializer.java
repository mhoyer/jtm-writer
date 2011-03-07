package de.topicmapslab.jtm.writer;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.tmapi.core.Association;

/**
 * Class to convert an association to JTM
 * 
 * Author: mhoyer Created: 28.10.2010 01:04
 */
public class AssociationSerializer extends ConstructSerializer<Association> {

	/**
	 * Jackson Mapper
	 */
	@JsonSerialize(using = AssociationSerializer.class)
	public class AssociationMixIn {
		// VOID
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(Association value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
		if (value.getRoles().size() == 0) {
			throw new JsonGenerationException("JTM 1.0 and 1.1 does not support associations without a role.");
		}

		writeStart(jgen);
		{
			writeId(jgen, value.getId());
			writeType(jgen, value);
			writeScope(jgen, value);
			writeList(jgen, value.getRoles(), IJTMConstants.ROLES);
			writeReifier(jgen, value);
			writeLocators(jgen, value.getItemIdentifiers(), IJTMConstants.ITEM_IDENTIFIERS);
		}
		jgen.writeEndObject();
	}
}
