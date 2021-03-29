package scixtracerj;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class SxRequestLocal extends SxRequest{


	public SxRequestLocal()
	{

	}

	public SxExperiment create_experiment(String name, String author, SxDate date, List<String> tag_keys, String destination)
	{
		
	}

	public SxExperiment get_experiment(String uri)
	{
		
	}

	public void update_experiment(SxExperiment experiment)
	{
		
	}

	public SxRawData import_data(SxExperiment experiment, String data_path, String name, String author, SxFormat format, SxDate date, SxTags tags, boolean copy)
	{
		
	}

	public SxRawData get_rawdata(String  uri)
	{
		
	}

	public void update_rawdata(SxRawData rawdata)
	{
		
	}

	public SxProcessedData get_processeddata(String uri)
	{
		
	}

	public void update_processeddata(SxProcessedData processeddata)
	{
		
	}

	public SxDataset get_dataset_from_uri(String uri)
	{
		
	}

	public void update_dataset(SxDataset dataset)
	{
		
	}

	public SxDataset create_dataset(SxExperiment experiment, String dataset_name)
	{
		
	}

	public SxRun create_run(SxDataset dataset, SxRun run_info)
	{
		
	}

	public SxRun get_run(String uri)
	{
		
	}

	public SxProcessedData create_data(SxDataset dataset, SxRun run, SxProcessedData processed_data)
	{
		
	}


	/**
	 * Write a run metadata in the database
	 * @param run Metadata container
	 */
	public void _write_run(SxRun run)
	{

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
		FileWriter file = new FileWriter(filename); 
		file.write(metadata.toJSONString());
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

		//qDebug() << "common part:" << common_part;

		int last_separator = common_part.lastIndexOf(separator);
		String short_reference_file = reference_file_.substring(reference_file_.length() - (last_separator+1), reference_file_.length()-1);
		//qDebug() << "short_reference_file:" << short_reference_file;

		int number_of_sub_folder = ( short_reference_file.split(separator, -1).length ) - 1;
		//qDebug() << "number_of_sub_folder:" << number_of_sub_folder;
		String short_file = file.substring(file.length() - (last_separator + 1), file.length());
		//qDebug() << "short_file:" << short_file;
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

		ArrayList<String> keep_folders = (ArrayList<String>) Arrays.asList(path.split(File.separator));

		boolean found = true;
		ArrayList<String> folders = keep_folders;
		while (found)
		{
			int pos = -1;
			folders = keep_folders;
			for (int i=0 ; i < folders.size() ; ++i)
			{
				if (folders.get(i) == "..")
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
}
