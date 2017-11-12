package view;

import com.google.common.collect.Lists;
import domain.SalaryInfo;
import domain.TeacherOutput;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import view.html.HtmlOutputFormatter;
import view.html.HtmlUtils;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

//todo: test format via system test
//todo: too many public methods, only reason is for test can be solved by better design
public class TeacherOutputFormatter {

  private static DateTimeFormatter inputDtf = DateTimeFormat.forPattern("dd/MM/yyyy");
  private static DateTimeFormatter outputDtf = DateTimeFormat.forPattern("MM/yyyy");

  private final HtmlOutputFormatter tableFormatter;

  public TeacherOutputFormatter() {
    Function<String,String> htmlTransformer = html -> HtmlUtils.prepareForEmail(html, "th, td, p");
    tableFormatter = new HtmlOutputFormatter(htmlTransformer);
  }

  public FormattedOutput formatTeacherOutput(String teacherName, TeacherOutput teacherOutput, String reciptTo) {

    String outputDate = extractDate(teacherOutput);
    SentenceContainer header = formatMailHeader(reciptTo, outputDate, teacherOutput.teacherMessage);
    SentenceContainer footer = formatMailFooter(teacherOutput.totalPayment);
    String entireHtml = tableFormatter.toEntireHtml(teacherOutput.classNameToSalariesInfo.values(), header, footer);

    return FormattedOutput.create(
            formatSubjectLine(teacherName, outputDate),
            entireHtml);
  }

  public SentenceContainer formatMailHeader(String receiptTo, String outputDate,
                                            @SuppressWarnings("OptionalUsedAsFieldOrParameterType") Optional<String> teacherMessage) {
    List<String> sentences = Lists.newArrayList();
    sentences.add(("היי"));
    sentences.add("להלן פירוט תשלומים עבור חודש " + outputDate);
    sentences.add(("נא לרשום על הקבלה / חשבונית עבור " + '"' + receiptTo + "\" " + "על הסכום הנ\"ל"));
    sentences.add(("(בתוספת מע\"מ במידה וצריך)."));
    sentences.add("");
    teacherMessage.ifPresent(message -> {
      if (!message.isEmpty()) {
        sentences.add(message);
        sentences.add("");
      }
    });
    sentences.add("דו\"ח שיעורים");
    return SentenceContainer.create(sentences);
  }

  private String extractDate(TeacherOutput teacherOutput) {
    SalaryInfo salaryInfo = teacherOutput.classNameToSalariesInfo.values().iterator().next().iterator().next();
    String origDate = salaryInfo.getDate();
    DateTime dateTime = inputDtf.parseDateTime(origDate);
    return outputDtf.print(dateTime);
  }

  public SentenceContainer formatMailFooter(int totalPayment) {
    List<String> sentences = Lists.newArrayList();
    sentences.add("סה\"כ בגין שיעורים: " + totalPayment + " ש\"ח");
    sentences.add("תודה");
    sentences.add("");
    sentences.add("לנה");
    sentences.add("סטודיו נעים - יוגה, מחול, פילאטיס");
    sentences.add("דרך שלמה (סלמה) 46, תל אביב, מיקוד: 66073");
    sentences.add("");
    sentences.add("naim.org.il");
    sentences.add("facebook.com/stnaim");
    sentences.add("facebook.com/gymnaim");
    return SentenceContainer.create(sentences);
  }

  String formatSubjectLine(String teacherName, String outputDate) {
    return String.format("%s פירוט תשלום עבור חודש - %s", teacherName, outputDate); //reversed param order due to Hebrew
  }
}
