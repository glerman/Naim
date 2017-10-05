package view;

import dnl.utils.text.table.TextTable;
import domain.TeacherOutput;
import view.texttable.SalariesTextTableFormatter;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public class TeacherOutputFormatter {

  private static final String[] OUTPUT_COLUMNS = {"כיתה", "אולם", "תאריך", "משתתפים", "תעריף"};
  private static final String subjectSuffix = "פירוט תשלום עבור חודש - 09/2017";
  private static final String message = "הי,\n" +
          "\n" +
          "להלן פירוט תשלומים עבור חודש 09/2017 \n" +
          "נא לרשום על הקבלה / חשבונית עבור \"אלה בן אהרון\" על הסכום הנ״ל \n" + //todo: make the name a param
          "(בתוספת מע\"מ במידה וצריך).\n" +
          "\n";

  private final SalariesTextTableFormatter tableFormatter;

  public TeacherOutputFormatter() {
    tableFormatter = new SalariesTextTableFormatter();
  }

  public StringBuilder formatTeacherOutput(String charset, TeacherOutput teacherOutput) {
    StringBuilder sb = new StringBuilder();

    sb.append("\n").append(message).append("\n").append("דו\"ח שיעורים\n");

    teacherOutput.classNameToSalariesInfo.forEach((className, salariesInfo) -> {
      TextTable outputTable = tableFormatter.toTextTable(salariesInfo, OUTPUT_COLUMNS);
      handleSingleOutputTable(charset, sb, outputTable);
    });
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

  private void handleSingleOutputTable(String charset, StringBuilder sb, TextTable outputTable) {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    outputTable.printTable(new PrintStream(outputStream), 0);
    try {
      String outputTableStr = outputStream.toString(charset);
      sb.append(outputTableStr).append("\n");
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }
}
