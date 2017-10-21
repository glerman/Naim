package view;

import com.google.common.collect.Lists;
import domain.SalaryInfo;
import domain.TeacherOutput;
import org.junit.Test;

import java.util.ArrayList;
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
    FormattedOutput formattedOutput = formatter.formatTeacherOutput("teach", teacherOutput);

    System.out.println(formattedOutput.toString());
  }
//  private static final String[] OUTPUT_COLUMNS = {"כיתה", "אולם", "תאריך", "משתתפים", "תעריף"};
  private static final String[] OUTPUT_COLUMNS = {"תעריף","משתתפים","תאריך","אולם","כיתה"};
  @Test
  public void name() throws Exception {

    Lists.reverse(Lists.newArrayList(OUTPUT_COLUMNS)).forEach(s -> System.out.print("\""+s+"\","));
    ;
  }
}
