package scixtracerj;

import java.util.Map;
import java.util.Set;

public class SxTags {

	private Map<String, String> m_tags;
	
    public SxTags()
    {
    	
    }

    /**
     * Get the value of a tag from it key
     * @param key Key of the tag
     * @return value of the tag
     */
    public String get_tag(String key)
    {
    	return m_tags.get(key);
    }
    
    /**
     * Get the number of pairs 'key=value' of tags
     * @return Number of tag in the Tags map
     */
    public int get_count()
    {
    	return m_tags.size();
    }
    
    /**
     * Get the list of keys in the Tags map
     * @return List of tags keys
     */
    public Set<String> get_keys()
    {
    	return m_tags.keySet();
    }

    /**
     * Set a tag to the map. If the tag already exists, it is replaced
     * @param key Key of the tag
     * @param value Value of the tag
     */
    void set_tag(String key, String value)
    {
    	m_tags.put(key, value);
    }

}
