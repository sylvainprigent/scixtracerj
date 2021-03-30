package scixtracerj;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;

public class TestSxRequest {
	
	SxRequestLocal req;
	String tst_experiment_dir; 
	String tst_experiment_file;
	String ref_experiment_file;
	String test_images_data_file1;
	String test_images_data_dir;
	String ref_rawdata_file;
	String tst_rawdata_file;
	String ref_processeddata_file;
	String tst_processeddata_file;
	String ref_dataset_file;
	String tst_dataset_file;
	String ref_processed2data2_file;
	String ref_run_file;
	
	protected boolean deleteDirectory(File directoryToBeDeleted) {
	    File[] allContents = directoryToBeDeleted.listFiles();
	    if (allContents != null) {
	        for (File file : allContents) {
	            deleteDirectory(file);
	        }
	    }
	    return directoryToBeDeleted.delete();
	}
	
	@Before
	public void setUp()
	{
		this.req = new SxRequestLocal();
		this.tst_experiment_dir = "src/test/resources/test_metadata_local/";
		this.ref_experiment_file = "src/test/resources/test_metadata_local/experiment.md.json";
		this.tst_experiment_file = "src/test/resources/test_metadata_local/experiment_tst.md.json";
		this.test_images_data_file1 = "src/test/resources/test_images/data/population1_001.tif";
		this.test_images_data_dir = "src/test/resources/test_images/data/";
		this.ref_rawdata_file = "src/test/resources/test_metadata_local/data/population1_001.md.json";
		this.tst_rawdata_file = "src/test/resources/test_metadata_local/data/population1_001_tst.md.json";
		this.ref_processeddata_file = "src/test/resources/test_metadata_local/process1/population1_001_o.md.json";
		this.tst_processeddata_file = "src/test/resources/test_metadata_local/process1/population1_001_o_tst.md.json";
		this.ref_dataset_file = "src/test/resources/test_metadata_local/data/rawdataset.md.json";
		this.tst_dataset_file = "src/test/resources/test_metadata_local/data/rawdataset_tst.md.json";
		this.ref_processed2data2_file = "src/test/resources/test_metadata_local/process2/population1_002_o_o.md.json";
		this.ref_run_file = "src/test/resources/test_metadata_local/process1/run.md.json";
	}
	
	@After
	public void tearDown()
	{
		File experiment_dir = new File(this.tst_experiment_dir + File.separator + "myexperiment");
		if (experiment_dir.exists()) {
			this.deleteDirectory(experiment_dir);
		}
		
		File tst_experiment_jfile = new File(this.tst_experiment_file);
		if (tst_experiment_jfile.exists()) {
			tst_experiment_jfile.delete();
		}
		
	}
	
	@Test
	public void test_create_experiment() throws Exception 
	{
		
		System.out.println("create experiment to :" + this.tst_experiment_dir );
		this.req.create_experiment("myexperiment", "sprigent", new SxDate("now"), Arrays.asList("key1", "key2"), this.tst_experiment_dir);

		File exp_dir = new File(this.tst_experiment_dir + File.separator + "myexperiment");

		boolean t1 = false;
		if (exp_dir.exists() && exp_dir.isDirectory())
		{
			t1 = true;
		}

		File exp_md = new File(this.tst_experiment_dir + File.separator + "myexperiment" + File.separator + "experiment.md.json");
		boolean t2 = false;
		if (exp_md.exists() && exp_md.isFile())
		{
			t2 = true;
		}

		File data_dir = new File(this.tst_experiment_dir + File.separator + "myexperiment" + File.separator + "data");
		boolean t3 = false;
		if (data_dir.exists() && data_dir.isDirectory())
		{
			t3 = true;
		}

		File data_md = new File(this.tst_experiment_dir + File.separator + "myexperiment" + File.separator + "data" + File.separator + "rawdataset.md.json");
		boolean t4 = false;
		if (data_md.exists() && data_md.isFile())
		{
			t4 = true;
		}

		System.out.println("t1 = " + t1);
		System.out.println("t2 = " + t2);
		System.out.println("t3 = " + t3);
		System.out.println("t4 = " + t4);

		assertTrue(t1 && t2 && t3 && t4);
	}

	
	@Test
	public void test_get_experiment() throws Exception
	{
	    SxExperiment experiment = this.req.get_experiment(this.ref_experiment_file);
	    SxExperiment ref_experiment = CreateMetadata.create_experiment();

	    boolean value = SxCompare.compare_metadata(experiment, ref_experiment);
	    System.out.println("get experment test:" + value);
	    assertTrue(value);
	}

	@Test
	public void test_update_experiment() throws Exception
	{
	    SxExperiment experiment = CreateMetadata.create_experiment();
	    experiment.set_md_uri(this.tst_experiment_file);
	    // print(serialize_experiment(experiment))
	    this.req.update_experiment(experiment);

	    boolean value = SxCompare.compare_txt_files(tst_experiment_file, ref_experiment_file);
	    System.out.println("update experiement test:" + value);
	    assertTrue(value);
	}

	/*
	@Test
	public void test_import_data() throws Exception
	{
	    SxExperiment experiment = this.req.create_experiment("myexperiment", "sprigent", new SxDate("now"), Arrays.asList("key1", "key2"), this.tst_experiment_dir);

	    String data_path = this.test_images_data_file1;
	    String name = "population1_001.tif";
	    String author = "sprigent";
	    SxFormat format_ = new SxFormat("tif");
	    SxTags tags = new SxTags();
	    tags.set_tag("Population", "population1");
	    tags.set_tag("ID", "001");
	    SxRawData rawData = this.req.import_data(experiment, data_path, name, author, format_, new SxDate("now"), tags, true);

	    // test generated files

	    File md_qfile = new File(this.tst_experiment_dir + File.separator + "myexperiment" + File.separator + "data" + File.separator + "population1_001.md.json");
	    boolean t1 = false;
	    if (md_qfile.exists())
	    {
	        t1 = true;
	    }

	    boolean t2 = false;
	    if (rawData.get_name() == "population1_001.tif")
	    {
	        t2 = true;
	    }

	    File data_qfile = new File(this.tst_experiment_dir + File.separator + "myexperiment" + File.separator + "data" + File.separator + "population1_001.tif");
	    boolean t3 = false;
	    if (data_qfile.exists())
	    {
	        t3 = true;
	    }

	    boolean t4 = false;
	     if (rawData.get_tags().get_tag("Population").equals("population1"))
	     {
	         t4 = true;
	     }
	     boolean t5 = false;
	     if (experiment.get_tags_keys().contains("Population")){
	         t5 = true;
	     }

	     System.out.println("test import data:");
	     System.out.println("t1:" + t1);
	     System.out.println("t2:" + t2);
	     System.out.println("t3:" + t3);
	     System.out.println("t4:" + t4);
	     System.out.println("t5:" + t5);

	     assertTrue( t1 && t2 && t3 && t4 && t5);
	}

	@Test
	public void test_import_dir() throws Exception
	{
	    SxExperiment experiment = this.req.create_experiment("myexperiment", "sprigent", new SxDate("now"), Arrays.asList("key1", "key2"), this.tst_experiment_dir);

	    this.req.import_dir(experiment, this.test_images_data_dir, "\\.tif$", "sprigent", new SxFormat("tif"), new SxDate("now"), true);

	    String data_dir = this.tst_experiment_dir + File.separator + "myexperiment" + File.separator + "data";
	    File data_qdir = new File(data_dir);
	    int data_files_count = data_qdir.listFiles().length;
	    System.out.println("data_files_count=" + data_files_count);
	    // count the number of imported files
	    boolean t1 = false;
	    if (data_files_count == 83)
	    {
	        t1 = true;
	    }
	    // count the number of lines in the rawdataset.md.json file
	    boolean t2 = false;
	    
	    Path path = Paths.get(data_dir+File.separator+"rawdataset.md.json");
	    int number_of_lines = (int) Files.lines(path).count();
	    
	    if (number_of_lines == 166)
	    {
	        t2 = true;
	    }

	    System.out.println("test import dir:");
	    System.out.println("t1:" + t1);
	    System.out.println("t2:" + t2);
	    assertTrue(t1 && t2);
	}

	@Test
	public void test_tag_from_name() throws Exception
	{
	    SxExperiment experiment = this.req.create_experiment("myexperiment", "sprigent", new SxDate("now"), Arrays.asList("key1", "key2"), tst_experiment_dir);

	    this.req.import_dir(experiment, test_images_data_dir, "\\.tif$", "sprigent", new SxFormat("tif"), new SxDate("now"), true);

	    this.req.tag_from_name(experiment, "Population", Arrays.asList("population1", "population2"));
	    // test if tag Population in the experiment metadata
	    boolean t1 = false;
	    if (experiment.get_tags_keys().contains("Population"))
	    {
	        t1 = true;
	    }
	    // test few images tags
	    String data_dir = this.tst_experiment_dir + File.separator + "myexperiment" + File.separator + "data" + File.separator;
	    SxRawData data1 = this.req.get_rawdata(data_dir + File.separator + "population1_012.md.json");

	    boolean t2 = false;
	    if ( data1.get_tags().get_keys().contains("Population") && data1.get_tags().get_tag("Population") == "population1")
	    {
	        t2 = true;
	    }

	    SxRawData data2 = this.req.get_rawdata(data_dir + File.separator + "population2_011.md.json");
	    boolean t3 = false;
	    if (data2.get_tags().get_keys().contains("Population") && data2.get_tags().get_tag("Population") == "population2")
	    {
	        t3 = true;
	    }

	    System.out.println("test tag from name:");
	    System.out.println("t1:" + t1);
	    System.out.println("t2:" + t2);
	    System.out.println("t3:" + t3);
	    assertTrue(t1 && t2 && t3);
	}

	@Test
	public void test_tag_using_separator() throws Exception
	{
	    SxExperiment experiment = this.req.create_experiment("myexperiment", "sprigent", new SxDate("now"), Arrays.asList("key1", "key2"), tst_experiment_dir);

	    this.req.import_dir(experiment, test_images_data_dir, "\\.tif$", "sprigent", new SxFormat("tif"), new SxDate("now"), true);

	     this.req.tag_using_separator(experiment, "ID", "_", 1);
	     // test if tag ID in the experiment metadata
	     boolean t1 = false;
	     if (experiment.get_tags_keys().contains("ID"))
	     {
	         t1 = true;
	     }
	     // test few images tags
	     String data_dir = this.tst_experiment_dir + File.separator + "myexperiment" + File.separator + "data" + File.separator;
	     SxRawData data1 = this.req.get_rawdata(data_dir + File.separator + "population1_012.md.json");

	     boolean t2 = false;
	     if (data1.get_tags().get_keys().contains("ID") && data1.get_tags().get_tag("ID")== "012")
	     {
	         t2 = true;
	     }

	     SxRawData data2 = this.req.get_rawdata(data_dir + File.separator + "population2_011.md.json");
	     boolean t3 = false;
	     if (data2.get_tags().get_keys().contains("ID") && data2.get_tags().get_tag("ID")== "011")
	     {
	         t3 = true;
	     }

	     System.out.println("test_tag_using_separator:");
	     System.out.println("t1:" + t1);
	     System.out.println("t2:" + t2);
	     System.out.println("t3:" + t3);
	     assertTrue(t1 && t2 && t3);
	}

	@Test
	public void test_get_rawdata() throws Exception
	{
	    SxRawData raw_data_read = this.req.get_rawdata(this.ref_rawdata_file);
	    SxRawData raw_data_ref = CreateMetadata.create_raw_data();
	    boolean value = SxCompare.compare_metadata(raw_data_read, raw_data_ref);
	    System.out.println("test_get_rawdata cmp:" + value);
	    assertTrue(value);
	}

	@Test
	public void test_update_rawdata() throws Exception
	{
	    SxRawData raw_data = CreateMetadata.create_raw_data();
	    raw_data.set_md_uri(this.tst_rawdata_file);
	    this.req.update_rawdata(raw_data);

	    boolean value = SxCompare.compare_txt_files(this.tst_rawdata_file, ref_rawdata_file);
	    System.out.println("test_get_rawdata cmp:" + value);
	    assertTrue(value);
	}

	@Test
	public void test_get_processeddata() throws Exception
	{
	    SxProcessedData processed_data_read = this.req.get_processeddata(this.ref_processeddata_file);
	    SxProcessedData processed_data_ref = CreateMetadata.create_processed_data();

	    boolean value = SxCompare.compare_metadata(processed_data_read, processed_data_ref);
	    System.out.println("test_get_processeddata cmp:" + value);
	    assertTrue(value);
	}

	@Test
	public void test_update_processeddata() throws Exception
	{
	    SxProcessedData processed_data = CreateMetadata.create_processed_data();
	    processed_data.set_md_uri(this.tst_processeddata_file);
	    this.req.update_processeddata(processed_data);
	    boolean value = SxCompare.compare_txt_files(this.tst_processeddata_file, this.ref_processeddata_file);
	    System.out.println("test_update_processeddata cmp:" + value);
	    assertTrue(value);
	}

	@Test
	public void test_get_dataset_from_uri() throws Exception
	{
	    SxDataset ref_dataset = CreateMetadata.create_dataset();
	    SxDataset read_dataset = this.req.get_dataset_from_uri(this.ref_dataset_file);

	    boolean value = SxCompare.compare_metadata(ref_dataset, read_dataset);
	    System.out.println("test_get_dataset_from_uri cmp:" + value);
	    assertTrue(value);
	}

	@Test
	public void test_update_dataset() throws IOException
	{
	    SxDataset container = CreateMetadata.create_dataset();
	    container.set_md_uri(this.tst_dataset_file);
	    this.req.update_dataset(container);

	    boolean value = SxCompare.compare_txt_files(this.tst_dataset_file, this.ref_dataset_file);
	    System.out.println("test_update_dataset cmp:" + value);
	    assertTrue(value);
	}

	@Test
	public void test_get_rawdataset() throws Exception
	{
	    SxExperiment experiment = this.req.get_experiment(ref_experiment_file);
	    SxDataset read_dataset = this.req.get_rawdataset(experiment);
	    SxDataset ref_dataset = CreateMetadata.create_dataset();

	    boolean value = SxCompare.compare_metadata(ref_dataset, read_dataset);
	    System.out.println("test_get_rawdataset cmp:" + value);
	    assertTrue(value);
	}

	@Test
	public void test_get_parent() throws Exception
	{
	    SxProcessedData processed_data = this.req.get_processeddata(ref_processed2data2_file);
	    SxData parent_data = this.req.get_parent(processed_data);
	    boolean cmp = false;
	    System.out.println("get parent original name " +  processed_data.get_name());
	    System.out.println("get parent name " +  parent_data.get_name());
	    if (parent_data.get_name() == "population1_002_o")
	    {
	        cmp = true;
	    }
	    System.out.println("test_get_parent cmp:" + cmp);
	    assertTrue(cmp);
	}

	@Test
	public void test_get_origin() throws Exception
	{
	    SxProcessedData processed_data = this.req.get_processeddata(this.ref_processed2data2_file);
	    SxRawData origin_data = this.req.get_origin(processed_data);
	    boolean cmp = false;
	    System.out.println("get origin original name " +  processed_data.get_name());
	    System.out.println("get origin name " +  origin_data.get_name());
	    if (origin_data.get_name() == "population1_002.tif")
	    {
	        cmp = true;
	    }
	    System.out.println("test_get_origin cmp:" + cmp);
	    assertTrue(cmp);
	}

	@Test
	public void test_get_dataset_raw() throws Exception
	{
	    SxExperiment experiment = this.req.get_experiment(ref_experiment_file);
	    SxDataset dataset = this.req.get_dataset(experiment, "data");
	    boolean cmp = true;
	    if (dataset.get_name() == "data")
	    {
	        cmp = false;
	    }
	    System.out.println("test_get_dataset_raw cmp:" + cmp);
	    assertTrue(cmp);
	}

	@Test
	public void test_get_dataset_processed() throws Exception
	{
	    SxExperiment experiment = this.req.get_experiment(ref_experiment_file);
	    SxDataset dataset = this.req.get_dataset(experiment, "process1");
	    boolean cmp = false;
	    if (dataset.get_name() == "process1")
	    {
	        cmp = true;
	    }
	    System.out.println("test_get_dataset_processed cmp:" + cmp);
	    assertTrue(cmp);
	}

	@Test
	public void test_get_data() throws Exception
	{
	    SxExperiment experiment = this.req.get_experiment(ref_experiment_file);
	    SxDataset dataset = this.req.get_dataset(experiment, "process1");
	    List<SxData> data = this.req.get_data(dataset, "name=population1_001_o", "o");
	    boolean cmp = false;
	    if (data.get(0).get_name() == "population1_001_o")
	    {
	        cmp = true;
	    }
	    System.out.println("test_get_data cmp:" + cmp);
	    assertTrue(cmp);
	}

	@Test
	public void test_create_dataset() throws Exception
	{
	    SxExperiment experiment = this.req.create_experiment("myexperiment", "sprigent", new SxDate("now"), Arrays.asList("key1", "key2"), this.tst_experiment_dir);
	    this.req.create_dataset(experiment, "myprocess");

	    boolean t1 = false;
	    String dataset_file = this.tst_experiment_dir + File.separator + "myexperiment" + File.separator + "myprocess"+ File.separator + "processeddataset.md.json";
	    File dataset_jfile = new File(dataset_file);
	    if (dataset_jfile.exists())
	    {
	        t1 = true;
	    }

	    boolean t2 = false;
	    SxDataset dataset = this.req.get_dataset(experiment, "myprocess");
	    if (dataset.get_name() == "myprocess")
	    {
	        t2 = true;
	    }

	    System.out.println("test_create_dataset:");
	    System.out.println("t1:" + t1);
	    System.out.println("t2:" + t2);
	    assertTrue(t1 && t2);
	}

	@Test
	public void test_create_run() throws Exception
	{
	    SxExperiment experiment = this.req.create_experiment("myexperiment", "sprigent", new SxDate("now"), Arrays.asList("key1", "key2"), this.tst_experiment_dir);

	    SxDataset dataset = this.req.create_dataset(experiment, "threshold");
	    SxRun run_info = new SxRun();
	    run_info.set_process("threshold", "uniqueIdOfMyAlgorithm");
	    run_info.add_input("image", "data","Population=Population1");
	    run_info.add_parameter("threshold", "100");
	    this.req.create_run(dataset, run_info);

	    boolean t1 = false;
	    String run_file =this.tst_experiment_dir + File.separator + "myexperiment" + File.separator + "threshold" + File.separator + "run.md.json";
	    
	    File run_jfile = new File(run_file);
	    if (run_jfile.exists()){
	        t1 = true;
	    }

	    boolean t2 = false;
	    if (run_info.get_process_name() == "threshold")
	    {
	        t2 = true;
	    }

	    System.out.println("test_create_run:");
	    System.out.println("t1:" + t1);
	    System.out.println("t2:" + t2);
	    assertTrue(t1 && t2);
	}

	@Test
	public void test_get_run() throws Exception
	{
	    SxRun run = this.req.get_run(this.ref_run_file);
	    boolean t1 = false;
	    if (run.get_process_name() =="SPARTION 2D")
	    {
	        t1 = true;
	    }
	    boolean t2 = false;
	    if (run.get_processed_dataset().get_uuid() == "fake_uuid")
	    {
	        t2 = true;
	    }
	    System.out.println("test_get_run:");
	    System.out.println("t1:" + t1);
	    System.out.println("t2:" + t2);
	    assertTrue(t1 && t2);
	}

	@Test
	public void test_create_data() throws Exception
	{
	    SxExperiment experiment = this.req.create_experiment("myexperiment", "sprigent", new SxDate("now"), Arrays.asList("key1", "key2"), tst_experiment_dir);
	    this.req.import_dir(experiment, test_images_data_dir, "\\.tif$", "sprigent", new SxFormat("tif"), new SxDate("now"), true);

	    SxDataset raw_dataset = this.req.get_dataset(experiment, "data");

	    List<SxData> raw_data_list = this.req.get_data(raw_dataset, "name=population1_001", "");
	    SxData raw_data = raw_data_list.get(0);

	    SxDataset dataset = this.req.create_dataset(experiment, "threshold");
	    SxRun run_info = new SxRun();
	    run_info.set_process("threshold", "uniqueIdOfMyAlgorithm");
	    run_info.add_input("image", "data", "Population=Population1");
	    run_info.add_parameter("threshold", "100");
	    this.req.create_run(dataset, run_info);
	    SxProcessedData processed_data = new SxProcessedData();
	    String output_image_path = this.tst_experiment_dir + File.separator + "myexperiment" + File.separator + "threshold" + File.separator + "o_" + raw_data.get_name() +".tif";

	    processed_data.set_info("myimage","sprigent", new SxDate("now"), new SxFormat("tif"), output_image_path);
	    processed_data.add_input("i", raw_data, raw_data.get_type());
	    processed_data.set_output("o", "threshold");

	    this.req.create_data(dataset, run_info, processed_data);

	    boolean t1 = false;
	    File process_jfile = new File(processed_data.get_md_uri());
	    if (process_jfile.exists())
	    {
	        t1 = true;
	    }
	    boolean t2 = false;
	    if (processed_data.get_name() == "myimage")
	    {
	        t2 = true;
	    }
	    boolean t3 = false;
	    if (processed_data.get_uri().contains("o_population1_001.tif.tif"))
	    {
	        t3 = true;
	    }
	    boolean t4 = false;
	    if (processed_data.get_run().get_md_uri().contains("run.md.json"))
	    {
	        t4 = true;
	    }

	    System.out.println("test_create_data:");
	    System.out.println("t1:" + t1);
	    System.out.println("t2:" + t2);
	    System.out.println("t3:" + t3);
	    System.out.println("t4:" + t4);
	    assertTrue(t1 && t2 && t3 && t4);
	}
	
	*/

}
