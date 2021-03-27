package scixtracerj;

public class SxProcessedDataInput {

	private String m_name;
	private SxMetadata m_data;
	private String m_type;

	public SxProcessedDataInput()
	{

	}

	public SxProcessedDataInput(String name, SxMetadata data, String type)
	{
		m_name = name;
		m_data = data;
		m_type = type;
	}

	/**
	 * Get the input name
	 * @return Name of the process input
	 */
	public String get_name()
	{
		return m_name;
	}

	/**
	 * Get the input data URI
	 * @return Input data metadata
	 */
	public SxMetadata get_data()
	{
		return m_data;
	}

	/**
	 * Get the input data type (raw or processed)
	 * @return Type of the data (raw or processed)
	 */
	public String get_type()
	{
		return m_type;
	}

	/**
	 * Set the input name
	 * @param name Name of the process input
	 */
	public void set_name(String name)
	{
		m_name = name;
	}

	/**
	 * Set the input data URI
	 * @param data Metadata of the input data
	 */
	public void set_data(SxMetadata data)
	{
		m_data = data;
	}

	/**
	 * Set the input data type (raw or processed)
	 * @param type Type of the data (raw or processed)
	 */
	public void set_type(String type)
	{
		m_type = type;
	}

}
