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

  private static void runApp(String[] args) throws IOException {
    ReportAggregator.instance.appInput(args);
    String salariesFilePath = args[0];
    String teacherFilePath = args[1];
    String charset = args[2];
    boolean sendMails = Boolean.valueOf(args[3]);
    boolean sendFromNaim = Boolean.valueOf(args[4]);

    Optional<List<String>> salaryLines = fileReader.read(salariesFilePath, charset);
    Optional<List<String>> teacherLines = fileReader.read(teacherFilePath, charset);
    if (salaryLines.isPresent() && teacherLines.isPresent()) {
      CsvResult parsedSalaries = csvParser.parse(salaryLines.get());
      CsvResult parsedTeachers = csvParser.parse(teacherLines.get());
      teacherRegistry.registerAll(parsedTeachers.data);
      SalariesLogic salariesLogic = new SalariesLogic(parsedSalaries.data);
      Map<String, TeacherOutput> teacherOutputs = salariesLogic.createTeacherOutputs();
      ReportAggregator.instance.teacherOutputs(teacherOutputs);

      appLogic(charset, sendMails, sendFromNaim, teacherOutputs);
    }
  }


  private static void appLogic(String charset, boolean sendMails, boolean sendFromNaim,
                               Map<String, TeacherOutput> teacherOutputs) throws IOException {

    Sender sender = new Sender(sendFromNaim);
    teacherOutputs.forEach((teacherName, teacherOutput) -> {
      Teacher teacher = teacherRegistry.getTeacher(teacherName);
      if (teacher == null) {
        ReportAggregator.instance.addTeacherWithoutEmail(teacherName);
        return;
      }
      StringBuilder formattedTeacherOutput = formatter.formatTeacherOutput(charset, teacherOutput);
      String subjectLine = formatter.formatSubjectLine(teacherName);
      if (sendMails) {
        String emailBodyText = formattedTeacherOutput.toString();
        try {
          ReportAggregator.instance.incSendMailAttempt();
          sender.sendMail(
                  teacher.getEmail(),
                  subjectLine,
                  emailBodyText);
          Thread.sleep(500);
        } catch (Exception e) {
          ReportAggregator.instance.sendMailFailure(teacher, subjectLine, emailBodyText, e);
        }
      }
      //Add subject and email to the printed version
      formattedTeacherOutput.append("\n").append(subjectLine).append("\n").append(teacher.getEmail());
      System.out.println(formattedTeacherOutput);
    });
  }
}
