package parse;

import java.util.List;

public class SalariesParser {

  private FileReader fileReader;

  public SalariesParser() {
    fileReader = new FileReader();
  }

  public List<String> readAndParse(final String path) {

    List<String> lines = fileReader.read(path);
    return lines;
  }
}
