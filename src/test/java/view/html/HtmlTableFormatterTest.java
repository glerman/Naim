package view.html;

import org.junit.Before;
import org.junit.Test;

public class HtmlTableFormatterTest {

  private String[] columnNames = {"כיתה", "אולם", "תאריך", "משתתפים", "תעריף"};
  private HtmlTableFormatter formatter;

  @Before
  public void setUp() throws Exception {
    formatter = new HtmlTableFormatter();
  }

  @Test
  public void test() throws Exception {
    String html = formatter.toHtml(null, columnNames);

    System.out.println(html);
  }
}