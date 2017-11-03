package file;

import com.google.common.io.Files;
import org.apache.commons.lang.StringUtils;
import report.ReportAggregator;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FileReader {

  /**
   * @return the file as a list of lines
   */
  public Optional<List<String>> read(final String path, final String charset) {

    File file = new File(path);
    if (!file.exists()) {
      ReportAggregator.instance.userInputError("File not found: " + path);
      return Optional.empty();
    }

    List<String> lines = read(charset, file);
    if (lines == null) {
      return Optional.empty();
    } else {
      lines = lines.
              stream().
              filter(StringUtils::isNotEmpty).
              map(String::trim).
              collect(Collectors.toList());
      return Optional.of(lines);
    }
  }

  private List<String> read(String charset, File file) {
    try {
      List<String> lines = Files.readLines(file, Charset.forName(charset));
      if (lines == null) {
        ReportAggregator.instance.ioError("File reading returned null: " + file.getPath(), new RuntimeException());
        return null;
      }
      return lines;
    } catch (IOException e) {
      ReportAggregator.instance.ioError("Failed reading file: " + file.getPath(), e);
      return null;
    }
  }
}
