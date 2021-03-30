package scixtracerj;

public abstract class SxSerialize {

	   /// \brief Constructor
    public SxSerialize()
    {
    	
    }

    /**
     * Serialize a metadata to a string
     * @param container Pointer to the container
     * @return string that represent the container content
     * @throws Exception 
     */
	public String serialize(SxMetadata container) throws Exception
	{
		
		//System.out.println("Container class=");
		//System.out.println(container.getClass());
	    
	    if(container.getClass() == SxRawData.class)
	    {
	    	SxRawData raw_data = (SxRawData)(container);
	        return this.serialize_rawdata(raw_data);
	    }
	    
	    if(container.getClass() == SxProcessedData.class)
	    {
	    	SxProcessedData processed_data = (SxProcessedData)(container);
	        return this.serialize_processeddata(processed_data);
	    }
	    
	    if(container.getClass() == SxDataset.class)
	    {
	    	SxDataset dataset = (SxDataset)(container);
	        return this.serialize_dataset(dataset);
	    }
	    
	    if(container.getClass() == SxRun.class)
	    {
	    	SxRun run = (SxRun)(container);
	        return this.serialize_run(run);
	    }
	    
	    if(container.getClass() == SxExperiment.class)
	    {
	    	SxExperiment experiment = (SxExperiment)(container);
	        return this.serialize_experiment(experiment);
	    }
	    throw new Exception("SxSerialize cannot cast the metadata container");
	}
	
	/**
	 * Serialize a raw data to a string
	 * @param container Pointer to the container
	 * @return string that represent the container content
	 * @throws Exception 
	 */
    public abstract String serialize_rawdata(SxRawData container) throws Exception;
    
    /**
     * Serialize a processed data to a string
     * @param container Pointer to the container
     * @return string that represent the container content
     * @throws Exception 
     */
    public abstract String serialize_processeddata(SxProcessedData container) throws Exception;
    
    /**
     * Serialize a dataset to a string
     * @param container Pointer to the container
     * @return string that represent the container content
     */
    public abstract String serialize_dataset(SxDataset container);
    
    /**
     * Serialize a run to a string
     * @param container Pointer to the container
     * @return string that represent the container content
     */
    public abstract String serialize_run(SxRun container);
    
    /**
     * Serialize an axperiment to a string
     * @param container Pointer to the container
     * @return string that represent the container content
     * @throws Exception 
     */
    public abstract String serialize_experiment(SxExperiment container) throws Exception;
    
}
