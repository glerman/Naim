package file;

import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

public class FileReader {

  /**
   * @return the file as a list of lines
   */
  public List<String> read(final String path, final String charset) {

    File file = new File(path);
    if (!file.exists()) {
      throw new RuntimeException("File not found: " + path);
    }

    try {
      return Files.readLines(file, Charset.forName(charset));
    } catch (IOException e) {
      throw new RuntimeException("Failed reading file: " + path, e);
    }
  }
}
