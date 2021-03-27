package scixtracerj;

public class SxSearchContainer {

	private String m_name;
	private String m_uri;
	private String m_uuid;
	private SxTags m_tags;

	public SxSearchContainer()
	{

	}

	public SxSearchContainer(String name, String uri, String uuid, SxTags tags)
	{
	    m_name = name;
	    m_uri = uri;
	    m_uuid = uuid;
	    m_tags = tags;
	}

	/**
	 * Get the data name
	 * @return data name
	 */
	public String get_name()
	{
		return m_name;
	}
	
	/**
	 * Get the data URI
	 * @return data URI
	 */
	public String get_uri()
	{
		return m_uri;
	}
	
	/**
	 * Get the data UUID
	 * @return data UUID
	 */
	public String get_uuid()
	{
		return m_uuid;
	}
	
	/**
	 * Get the data Tags
	 * @return Pointer to the tags
	 */
	public SxTags get_tags()
	{
		return m_tags;
	}
	
	/**
	 * Set the data name
	 * @param name Data name
	 */
	public void set_name(String name)
	{
		m_name = name;
	}
	
	/**
	 * Set the data URI
	 * @param uri Data URI
	 */
	public void set_uri(String uri)
	{
		m_uri = uri;
	}
	
	/**
	 * Set the data UUID
	 * @param uuid Data UUID
	 */
	public void set_uuid(String uuid)
	{
		m_uuid = uuid;
	}
	
	/**
	 * set the data Tags
	 * @param tags Pointer to the tags
	 */
	public void set_tags(SxTags tags)
	{
		m_tags = tags;
	}

	/**
	 * Check if a tag key exists
	 * @param key Key of the tag
	 * @return True if the key exists, false otherwise
	 */
	public boolean is_tag(String key)
	{
		return m_tags.get_keys().contains(key);
	}
	
	/**
	 * Get a tag from key
	 * @param key Key of the tag
	 * @return Value of the tag
	 */
	public String get_tag(String key)
	{
		return m_tags.get_tag(key);
	}

}
