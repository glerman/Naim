package view;

import domain.TeacherOutput;
import view.html.HtmlTableFormatter;

//todo: test format via system test
//todo: make sure all text is aligned to the right
public class TeacherOutputFormatter {

  private static final String[] OUTPUT_COLUMNS = {"תעריף","משתתפים","תאריך","אולם","כיתה"};
  private static final String subjectSuffix = "פירוט תשלום עבור חודש - 09/2017";
  private static final String message = "הי,\n" +
          "\n" +
          "להלן פירוט תשלומים עבור חודש 09/2017 \n" +//todo: make month a param
          "נא לרשום על הקבלה / חשבונית עבור \"אלה בן אהרון\" על הסכום הנ״ל \n" + //todo: make the name a param
          "(בתוספת מע\"מ במידה וצריך).\n" +
          "\n" +
          "דו״ח שיעורים" +
          "\n";

  private final HtmlTableFormatter tableFormatter;

  public TeacherOutputFormatter() {
    tableFormatter = new HtmlTableFormatter();
  }

  public FormattedOutput formatTeacherOutput(String teacherName, TeacherOutput teacherOutput) {
    return FormattedOutput.create(
            formatSubjectLine(teacherName),
            formatMailHeader(),
            formatSalaryTables(teacherOutput),
            formatMailFooter(teacherOutput)
    );
  }

  private String formatMailHeader() {
    return message;
  }

  private String formatMailFooter(TeacherOutput teacherOutput) {
    StringBuilder sb = new StringBuilder();
    sb.append("סה\"כ בגין שיעורים: ").append(teacherOutput.totalPayment).append(" ש״ח").append("\n");
    sb.append("\n").append("תודה").append("\n\n");
    sb.append("לנה").append("\n");
    sb.append("סטודיו נעים - יוגה, מחול, פילאטיס").append("\n");
    sb.append("דרך שלמה (סלמה) 46, תל אביב, מיקוד: 66073").append("\n\n");
    sb.append("naim.org.il\n" +
            "facebook.com/stnaim\n" +
            "facebook.com/gymnaim\n");
    return sb.toString();
  }

  private String formatSalaryTables(TeacherOutput teacherOutput) {
    return tableFormatter.toHtml(teacherOutput.classNameToSalariesInfo.values(), OUTPUT_COLUMNS);
  }

  private String formatSubjectLine(String teacherName) {
    return String.format("%s %s", teacherName, subjectSuffix);
  }
}
