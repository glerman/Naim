package parse;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class SalariesParserTest {

  private final String salariesFilePath = this.getClass().getResource("/salaries.csv").getFile();

  @Test
  public void test() throws Exception {

    SalariesParser salariesParser = new SalariesParser();

    List<String> salaries = salariesParser.readAndParse(salariesFilePath);

    System.out.println();
    System.out.println(salaries);

    Assert.assertEquals(120, salaries.size());
  }
}