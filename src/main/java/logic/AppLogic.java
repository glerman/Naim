package logic;

import domain.Teacher;
import domain.TeacherOutput;
import domain.TeacherRegistry;
import file.FileReader;
import mail.Sender;
import org.apache.commons.lang.StringUtils;
import parse.CsvParser;
import parse.CsvResult;
import report.ReportAggregator;
import view.FormattedOutput;
import view.TeacherOutputFormatter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AppLogic {

  private final FileReader fileReader = new FileReader();
  private final CsvParser csvParser = new CsvParser();
  private final TeacherRegistry teacherRegistry = new TeacherRegistry();
  private final TeacherOutputFormatter formatter = new TeacherOutputFormatter();
  private final StringBuilder previewBuilder = new StringBuilder();


  public String start(String salariesFilePath, String teacherFilePath, String charset, boolean sendMails, boolean sendFromNaim,
                      TeachersToIterate teachersToIterate, String receiptTo) {

    if (!validInput(salariesFilePath, teacherFilePath, charset, receiptTo)) {
      return "";
    }
    Optional<List<String>> salaryLines = fileReader.read(salariesFilePath, charset);
    Optional<List<String>> teacherLines = fileReader.read(teacherFilePath, charset);
    if (salaryLines.isPresent() && teacherLines.isPresent()) {
      CsvResult parsedSalaries = csvParser.parse(salaryLines.get());
      CsvResult parsedTeachers = csvParser.parse(teacherLines.get());
      teacherRegistry.registerAll(parsedTeachers.data);
      SalariesLogic salariesLogic = new SalariesLogic(parsedSalaries.data);
      Map<String, TeacherOutput> teacherOutputs = salariesLogic.createTeacherOutputs();
      ReportAggregator.instance.emailsToSend(teacherOutputs, teachersToIterate);

      Optional<Sender> sender;
      try {
        sender = sendMails ?
                Optional.of(new Sender(sendFromNaim)) :
                Optional.empty();
      } catch (IOException e) {
        ReportAggregator.instance.ioError("Failed to create mail service instance", e);
        return "";
      }
      for (Map.Entry<String, TeacherOutput> teacherToOutput : teacherOutputs.entrySet()) {
        formatAndSendTeacher(teacherToOutput.getKey(), teacherToOutput.getValue(), sender, receiptTo);
        if (teachersToIterate.equals(TeachersToIterate.ONE)) {
          break;
        }
      }
    }
    return previewBuilder.toString();
  }

  private void formatAndSendTeacher(String teacherName, TeacherOutput teacherOutput, Optional<Sender> sender,
                                    String receiptTo) {
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
        Thread.sleep(500); //todo: is this needed?
      } catch (Exception e) {
        ReportAggregator.instance.sendMailFailure("Failed to send teacher mail", teacher, formattedTeacherOutput.subject(), e);
      }
    }
    ReportAggregator.instance.incSendEmailSuccess();
    appendToPreview(teacher, formattedTeacherOutput);
  }

  private void appendToPreview(Teacher teacher, FormattedOutput formattedTeacherOutput) {
    previewBuilder.append(teacher.getEmail()).append("\n");
    previewBuilder.append(formattedTeacherOutput.subject()).append("\n");
    previewBuilder.append(formattedTeacherOutput.header()).append("\n");
    previewBuilder.append(formattedTeacherOutput.salaryTablesHtml()).append("\n");
    previewBuilder.append(formattedTeacherOutput.footer()).append("\n");
    previewBuilder.append("\n\n\n");
  }

  private boolean validInput(String salariesFilePath, String teacherFilePath, String charset, String receiptTo) {

    if (StringUtils.isEmpty(salariesFilePath)) {
      ReportAggregator.instance.userInputError("Missing salaries file path");
      return false;
    }
    if (StringUtils.isEmpty(teacherFilePath)) {
      ReportAggregator.instance.userInputError("Missing teachers file path");
      return false;
    }
    if (StringUtils.isEmpty(charset)) {
      ReportAggregator.instance.userInputError("Missing charset input");
      return false;
    }
    if (StringUtils.isEmpty(receiptTo)) {
      ReportAggregator.instance.userInputError("Missing receiptTo input");
      return false;
    }
    return true;
  }

  public enum TeachersToIterate {ONE, ALL}
}
