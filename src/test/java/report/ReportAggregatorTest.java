package report;

import domain.SalaryInfo;
import domain.Teacher;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import parse.SalaryInfoParser;
import parse.TeacherParser;

import java.util.Optional;

public class ReportAggregatorTest {

  private ReportAggregator instance;

  @Before
  public void setUp() throws Exception {
    instance = ReportAggregator.instance;
  }

  @Test
  public void testParsingErrors() throws Exception {

    Optional<SalaryInfo> salaryOptional = new SalaryInfoParser().parse(new Object[0]);
    Assert.assertFalse(salaryOptional.isPresent());
    Assert.assertEquals(1, instance.salaryParsingErrors.size());
    Assert.assertEquals(0, instance.teacherParsingErrors.size());

    Object[] teacherRow = new Object[1];
    teacherRow[0] = 15; //wrong type !
    Optional<Teacher> teacherOptional = new TeacherParser().parse(teacherRow);
    Assert.assertFalse(teacherOptional.isPresent());
    Assert.assertEquals(1, instance.salaryParsingErrors.size());
    Assert.assertEquals(1, instance.teacherParsingErrors.size());

    System.out.println(instance.report());
  }
}