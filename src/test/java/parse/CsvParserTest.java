package parse;

import dnl.utils.text.table.TextTable;
import file.FileReader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;


public class CsvParserTest {

  private FileReader fileReader;
  private CsvParser csvParser;

  @Before
  public void setUp() throws Exception {
    fileReader = new FileReader();
    csvParser = new CsvParser();
  }

  @Test
  public void testSalaries() throws Exception {

    List<String> fileLines = fileReader.read(FilePathProvider.salariesFilePath);
    CsvResult result = csvParser.parse(fileLines);

    Assert.assertEquals(12, result.header.length);
    Assert.assertEquals(120, result.data.length);

    doTest(result);
  }

  @Test
  public void testTeachers() throws Exception {

    List<String> fileLines = fileReader.read(FilePathProvider.teachersFilePath);
    CsvResult result = csvParser.parse(fileLines);

    Assert.assertEquals(3, result.header.length);
    Assert.assertEquals(2, result.data.length);

    doTest(result);
  }

  private void doTest(CsvResult result) {
    for (int i = 0; i < result.data.length; i++) {
      Assert.assertNotNull("Data row is null at row #" + i, result.data[i]);
      Assert.assertEquals("Data row length is unexpected at row #" + i + "\n" +result.data[i], result.header.length, result.data[i].length);
      for (int j = 0 ; j < result.header.length; j++ ) {
        Assert.assertNotNull("Data element (" + i + "," + j + ") is null", result.data[i][j]);
        Assert.assertFalse("Data element (" + i + "," + j + ") is empty", ((String)result.data[i][j]).isEmpty());
      }
    }
    new TextTable(result.header, result.data).printTable();
  }
}