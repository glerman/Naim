package view;

import domain.SalaryInfo;
import domain.TeacherOutput;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class TeacherOutputFormatterTest {

  @Test
  public void test() throws Exception {
    TeacherOutputFormatter formatter = new TeacherOutputFormatter();
    Map<String, Collection<SalaryInfo>> map = Collections.singletonMap("class", ViewTestDataGenerator.salaries);
    int sum = map.values().stream().flatMap(Collection::stream).mapToInt(SalaryInfo::getPayment).sum();
    TeacherOutput teacherOutput = new TeacherOutput(map, sum);
    FormattedOutput_2 formattedOutput = formatter.formatTeacherOutput_2("teach", teacherOutput, "גברת שושנה");

    Assert.assertNotNull(formattedOutput);
    Assert.assertFalse(StringUtils.isEmpty(formattedOutput.subject()));
    Assert.assertFalse(StringUtils.isEmpty(formattedOutput.header()));
    Assert.assertFalse(StringUtils.isEmpty(formattedOutput.salaryTablesHtml()));
    Assert.assertFalse(StringUtils.isEmpty(formattedOutput.footer()));
    Assert.assertFalse(StringUtils.isEmpty(formattedOutput.entireHtml()));

    System.out.println(formattedOutput);
  }
}
