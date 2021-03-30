package scixtracerj;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class SxCompare {

	public static boolean compare_txt_files(String file1, String file2)
	{
	    File qfile1 = new File(file1);
	    if (!qfile1.exists())
	        return false;

	    File qfile2 = new File(file2);
	    if (!qfile2.exists())
	        return false;

	    String content1 = readLineByLineJava8(file1); 
	    String content2 = readLineByLineJava8(file2);

	    if (content1.equals(content2)){
	        return true;
	    }
	    return false;
	}

	public static boolean compare_metadata(SxMetadata container1, SxMetadata container2) throws Exception
	{
	    SxSerializeJson serializer = new SxSerializeJson();
	    String c1 = serializer.serialize(container1);
	    String c2 = serializer.serialize(container2);

	    System.out.println("C1 = " + c1);
	    System.out.println("C2 = " + c2);

	    if (c1.equals(c2)){
	        return true;
	    }
	    return false;
	}
	
	private static String readLineByLineJava8(String filePath) 
    {
        final StringBuilder contentBuilder = new StringBuilder();
 
        try (Stream<String> stream = Files.lines( Paths.get(filePath), StandardCharsets.UTF_8)) 
        {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        }
        catch (IOException e) 
        {
            e.printStackTrace();
        }
 
        return contentBuilder.toString();
    }
	
}
