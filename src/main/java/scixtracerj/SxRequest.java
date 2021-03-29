package scixtracerj;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class SxRequest {

	public SxRequest()
	{

	}	

	/**
	 * Create a new experiment
	 * @param name Name of the experiment
	 * @param author Username of the experiment author
	 * @param  date Creation date of the experiment
	 * @param tag_keys List of keys used for the experiment vocabulary
	 * @param destination Destination where the experiment is created. It is a the path of the directory where the experiment will be created for local use case
	 * @returns Experiment container with the experiment metadata
	 */
	public abstract SxExperiment create_experiment(String name, String author, SxDate date, List<String> tag_keys, String destination);

	/**
	 * Create a new experiment
	 * @param name Name of the experiment
	 * @param author Username of the experiment author
	 * @throws Exception 
	 * @returns Experiment container with the experiment metadata
	 */
	public SxExperiment create_experiment(String name, String author) throws Exception
	{
		List<String> tag_keys = new ArrayList<String>();
		return this.create_experiment(name, author, new SxDate("now"), tag_keys, ".");
	}

	/**
	 * Read an experiment from the database
	 * @param uri URI of the experiment. For local use case, the URI is either the path of the experiment directory, or the path of the experiment.md.json file
	 * @return Experiment container with the experiment metadata
	 */
	public abstract SxExperiment get_experiment(String uri);

	/**
	 *  Write an experiment to the database
	 * @param experiment Container of the experiment metadata
	 */
	public abstract void update_experiment(SxExperiment experiment);

	/**
	 * import one data to the experiment. The data is imported to the rawdataset
	 * @param experiment Container of the experiment metadata
	 * @param data_path Path of the accessible data on your local computer
	 * @param name Name of the data
	 * @param author Person who created the data
	 * @param format Format of the data (ex: tif)
	 * @param date Date when the data where created
	 * @param tags Tags to identify the data
	 * @param copy True to copy the data to the Experiment database False otherwise
	 * @return a RawData containing the metadata
	 */
	public abstract SxRawData import_data(SxExperiment experiment, String data_path, String name, String author, SxFormat format, SxDate date, SxTags tags, boolean copy);

	/**
	 * Read a raw data from the database
	 * @param uri URI if the rawdata
	 * @return RawData object containing the raw data metadata
	 */
	public abstract SxRawData get_rawdata(String uri);

	/**
	 * Read a raw data from the database
	 * @param rawdata Container with the rawdata metadata
	 */
	public abstract void update_rawdata(SxRawData rawdata);

	/**
	 * Read a processed data from the database
	 * @param uri URI if the processeddata
	 * @return ProcessedData object containing the raw data metadata
	 */
	public abstract  SxProcessedData get_processeddata(String uri);

	/**
	 * Read a processed data from the database
	 * @param processeddata Container with the processeddata metadata
	 */
	public abstract void update_processeddata(SxProcessedData processeddata);

	/**
	 * Read a dataset from the database using it URI
	 * @param uri URI if the dataset
	 * @return Dataset object containing the dataset metadata
	 */
	public abstract SxDataset get_dataset_from_uri(String uri);

	/**
	 * Read a processed data from the database
	 * @param dataset Container with the dataset metadata
	 */
	public abstract void update_dataset(SxDataset dataset);

	/**
	 * Create a processed dataset in an experiment
	 * @param experiment Object containing the experiment metadata
	 * @param dataset_name Name of the dataset
	 * @return Dataset object containing the new dataset metadata
	 */
	public abstract SxDataset create_dataset(SxExperiment experiment, String dataset_name);

	/**
	 * Create a new run metadata
	 * @param dataset Object of the dataset metadata
	 * @param run_info Object containing the metadata of the run. md_uri is ignored and created automatically by this method
	 * @return Run object with the metadata and the new created md_uri
	 */
	public abstract SxRun create_run(SxDataset dataset, SxRun run_info);

	/**
	 * Read a run metadata from the data base
	 * @param  uri URI of the run entry in the database
	 * @return Run: object containing the run metadata
	 */
	public abstract SxRun get_run(String uri);

	/**
	 * Create a new processed data for a given dataset
	 * @param dataset Object of the dataset metadata
	 * @param run Metadata of the run
	 * @param processed_data Object containing the new processed data. md_uri is ignored and created automatically by this method
	 * @return
	 */
	public abstract SxProcessedData create_data(SxDataset dataset, SxRun run, SxProcessedData processed_data);


	/**
	 * Import data from a directory to the experiment
	 * This method import with or without copy data contained
	 * in a local folder into an experiment. Imported data are
	 * considered as RawData for the experiment
	 * @param experiment Container of the experiment metadata
	 * @param dir_uri URI of the directory containing the data to be imported
	 * @param filter Regular expression to filter which files in the folder to import
	 * @param author Name of the person who created the data
	 * @param format Format of the image (ex: tif)
	 * @param date Date when the data where created
	 * @param copy_data True to copy the data to the experiment, false otherwise. If the data are not copied, an absolute link to dir_uri is kept in the experiment metadata. The original data directory must then not be changed for the experiment to find the data.
	 */
	void import_dir(SxExperiment experiment, String dir_uri, String filter, String author, SxFormat format, SxDate date, boolean copy_data)
	{
		File directory = new File(dir_uri);
	    String data_files[] = directory.list();
	    Arrays.sort(data_files);   
	      
	    for (int i = 0 ; i < data_files.length ; i++){
	        String filename = data_files[i];
	        
	        Pattern pattern = Pattern.compile(filter, Pattern.CASE_INSENSITIVE);
	        Matcher matcher = pattern.matcher(filename);
	        boolean matchFound = matcher.find();
	        if(matchFound) {
	            String data_url = dir_uri + File.separator + filename;
	            this.import_data(experiment, data_url, filename,
	                              author, format, date,
	                              new SxTags(), copy_data);
	        }
	    }
	}

	/**
	 * Tag an experiment raw data using raw data file names
	 * @param experiment Container of the experiment metadata
	 * @param tag The name (or key) of the tag to add to the data
	 * @param values List of possible values (str) for the tag to find in the filename
	 */
	void tag_from_name(SxExperiment experiment, String tag, List<String> values)
	{
	    experiment.set_tag_key(tag);
	    this.update_experiment(experiment);
	    SxDataset _rawdataset = this.get_rawdataset(experiment);
	    for (int i = 0 ; i < _rawdataset.get_data_count() ; ++i){
	        SxRawData _rawdata = this.get_rawdata(_rawdataset.get_data(i).get_md_uri());
	        for (int j = 0 ; j < values.size() ; j++){
	            String value = values.get(j);
	            if (_rawdata.get_name().contains(value)){
	                _rawdata.get_tags().set_tag(tag, value);
	                this.update_rawdata(_rawdata);
	                break;
	            }
	        }
	    }
	}

	/**
	 * Tag an experiment raw data using file name and separator
	 * @param experiment Container of the experiment metadata
	 * @param tag The name (or key) of the tag to add to the data
	 * @param separator The character used as a separator in the filename (ex: _)
	 * @param value_position Position of the value to extract with respect to the separators
	 */
	void tag_using_separator(SxExperiment experiment, String tag, String separator, int value_position)
	{
	    experiment.set_tag_key(tag);
	    this.update_experiment(experiment);
	    SxDataset _rawdataset = this.get_rawdataset(experiment);
	    for (int i = 0 ; i < _rawdataset.get_data_count() ; i++)
	    {
	        SxRawData _rawdata = this.get_rawdata(_rawdataset.get_data(i).get_md_uri());
	        Path path = Paths.get(_rawdata.get_uri());
	        String splited_name[] = path.getFileName().toString().split(separator);
	        
	        String value = "";
	        if (splited_name.length > value_position){
	            value = splited_name[value_position];
	        }
	        _rawdata.get_tags().set_tag(tag, value);
	        this.update_rawdata(_rawdata);
	    }
	}

	/**
	 * Get the metadata of the parent data
	 * The parent data can be a RawData or a ProcessedData
	 * depending on the process chain
	 * @param processed_data Container of the processed data URI
	 * @return Parent data (RawData or ProcessedData)
	 */
	SxData get_parent(SxProcessedData processed_data)
	{
	    if (processed_data.get_run_inputs_count() > 0)
	    {
	        if (processed_data.get_run_input(0).get_type() == "raw")
	        {
	            return this.get_rawdata(processed_data.get_run_input(0).get_data().get_md_uri());
	        }
	        else
	        {
	            return this.get_processeddata(processed_data.get_run_input(0).get_data().get_md_uri());
	        }
	    }
	    return null;
	}

	/**
	 * Get the first metadata of the parent data
	 * The origin data is a RawData. It is the first data that have
	 * been seen in the raw dataset
	 * @param processeddata Container of the processed data URI
	 * @return  the origin data in a RawData object
	 */
	SxRawData get_origin(SxProcessedData processed_data)
	{
	    if (processed_data.get_run_inputs_count() > 0)
	    {
	        if (processed_data.get_run_input(0).get_type() == "raw"){
	            return this.get_rawdata(processed_data.get_run_input(0).get_data().get_md_uri());
	        }
	        else
	        {
	            return this.get_origin(this.get_processeddata(processed_data.get_run_input(0).get_data().get_md_uri()));
	        }
	    }
	    return null;
	}

	/**
	 * Read the raw dataset from the database
	 * @param experiment Container of the experiment metadata
	 * @return Dataset object containing the dataset metadata
	 */
	SxDataset get_rawdataset(SxExperiment experiment)
	{
		return this.get_dataset_from_uri(experiment.get_raw_dataset().get_md_uri());
	}

	/**
	 * Query a dataset from it name
	 * @param experiment Object containing the experiment metadata
	 * @param name Name of the dataset to query
	 * @return a Dataset containing the dataset metadata. None is return if the dataset is not found
	 */
	SxDataset get_dataset(SxExperiment experiment, String name)
	{
	    if (name == "data"){
	        return this.get_dataset_from_uri(experiment.get_raw_dataset().get_md_uri());
	    }
	    else
	    {
	        for (int i = 0 ; i < experiment.get_processed_datasets_count() ; ++i)
	        {
	            SxDataset pdataset = this.get_dataset_from_uri(experiment.get_processed_dataset(i).get_md_uri());
	            if (pdataset.get_name() == name)
	            {
	                return pdataset;
	            }
	        }
	    }
	    return null;
	}

	/**
	 * Query data from a dataset
	 * @param dataset Object containing the dataset metadata
	 * @param query String query with the key=value format
	 * @param origin_output_name Name of the output origin (ex: -o) in the case of ProcessedDataset search
	 * @return List of selected data (list of RawData or ProcessedData objects)
	 * @throws Exception 
	 */
	List<SxData> get_data(SxDataset dataset, String query, String origin_output_name) throws Exception
	{

	    if (dataset.get_data_count() < 1)
	    {
	        List<SxData> li = new ArrayList<SxData>();
	        return li;
	    }

	    // search the dataset
	    String queries[] = query.split(" AND ");

	    // initially all the raw data are selected
	    List<SxSearchContainer> selected_list = new ArrayList<SxSearchContainer>();
	    // raw dataset
	    if (dataset.get_name() == "data")
	    {
	        for (int i = 0 ; i < dataset.get_data_count() ; ++i)
	        {
	            SxMetadata data_info = dataset.get_data(i);
	            SxRawData data_container = this.get_rawdata(data_info.get_md_uri());
	            selected_list.add(this._rawdata_to_search_container(data_container));
	        }
	    }
	    // processed dataset
	    else{
	        List<SxSearchContainer> pre_list = new ArrayList<SxSearchContainer>();
	        for (int i = 0 ; i < dataset.get_data_count() ; ++i)
	        {
	            SxProcessedData p_con = this.get_processeddata(dataset.get_data(i).get_md_uri());
	            pre_list.add(this._processed_data_to_search_container(p_con));
	        }
	        // remove the data where output origin is not the asked one
	        if (origin_output_name != "")
	        {
	            for (int i = 0 ; i < pre_list.size() ; ++i)
	            {
	                SxProcessedData data = this.get_processeddata(pre_list.get(i).get_uri());
	                if (data.get_run_output().get_name() == origin_output_name){
	                    selected_list.add(pre_list.get(i));
	                }
	            }
	        }
	        else{
	            selected_list = pre_list;
	        }
	    }
	    // run all the AND queries on the preselected dataset
	    if (query != "")
	    {
	        for (int i = 0 ; i < queries.length ; ++i)
	        {
	            selected_list = SxSearch.query_list_single(selected_list, queries[i]);
	        }
	    }

	    // convert SearchContainer list to uri list
	    List<SxData> out = new ArrayList<SxData>();
	    for (int i = 0 ; i < selected_list.size() ; ++i)
	    {
	        SxSearchContainer d = selected_list.get(i);
	        if (dataset.get_name() == "data")
	        {
	            out.add(this.get_rawdata(d.get_uri()));
	        }
	        else
	        {
	            out.add(this.get_processeddata(d.get_uri()));
	        }
	    }
	    return out;
	}

	/**
	 * Convert a RawData to SearchContainer
	 * @param rawdata Object containing the rawdata
	 * @return SearchContainer object
	 */
	SxSearchContainer _rawdata_to_search_container(SxRawData rawdata)
	{
	    SxSearchContainer info = new SxSearchContainer(rawdata.get_name(),
										               rawdata.get_md_uri(),
										               rawdata.get_uuid(),
										               rawdata.get_tags());
	    return info;
	}

	/**
	 * convert a ProcessedData to SearchContainer
	 * @param processeddata Object containing the processeddata
	 * @return SearchContainer object
	 */
	SxSearchContainer _processed_data_to_search_container(SxProcessedData processeddata)
	{
	    SxSearchContainer container;// = new SxSearchContainer();
	    try{
	        SxRawData origin = this.get_origin(processeddata);
	        if (origin != null){
	            container = this._rawdata_to_search_container(origin);
	        }
	        else{
	            container = new SxSearchContainer();
	        }
	    }
	    catch (Exception e){
	    	container = new SxSearchContainer();
	    }
	    container.set_name(processeddata.get_name());
	    container.set_uri(processeddata.get_md_uri());
	    container.set_uuid(processeddata.get_uuid());
	    return container;
	}


}
