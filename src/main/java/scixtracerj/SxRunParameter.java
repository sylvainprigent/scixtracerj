package scixtracerj;

public class SxRunParameter {

	private String m_name;
	private String m_value;
	    
    public SxRunParameter()
    {
    	
    }
    
    public SxRunParameter(String  name, String  value)
    {
    	
    }

    /**
     * Get the name of the parameter
     * @return Name of the parameter
     */
    public String get_name()
    {
    	return m_name;
    }
    
    /**
     * Get the value of the parameter
     * @return Value of the parameter
     */
    public String get_value()
    {
    	return m_value;
    }

    /**
     * Set the name of the parameter
     * @param name Name of the parameter
     */
    void set_name(String  name)
    {
    	m_name = name;
    }
    
    /**
     * Set the value of the parameter
     * @param value Value of the parameter
     */
    void set_value(String  value)
    {
    	m_value = value;
    }
    
}
