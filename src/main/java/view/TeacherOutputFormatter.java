package view;

import domain.SalaryInfo;
import domain.TeacherOutput;
import view.html.HtmlTableFormatter;

import java.io.UnsupportedEncodingException;
import java.util.Collection;

//todo: test format via system test
public class TeacherOutputFormatter {

  private static final String[] OUTPUT_COLUMNS = {"כיתה", "אולם", "תאריך", "משתתפים", "תעריף"};
  private static final String subjectSuffix = "פירוט תשלום עבור חודש - 09/2017";
  private static final String message = "הי,\n" +
          "\n" +
          "להלן פירוט תשלומים עבור חודש 09/2017 \n" +
          "נא לרשום על הקבלה / חשבונית עבור \"אלה בן אהרון\" על הסכום הנ״ל \n" + //todo: make the name a param
          "(בתוספת מע\"מ במידה וצריך).\n" +
          "\n";

  private final HtmlTableFormatter tableFormatter;

  public TeacherOutputFormatter() {
    tableFormatter = new HtmlTableFormatter();
  }

  public StringBuilder formatTeacherOutput(TeacherOutput teacherOutput) throws UnsupportedEncodingException {
    StringBuilder sb = new StringBuilder();

    sb.append("\n").append(message).append("\n");

    for (Collection<SalaryInfo> salaries : teacherOutput.classNameToSalariesInfo.values()) {
      String htmlTable = tableFormatter.toHtml(salaries, OUTPUT_COLUMNS);
      sb.append(htmlTable).append("\n");
    }
    sb.append("סה\"כ בגין שיעורים: ").append(teacherOutput.totalPayment).append(" ש״ח").append("\n");
    sb.append("\n").append("תודה").append("\n\n");
    sb.append("לנה").append("\n");
    sb.append("סטודיו נעים - יוגה, מחול, פילאטיס").append("\n");
    sb.append("דרך שלמה (סלמה) 46, תל אביב, מיקוד: 66073").append("\n\n");
    sb.append("naim.org.il\n" +
            "facebook.com/stnaim\n" +
            "facebook.com/gymnaim\n");
    return sb;
  }

  public String formatSubjectLine(String teacherName) {
    return String.format("%s %s", teacherName, subjectSuffix);
  }
}
