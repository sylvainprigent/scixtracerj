package scixtracerj;

public class SxRawData extends SxData{

	protected SxTags m_tags;
	
    public SxRawData()
    {
    	m_type = "raw";
    }


    /**
     * Get the map of the tag associated to the data
     * @return Map of the tags describing to the data
     */
    SxTags get_tags()
    {
    	return m_tags;
    }

    /**
     * Set the map of the tag associated to the data
     * @param tags Map of the tags describing to the data
     */
    void set_tags(SxTags tags)
    {
    	m_tags = tags;
    }

}
