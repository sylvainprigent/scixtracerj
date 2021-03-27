package scixtracerj;

import java.util.ArrayList;
import java.util.List;

public class SxSearch {

	/**
	 * Search if the query is on the search_list
	 * @param search_list Data search list (list of SearchContainer)
	 * @param query  String query with the key=value format. No 'AND', 'OR'...
	 * @return list of selected SearchContainer
	 * @throws Exception 
	 */
    public static List<SxSearchContainer> query_list_single(List<SxSearchContainer> search_list, String query) throws Exception
    {
        List<SxSearchContainer> selected_list = new ArrayList<SxSearchContainer>();

        if (query.contains("name"))
        {
            String[] split_query = query.split("=");
            if (split_query.length != 2){
                throw new Exception("Error: the query " + query + "is not correct. Must be (name=value)");
            }
            String value = split_query[1];
            for (int i = 0 ; i < search_list.size() ; ++i)
            {
                if (search_list.get(i).get_name().contains(value))
                {
                    selected_list.add(search_list.get(i));
                }
            }
            return selected_list;
        }

        if (query.contains("<="))
        {
        	String[] split_query = query.split("<=");
            if (split_query.length != 2){
                throw new Exception("Error: the query " + query + "is not correct. Must be (key<=value)");
            }
            String key = split_query[0];
            float value = Float.parseFloat(split_query[1].replace(" ", ""));
            for (int i = 0 ; i < search_list.size() ; ++i)
            {
                if (search_list.get(i).is_tag(key) && Float.parseFloat(search_list.get(i).get_tag(key).replace(" ", "")) <= value)
                {
                    selected_list.add(search_list.get(i));
                }
            }
        }

        else if (query.contains(">="))
        {
        	String[] split_query = query.split(">=");
            if (split_query.length != 2){
                throw new Exception("Error: the query " + query + "is not correct. Must be (key>=value)");
            }
            String key = split_query[0];
            String value = split_query[1];
            for (int i = 0 ; i < search_list.size() ; ++i)
            {
                if (search_list.get(i).is_tag(key) && Float.parseFloat(search_list.get(i).get_tag(key)) >= Float.parseFloat(value)){
                    selected_list.add(search_list.get(i));
                }
            }
        }

        else if (query.contains("="))
        {
        	String[] split_query = query.split("=");
            if (split_query.length != 2){
                throw new Exception("Error: the query " + query + "is not correct. Must be (key=value)");
            }
            String key = split_query[0];
            String value = split_query[1];
            for (int i = 0 ; i < search_list.size() ; ++i)
            {
                if (search_list.get(i).is_tag(key) && search_list.get(i).get_tag(key) == value){
                    selected_list.add(search_list.get(i));
                }
            }
        }

        else if (query.contains("<"))
        {
        	String[] split_query = query.split("<");
            if (split_query.length != 2){
                throw new Exception("Error: the query " + query + "is not correct. Must be (key<value)");
            }
            String key = split_query[0];
            String value = split_query[1];
            for (int i = 0 ; i < search_list.size() ; ++i)
            {
                if (search_list.get(i).is_tag(key) && Float.parseFloat(search_list.get(i).get_tag(key)) < Float.parseFloat(value)){
                    selected_list.add(search_list.get(i));
                }
            }
        }

        else if (query.contains(">"))
        {
        	String[] split_query = query.split(">");
            if (split_query.length != 2){
                throw new Exception("Error: the query " + query + "is not correct. Must be (key>value)");
            }
            String key = split_query[0];
            String value = split_query[1];
            for (int i = 0 ; i < search_list.size() ; ++i)
            {
                if (search_list.get(i).is_tag(key) && Float.parseFloat(search_list.get(i).get_tag(key)) > Float.parseFloat(value)){
                    selected_list.add(search_list.get(i));
                }
            }
        }

         return selected_list;
    }
    
}
