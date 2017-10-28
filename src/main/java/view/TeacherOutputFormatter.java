package view;

import domain.SalaryInfo;
import domain.TeacherOutput;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import view.html.HtmlTableFormatter;

//todo: test format via system test
//todo: make sure all text is aligned to the right
public class TeacherOutputFormatter {

  private static DateTimeFormatter inputDtf = DateTimeFormat.forPattern("dd/MM/yyyy");
  private static DateTimeFormatter outputDtf = DateTimeFormat.forPattern("MM/yyyy");

  private final HtmlTableFormatter tableFormatter;

  public TeacherOutputFormatter() {
    tableFormatter = new HtmlTableFormatter();
  }

  public FormattedOutput formatTeacherOutput(String teacherName, TeacherOutput teacherOutput, String reciptTo) {

    String outputDate = extractDate(teacherOutput);
    return FormattedOutput.create(
            formatSubjectLine(teacherName, outputDate),
            formatMailHeader(reciptTo, outputDate),
            formatSalaryTables(teacherOutput),
            formatMailFooter(teacherOutput)
    );
  }

  public FormattedOutput_2 formatTeacherOutput_2(String teacherName, TeacherOutput teacherOutput, String reciptTo) {
    String outputDate = extractDate(teacherOutput);
    return FormattedOutput_2.create(
            formatSubjectLine(teacherName, outputDate),
            formatMailHeader(reciptTo, outputDate),
            formatSalaryTables(teacherOutput),
            formatMailFooter(teacherOutput),
            tableFormatter.toEntireHtml(teacherOutput.classNameToSalariesInfo.values(), formatMailHeader(reciptTo, outputDate), formatMailFooter(teacherOutput))
    );
  }

  private String formatMailHeader(String receiptTo, String outputDate) {

    StringBuilder sb = new StringBuilder();
    sb.append("היי").append("\n");
    sb.append("להלן פירוט תשלומים עבור חודש ").append(outputDate).append("\n");
    sb.append("נא לרשום על הקבלה / חשבונית עבור ").append('"').append(receiptTo).append("\" ").append("על הסכום הנ״ל").append("\n");
    sb.append("(בתוספת מע\"מ במידה וצריך).").append("\n");
    sb.append("\n");
    sb.append("דו״ח שיעורים").append("\n");
    return sb.toString();
  }

  private String extractDate(TeacherOutput teacherOutput) {
    SalaryInfo salaryInfo = teacherOutput.classNameToSalariesInfo.values().iterator().next().iterator().next();
    String origDate = salaryInfo.getDate();
    DateTime dateTime = inputDtf.parseDateTime(origDate);
    return outputDtf.print(dateTime);
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
    return tableFormatter.toHtml(teacherOutput.classNameToSalariesInfo.values());
  }

  private String formatSubjectLine(String teacherName, String outputDate) {
    return String.format("%s - פירוט תשלום עבור חודש %s", outputDate, teacherName);
  }
}
