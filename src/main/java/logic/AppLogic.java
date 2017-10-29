package logic;

import domain.MessageRegistry;
import domain.Teacher;
import domain.TeacherOutput;
import domain.TeacherRegistry;
import file.FileReader;
import mail.Sender;
import org.apache.commons.lang.StringUtils;
import parse.CsvParser;
import parse.CsvResult;
import report.ReportAggregator;
import view.FormattedOutput_2;
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


  public String start(String salariesFilePath, String teacherFilePath, String messagesFilePath,
                      String charset, boolean sendMails, boolean sendFromNaim, TeachersToIterate teachersToIterate,
                      String receiptTo) {

    if (!validInput(salariesFilePath, teacherFilePath, charset, receiptTo)) {
      //reason already logged
      return "";
    }
    createTeacherOutputs(salariesFilePath, teacherFilePath, messagesFilePath, charset).
            ifPresent(outputs -> sendTeacherOutput(sendMails, sendFromNaim, teachersToIterate, receiptTo, outputs));
    return previewBuilder.toString();
  }

  private void sendTeacherOutput(boolean sendMails, boolean sendFromNaim, TeachersToIterate teachersToIterate, String receiptTo, Map<String, TeacherOutput> teacherOutputs) {
    ReportAggregator.instance.emailsToSend(teacherOutputs, teachersToIterate, sendMails);
    Optional<Sender> sender = getSender(sendMails, sendFromNaim);

    for (Map.Entry<String, TeacherOutput> teacherToOutput : teacherOutputs.entrySet()) {
      formatAndSendTeacher(teacherToOutput.getKey(), teacherToOutput.getValue(), sender, receiptTo);
      if (teachersToIterate.equals(TeachersToIterate.ONE)) {
        break;
      }
    }
  }

  private Optional<Sender> getSender(boolean sendMails, boolean sendFromNaim) {
    try {
      return sendMails ?
              Optional.of(new Sender(sendFromNaim)) :
              Optional.empty();
    } catch (IOException e) {
      ReportAggregator.instance.ioError("Failed to create mail service instance", e);
      return Optional.empty();
    }
  }

  private Optional<Map<String, TeacherOutput>> createTeacherOutputs(String salariesFilePath, String teacherFilePath, String messagesFilePath, String charset) {

    Optional<List<String>> salaryLines = fileReader.read(salariesFilePath, charset);
    Optional<List<String>> teacherLines = fileReader.read(teacherFilePath, charset);
    Optional<List<String>> messageLines = StringUtils.isEmpty(messagesFilePath) ?
            Optional.empty() :
            fileReader.read(messagesFilePath, charset);
    if (!salaryLines.isPresent() || !teacherLines.isPresent()) {
      //reason already logged
      return Optional.empty();
    }
    CsvResult parsedSalaries = salaryLines.map(csvParser::parse).get();
    CsvResult parsedTeachers = teacherLines.map(csvParser::parse).get();
    Optional<MessageRegistry> messageRegistry = messageLines.
            map(csvParser::parse).
            map(MessageRegistry::new);
    teacherRegistry.registerTeachers(parsedTeachers.data);
    messageRegistry.ifPresent(teacherRegistry::registerMessages);
    SalariesLogic salariesLogic = new SalariesLogic(parsedSalaries.data);
    return Optional.of(salariesLogic.createTeacherOutputs());
  }

  private void formatAndSendTeacher(String teacherName, TeacherOutput teacherOutput, Optional<Sender> sender,
                                    String receiptTo) {
    Teacher teacher = teacherRegistry.getTeacher(teacherName);
    if (teacher == null) {
      ReportAggregator.instance.addTeacherWithoutEmail(teacherName);
      return;
    }
    FormattedOutput_2 formattedTeacherOutput = formatter.formatTeacherOutput_2(teacherName, teacherOutput, receiptTo);
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
      ReportAggregator.instance.incSendEmailSuccess();
    }
    appendToPreview(teacher, formattedTeacherOutput);
  }

  //todo: change to a list of objects, each object holds address, subject and entire html. when getting the response we'll align the address and subject to the right (inline) and the entire html will align itself
  private void appendToPreview(Teacher teacher, FormattedOutput_2 formattedTeacherOutput) {
//    previewBuilder.append(teacher.getEmail()).append("\n"); //todo: first email isn't shown (html issue)
//    previewBuilder.append(formattedTeacherOutput.subject()).append("\n");
    previewBuilder.append(formattedTeacherOutput.entireHtml());
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
