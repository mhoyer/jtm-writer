package de.topicmapslab.jtm.writer;

import org.codehaus.jackson.map.ObjectMapper;
import org.tmapi.core.Association;
import org.tmapi.core.Name;
import org.tmapi.core.Occurrence;
import org.tmapi.core.Role;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;
import org.tmapi.core.Variant;

/**
 * Author: mhoyer Created: 03.11.10 13:53
 */
public class JTMMapper extends ObjectMapper {

	private JTMVersion version;

	/**
	 * construct
	 */
	public JTMMapper() {
		this.version = JTMVersion.JTM_1_0;
		getSerializationConfig().addMixInAnnotations(Variant.class, VariantSerializer.VariantMixIn.class);
		getSerializationConfig().addMixInAnnotations(Name.class, NameSerializer.NameMixIn.class);
		getSerializationConfig().addMixInAnnotations(Occurrence.class, OccurrenceSerializer.OccurrenceMixIn.class);
		getSerializationConfig().addMixInAnnotations(Topic.class, TopicSerializer.TopicMixIn.class);
		getSerializationConfig().addMixInAnnotations(Role.class, RoleSerializer.RoleMixIn.class);
		getSerializationConfig().addMixInAnnotations(Association.class, AssociationSerializer.AssociationMixIn.class);
		getSerializationConfig().addMixInAnnotations(TopicMap.class, TopicMapSerializer.TopicMapMixIn.class);
	}

	/**
	 * Modify the version
	 * 
	 * @param version
	 *            the version to set
	 */
	public void setVersion(JTMVersion version) {
		this.version = version;
	}

	/**
	 * Returns the JTM version
	 * 
	 * @return the version
	 */
	public JTMVersion getVersion() {
		return version;
	}

}
