import domain.Teacher;
import domain.TeacherOutput;
import domain.TeacherRegistry;
import file.FileReader;
import logic.SalariesLogic;
import mail.Sender;
import parse.CsvParser;
import parse.CsvResult;
import report.ReportAggregator;
import view.FormattedOutput;
import view.TeacherOutputFormatter;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by glerman on 24/9/17.
 */
public class App {

  private static final FileReader fileReader = new FileReader();
  private static final CsvParser csvParser = new CsvParser();
  private static final TeacherRegistry teacherRegistry = new TeacherRegistry();
  private static final TeacherOutputFormatter formatter = new TeacherOutputFormatter();

  public static void main(String[] args) throws IOException, MessagingException {

    try {
      runApp(args);
    } catch (Throwable t) {
      ReportAggregator.instance.unexpectedError(t);
    } finally {
      System.out.println();
      System.out.println();
      System.out.println(ReportAggregator.instance.report());
    }
  }

  enum TeachersToIterate {ONE, ALL}

  private static void runApp(String[] args) {
    ReportAggregator.instance.appInput(args);
    String salariesFilePath = args[0];
    String teacherFilePath = args[1];
    String charset = args[2];
    boolean sendMails = Boolean.valueOf(args[3]);
    boolean sendFromNaim = Boolean.valueOf(args[4]);
    TeachersToIterate teachersToIterate = TeachersToIterate.valueOf(args[5]);
    String receiptTo = args[6];

    Optional<List<String>> salaryLines = fileReader.read(salariesFilePath, charset);
    Optional<List<String>> teacherLines = fileReader.read(teacherFilePath, charset);
    if (salaryLines.isPresent() && teacherLines.isPresent()) {
      CsvResult parsedSalaries = csvParser.parse(salaryLines.get());
      CsvResult parsedTeachers = csvParser.parse(teacherLines.get());
      teacherRegistry.registerAll(parsedTeachers.data);
      SalariesLogic salariesLogic = new SalariesLogic(parsedSalaries.data);
      Map<String, TeacherOutput> teacherOutputs = salariesLogic.createTeacherOutputs();
      ReportAggregator.instance.teacherOutputs(teacherOutputs);

      Optional<Sender> sender;
      try {
        sender = sendMails ?
                Optional.of(new Sender(sendFromNaim)) :
                Optional.empty();
      } catch (IOException e) {
        ReportAggregator.instance.ioError("Failed to create mail service instance", e);
        return;
      }
      for (Map.Entry<String, TeacherOutput> teacherToOutput : teacherOutputs.entrySet()) {
        appLogic(teacherToOutput.getKey(), teacherToOutput.getValue(), sender, receiptTo);
        if (teachersToIterate.equals(TeachersToIterate.ONE)) {
          break;
        }
      }
    }
  }

  private static void appLogic(String teacherName, TeacherOutput teacherOutput, Optional<Sender> sender, String receiptTo) {
    Teacher teacher = teacherRegistry.getTeacher(teacherName);
    if (teacher == null) {
      ReportAggregator.instance.addTeacherWithoutEmail(teacherName);
      return;
    }
    FormattedOutput formattedTeacherOutput = formatter.formatTeacherOutput(teacherName, teacherOutput, receiptTo);
    if (sender.isPresent()) {
      try {
        ReportAggregator.instance.incSendMailAttempt();
        sender.get().sendMail(
                teacher.getEmail(),
                formattedTeacherOutput);
        Thread.sleep(500);
      } catch (Exception e) {
        ReportAggregator.instance.sendMailFailure("Failed to send teacher mail", teacher, formattedTeacherOutput.subject(), e);
      }
    }
    printToScreen(teacher, formattedTeacherOutput);
  }

  private static void printToScreen(Teacher teacher, FormattedOutput formattedTeacherOutput) {
    System.out.println(teacher.getEmail());
    System.out.println(formattedTeacherOutput.subject());
    System.out.println(formattedTeacherOutput.header());
    System.out.println(formattedTeacherOutput.salaryTablesHtml());
    System.out.println(formattedTeacherOutput.footer());
    System.out.println();
    System.out.println();
  }


  private static void appLogic(String charset, boolean sendMails, boolean sendFromNaim,
                               Map<String, TeacherOutput> teacherOutputs) {

//    Sender sender = new Sender(sendFromNaim);
//    teacherOutputs.forEach((teacherName, teacherOutput) -> {
//      Teacher teacher = teacherRegistry.getTeacher(teacherName);
//      if (teacher == null) {
//        ReportAggregator.instance.addTeacherWithoutEmail(teacherName);
//        return;
//      }
//      StringBuilder formattedTeacherOutput = null;
//      try {
//        formattedTeacherOutput = formatter.formatSalaryTables(charset, teacherOutput);
//      } catch (UnsupportedEncodingException e) {
//        ReportAggregator.instance.teacherOutputFormattingError(teacherName, e, charset);
//        return;
//      }
//      String subjectLine = formatter.formatSubjectLine(teacherName);
//      if (sendMails) {
//        String emailBodyText = formattedTeacherOutput.toString();
//        try {
//          ReportAggregator.instance.incSendMailAttempt();
//          sender.sendMail(
//                  teacher.getEmail(),
//                  subjectLine,
//                  emailBodyText);
//          Thread.sleep(500);
//        } catch (Exception e) {
//          ReportAggregator.instance.sendMailFailure(teacher, subjectLine, emailBodyText, e);
//        }
//      }
      //Add subject and email to the printed version
//      formattedTeacherOutput.append("\n").append(subjectLine).append("\n").append(teacher.getEmail());
//      System.out.println(formattedTeacherOutput);
//    });
  }
}
