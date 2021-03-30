package scixtracerj;

import java.io.File;

public class CreateMetadata {

	public static SxRawData create_raw_data() throws Exception{
		
		File rawdata_file = new File("src/test/resources/test_metadata_local/data/population1_001.tif");
		String rawdata_uri = rawdata_file.getAbsolutePath();
		
		
	    SxRawData raw_data_container2 = new SxRawData();
	    raw_data_container2.set_uuid("fake_uuid");
	    raw_data_container2.set_name("population1_001.tif");
	    raw_data_container2.set_author("Sylvain Prigent");
	    raw_data_container2.set_date(new SxDate("2019-03-17"));
	    raw_data_container2.set_uri(rawdata_uri);
	    raw_data_container2.set_format(new SxFormat("tif"));
	    SxTags tags = new SxTags();
	    tags.set_tag("Population", "population1");
	    tags.set_tag("number", "001");
	    raw_data_container2.set_tags(tags);
	    return raw_data_container2;
	}

	public static SxProcessedData create_processed_data() throws Exception
	{
		File ref_processed_data_jfile = new File("src/test/resources/test_metadata_local/process1/population1_001_o.tif");
		File ref_process1_run_jfile = new File("src/test/resources/test_metadata_local/process1/run.md.json");
		File ref_rawdata_jfile1 = new File("src/test/resources/test_metadata_local/data/population1_001.md.json");
		
		String ref_processed_data_file = ref_processed_data_jfile.getAbsolutePath();
		String ref_process1_run_file = ref_process1_run_jfile.getAbsolutePath();
		String ref_rawdata_file1 = ref_rawdata_jfile1.getAbsolutePath();
		
	    SxProcessedData processed_data_container2 = new SxProcessedData();
	    processed_data_container2.set_uuid("fake_uuid");
	    processed_data_container2.set_name("population1_001_o");
	    processed_data_container2.set_author("Sylvain Prigent");
	    processed_data_container2.set_date(new SxDate("2020-03-04"));
	    processed_data_container2.set_uri(ref_processed_data_file);
	    processed_data_container2.set_format(new SxFormat("tif"));
	    
	    processed_data_container2.set_run(new SxMetadata(ref_process1_run_file, "fake_uuid"));
	    processed_data_container2.set_run_input("i", new SxProcessedDataInput("i", new SxMetadata(ref_rawdata_file1, "fake_uuid"), "raw"));
	    processed_data_container2.set_run_output(new SxProcessedDataOutput("o", "Denoised image"));

	    return processed_data_container2;
	}

	public static SxDataset create_dataset()
	{
		File ref_rawdata_jfile1 = new File("src/test/resources/test_metadata_local/data/population1_001.md.json");
		File ref_rawdata_jfile2 = new File("src/test/resources/test_metadata_local/data/population1_002.md.json");
		File ref_rawdata_jfile3 = new File("src/test/resources/test_metadata_local/data/population1_003.md.json");
		
		String ref_rawdata_file1 = ref_rawdata_jfile1.getAbsolutePath();
		String ref_rawdata_file2 = ref_rawdata_jfile2.getAbsolutePath();
		String ref_rawdata_file3 = ref_rawdata_jfile3.getAbsolutePath();
		
	    SxDataset container = new SxDataset();
	    container.set_uuid("fake_uuid");
	    container.set_name("data");
	    container.set_data(new SxMetadata(ref_rawdata_file1, "fake_uuid"));
	    container.set_data(new SxMetadata(ref_rawdata_file2, "fake_uuid"));
	    container.set_data(new SxMetadata(ref_rawdata_file3, "fake_uuid"));
	    return container;
	}

	public static SxExperiment create_experiment() throws Exception
	{
		
		File ref_rawdataset_jfile = new File("src/test/resources/test_metadata_local/data/rawdataset.md.json");
		File ref_processeddataset1_jfile = new File("src/test/resources/test_metadata_local/process1/processeddataset.md.json");
		File ref_processeddataset2_jfile = new File("src/test/resources/test_metadata_local/process2/processeddataset.md.json");
		
		
		String ref_rawdataset_file = ref_rawdataset_jfile.getAbsolutePath();
		String ref_processeddataset1_file = ref_processeddataset1_jfile.getAbsolutePath();
		String ref_processeddataset2_file = ref_processeddataset2_jfile.getAbsolutePath();
	    SxExperiment container = new SxExperiment();
	    container.set_uuid("fake_uuid");
	    container.set_name("myexperiment");
	    container.set_author("Sylvain Prigent");
	    container.set_date(new SxDate("2020-03-04"));
	    container.set_raw_dataset( new SxDatasetMetadata("data", ref_rawdataset_file, "fake_uuid"));
	    container.set_processed_dataset(new SxDatasetMetadata("process1", ref_processeddataset1_file, "fake_uuid"));
	    container.set_processed_dataset(new SxDatasetMetadata("process2", ref_processeddataset2_file, "fake_uuid"));
	    container.set_tag_key("Population");
	    container.set_tag_key("number");
	    return container;
	}
}
