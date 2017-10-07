import report.ReportAggregator;
import logic.SalariesLogic;
import domain.Teacher;
import domain.TeacherOutput;
import domain.TeacherRegistry;
import file.FileReader;
import mail.Sender;
import parse.CsvParser;
import parse.CsvResult;
import view.TeacherOutputFormatter;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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

  enum MailToSend {ONE, ALL}

  private static void runApp(String[] args) {
    ReportAggregator.instance.appInput(args);
    String salariesFilePath = args[0];
    String teacherFilePath = args[1];
    String charset = args[2];
    boolean sendMails = Boolean.valueOf(args[3]);
    boolean sendFromNaim = Boolean.valueOf(args[4]);
    MailToSend mailToSend = MailToSend.valueOf(args[5]);

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
        appLogic(teacherToOutput.getKey(), teacherToOutput.getValue(), sender);
        if (mailToSend.equals(MailToSend.ONE)) {
          break;
        }
      }
    }
  }

  private static void appLogic(String teacherName, TeacherOutput teacherOutput, Optional<Sender> sender) {
    Teacher teacher = teacherRegistry.getTeacher(teacherName);
    if (teacher == null) {
      ReportAggregator.instance.addTeacherWithoutEmail(teacherName);
      return;
    }
    StringBuilder formattedTeacherOutput;
    try {
      formattedTeacherOutput = formatter.formatTeacherOutput(teacherOutput);
    } catch (UnsupportedEncodingException e) {
      ReportAggregator.instance.formattingError(teacherName, e);
      return;
    }
    String subjectLine = formatter.formatSubjectLine(teacherName);
    if (sender.isPresent()) {
      String emailBodyText = formattedTeacherOutput.toString();
      try {
        ReportAggregator.instance.incSendMailAttempt();
        sender.get().sendMail(
                teacher.getEmail(),
                subjectLine,
                emailBodyText);
        Thread.sleep(500);
      } catch (Exception e) {
        ReportAggregator.instance.sendMailFailure("Failed to send teacher mail", teacher, subjectLine, e);
      }
    }
    //Add subject and email to the printed version
    formattedTeacherOutput.append("\n").append(subjectLine).append("\n").append(teacher.getEmail());
    System.out.println(formattedTeacherOutput);
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
//        formattedTeacherOutput = formatter.formatTeacherOutput(charset, teacherOutput);
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
