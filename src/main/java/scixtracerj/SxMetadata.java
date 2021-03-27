package scixtracerj;

public class SxMetadata {

	String m_md_uri;
	String m_uuid;
	
	public SxMetadata()
	{
		
	}
	
	public SxMetadata(String md_uri, String uuid)
	{
		m_md_uri = md_uri;
		m_uuid = uuid;
	}

	/**
     * Get the metadata URI
     * 
     * @return URI of the metadata
     */
    public String get_md_uri()
    {
    	return m_md_uri;
    }
    /**
     * Get the metadata UUID
     * 
     * @return a string containing the UUID
     */
    public String get_uuid()
    {
    	return m_uuid;
    }

    /**
     * Set the container URI
     * 
     * @param md_uri URI of the metadata
     */
    public void set_md_uri(String md_uri)
    {
    	m_md_uri = md_uri;
    }

    /**
     * Set the container URI
     * 	
     * @param md_uri URI of the metadata
     */
    public void set_uuid(String uuid)
    {
    	m_uuid = uuid;
    }
    
}
