package scixtracerj;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

public class TestSxRequestLocal {

	@Test
	public void test_simplify_path1() 
	{	
		String sep = File.separator;		
	    String file = sep+"my"+sep+"computer"+sep+"experiment"+sep+"svdeconv"+sep+".."+sep+"data"+sep+"raw.md.json";
	    String simplified_file = SxRequestLocal.simplify_path(file);
	    String ref = sep+"my"+sep+"computer"+sep+"experiment"+sep+"data"+sep+"raw.md.json";
	    
	    /*
	    System.out.println("file:" + file);
	    System.out.println("res:" + simplified_file);
	    System.out.println("exp:" + ref);
	    */

	    assertEquals(simplified_file, ref);

	}
	
	@Test
	public void test_simplify_path2()
	{
		String sep = File.separator;
	    String file = sep + "my" + sep + "computer" + sep + "experiment" + sep
	            + "svdeconv" + sep + "denoise" + sep + ".." + sep + ".." + sep
	            + "data" + sep + "raw.md.json";
	    String simplified_file = SxRequestLocal.simplify_path(file);

	    String ref = sep + "my" + sep + "computer" + sep + "experiment" + sep + "data" + sep + "raw.md.json";
	    
	    /*
	    System.out.println("file:" + file);
	    System.out.println("res:" + simplified_file);
	    System.out.println("exp:" + ref);
	    */
	   
	    assertEquals(simplified_file, ref);
	}
	
	@Test
	public void test_relative_path1()
	{
		String sep = File.separator;
	    String reference_file = "my" + sep + "computer" + sep + "experiment" + sep + "data" + sep + "rawdata.md.json";
	    String file = "my" + sep + "computer" + sep + "experiment" + sep + "data" + sep + "rawdata.tif";
	    String relative_file = SxRequestLocal.relative_path(file, reference_file);

	    /*
	    System.out.println("reference_file:" + reference_file);
	    System.out.println("file:" + file);
	    System.out.println("relative_file:" + relative_file);
	    System.out.println("exp:" + "rawdata.tif");
	    */
	    
	    assertEquals(relative_file, "rawdata.tif");
	}
	
	@Test
	public void test_relative_path2()
	{
		String sep = File.separator;
	    String reference_file = "my" + sep + "computer" + sep + "experiment" + sep + "svdeconv" + sep + "processeddata.md.json";
	    String file = "my" + sep + "computer" + sep + "experiment" + sep + "data" + sep + "raw.md.json";
	    String relative_file = SxRequestLocal.relative_path(file, reference_file);

	    /*
	    System.out.println("reference_file:" + reference_file);
	    System.out.println("file:" + file);
	    System.out.println("relative_file:" + relative_file);
	    System.out.println("exp:" + "rawdata.tif");
	    */
	    
	    assertEquals(relative_file, ".." + sep + "data" + sep + "raw.md.json");
	}
	
	@Test
	public void test_absolute_path()
	{
		String sep = File.separator;
	    String reference_file = "my" + sep + "computer" + sep + "experiment" + sep + "data" + sep + "rawdata.md.json";
	    String file = "rawdata.tif";
	    String abs_file = SxRequestLocal.absolute_path(file, reference_file);
	    String exp = "my" + sep + "computer" + sep + "experiment" + sep + "data" + sep + "rawdata.tif";

	    //qDebug() << "reference_file:" << reference_file;
	    //qDebug() << "file:" << file;
	    //qDebug() << "abs_file:" << abs_file;
	    //qDebug() << "exp:" << exp;

	    assertEquals(abs_file, exp);
	}

}
