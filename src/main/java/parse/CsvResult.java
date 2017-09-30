package parse;

import com.google.common.base.MoreObjects;

public class CsvResult {

  public final String[] header;
  public final Object[][] data;

  public CsvResult(String[] header, Object[][] data) {
    this.header = header;
    this.data = data;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
            .add("header", header)
            .add("data", data)
            .toString();
  }
}
