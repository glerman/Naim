package util;

import file.FileReader;
import parse.CsvParser;
import parse.CsvResult;

import java.util.List;

public class InputReaderHelper {

  public final static String salariesFilePath = InputReaderHelper.class.getResource("/salaries.csv").getFile();
  public final static String teachersFilePath = InputReaderHelper.class.getResource("/teachers.csv").getFile();

  public static CsvResult readCsv(final String filepath) {
    FileReader fileReader = new FileReader();
    CsvParser csvParser = new CsvParser();
    List<String> lines = fileReader.read(filepath, "UTF-8");
    return csvParser.parse(lines);
  }

  public static List<String> readLines(final String filepath) {
    FileReader fileReader = new FileReader();
    return fileReader.read(filepath, "UTF-8");
  }
}
