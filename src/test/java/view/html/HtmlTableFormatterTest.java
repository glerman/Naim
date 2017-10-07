package view.html;

import org.junit.Before;
import org.junit.Test;
import view.ViewTestDataGenerator;

public class HtmlTableFormatterTest {

  private HtmlTableFormatter formatter;

  @Before
  public void setUp() throws Exception {
    formatter = new HtmlTableFormatter();
  }

  @Test
  public void test() throws Exception {
    String html = formatter.toHtml(ViewTestDataGenerator.salaries, ViewTestDataGenerator.columnNames);

    System.out.println(html);
  }
}