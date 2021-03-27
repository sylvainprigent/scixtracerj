package scixtracerj;

public class SxData extends SxMetadata {
	
	protected String m_type;
	protected String m_name;
	protected String m_author;
	protected SxDate m_date;
	protected SxFormat m_format;
	protected String m_uri;
	    
    public SxData()
    {
    	
    }

    /**
     *  Get the data type. Possible values are (raw, processed)
     * @return Type of the data
     */
    public String get_type()
    {
    	return m_type;
    }
    
    /**
     *  Get the name of the data
     * @return Name of the data
     */
    public String get_name()
    {
    	return m_name;
    }
    /**
     *  Get the the data author
     * @return Reference to the user information
     */
    public String get_author()
    {
    	return m_author;
    }
    
    /**
     *  Get the creation date of the data
     * @return Creation date of the data
     */
    public SxDate get_date()
    {
    	return m_date;
    }
    
    /**
     *  Get the data format
     * @return Data format
     */
    public SxFormat get_format()
    {
    	return m_format;
    }
    
    /**
     *  Get the URI of the data file
     * @return URI of the data file
     */
    public String get_uri()
    {
    	return m_uri;
    }
    
    /**
     *  Set the type of the data. Possible values are (raw, processed)
     * @param type Type of the data
     */
    public void set_type(String type)
    {
    	m_type = type;
    }
    
    /**
     *  Set the name of the data
     * @param name Name of the data
     */
    public void set_name(String name)
    {
    	m_name = name;
    }
    
    /**
     *  Set the username of the data author
     * @param username Username of the data author
     */
    public void set_author(String user)
    {
    	m_author = user;
    }
    
    /**
     *  Set the creation date of the data
     * @param date Creation date of the data
     */
    public void set_date(SxDate date)
    {
    	m_date = date;
    }
    
    /**
     *  Set the data format
     * @param format Data format
     */
    public void set_format(SxFormat format)
    {
    	m_format = format;
    }
    
    /**
     *  Set the data file URI
     * @param uri URI of the data file
     */
    public void set_uri(String uri)
    {
    	m_uri = uri;
    }

}
