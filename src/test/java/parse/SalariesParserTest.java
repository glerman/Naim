package parse;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class SalariesParserTest {

  @Test
  public void test() throws Exception {

    SalariesParser salariesParser = new SalariesParser();

    List<String> salaries = salariesParser.readAndParse(FilePathProvider.salariesFilePath);

    System.out.println();
    System.out.println(salaries);

    Assert.assertEquals(120 + 1, salaries.size());
  }
}