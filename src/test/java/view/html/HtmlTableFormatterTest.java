package view.html;

import com.google.common.collect.Maps;
import domain.SalaryInfo;
import org.junit.Before;
import org.junit.Test;
import view.ViewTestDataGenerator;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class HtmlTableFormatterTest {

  private HtmlTableFormatter formatter;

  @Before
  public void setUp() throws Exception {
    formatter = new HtmlTableFormatter();
  }

  @Test
  public void test() throws Exception {
    Map<String, Collection<SalaryInfo>> classToSalaries = Maps.newHashMap();
    ViewTestDataGenerator.salaries.forEach(salary -> classToSalaries.put(salary.getClassName(), Collections.singleton(salary)));
    String html = formatter.toHtml(classToSalaries.values(), ViewTestDataGenerator.columnNames);

    System.out.println(html);
  }
}