package scixtracerj;

import java.util.ArrayList;
import java.util.List;

public class SxDataset extends SxMetadata {

	private String m_type;
	private String m_name;
	private List<SxMetadata> m_data;


	public SxDataset()
	{
		m_data = new ArrayList<SxMetadata>();
	}

	/**
	 * Get the SxDataset type: (raw or processed)
	 * \return SxDataset type: (raw or processed)
	 */
	public String get_type()
	{
		return m_type;
	}

	/**
	 * Get the SxDataset name
	 * \return SxDataset type: (raw or processed)
	 */
	public String get_name()
	{
		return m_name;
	}
	
	/**
	 * Get the number of data in the SxDataset
	 * \return Number of data in the SxDataset
     */
	public int get_data_count()
	{
		return m_data.size();
	}
	
	/**
	 * Get the URI of one data in the SxDataset
	 * @param Index of the data in the SxDataset
	 * \return URI of the data at index
	 */
	public String get_data_uri(int index)
	{
		return m_data.get(index).get_md_uri();
	}
	/**
	 * Get the UUID of one data in the SxDataset
	 * @param Index of the data in the SxDataset
	 * \return UUID of the data at index
	 */
	public String get_data_uuid(int index)
	{
		return m_data.get(index).get_uuid();
	}
	/**
	 * Get the metadata (URI and UUID) of one data in the SxDataset
	 * \return a pointer to the data metadata
	 */
	SxMetadata get_data(int index)
	{
		return m_data.get(index);
	}
	/**
	 * Get the URIs list of the data the SxDataset
	 * \return URIs list of the data the SxDataset
	 */
	List<String> get_data_list()
	{
		List<String> uris = new ArrayList<String>();
	    for (int i = 0 ; i < m_data.size() ; i++){
	        uris.add(m_data.get(i).get_md_uri());
	    }
	    return uris;
	}

	/**
	 * Set the SxDataset type
	 * @param type Type of the SxDataset: (raw or processed)
	 */
	void set_type(String type)
	{
		m_type = type;
	}

	/**
	 * Set the SxDataset name
	 * @param name Name of the SxDataset
	 */
	void set_name(String name)
	{
		m_name = name;
	}

	/**
	 * Set a data in the SxDataset. Add if not exists
	 * @param uri URI of the data
	 */
	void set_data(SxMetadata metadata)
	{
	    boolean found = false;
	    for (int i = 0 ; i < m_data.size() ; i++){
	        if (m_data.get(i).get_md_uri() == metadata.get_md_uri()){
	            found = true;
	            break;
	        }
	    }
	    if (!found){
	        m_data.add(metadata);
	    }
	}
	
}
