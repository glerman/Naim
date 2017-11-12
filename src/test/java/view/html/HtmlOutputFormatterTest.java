package view.html;

import com.google.common.collect.Maps;
import domain.SalaryInfo;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import view.SentenceContainer;
import view.TeacherOutputFormatter;
import view.ViewTestDataGenerator;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class HtmlOutputFormatterTest {

  private HtmlOutputFormatter formatter;
  private TeacherOutputFormatter dataHelper;

  @Before
  public void setUp() throws Exception {
    formatter = new HtmlOutputFormatter(Function.identity());
    dataHelper = new TeacherOutputFormatter();
  }


  @Test
  public void testEntireHtml() throws Exception {
    Map<String, Collection<SalaryInfo>> classToSalaries = Maps.newHashMap();
    ViewTestDataGenerator.salaries.forEach(salary -> classToSalaries.put(salary.getClassName(), Collections.singleton(salary)));

    SentenceContainer header = dataHelper.formatMailHeader("שושנה", "08/2017", Optional.of("הודעה למורה..."));
    SentenceContainer footer = dataHelper.formatMailFooter(1370);

    String html = formatter.toEntireHtml(classToSalaries.values(), header, footer);

    Assert.assertFalse(StringUtils.isEmpty(html));
    System.out.println(html);
  }
}
