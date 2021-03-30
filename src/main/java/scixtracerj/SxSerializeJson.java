package scixtracerj;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class SxSerializeJson extends SxSerialize{

	public SxSerializeJson()
	{

	}

	@SuppressWarnings("unchecked")
	public String serialize_rawdata(SxRawData container) throws Exception
	{
	    JSONObject metadata = new JSONObject();
	    metadata.put("uuid", container.get_uuid());

	    JSONObject origin = new JSONObject();
	    origin.put("type", "raw");
	    metadata.put("origin", origin);

	    JSONObject common = new JSONObject();
	    common.put("name", container.get_name());
	    common.put("author", container.get_author());
	    common.put("date", container.get_date().get_to_string("YYYY-MM-DD"));
	    common.put("format", container.get_format().get_name());
	    common.put("url", container.get_uri());
	    metadata.put("common", common);

	    JSONObject tags = new JSONObject();
	    SxTags _tags = container.get_tags();
	    for(String key: _tags.get_keys())
	    {
	    	tags.put(key, _tags.get_tag(key));
	    }

	    return metadata.toString();
	}

	@SuppressWarnings("unchecked")
	public String serialize_processeddata(SxProcessedData container) throws Exception
	{
	    JSONObject metadata = new JSONObject();
	    metadata.put("uuid", container.get_uuid());

	     // common
	    JSONObject common = new JSONObject();
	    common.put("name", container.get_name());
	    common.put("author", container.get_author());
	    common.put("date", container.get_date().get_to_string("YYYY-MM-DD"));
	    common.put("format", container.get_format().get_name());
	    common.put("url", container.get_uri());
	    metadata.put("common", common);
	    // origin type
	    JSONObject origin = new JSONObject();
	    origin.put("type", "processed");
	    // run url
	    JSONObject runObject = new JSONObject();
	    runObject.put("uuid", container.get_run().get_uuid());
	    runObject.put("url", container.get_run().get_md_uri());
	    origin.put("run", runObject);
	    // origin inputs
	    JSONArray inputs_json = new JSONArray();
	    for (int i = 0 ; i < container.get_run_inputs_count() ; ++i)
	    {
	        SxProcessedDataInput input_data = container.get_run_input(i);
	        JSONObject input_json = new JSONObject();
	        input_json.put("name", input_data.get_name());
	        input_json.put("uuid", input_data.get_data().get_uuid());
	        input_json.put("url", input_data.get_data().get_uuid());
	        input_json.put("type", input_data.get_type());
	        inputs_json.add(input_json);
	    }
	    origin.put("inputs", inputs_json);
	    // origin ouput
	    JSONObject output_json = new JSONObject();
	    output_json.put("name", container.get_run_output().get_name());
	    output_json.put("label", container.get_run_output().get_label());
	    origin.put("output", inputs_json);
	    metadata.put("origin", origin);

	    return metadata.toString();
	}

	@SuppressWarnings("unchecked")
	public String serialize_dataset(SxDataset container)
	{
	    JSONObject metadata = new JSONObject();
	    metadata.put("uuid", container.get_uuid());
	    metadata.put("name", container.get_name());
	    JSONArray urls_json = new JSONArray();
	    for (int i = 0 ; i < container.get_data_count() ; ++i)
	    {
	        JSONObject obj = new JSONObject();
	        obj.put("uuid", container.get_data(i).get_uuid());
	        obj.put("url", container.get_data(i).get_md_uri());
	        urls_json.add(obj);
	    }
	    metadata.put("urls", urls_json);

	    return metadata.toString();
	}

	@SuppressWarnings("unchecked")
	public String serialize_run(SxRun container)
	{
	    JSONObject json_metadata = new JSONObject();
	    json_metadata.put("uuid", container.get_uuid());
	    JSONObject json_process = new JSONObject();
	    json_process.put("name", container.get_process_name());
	    json_process.put("url", container.get_process_uri());
	    json_metadata.put("process", json_process);

	    JSONObject json_processeddataset = new JSONObject();
	    json_processeddataset.put("uuid", container.get_processed_dataset().get_uuid());
	    json_processeddataset.put("url", container.get_processed_dataset().get_md_uri());
	    json_metadata.put("processeddataset", json_processeddataset);

	    JSONArray json_inputs = new JSONArray();
	    for (int i = 0 ; i < container.get_inputs_count() ; ++i){
	        JSONObject json_input = new JSONObject();
	        SxRunInput input = container.get_input(i);
	        json_input.put("name", input.get_name());
	        json_input.put("dataset", input.get_dataset_name());
	        json_input.put("query", input.get_query());
	        json_input.put("origin_output_name", input.get_origin_output_name());
	        json_inputs.add(json_input);
	    }
	    json_metadata.put("inputs", json_inputs);

	    JSONArray json_parameters = new JSONArray();
	    for (int i = 0 ; i < container.get_parameters_count() ; ++i){
	        JSONObject json_parameter = new JSONObject();
	        json_parameter.put("name", container.get_parameter(i).get_name());
	        json_parameter.put("value", container.get_parameter(i).get_value());
	        json_parameters.add(json_parameter);
	    }
	    json_metadata.put("parameters", json_parameters);

	    return json_metadata.toString();
	}

	@SuppressWarnings("unchecked")
	public String serialize_experiment(SxExperiment container) throws Exception
	{
	    JSONObject metadata = new JSONObject();
	    JSONObject information = new JSONObject();
	    information.put("name", container.get_name());
	    information.put("author",container.get_author());
	    information.put("date", container.get_date().get_to_string("YYYY-MM-DD"));
	    metadata.put("information", information);

	    JSONObject json_rawdataset = new JSONObject();
	    json_rawdataset.put("name", container.get_raw_dataset().get_name());
	    json_rawdataset.put("url", container.get_raw_dataset().get_md_uri());
	    json_rawdataset.put("uuid", container.get_raw_dataset().get_uuid());
	    metadata.put("rawdataset", json_rawdataset);

	    JSONArray jprocesseddatasets = new JSONArray();
	    for (int i = 0 ; i < container.get_processed_datasets_count() ; ++i)
	    {
	        SxDatasetMetadata met = container.get_processed_dataset(i);
	        JSONObject json_met = new JSONObject();
	        json_met.put("name", met.get_name());
	        json_met.put("url", met.get_md_uri());
	        json_met.put("uuid", met.get_uuid());
	        jprocesseddatasets.add(json_met);
	    }
	    metadata.put("processeddatasets", jprocesseddatasets);
	    JSONArray jtags = new JSONArray();
	    for (int i = 0 ; i < container.get_tags_keys_count() ; ++i)
	    {
	        jtags.add(container.get_tags_key(i));
	    }
	    metadata.put("tags", jtags);

	    return metadata.toString();
	}
	
}
