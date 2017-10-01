package parse;

public class FilePathProvider {

  public final static String salariesFilePath = FilePathProvider.class.getResource("/salaries.csv").getFile();
  public final static String salariesFilePath_real = FilePathProvider.class.getResource("/salaries_09_2017.csv").getFile();
  public final static String teachersFilePath = FilePathProvider.class.getResource("/teachers.csv").getFile();
  public final static String teachersFilePath_real = FilePathProvider.class.getResource("/teachers_09_2017.csv").getFile();
}
