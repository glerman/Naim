package file;

import com.google.common.io.Files;
import report.ReportAggregator;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;

public class FileReader {

  /**
   * @return the file as a list of lines
   */
  public Optional<List<String>> read(final String path, final String charset) {

    File file = new File(path);
    if (!file.exists()) {
      ReportAggregator.instance.inputFileError("File not found: " + path, null);
      return Optional.empty();
    }

    try {
      List<String> lines = Files.readLines(file, Charset.forName(charset));
      return Optional.of(lines);
    } catch (IOException e) {
      ReportAggregator.instance.inputFileError("Failed reading file: " + path, e);
      return Optional.empty();
    }
  }
}
