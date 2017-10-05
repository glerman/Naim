package parse;

import domain.SalaryInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import util.InputReaderHelper;

public class SalaryInfoParserTest {


  private SalaryInfoParser parser;
  private CsvResult csvResult;

  @Before
  public void setUp() throws Exception {
    parser = new SalaryInfoParser();
    csvResult = InputReaderHelper.readCsv(InputReaderHelper.salariesFilePath);
  }

  @Test
  public void testDateFormat() throws Exception {

    SalaryInfo salaryInfo = parser.parse(csvResult.data[0]);
    Assert.assertEquals("02/08", salaryInfo.getDate());
  }

  @Test
  public void testAttendees() throws Exception {
    SalaryInfo salaryInfo = parser.parse(csvResult.data[1]);
    Assert.assertEquals(4, salaryInfo.getAttendees());
  }
}