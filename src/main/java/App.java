import logic.ReportAggregator;
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
import java.util.Map;

/**
 * Created by glerman on 24/9/17.
 */
public class App {

  private static final FileReader fileReader = new FileReader();
  private static final CsvParser csvParser = new CsvParser();
  private static final TeacherRegistry teacherRegistry = new TeacherRegistry();
  private static final TeacherOutputFormatter formatter = new TeacherOutputFormatter();

  public static void main(String[] args) throws IOException, MessagingException {

    String salariesFilePath = args[0];
    String teacherFilePath = args[1];
    String charset = args[2];
    boolean sendMails = Boolean.valueOf(args[3]);
    boolean sendFromNaim = Boolean.valueOf(args[4]);

    CsvResult parsedSalaries = csvParser.parse(fileReader.read(salariesFilePath, charset));
    CsvResult parsedTeachers = csvParser.parse(fileReader.read(teacherFilePath, charset));
    teacherRegistry.registerAll(parsedTeachers.data);
    SalariesLogic salariesLogic = new SalariesLogic(parsedSalaries.data);
    Map<String, TeacherOutput> teacherOutputs = salariesLogic.createTeacherOutputs();
    ReportAggregator.instance.teacherOutputs(teacherOutputs);

    appLogic(charset, sendMails, sendFromNaim, teacherOutputs);

    System.out.println(ReportAggregator.instance.report());
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
        try {
          ReportAggregator.instance.incSendMailAttempt();
          sender.sendMail(
                  teacher.getEmail(),
                  subjectLine,
                  formattedTeacherOutput.toString());
          Thread.sleep(500);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
      //Add subject and email to the printed version
      formattedTeacherOutput.append("\n").append(subjectLine).append("\n").append(teacher.getEmail());
      System.out.println(formattedTeacherOutput);
    });
  }
}
