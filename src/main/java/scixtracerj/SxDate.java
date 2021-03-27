package scixtracerj;

import java.util.Calendar;

public class SxDate {

    int m_year; ///< date year
    int m_month; ///< date month
    int m_day; ///< date day
    
    
    public SxDate(String date) throws Exception
    {
        this.set_from_string(date);
    }
    
    /**
     * Get the the date as a string
     * @param format Format of the date. Possible values are 'YYYY-MM-DD' or 'DD/MM/YYYY'
     * @return Date representation as a string
     * @throws Exception
     */
    public String get_to_string(String format) throws Exception
    {
        if (format == "YYYY-MM-DD"){
            return this.number_to_str(m_year, 4) + "-" + this.number_to_str(m_month, 2) + "-" + this.number_to_str(m_day, 2);
        }
        else if (format == "DD/MM/YYYY"){
            return this.number_to_str(m_day, 2) + "/" + this.number_to_str(m_month, 2) + "/" + this.number_to_str(m_year, 4);
        }
        else{
            throw new Exception("Date format not correct");
        }
    }
    
    /**
     * Get the the date year
     * @return Year value. Example 2021
     */
    public int get_year()
    {
        return m_year;
    }

    /**
     * Get the the date month
     * @return Month value. Example 9
     */
    public int get_month()
    {
        return m_month;
    }

    /**
     * Get the the date day
     * @return Day value. Ex 2
     */
    public int get_day()
    {
        return m_day;
    }

    /**
     * Set the date year
     * @param value Year of the date
     */
    public void set_year(int value)
    {
        m_year = value;
    }

    /**
     * Set the date month
     * @param value Month of the date
     */
    public void set_month(int value)
    {
        m_month = value;
    }

    /**
     * Set the date day
     * @param value Day of the date
     */
    public void set_day(int value)
    {
        m_day = value;
    }

    /**
     * Set the date by parsing a string. Possible formats are 'YYYY-MM-DD' or 'DD/MM/YYYY'
     * @param value Date as a string
     * @throws Exception
     */
    private void set_from_string(String value) throws Exception
    {
        if (value == "now")
        {
        	Calendar calendar = Calendar.getInstance();
            m_day = calendar.get(Calendar.DAY_OF_MONTH);
            m_month = calendar.get(Calendar.MONTH)+1;
            m_year = calendar.get(Calendar.YEAR);
            return;
        }

        String v1 = value;
        String[] date_fr = v1.split("/");
        if (date_fr.length == 3){
            m_day = Integer.parseInt(date_fr[0]);
            m_month = Integer.parseInt(date_fr[1]);
            m_year = Integer.parseInt(date_fr[2]);
            return;
        }

        String v2 = value;
        String[] date_en = v2.split("-");
        if (date_en.length == 3){

            m_day = Integer.parseInt(date_en[2]);
            m_month = Integer.parseInt(date_en[1]);
            m_year = Integer.parseInt(date_en[0]);
            return;
        }
        throw new Exception("Cannot set the date from string. Format not correct");
    }

    /**
     * Convert a number to string with a fixed number of digit
     * @param number Number to convert
     * @param len Number of digits
     * @return Formated number to string
     */
    private String number_to_str(int number, int len)
    {
    	String str = Integer.toString(number);
    	while (str.length() < len)
    	{
    	    str = "0" + str;
    	}
    	return str;
    }
}
