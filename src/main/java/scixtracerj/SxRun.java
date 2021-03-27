package scixtracerj;

import java.util.List;

public class SxRun {

	private SxMetadata m_processed_dataset;
	private String m_process_name;
	private String m_process_uri;
	private List<SxRunInput> m_inputs;
	private List<SxRunParameter> m_parameters;
	    
    public SxRun()
    {
    	
    }

    /**
     * Get the metadata of the processed dataset where the run stored the results
     * @return URI of the processed dataset
     */
    public SxMetadata get_processed_dataset()
    {
    	return m_processed_dataset;
    }
    
    /**
     * Get the name of the process that is runned
     * @return Name of the process
     */
    public String get_process_name()
    {
    	return m_process_name;
    }
    
    /**
     * Get the URI of the process that is runned
     * @return URI of the process
     */
    public String get_process_uri()
    {
    	return m_process_uri;
    }
    
    /**
     * Get the number of input data used by the process
     * @return Number of input data used by the process
     */
    public int get_inputs_count()
    {
    	return m_inputs.size();
    }
    
    /**
     * Get the input at the index
     * @param index Index of the input in the input list
     * @return Reference to the run input container
     */
    public SxRunInput get_input(int index)
    {
    	return m_inputs.get(index);
    }
    
    /**
     * Get the list of all inputs used by the process
     * @return List of references to the run input containers
     */
    public List<SxRunInput> get_inputs()
    {
    	return m_inputs;
    }
    
    /**
     * Get the number of parameters used to tune the process
     * @return Number of parameters
     */
    public int get_parameters_count()
    {
    	return m_parameters.size();
    }
    
    /**
     * Get the parameter at the index
     * @param index Index of the parameter in the output list
     * @return Reference to the parameter container
     */
    public SxRunParameter get_parameter(int index)
    {
    	return m_parameters.get(index);
    }
    
    /**
     * Get the list of all parameters used by the process
     * @return List of references to the parameter containers
     */
    public List<SxRunParameter> get_parameters()
    {
    	return m_parameters;
    }

    /**
     * Set the URI of the processed dataset where the run stored the results
     * @param dataset Processed dataset
     */
    public void set_processed_dataset(SxMetadata dataset)
    {
    	m_processed_dataset = dataset;
    }
    
    /**
     * Set the information of the process that is runned
     * @param name Name of the process
     * @param uri URI of the process
     */
    public void set_process(String name, String uri)
    {
        m_process_name = name;
        m_process_uri = uri;
    }
    
    /**
     * Set the name of the process that is runned
     * @param name Name of the process
     */
    public void set_process_name(String  name)
    {
    	m_process_name = name;
    }
    
    /**
     * Set the URI of the process that is runned
     * @param uri URI of the process
     */
    public void set_process_uri(String uri)
    {
    	m_process_uri = uri;
    }
    
    /**
     * Set the input
     * @param input Reference to the run input container
     */
    public void set_input(SxRunInput input)
    {
        boolean found = false;
        for (int i = 0 ; i < m_inputs.size() ; i++){
            if (m_inputs.get(i).get_name() == input.get_name()){
                m_inputs.set(i, input);
                found = true;
            }
        }
        if (!found)
        {
            m_inputs.add(input);
        }
    }
    
    /**
     * Add a run input
     * @param name
     * @param dataset_name
     * @param query
     */
    public void add_input(String name, String  dataset_name, String  query)
    {
    	this.add_input(name, dataset_name, query, "");
    }
    
    /**
     * Add a run input
     * @param name
     * @param dataset_name
     * @param query
     * @param origin
     */
    public void add_input(String name, String  dataset_name, String  query, String origin)
    {
    	m_inputs.add(new SxRunInput(name, dataset_name, query, origin));
    }
    
    /**
     * Set the parameter
     * @param parameter Reference to the run parameter container
     */
    public void set_parameter(SxRunParameter parameter)
    {
        boolean found = false;
        for (int i = 0 ; i < m_parameters.size() ; i++){
            if (m_parameters.get(i).get_name() == parameter.get_name()){
                m_parameters.set(i,  parameter);
                found = true;
            }
        }
        if (!found)
        {
           m_parameters.add(parameter);
        }
    }
    
    /**
     * Add a parameter to the Run
     * @param name Name of the parameter
     * @param label Label of the parameter
     */
    public void add_parameter(String name, String label)
    {
    	m_parameters.add(new SxRunParameter(name, label));
    }
    
}
