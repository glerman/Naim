package view.html;

import com.google.common.collect.Lists;
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
  public void name() throws Exception {
    String a = "ת";

    a.chars().forEach(System.out::println);
    System.out.println((int)a.charAt(0));

    char[] chars = Character.toChars(1514);
    System.out.println(Lists.newArrayList(chars));
    System.out.println(new String(chars));
  }

  @Test
  public void test() throws Exception {
    Map<String, Collection<SalaryInfo>> classToSalaries = Maps.newHashMap();
    ViewTestDataGenerator.salaries.forEach(salary -> classToSalaries.put(salary.getClassName(), Collections.singleton(salary)));
    String html = formatter.toHtml(classToSalaries.values(), ViewTestDataGenerator.columnNames);

    System.out.println(html);
  }
}
