package scixtracerj;

public class SxRunInput {

	private String m_name;
	private String m_dataset_name;
	private String m_query;
	private String m_origin;
	    
	public  SxRunInput()
	{
		
	}
	
	public  SxRunInput(String  name, String  dataset_name, String  query, String  origin)
	{
		
	}

	/**
	 * Get the name of the input
	 * @return Name of the input
	 */ 
    public String get_name()
    {
    	return m_name;
    }
    
    /**
     * Get the name of the dataset where the input data is stored
     * @return  Name the input dataset
     */
    public String get_dataset_name()
    {
    	return m_dataset_name;
    }
    
    /**
     * Get the query used to select data in the input dataset. The query is a RegExp
     * @return Query used to select input data
     */
    public String get_query()
    {
    	return m_query;
    }
    
    /**
     * Get the origin output name. It is the name of the output of the previous job in the pipeline
     * @return Output name of the run that created the input data
     */
    public String get_origin_output_name()
    {
    	return m_origin;
    }
    
    /**
     * Set the name of the input
     * @param name Name of the input
     */
    public void set_name(String  name)
    {
    	m_name = name;
    }
    
    /**
     * Set the dataset where the input data is stored
     * @param name Name of the input dataset
     */
    public void set_dataset_name(String  name)
    {
    	m_dataset_name = name;
    }
    
    /**
     * Set the query used to select data in the input dataset. The query is a RegExp
     * @param query Query used to select input data
     */
    public void set_query(String  query)
    {
    	m_query = query;
    }
    
    /**
     * Set the origin output name. It is the name of the output of the previous job in the pipeline
     * @param name Name of the origin output
     */
    public void set_origin_output_name(String  name)
    {
    	m_origin = name;
    }
    
}
