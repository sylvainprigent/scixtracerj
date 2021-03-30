package scixtracerj;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class SxRequestLocal extends SxRequest{


	public SxRequestLocal()
	{

	}

	public SxExperiment create_experiment(String name, String author, SxDate date, List<String> tag_keys, String destination) throws Exception
	{
		SxExperiment container = new SxExperiment();
		container.set_uuid(this._generate_uuid());
		container.set_name(name);
		container.set_author(author);
		container.set_date(date);
		for (String key: tag_keys) {
			container.set_tag_key(key);
		}

		// check the destination dir
		File f_info = new File(destination);
		String uri = f_info.getAbsolutePath();
		if (!f_info.exists()){
			throw new Exception("Cannot create Experiment: the destination directory does not exists");
		}

		// create the experiment directory
		String filtered_name = name;
		filtered_name = filtered_name.replace(" ", "");
		String experiment_path = SxRequestLocal.path_join(uri, filtered_name);
		File exp_dir = new File(experiment_path);
		if (!exp_dir.exists())
		{
			exp_dir.mkdir();
		}
		else{
			throw new Exception("Cannot create Experiment: the experiment directory already exists");
		}

		// create an empty raw dataset
		String rawdata_path = SxRequestLocal.path_join(experiment_path, "data");
		String rawdataset_md_url = SxRequestLocal.path_join(rawdata_path, "rawdataset.md.json");
		File raw_data_dir = new File(rawdata_path);
		if (!raw_data_dir.exists()){
			raw_data_dir.mkdir();
		}
		else
		{
			throw new Exception("Cannot create Experiment raw dataset: the experiment directory does not exists");
		}

		SxDataset rawdataset = new SxDataset();
		rawdataset.set_uuid(this._generate_uuid());
		rawdataset.set_md_uri(rawdataset_md_url);
		rawdataset.set_name("data");
		this.update_dataset(rawdataset);
		container.set_raw_dataset(new SxDatasetMetadata(rawdataset.get_name(), rawdataset_md_url, rawdataset.get_uuid()));

		// save the experiment.md.json metadata file
		container.set_md_uri(SxRequestLocal.path_join(experiment_path, "experiment.md.json"));
		this.update_experiment(container);
		return container;
	}

	public SxExperiment get_experiment(String uri) throws Exception 
	{
		String md_uri = SxRequestLocal.abspath(uri);
		File f_info = new File(md_uri);
		if (f_info.exists())
		{
			JSONObject metadata = this._read_json(md_uri);
			SxExperiment container = new SxExperiment();
			container.set_uuid(metadata.get("uuid").toString());
			container.set_md_uri(md_uri);

			JSONObject information = (JSONObject)metadata.get("information");
			container.set_name(information.get("name").toString());
			container.set_author(information.get("author").toString());
			container.set_date(new SxDate(information.get("date").toString()));

			JSONObject json_rawdataset = (JSONObject)metadata.get("rawdataset");
			String rawdataset_url = SxRequestLocal.absolute_path(SxRequestLocal.normalize_path_sep(json_rawdataset.get("url").toString()), md_uri);
			container.set_raw_dataset(new SxDatasetMetadata(json_rawdataset.get("name").toString(), rawdataset_url, json_rawdataset.get("uuid").toString()));

			JSONArray json_processeddatasets = (JSONArray)metadata.get("processeddatasets");
			for (int i = 0 ; i < json_processeddatasets.size() ; ++i)
			{
				JSONObject dataset = (JSONObject)json_processeddatasets.get(i);
				String processeddataset_url = SxRequestLocal.absolute_path(SxRequestLocal.normalize_path_sep(dataset.get("url").toString()), md_uri);
				container.set_processed_dataset(new SxDatasetMetadata(dataset.get("name").toString(), processeddataset_url, dataset.get("uuid").toString()));
			}
			JSONArray json_tags = (JSONArray)metadata.get("tags");
			for (int i = 0 ; i < json_tags.size() ; ++i)
			{
				String key = json_tags.get(i).toString();
				container.set_tag_key(key);
			}
			return container;
		}
		throw new Exception("Cannot read the experiment at " + md_uri);
	}

	@SuppressWarnings("unchecked")
	public void update_experiment(SxExperiment experiment) throws Exception
	{
		String md_uri = SxRequestLocal.abspath(experiment.get_md_uri());
		JSONObject metadata = new JSONObject();
		metadata.put("uuid", experiment.get_uuid());

		// informations
		JSONObject information = new JSONObject();
		information.put("name", experiment.get_name());
		information.put("author", experiment.get_author());
		information.put("date", experiment.get_date().get_to_string("YYYY-MM-DD"));
		metadata.put("information", information);

		// raw dataset
		String tmp_url = SxRequestLocal.to_unix_path(SxRequestLocal.relative_path(experiment.get_raw_dataset().get_md_uri(), md_uri));
		JSONObject json_rawdataset = new JSONObject();
		json_rawdataset.put("name", experiment.get_raw_dataset().get_name());
		json_rawdataset.put("url", tmp_url);
		json_rawdataset.put("uuid", experiment.get_raw_dataset().get_uuid());
		metadata.put("rawdataset", json_rawdataset);

		// processed datasets
		JSONArray json_pdatasets = new JSONArray();
		for (int i = 0 ; i < experiment.get_processed_datasets_count() ; ++i)
		{
			SxDatasetMetadata pdataset = experiment.get_processed_dataset(i);
			String tmp_url2 = SxRequestLocal.to_unix_path(SxRequestLocal.relative_path(pdataset.get_md_uri(), md_uri));

			JSONObject json_pdataset = new JSONObject();
			json_pdataset.put("name", pdataset.get_name());
			json_pdataset.put("url", tmp_url2);
			json_pdataset.put("uuid", pdataset.get_uuid());

			json_pdatasets.add(json_pdataset);
		}
		metadata.put("processeddatasets", json_pdatasets);

		// tags keys
		JSONArray json_tags = new JSONArray();
		for (int i = 0 ; i < experiment.get_tags_keys_count() ; ++i)
		{
			json_tags.add(experiment.get_tags_key(i));
		}
		metadata.put("tags", json_tags);

		// write
		this._write_json(metadata, md_uri);
	}

	public SxRawData import_data(SxExperiment experiment, String data_path, String name, String author, SxFormat format, SxDate date, SxTags tags, boolean copy) throws Exception
	{
		String rawdataset_uri = SxRequestLocal.abspath(experiment.get_raw_dataset().get_md_uri());
		File file_info = new File(rawdataset_uri);
		String data_dir_path = file_info.getParentFile().getAbsolutePath();

		// create the new data uri
		File data_file_info = new File(data_path);
		String data_base_name = remove_extension(data_file_info.getName());
		String filtered_name = data_base_name.replace(" ", "");
		String md_uri = SxRequestLocal.path_join(data_dir_path, filtered_name + ".md.json");

		// create the container
		SxRawData metadata = new SxRawData();
		metadata.set_uuid(this._generate_uuid());
		metadata.set_md_uri(md_uri);
		metadata.set_name(name);
		metadata.set_author(author);
		metadata.set_format(format);
		metadata.set_date(date);
		metadata.set_tags(tags);

		// import data
		if (copy){
			String extension;
			if (format.get_name().startsWith(".")){
				extension = format.get_name();
			}
			else{
				extension = "." + format.get_name();
			}
			String copied_data_path = SxRequestLocal.path_join(data_dir_path, data_base_name + extension);
			Files.copy((Path)Paths.get(data_path), (Path)Paths.get(copied_data_path));
			metadata.set_uri(copied_data_path);
		}
		else
		{
			metadata.set_uri(data_path);
		}
		this.update_rawdata(metadata);

		// add data to experiment RawDataSet
		SxDataset rawdataset_container = this.get_dataset_from_uri(rawdataset_uri);
		SxMetadata raw_c = new SxMetadata(metadata.get_md_uri(), metadata.get_uuid());
		rawdataset_container.set_data(raw_c);
		this.update_dataset(rawdataset_container);

		// add tags keys to experiment
		for (String item: tags.get_keys()) 
		{
			experiment.set_tag_key(item);
        }
		this.update_experiment(experiment);

		return metadata;
	}

	public SxRawData get_rawdata(String  uri) throws Exception
	{
		String md_uri = SxRequestLocal.abspath(uri);
		File md_file = new File(md_uri);
		if (md_file.exists() && md_uri.endsWith(".md.json"))
		{
			JSONObject metadata = this._read_json(md_uri);
			SxRawData container = new SxRawData();
			container.set_uuid(metadata.get("uuid").toString());
			container.set_md_uri(md_uri);
			JSONObject json_origin = (JSONObject)metadata.get("origin");
			container.set_type(json_origin.get("type").toString());

			JSONObject json_common = (JSONObject)metadata.get("common");
			container.set_name(json_common.get("name").toString());
			container.set_author(json_common.get("author").toString());
			container.set_date(new SxDate(json_common.get("date").toString()));
			container.set_format(new SxFormat(json_common.get("format").toString()));
			// copy the url if absolute, append md_uri path otherwise
			container.set_uri(SxRequestLocal.absolute_path(SxRequestLocal.normalize_path_sep(json_common.get("url").toString()), md_uri));


			if (metadata.keySet().contains("tags"))
			{
				JSONObject tags = (JSONObject)metadata.get("tags");
				SxTags tags_container = new SxTags();
				for(Object item: tags.keySet()) {
					tags_container.set_tag(item.toString(), tags.get(item.toString()).toString());
				}
				container.set_tags(tags_container);
			}
			return container;
		}
		throw new Exception("Metadata file format not supported");
	}

	@SuppressWarnings("unchecked")
	public void update_rawdata(SxRawData rawdata) throws Exception
	{
		String md_uri = SxRequestLocal.abspath(rawdata.get_md_uri());

		JSONObject metadata = new JSONObject();
		metadata.put("uuid", rawdata.get_uuid());

		JSONObject json_origin = new JSONObject();
		json_origin.put("type", "raw");
		metadata.put("origin", json_origin);

		JSONObject json_common = new JSONObject();
		json_common.put("name", rawdata.get_name());
		json_common.put("author", rawdata.get_author());
		json_common.put("date", rawdata.get_date().get_to_string("YYYY-MM-DD"));
		json_common.put("format", rawdata.get_format().get_name());
		json_common.put("url", SxRequestLocal.to_unix_path(SxRequestLocal.relative_path(rawdata.get_uri(), md_uri)));
		metadata.put("common", json_common);

		JSONObject json_tags = new JSONObject();
		SxTags _tags = rawdata.get_tags();
		for (String item: _tags.get_keys()) 
		{
			json_tags.put(item, _tags.get_tag(item));
        }
		metadata.put("tags", json_tags);

		this._write_json(metadata, md_uri);
	}

	public SxProcessedData get_processeddata(String uri) throws Exception
	{
		String md_uri = SxRequestLocal.abspath(uri);
		File md_file = new File(md_uri);
		if (md_file.exists() && md_uri.endsWith(".md.json"))
		{
			JSONObject metadata = this._read_json(md_uri);
			SxProcessedData container = new SxProcessedData();
			container.set_uuid(metadata.get("uuid").toString());
			container.set_md_uri(md_uri);

			JSONObject json_common = (JSONObject)metadata.get("common");
			container.set_name(json_common.get("name").toString());
			container.set_author(json_common.get("author").toString());
			container.set_date(new SxDate(json_common.get("date").toString()));
			container.set_format(new SxFormat(json_common.get("format").toString()));
			container.set_uri(SxRequestLocal.absolute_path(SxRequestLocal.normalize_path_sep(json_common.get("url").toString()), md_uri));
			// origin run
			JSONObject json_run = (JSONObject)(((JSONObject)metadata.get("origin")).get("run"));
			container.set_run(new SxMetadata(SxRequestLocal.absolute_path(SxRequestLocal.normalize_path_sep(json_run.get("url").toString()), md_uri), json_run.get("uuid").toString()));
			// origin input
			JSONArray json_inputs = (JSONArray)(((JSONObject)metadata.get("origin")).get("inputs"));
			for(int i = 0 ; i < json_inputs.size() ; ++i)
			{
				JSONObject json_input = (JSONObject)json_inputs.get(i);
				container.set_run_input(json_input.get("name").toString(),
						new SxProcessedDataInput(
								json_input.get("name").toString(),
								new SxMetadata(SxRequestLocal.absolute_path(SxRequestLocal.normalize_path_sep(json_input.get("url").toString()), md_uri), json_input.get("uuid").toString()),
								json_input.get("type").toString()
								));
			}

			// origin output
			JSONObject json_output = (JSONObject)(((JSONObject)metadata.get("origin")).get("output"));
			if (json_output.containsKey("name") && json_output.containsKey("label"))
			{
				SxProcessedDataOutput poutput = new SxProcessedDataOutput();
				poutput.set_name(json_output.get("name").toString());
				poutput.set_label(json_output.get("label").toString());
				container.set_run_output(poutput);
			}
			return container;
		}
		throw new Exception("Metadata file format not supported");
	}

	@SuppressWarnings("unchecked")
	public void update_processeddata(SxProcessedData processeddata) throws Exception
	{
		String md_uri = SxRequestLocal.abspath(processeddata.get_md_uri());
		JSONObject metadata = new JSONObject();
		metadata.put("uuid", processeddata.get_uuid());

		// common
		JSONObject json_common = new JSONObject();
		json_common.put("name", processeddata.get_name());
		json_common.put("author",processeddata.get_author());
		json_common.put("date", processeddata.get_date().get_to_string("YYYY-MM-DD"));
		json_common.put("format", processeddata.get_format().get_name());
		json_common.put("url", SxRequestLocal.to_unix_path(SxRequestLocal.relative_path(processeddata.get_uri(), md_uri)));
		metadata.put("common", json_common);

		// origin type
		JSONObject json_origin = new JSONObject();
		json_origin.put("type", "processed");


		// run url
		String run_url = SxRequestLocal.to_unix_path(SxRequestLocal.relative_path(processeddata.get_run().get_md_uri(), md_uri));
		JSONObject json_run = new JSONObject();
		json_run.put("url", run_url);
		json_run.put("uuid", processeddata.get_run().get_uuid());
		json_origin.put("run", json_run);

		// origin inputs
		JSONArray json_inputs = new JSONArray();
		for (int i = 0 ; i < processeddata.get_run_inputs_count() ; ++i)
		{
			SxProcessedDataInput input_ = processeddata.get_run_input(i);
			JSONObject json_input = new JSONObject();
			json_input.put("name", input_.get_name());
			json_input.put("url", SxRequestLocal.to_unix_path(SxRequestLocal.relative_path(input_.get_data().get_md_uri(), md_uri)));
			json_input.put("uuid", input_.get_data().get_uuid());
			json_input.put("type", input_.get_type());
			json_inputs.add(json_input);
		}
		json_origin.put("inputs", json_inputs);

		// origin ouput
		JSONObject json_output = new JSONObject();
		json_output.put("name", processeddata.get_run_output().get_name());
		json_output.put("label", processeddata.get_run_output().get_label());
		json_origin.put("output", json_output);

		metadata.put("origin", json_origin);
		this._write_json(metadata, md_uri);
	}

	public SxDataset get_dataset_from_uri(String uri) throws Exception
	{
		String md_uri = SxRequestLocal.abspath(uri);
		File md_file = new File(md_uri);
		if (md_file.exists() && md_uri.endsWith(".md.json"))
		{
			JSONObject metadata = this._read_json(md_uri);
			SxDataset container = new SxDataset();
			container.set_uuid(metadata.get("uuid").toString());
			container.set_md_uri(md_uri);
			container.set_name(metadata.get("name").toString());
			JSONArray json_datas = (JSONArray)metadata.get("urls");
			for (int i = 0 ; i < json_datas.size(); ++i)
			{
				JSONObject json_data = (JSONObject)json_datas.get(i);
				container.set_data(new SxMetadata(SxRequestLocal.absolute_path(SxRequestLocal.normalize_path_sep(json_data.get("url").toString()), md_uri), json_data.get("uuid").toString()));
			}
			return container;
		}
		throw new Exception("Dataset not found");
	}

	@SuppressWarnings("unchecked")
	public void update_dataset(SxDataset dataset) throws IOException
	{
		String md_uri = SxRequestLocal.abspath(dataset.get_md_uri());
		JSONObject metadata = new JSONObject();
		metadata.put("uuid", dataset.get_uuid());
		metadata.put("name", dataset.get_name());

		JSONArray json_urls = new JSONArray();
		for (int i = 0 ; i < dataset.get_data_count() ; ++i)
		{
			SxMetadata data = dataset.get_data(i);
			JSONObject json_data = new JSONObject();
			json_data.put("url", SxRequestLocal.to_unix_path(SxRequestLocal.relative_path(data.get_md_uri(), md_uri)));
			json_data.put("uuid", data.get_uuid());
			json_urls.add(json_data);
		}
		metadata.put("urls", json_urls);
		this._write_json(metadata, md_uri);
	}

	public SxDataset create_dataset(SxExperiment experiment, String dataset_name) throws Exception
	{
		// create the dataset metadata
		String experiment_md_uri = SxRequestLocal.abspath(experiment.get_md_uri());
		File experiment_jfile = new File(SxRequestLocal.md_file_path(experiment_md_uri));
		String experiment_dir = experiment_jfile.getParent();
		String dataset_dir = SxRequestLocal.path_join(experiment_dir, dataset_name);
		File dataset_qdir = new File(dataset_dir);
		if (!dataset_qdir.exists()){
			dataset_qdir.mkdir();
		}
		String processeddataset_uri = SxRequestLocal.path_join(SxRequestLocal.path_join(experiment_dir, dataset_name), "processeddataset.md.json");
		SxDataset container = new SxDataset();
		container.set_uuid(this._generate_uuid());
		container.set_md_uri(processeddataset_uri);
		container.set_name(dataset_name);
		this.update_dataset(container);

		// add the dataset to the experiment
		String tmp_url = SxRequestLocal.to_unix_path(processeddataset_uri);
		experiment.set_processed_dataset(new SxDatasetMetadata(dataset_name, tmp_url, container.get_uuid()));
		this.update_experiment(experiment);

		return container;
	}

	public SxRun create_run(SxDataset dataset, SxRun run_info) throws IOException
	{
		// create run URI
		String dataset_md_uri = SxRequestLocal.abspath(dataset.get_md_uri());
		File dataset_jfile = new File(SxRequestLocal.md_file_path(dataset_md_uri));
		String dataset_dir = dataset_jfile.getParent();
		String run_md_file_name = "run.md.json";
		int runid_count = 0;

		File jfile = new File(SxRequestLocal.path_join(dataset_dir, run_md_file_name));
		boolean run_exists = jfile.exists();

		while (run_exists)
		{
			runid_count += 1;
			run_md_file_name = "run_" + String.valueOf(runid_count) + ".md.json";

			jfile = new File(SxRequestLocal.path_join(dataset_dir, run_md_file_name));
			run_exists = jfile.exists();
		}
		String run_uri = SxRequestLocal.path_join(dataset_dir, run_md_file_name);

		// write run
		run_info.set_processed_dataset(new SxMetadata(dataset.get_md_uri(), dataset.get_uuid()));
		run_info.set_uuid(this._generate_uuid());
		run_info.set_md_uri(run_uri);
		this._write_run(run_info);
		return run_info;
	}

	public SxRun get_run(String uri) throws Exception
	{
		String md_uri = SxRequestLocal.abspath(uri);
		File md_file = new File(md_uri);
		if (md_file.exists())
		{
			JSONObject metadata = this._read_json(md_uri);
			SxRun container = new SxRun();
			container.set_uuid(metadata.get("uuid").toString());
			container.set_md_uri(md_uri);

			// process info
			JSONObject json_process = (JSONObject)metadata.get("process");
			container.set_process_name(json_process.get("name").toString());
			container.set_process_uri(SxRequestLocal.normalize_path_sep(json_process.get("url").toString()));

			// processed dataset
			JSONObject json_processeddataset = (JSONObject)metadata.get("processeddataset");
			container.set_processed_dataset(new SxMetadata(SxRequestLocal.absolute_path(SxRequestLocal.normalize_path_sep(json_processeddataset.get("url").toString()), md_uri),
					json_processeddataset.get("uuid").toString()));

			// inputs
			JSONArray json_inputs = (JSONArray)metadata.get("inputs");
			for (int i = 0 ; i < json_inputs.size() ; ++i)
			{
				JSONObject json_input = (JSONObject)json_inputs.get(i);
				container.set_input(
						new SxRunInput(
								json_input.get("name").toString(),
								json_input.get("dataset").toString(),
								json_input.get("query").toString(),
								json_input.get("origin_output_name").toString()
								)
						);
			}

			// parameter
			JSONArray json_parameters = (JSONArray)metadata.get("parameters");
			for (int i = 0 ; i < json_parameters.size() ; ++i)
			{
				JSONObject json_parameter = (JSONObject)json_parameters.get(i);
				container.set_parameter(new SxRunParameter(json_parameter.get("name").toString(), json_parameter.get("value").toString()));
			}
			return container;
		}
		throw new Exception("Run not found");
	}

	public SxProcessedData create_data(SxDataset dataset, SxRun run, SxProcessedData processed_data) throws Exception
	{
		String md_uri = SxRequestLocal.abspath(dataset.get_md_uri());
		File dataset_jfile = new File(SxRequestLocal.md_file_path(md_uri));
		String dataset_dir = dataset_jfile.getParent();

		// create the data metadata
		String data_md_file = SxRequestLocal.path_join(dataset_dir, processed_data.get_name()+ ".md.json");
		processed_data.set_uuid(this._generate_uuid());
		processed_data.set_md_uri(data_md_file);

		processed_data.set_run(new SxMetadata(run.get_md_uri(), run.get_uuid()));

		this.update_processeddata(processed_data);

		// add the data to the dataset
		dataset.set_data(new SxMetadata(data_md_file, processed_data.get_uuid()));
		this.update_dataset(dataset);

		return processed_data;
	}

	/**
	 * Write a run metadata in the database
	 * @param run Metadata container
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	public void _write_run(SxRun run) throws IOException
	{
		JSONObject metadata = new JSONObject();
		metadata.put("uuid", run.get_uuid());

		// process
		JSONObject json_process = new JSONObject();
		json_process.put("name", run.get_process_name());
		json_process.put("url", SxRequestLocal.to_unix_path(run.get_process_uri()));
		metadata.put("process", json_process);

		// processed dataset
		JSONObject json_pdataset = new JSONObject();
		json_pdataset.put("uuid", run.get_processed_dataset().get_uuid());
		json_pdataset.put("url", SxRequestLocal.to_unix_path(SxRequestLocal.relative_path(run.get_processed_dataset().get_md_uri(), run.get_md_uri())));
		metadata.put("processeddataset", json_pdataset);

		// inputs
		JSONArray json_inputs = new JSONArray();
		for (int i = 0 ; i < run.get_inputs_count() ; ++i)
		{
			SxRunInput input_ = run.get_input(i);
			JSONObject json_input = new JSONObject();
			json_input.put("name", input_.get_name());
			json_input.put("dataset", input_.get_dataset_name());
			json_input.put("query", input_.get_query());
			json_input.put("origin_output_name",  input_.get_origin_output_name());
			json_inputs.add(json_input);
		}
		metadata.put("inputs", json_inputs);

		// parameters
		JSONArray json_parameters = new JSONArray();
		for (int i = 0 ; i < run.get_parameters_count() ; ++i)
		{
			SxRunParameter param = run.get_parameter(i);
			JSONObject json_param = new JSONObject();
			json_param.put("name", param.get_name());
			json_param.put("value", param.get_value());
			json_parameters.add(json_param);
		}
		metadata.put("parameters", json_parameters);
		this._write_json(metadata, run.get_md_uri());
	}

	/**
	 * Generate a new random UUID
	 * @return String containing the UUID
	 */
	public String _generate_uuid()
	{
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}

	/**
	 * Read the metadata from the a JSON file
	 * @param filename filename URI of the JSON file
	 * @return Object containing the JSON data
	 */
	public JSONObject _read_json(String filename) throws Exception
	{
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(new FileReader(filename));
		JSONObject jsonObject = (JSONObject)obj;
		return jsonObject;

	}

	/**
	 * Write the metadata to the a JSON file
	 * @param metadata Metadata in a JSON object
	 * @param md_uri URI of the destination file
	 * @throws IOException 
	 */
	public void _write_json(JSONObject metadata, String filename) throws IOException
	{   
		JsonObject jsonObject = JsonParser.parseString(metadata.toString()).getAsJsonObject();
		// create a writer
		FileWriter writer = new FileWriter(filename);
		// convert jsonObject to JSON File
		Gson gson = new GsonBuilder().setPrettyPrinting().create(); 
		gson.toJson(jsonObject, writer);
		writer.close();
	}

	/**
	 * get metadata file directory path
	 * @param md_uri URI of the metadata file
	 * @return The metadata file directory path
	 */
	public static String md_file_path(String md_uri)
	{
		File file = new File(md_uri);
		return file.getAbsolutePath();
	}

	/**
	 * Convert file absolute path to a relative path wrt reference_file
	 * @param file File to get absolute path
	 * @param reference_file Reference file path
	 * @return relative path of file w.r.t. reference_file
	 */
	public static String relative_path(String file, String reference_file)
	{
		String file_ = file;
		String reference_file_ = reference_file;
		String separator = File.separator;
		file_ = file_.replace(separator + separator, separator);
		reference_file_ = reference_file_.replace(separator + separator, separator);

		String common_part = new String();
		for (int i = 0 ; i < reference_file_.length() ; i++)
		{
			common_part = reference_file_.substring(0, i);
			if (!file_.contains(common_part)){
				common_part = reference_file_.substring(0, i-1);
				break;
			}
		}

		//System.out.println("common part:" + common_part);

		int last_separator = common_part.lastIndexOf(separator);
		//System.out.println("last_separator:" + last_separator);
		String short_reference_file = reference_file_.substring(last_separator+1, reference_file_.length());
		//System.out.println("short_reference_file:" + short_reference_file);

		int number_of_sub_folder = ( short_reference_file.split(separator, -1).length ) - 1;
		//System.out.println("number_of_sub_folder:" + number_of_sub_folder);

		String short_file = file.substring(last_separator + 1, file.length());
		//System.out.println("short_file:" + short_file);

		for (int i = 0 ; i < number_of_sub_folder ; i++){
			short_file = ".." + separator + short_file;
		}

		return short_file;
	}

	/**
	 * Convert file relative to reference_file into an absolute path
	 * @param file File to get absolute path
	 * @param reference_file Reference file
	 * @return absolute path of file
	 */
	public static String absolute_path(String file, String reference_file)
	{
		File f_info = new File(file);
		if (f_info.exists()){
			return f_info.getAbsolutePath();
		}

		int last_separator = reference_file.lastIndexOf(File.separator);
		String canonical_path = reference_file.substring(0, last_separator+1);
		return SxRequestLocal.simplify_path(canonical_path + file);
	}

	/**
	 * Simplify a path by removing ../
	 * @param path Path to simplify
	 * @return Simplified path
	 */
	public static String simplify_path(String path)
	{
		if (path.indexOf("..") < 0){
			return path;
		}

		List<String> keep_folders = Arrays.asList(path.split(File.separator));

		boolean found = true;
		List<String> folders = new ArrayList<String>(keep_folders);
		while (found)
		{
			int pos = -1;
			folders = new ArrayList<String>(keep_folders);
			for (int i=0 ; i < folders.size() ; ++i)
			{
				if (folders.get(i).contentEquals(".."))
				{
					pos = i;
					break;
				}
			}
			if (pos > -1)
			{
				keep_folders = new ArrayList<String>();

				for (int i = 0 ; i < pos-1 ; ++i)
				{
					keep_folders.add(folders.get(i));
				}
				for (int i = pos+1 ; i < folders.size() ; ++i)
				{
					keep_folders.add(folders.get(i));
				}
			}
			else
			{
				found = false;
			}
		}

		String clean_path = "";
		for (int i = 0 ; i < keep_folders.size(); ++i )
		{
			clean_path += keep_folders.get(i);
			if (i < keep_folders.size() - 1)
			{
				clean_path += File.separator;
			}
		}
		return clean_path;
	}

	/**
	 *  Normalize the separators of a path
	 * @param path Path to normalize
	 * @return path normalized
	 */
	public static String normalize_path_sep(String path)
	{
		String p = path;
		String p1 = p.replace("/", File.separator).replace("\\\\", File.separator);
		return p1;
	}

	/**
	 * Transform a path to Unix path
	 * @param path Path to unixify
	 * @return Path with Unix separator
	 */
	public static String to_unix_path(String path)
	{
		String p = path;
		return p.replace("\\\\", "/").replace("\\", "/");
	}

	/**
	 * Create a path by joining two part of path
	 * @param path1 First path of the path
	 * @param path2 Second part of the path
	 * @return the constructed path
	 */ 
	public static String path_join(String path1, String path2)
	{
		if (path1.endsWith(File.separator))
		{
			return path1 + path2;
		}
		return path1 + File.separator + path2;
	}

	/**
	 * get the absolute file path
	 * @param path Relative path
	 * @return the absolute path
	 */
	public static String abspath(String path)
	{
		File f = new File(path);
		return f.getAbsolutePath();
	}
	
	public static String remove_extension(String filename)
	{
		return filename.substring(0, filename.lastIndexOf('.'));
	}
}
