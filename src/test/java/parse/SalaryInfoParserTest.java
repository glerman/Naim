package parse;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Assert;
import org.junit.Test;

public class SalaryInfoParserTest {


  @Test
  public void test() throws Exception {

    String outputDate = getDateOutput("30/08/2017");
    Assert.assertEquals("30/08", outputDate);
  }

  private String getDateOutput(String dateStr) {
    DateTime dateTime = SalaryInfoParser.inputDtf.parseDateTime(dateStr);
    return SalaryInfoParser.ouputDtf.print(dateTime);
  }
}