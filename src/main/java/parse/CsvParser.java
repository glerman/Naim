package parse;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

import java.util.List;

public class CsvParser {

  private static final char SEPARATOR = ',';

  public CsvResult parse(final List<String> lines) {

    String[] header = Iterables.toArray(Splitter.on(SEPARATOR).split(lines.get(0)), String.class);
    List<String> dataLines = lines.subList(1, lines.size());
    Object[][] data = new Object[dataLines.size()][header.length];

    for (int i = 0; i < dataLines.size(); i++) {
      data[i] = Iterables.toArray(Splitter.on(SEPARATOR).split(dataLines.get(i)), String.class);
    }
    return new CsvResult(header, data);
  }
}
