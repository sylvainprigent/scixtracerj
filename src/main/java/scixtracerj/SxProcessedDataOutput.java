package scixtracerj;

public class SxProcessedDataOutput {


	private String m_name;
	private String m_label;

	public SxProcessedDataOutput()
	{

	}

	public SxProcessedDataOutput(String name, String label)
	{
		m_name = name;
		m_label = label;
	}

	/**
	 * Get the output name
	 * @return Name of the process output
	 */
	public String get_name()
	{
		return m_name;
	}
	
	/**
	 * Get the output label
	 * @return Label of the process output
	 */
	public String get_label()
	{
		return m_label;
	}

	/**
	 * Set the output name
	 * @param name Name of the process output
	 */
	public void set_name(String name)
	{
		m_name = name;
	}
	
	/**
	 * Set the output label
	 * @param label Label of the process output
	 */
	public void set_label(String label)
	{
		m_label = label;
	}

}
