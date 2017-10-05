package util;

import file.FileReader;
import parse.CsvParser;
import parse.CsvResult;

import java.util.List;
import java.util.Optional;

public class InputReaderHelper {

  public final static String salariesFilePath = InputReaderHelper.class.getResource("/salaries.csv").getFile();
  public final static String teachersFilePath = InputReaderHelper.class.getResource("/teachers.csv").getFile();

  public static CsvResult readCsv(final String filepath) {
    CsvParser csvParser = new CsvParser();
    return csvParser.parse(readFile(filepath));
  }

  public static List<String> readLines(final String filepath) {
    return readFile(filepath);
  }

  private static List<String> readFile(String filepath) {
    FileReader fileReader = new FileReader();
    Optional<List<String>> lines = fileReader.read(filepath, "UTF-8");
    if (!lines.isPresent()) {
      throw new RuntimeException("Failed to read file: " + filepath);
    }
    return lines.get();
  }
}
