package scixtracerj;

import java.util.ArrayList;
import java.util.List;

public class SxExperiment extends SxMetadata {

	private String m_name;
	private String m_author;
	private SxDate m_date;
	private SxDatasetMetadata m_raw_dataset;
	private List<SxDatasetMetadata> m_processed_datasets;
	private List<String> m_tags_keys;

	public SxExperiment()
	{
		m_processed_datasets = new ArrayList<SxDatasetMetadata>();
		m_tags_keys = new ArrayList<String>();
	}
	
	/**
	 * Get the name of the Experiment
	 * @returnName of the Experiment
	 */
	public String get_name()
	{
		return m_name;
	}

	/**
	 * Get the author of the Experiment
	 * @return Reference to the Experiment author
	 */
	public String get_author()
	{
		return m_author;
	}

	/**
	 * Get the date of the Experiment
	 * @return Date of the Experiment
	 */
	public SxDate get_date()
	{
		return m_date;
	}

	/**
	 * Get the URI of the raw SxDataset
	 * @return URI of the raw SxDataset
	 */
	public SxDatasetMetadata get_raw_dataset()
	{
		return m_raw_dataset;
	}

	/**
	 * Get the URI list of the processed SxDataset
	 * @return URI list of the processed SxDataset
	 */
	public List<SxDatasetMetadata> get_processed_datasets()
	{
		return m_processed_datasets;
	}

	/**
	 * Get the number of processed SxDataset
	 * @return Number of processed SxDataset
	 */
	public int get_processed_datasets_count()
	{
		return m_processed_datasets.size();
	}

	/**
	 * Get the URI of the processed SxDataset at index
	 * @param index Index of the processed SxDataset
	 * @return Pointer to the processed SxDataset metadata at index
	 */
	SxDatasetMetadata get_processed_dataset(int index)
	{
		return m_processed_datasets.get(index);
	}

	/**
	 * Get the list of tags keys used in the Experiment
	 * @return List of tags keys used in the Experiment
	 */
	public List<String> get_tags_keys()
	{
		return m_tags_keys;
	}

	/**
	 * Get the number of tag keys in the Experiment
	 * @return Number of tag keys in the Experiment
	 */
	public int get_tags_keys_count()
	{
		return m_tags_keys.size();
	}
	
	/**
	 * Get the tag key at index
	 * @param index Index of the tag key
	 * @return Tag key at index
	 */
	public String get_tags_key(int index)
	{
		return m_tags_keys.get(index);
	}

	/**
	 * Set the name of the Experiment
	 * @param name Name of the Experiment
	 */
	public void set_name(String name)
	{
		m_name = name;
	}
	
	/**
	 * Set the author of the Experiment
	 * @param user Reference to the Experiment author
	 */
	public void set_author(String user)
	{
		m_author = user;
	}
	
	/**
	 * Set the date of the Experiment
	 * @param date Date of the Experiment
	 */
	public void set_date(SxDate date)
	{
		m_date = date;
	}
	
	/**
	 * Set the URI of the raw SxDataset
	 * @param metadata URI of the raw SxDataset
	 */
	public void set_raw_dataset(SxDatasetMetadata metadata)
	{
		m_raw_dataset = metadata;
	}
	
	/**
	 * Set the metadata of a processed SxDataset
	 * @param metadata Processed SxDataset metadata
	 */
	public void set_processed_dataset(SxDatasetMetadata metadata)
	{
	    boolean found = false;
	    for (int i = 0 ; i < m_processed_datasets.size() ; i++)
	    {
	        if (m_processed_datasets.get(i).get_md_uri() == metadata.get_md_uri())
	        {
	            found = true;
	        }
	    }
	    if (!found)
	    {
	        m_processed_datasets.add(metadata);
	    }
	}
	
	/**
	 * Set a tag key
	 * @param key Tag key
	 */
	public void set_tag_key(String key)
	{
	    boolean found = false;
	    for (int i = 0 ; i < m_tags_keys.size() ; i++)
	    {
	        if (m_tags_keys.get(i) == key)
	        {
	            found = true;
	        }
	    }
	    if (!found)
	    {
	        m_tags_keys.add(key);
	    }
	}
	
	/**
	 * Set the tag keys
	 * @param keys list of tag keys
	 */
	public void set_tag_keys(List<String> keys)
	{
		m_tags_keys = keys;
	}

}
