package parse;

public class FilePathProvider {

  public final static String salariesFilePath = FilePathProvider.class.getResource("/salaries.csv").getFile();
  public final static String teachersFilePath = FilePathProvider.class.getResource("/teachers.csv").getFile();
}
