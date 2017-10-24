package parse;

import com.google.common.collect.Lists;
import domain.SalaryInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import util.InputReaderHelper;

import java.util.List;
import java.util.Optional;

public class SalaryInfoParserTest {


  private SalaryInfoParser parser;
  private CsvResult csvResult;

  @Before
  public void setUp() throws Exception {
    parser = new SalaryInfoParser();
    csvResult = InputReaderHelper.readCsv(InputReaderHelper.salariesFilePath);
  }

  @Test
  public void testAttendees() throws Exception {
    SalaryInfo salaryInfo = parser.parse(csvResult.data[1]).get();
    Assert.assertEquals(4, salaryInfo.getAttendees());
  }

  @Test
  public void print() throws Exception {
    Lists.newArrayList(csvResult.data).stream().
            map(parser::parse).
            map(Optional::get).
            map(SalaryInfo::getTeacherName).
            distinct().
            forEach(System.out::println);

  }
}
