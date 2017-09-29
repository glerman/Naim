package parse;

import java.util.List;

public class SalariesParser {

  public List<String> readAndParse(final String path) {

    List<String> lines = FileReader.read(path);
    return lines;
  }
}
