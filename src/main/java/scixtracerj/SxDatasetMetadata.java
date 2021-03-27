package scixtracerj;

public class SxDatasetMetadata {
	
	
	protected String m_name;
	protected String m_md_uri;
	protected String m_uuid;
	    
    public SxDatasetMetadata()
    {
    	
    }
    public SxDatasetMetadata(String name, String md_uri, String uuid)
    {
        m_name = name;
        m_md_uri = md_uri;
        m_uuid = uuid;
    }
    
    public String get_name()
    {
    	return m_name;
    }
    
    public String get_md_uri()
    {
    	return m_md_uri;
    }
    
    public String get_uuid()
    {
    	return m_uuid;
    }

    void set_name(String value)
    {
    	m_name = value;
    }
    
    void set_md_uri(String value)
    {
    	m_md_uri = value;
    }
    
    void set_uuid(String value)
    {
    	m_uuid = value;
    }

}
