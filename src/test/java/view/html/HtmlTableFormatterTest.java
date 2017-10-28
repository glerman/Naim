package view.html;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import domain.SalaryInfo;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
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
    String html = formatter.toHtml(classToSalaries.values());

    System.out.println(html);
  }


  @Test
  public void testEntireHtml() throws Exception {
    Map<String, Collection<SalaryInfo>> classToSalaries = Maps.newHashMap();
    ViewTestDataGenerator.salaries.forEach(salary -> classToSalaries.put(salary.getClassName(), Collections.singleton(salary)));

    String header = "היי\n" +
            "להלן פירוט תשלומים עבור חודש 08/2017\n" +
            "נא לרשום על הקבלה / חשבונית עבור \"נורמל מחקר\" על הסכום הנ״ל\n" +
            "(בתוספת מע\"מ במידה וצריך).\n" +
            "\n" +
            "דו״ח שיעורים";

    String footer = "\n" +
            "סה\"כ בגין שיעורים: 1370 ש״ח\n" +
            "\n" +
            "תודה\n" +
            "\n" +
            "לנה\n" +
            "סטודיו נעים - יוגה, מחול, פילאטיס\n" +
            "דרך שלמה (סלמה) 46, תל אביב, מיקוד: 66073\n" +
            "\n" +
            "naim.org.il\n" +
            "facebook.com/stnaim\n" +
            "facebook.com/gymnaim";

    String html = formatter.toEntireHtml(classToSalaries.values(), header, footer);

    Assert.assertFalse(StringUtils.isEmpty(html));
    System.out.println(html);
  }
}
