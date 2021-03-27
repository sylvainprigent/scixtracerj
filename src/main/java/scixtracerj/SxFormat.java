package scixtracerj;

public class SxFormat {

	private String m_name;

	public SxFormat()
	{

	}

	public SxFormat(String name)
	{
		m_name = name;
	}

	/**
	 * Get the name of the format
	 * @return Name of the format
	 */
	public String get_name()
	{
		return m_name;
	}

	/**
	 * Set the name of the format
	 * @param name Name of the format
	 */
	public void set_name(String name)
	{
		m_name = name;
	}
}
