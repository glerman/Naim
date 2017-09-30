package parse;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

import java.util.List;

public class CsvParser {

  public CsvResult parse(final List<String> lines) {

    String[] header = Iterables.toArray(Splitter.on(',').split(lines.get(0)), String.class);
    List<String> dataLines = lines.subList(1, lines.size());
    Object[][] data = new Object[lines.size() - 1][header.length];

    for (int i = 0; i < dataLines.size(); i++) {
      data[i] = Iterables.toArray(Splitter.on(',').split(dataLines.get(i)), String.class);
    }
    return new CsvResult(header, data);
  }
}
