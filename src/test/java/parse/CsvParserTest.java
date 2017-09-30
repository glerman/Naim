package parse;

import dnl.utils.text.table.TextTable;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;


public class CsvParserTest {

  @Test
  public void test() throws Exception {
    FileReader fileReader = new FileReader();
    List<String> fileLines = fileReader.read(FilePathProvider.salariesFilePath);
    CsvParser csvParser = new CsvParser();
    CsvResult result = csvParser.parse(fileLines);

    Assert.assertEquals(12, result.header.length);
    Assert.assertEquals(120, result.data.length);

    for (int i = 0; i < result.data.length; i++) {
      Assert.assertNotNull("Data row is null at row #" + i, result.data[i]);
      Assert.assertEquals("Data row length is unexpected at row #" + i + "\n" +result.data[i], 12, result.data[i].length);
      for (int j = 0 ; j < result.header.length; j++ ) {
        Assert.assertNotNull("Data element (" + i + "," + j + ") is null", result.data[i][j]);
        Assert.assertFalse("Data element (" + i + "," + j + ") is empty", ((String)result.data[i][j]).isEmpty());
      }
    }

    TextTable textTable = new TextTable(result.header, result.data);
    textTable.printTable();
  }
}