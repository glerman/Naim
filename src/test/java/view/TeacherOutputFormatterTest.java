package view;

import domain.SalaryInfo;
import domain.TeacherOutput;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public class TeacherOutputFormatterTest {


  private TeacherOutputFormatter underTest;
  private Map<String, Collection<SalaryInfo>> map;
  private int sum;

  @Before
  public void setUp() throws Exception {
    underTest = new TeacherOutputFormatter();
    map = Collections.singletonMap("class", ViewTestDataGenerator.salaries);
    sum = map.values().stream().flatMap(Collection::stream).mapToInt(SalaryInfo::getPayment).sum();
  }

  @Test
  public void test() throws Exception {
    TeacherOutput teacherOutput = new TeacherOutput(map, sum, Optional.of("teacherMessage"));
    FormattedOutput formattedOutput = underTest.formatTeacherOutput("teach", teacherOutput, "גברת שושנה");

    Assert.assertNotNull(formattedOutput);
    Assert.assertFalse(StringUtils.isEmpty(formattedOutput.subject()));
    Assert.assertFalse(StringUtils.isEmpty(formattedOutput.entireHtml()));

    System.out.println(formattedOutput);
  }

  @Test
  public void testNoTeacherMessage() throws Exception {

    Optional<String> teacherMessage = Optional.empty();
    SentenceContainer header = underTest.formatMailHeader("teacher", "11/12", teacherMessage);

    Assert.assertEquals(6, header.sentences().size());

    teacherMessage = Optional.of("");
    header = underTest.formatMailHeader("teacher", "11/12", teacherMessage);

    Assert.assertEquals(6, header.sentences().size());
  }

  @Test
  public void testSubjectLine() throws Exception {
    String subjectLine = underTest.formatSubjectLine("גל לרמן", "10/2017");
    System.out.println(subjectLine);
    Assert.assertEquals("גל לרמן פירוט תשלום עבור חודש - 10/2017", subjectLine);
  }

  @Test
  public void testTeacherMessage() throws Exception {

    Optional<String> teacherMessage = Optional.of("message");
    SentenceContainer header = underTest.formatMailHeader("teacher", "11/12", teacherMessage);

    Assert.assertEquals(8, header.sentences().size());
  }
}
