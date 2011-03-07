package de.topicmapslab.jtm.writer;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.tmapi.core.Role;

/**
 * Class to convert a role to JTM
 * 
 * Author: mhoyer Created: 28.10.2010 00:44
 */
public class RoleSerializer extends ConstructSerializer<Role> {
	/**
	 * Jackson Mapper
	 */
	@JsonSerialize(using = RoleSerializer.class)
	public class RoleMixIn {
		// VOID
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(Role value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
		writeStart(jgen);
		{
			writeId(jgen, value.getId());
			writePlayer(jgen, value);
			writeType(jgen, value);
			writeReifier(jgen, value);
			writeLocators(jgen, value.getItemIdentifiers(), IJTMConstants.ITEM_IDENTIFIERS);
		}
		jgen.writeEndObject();
	}

	protected void writePlayer(JsonGenerator jgen, Role value) throws IOException {
		jgen.writeStringField(IJTMConstants.PLAYER, createTopicReference(value.getPlayer()));
	}

}
