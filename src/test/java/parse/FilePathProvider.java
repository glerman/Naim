package parse;

public class FilePathProvider {

  public final static String salariesFilePath = FilePathProvider.class.getResource("/salaries.csv").getFile();
  public final static String teachersFilePath = FilePathProvider.class.getResource("/teachers.csv").getFile();
  public final static String salariesFilePath_salame = FilePathProvider.class.getResource("/salaries_09_2017.csv").getFile();
  public final static String teachersFilePath_salame = FilePathProvider.class.getResource("/teachers_09_2017.csv").getFile();
  public final static String salariesFilePath_maze = FilePathProvider.class.getResource("/salaries_09_2017_maze.csv").getFile();
}
