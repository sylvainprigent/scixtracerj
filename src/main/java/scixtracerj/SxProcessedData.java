package scixtracerj;

import java.util.Map;

public class SxProcessedData extends SxData {

	private SxMetadata m_run;
    private Map<String, SxProcessedDataInput> m_inputs;
    private SxProcessedDataOutput m_output;

    public SxProcessedData()
    {
    	
    }

    /**
     * Get the run metadata
     * @return The run metadata
     */
    public SxMetadata get_run()
    {
    	return m_run;
    }
    
    /**
     * Get the run output. Only one output possible which is this processed data
     * @return Container of the run output
     */ 
    public SxProcessedDataOutput get_run_output()
    {
    	return m_output;
    }
    
    /**
     * Get the number of run inputs
     * @return Number of run inputs
     */
    public int get_run_inputs_count()
    {
    	return m_inputs.size();
    }
    
    /**
     * Get a run inputs
     * @param index Index of the input in the input list
     * @return Container of run input
     */
    public SxProcessedDataInput get_run_input(int index)
    {
    	SxProcessedDataInput[] arr = (SxProcessedDataInput[]) m_inputs.values().toArray();
    	return  arr[index];  
    }

    /**
     * Set the run metadata
     * @param run Run metadata
     */
    public void set_run(SxMetadata run)
    {
    	m_run = run;
    }
    
    /**
     * \brief Set an input of the run. Update it if already exists or append it to the inputs list otherwise
     * @param name Name of the input data
     * @param input Container for a run input
     */
    public void set_run_input(String name, SxProcessedDataInput input)
    {
    	m_inputs.put(name, input);
    }
    
    /**
     * Set the run output. Only one ouput possible which is this processed data
     * @param output Container for a run output
     */
    public void set_run_output(SxProcessedDataOutput output)
    {
    	m_output = output;
    }
    
    /// \brief Set the common information of the data
    /**
     * Set the common information of the data
     * @param name Data name
     * @param author Data author
     * @param date Created date
     * @param format_ Data format
     * @param uri Data file URI
     */
    public void set_info(String name, String author, SxDate date, SxFormat format_, String uri)
    {
        m_name = name;
        m_author = author;
        m_date = date;
        m_format = format_;
        m_uri = uri;
    }
    
    /**
     * Add the informations of the process input data
     * @param name Name of the data in the process
     * @param data Metadata of the input data
     * @param type Type of the data (raw or processsed)
     */
    public void add_input(String name, SxMetadata data, String type)
    {
    	this.set_run_input(name, new SxProcessedDataInput(name, data, type));
    }
    
    /**
     * Set the data output information
     * @param name Name of the process output
     * @param label Label of the process output
     */
    public void set_output(String name, String label)
    {
    	this.set_run_output(new SxProcessedDataOutput(name, label));
    }
    
}
