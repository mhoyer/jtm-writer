package de.topicmapslab.jtm.writer;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.Set;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonStreamContext;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.map.JsonSerializer;
import org.tmapi.core.Construct;
import org.tmapi.core.Locator;
import org.tmapi.core.Reifiable;
import org.tmapi.core.Scoped;
import org.tmapi.core.Topic;
import org.tmapi.core.Typed;

/**
 * Author: mhoyer Created: 27.10.2010 22:05:41
 */
public abstract class ConstructSerializer<T extends Construct> extends JsonSerializer<T> {

	private final String itemType;

	/**
	 * constructor
	 */
	public ConstructSerializer() {
		/*
		 * extract type information
		 */
		Class<?> cla = getClass();
		while (!(cla.getGenericSuperclass() instanceof ParameterizedType)) {
			cla = cla.getSuperclass();
		}
		itemType = ((Class<?>) ((ParameterizedType) cla.getGenericSuperclass()).getActualTypeArguments()[0])
				.getSimpleName();
	}

	/**
	 * Writes the header of an construct JSON part
	 * 
	 * @param jgen
	 *            the generator
	 * @throws IOException
	 *             thrown if an I/O error occur
	 */
	public void writeStart(JsonGenerator jgen) throws IOException {
		JsonStreamContext outputContext = jgen.getOutputContext();

		boolean isRoot = outputContext.inRoot();
		boolean parentIsRootArray = outputContext.inArray() && outputContext.getParent().inRoot();
		jgen.writeStartObject();

		/*
		 * isn't the topic map note and not part of an root array
		 */
		if (!isRoot && !parentIsRootArray) {
			return;
		}

		/*
		 * get version
		 */
		ObjectCodec codec = jgen.getCodec();
		JTMVersion version = JTMVersion.JTM_1_0;
		if (codec instanceof JTMMapper) {
			version = ((JTMMapper) codec).getVersion();
		}
		/*
		 * write version part
		 */
		if (version == JTMVersion.JTM_1_0) {
			jgen.writeStringField(IJTMConstants.VERSION, IJTMConstants.VERSION_10);
		} else if (version == JTMVersion.JTM_1_1) {
			jgen.writeStringField(IJTMConstants.VERSION, IJTMConstants.VERSION_11);
		}

		jgen.writeStringField(IJTMConstants.ITEM_TYPE, itemType.toLowerCase());
	}

	/**
	 * Writes the id property
	 * 
	 * @param jgen
	 *            the JSON generator
	 * @param id
	 *            the is
	 * @throws IOException
	 *             thrown if an I/O error occur
	 */
	protected void writeId(JsonGenerator jgen, String id) throws IOException {
		jgen.writeStringField(IJTMConstants.ID, id);
	}

	/**
	 * Writes a JSON array of topic references
	 * 
	 * @param jgen
	 *            the JSON generator
	 * @param values
	 *            the topics
	 * @param fieldName
	 *            the field name
	 * @throws IOException
	 *             thrown if an I/O error occur
	 */
	protected void writeListOfReferences(JsonGenerator jgen, Set<Topic> values, String fieldName) throws IOException {
		if (values.size() == 0) {
			return;
		}

		jgen.writeArrayFieldStart(fieldName);
		for (Topic value : values) {
			jgen.writeString(createTopicReference(value));
		}

		jgen.writeEndArray();
	}

	/**
	 * Writes an list of values as JSON array for the given field name
	 * 
	 * @param jgen
	 *            the JSON generator
	 * @param values
	 *            the values
	 * @param fieldName
	 *            the field name
	 * @throws IOException
	 *             thrown if an I/O error occur
	 */
	protected void writeList(JsonGenerator jgen, Set<?> values, String fieldName) throws IOException {
		if (values.size() == 0) {
			return;
		}

		jgen.writeArrayFieldStart(fieldName);
		for (Object value : values) {
			jgen.writeObject(value);
		}

		jgen.writeEndArray();
	}

	/**
	 * Writes all locators as JSON array for the given field name
	 * 
	 * @param jgen
	 *            the JSON generator
	 * @param locators
	 *            the locators
	 * @param fieldName
	 *            the field name
	 * @throws IOException
	 *             thrown if an I/O error occur
	 */
	protected void writeLocators(JsonGenerator jgen, Set<Locator> locators, String fieldName) throws IOException {
		if (locators.size() == 0) {
			return;
		}

		jgen.writeArrayFieldStart(fieldName);

		for (Locator locator : locators) {
			jgen.writeString(locator.getReference());
		}

		jgen.writeEndArray();
	}

	/**
	 * Writes the reifier property of the construct, if the reifier is not <code>null</code>
	 * 
	 * @param jgen
	 *            the JSON generator
	 * @param value
	 *            the reified construct
	 * @throws IOException
	 *             thrown if an I/O error occur
	 */
	protected void writeReifier(JsonGenerator jgen, Reifiable value) throws IOException {
		Topic reifier = value.getReifier();
		if (reifier == null) {
			return;
		}
		jgen.writeStringField(IJTMConstants.REIFIER, createTopicReference(reifier));
	}

	/**
	 * Writes the type property of typed constructs
	 * 
	 * @param jgen
	 *            the JSON generator
	 * @param value
	 *            the typed construct
	 * @throws IOException
	 *             thrown if an I/O error occur
	 */
	protected void writeType(JsonGenerator jgen, Typed value) throws IOException {
		jgen.writeStringField(IJTMConstants.TYPE, createTopicReference(value.getType()));
	}

	/**
	 * Writes the scope property of scoped construct as an JSON array of all themes.
	 * 
	 * @param jgen
	 *            the JSON generator
	 * @param value
	 *            the scoped construct
	 * @throws IOException
	 *             thrown if an I/O error occur
	 */
	protected void writeScope(JsonGenerator jgen, Scoped value) throws IOException {
		Set<Topic> scope = value.getScope();
		if (scope.size() == 0) {
			return;
		}

		jgen.writeArrayFieldStart(IJTMConstants.SCOPE);

		for (Topic theme : scope) {
			jgen.writeString(createTopicReference(theme));
		}

		jgen.writeEndArray();
	}

	/**
	 * Writes a topic reference entry for the given topic
	 * 
	 * @param topic
	 *            the topic
	 * @return the reference
	 */
	protected String createTopicReference(Topic topic) {
		if (topic.getSubjectIdentifiers().size() > 0) {
			return IJTMConstants.PREFIX_SI + topic.getSubjectIdentifiers().iterator().next().getReference();
		}
		if (topic.getSubjectLocators().size() > 0) {
			return IJTMConstants.PREFIX_SL + topic.getSubjectLocators().iterator().next().getReference();
		}
		return IJTMConstants.PREFIX_II + topic.getItemIdentifiers().iterator().next().getReference();
	}
}
