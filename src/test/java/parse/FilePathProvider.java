package parse;

public class FilePathProvider {

  final static String salariesFilePath = FilePathProvider.class.getResource("/salaries.csv").getFile();
  final static String teachersFilePath = FilePathProvider.class.getResource("/teachers.csv").getFile();
}
