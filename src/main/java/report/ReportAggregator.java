package report;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import domain.Teacher;
import domain.TeacherOutput;
import report.pojo.InputFileError;
import report.pojo.SalaryParsingProblem;
import report.pojo.SendMailProblem;
import report.pojo.TeacherParsingProblem;

import java.awt.peer.ChoicePeer;
import java.io.IOException;
import java.util.*;

public class ReportAggregator {

  public static final ReportAggregator instance = new ReportAggregator();

  final Set<String> teachersWithoutEmail;
  final Set<SalaryParsingProblem> salaryParsingErrors;
  final Set<TeacherParsingProblem> teacherParsingErrors;
  int numMailsToSend;
  int sentMailAttempt;
  private List<String> appInput;
  private Set<SendMailProblem> sendMailProblems;
  private Set<InputFileError> inputFileErrors;
  private Set<Throwable> unexpectedErrors;

  private ReportAggregator() {
    teachersWithoutEmail = Sets.newHashSet();
    salaryParsingErrors = Sets.newHashSet();
    teacherParsingErrors = Sets.newHashSet();
    sendMailProblems = Sets.newHashSet();
    inputFileErrors = Sets.newHashSet();
    unexpectedErrors = Sets.newHashSet();
  }

  public void addTeacherWithoutEmail(final String teacherName) {
    teachersWithoutEmail.add(teacherName);
  }

  public String report() {
    StringBuilder report = new StringBuilder();

    reportCollectionLineByLine(appInput, "App input: ", report);
    reportProblemsCollection(unexpectedErrors, "Unexpected errors: ", "Unexpected errors were: ", report);
    reportProblemsCollection(inputFileErrors, "Input file errors: ", "Input file errors are: ", report);
    reportProblemsCollection(salaryParsingErrors, "Salary parsing errors: ", "The salary parsing errors were: ", report);
    reportProblemsCollection(teacherParsingErrors, "Teacher parsing errors: ", "The teacher parsing errors were: ", report);
    reportNumber(numMailsToSend, "Total mails to send: ", report);
    reportProblemsCollection(teachersWithoutEmail, "Number of teachers without emails (send wasn't attempted): ", "The teachers without emails are: ", report);
    reportNumber(sentMailAttempt, "Send mail attempts: ", report);
    reportProblemsCollection(sendMailProblems, "Send mail failures: ", "Problems with sending mails were: ", report);
    return report.toString();
  }

  private void reportNumber(Number number, String numberPrefix, StringBuilder report) {
    report.append(numberPrefix).append(number).append("\n\n");
  }

  private void reportProblemsCollection(Collection<?> collection, String sizePrefix, String collectionPrefix, StringBuilder report) {
    report.append(sizePrefix).append(collection.size()).append("\n");
    reportCollectionLineByLine(collection, collectionPrefix, report);
  }

  private void reportCollectionLineByLine(Collection<?> c, String collectionPrefix, StringBuilder report) {
    if (c != null && !c.isEmpty()) {
      report.append(collectionPrefix).append("\n");
      for (Object o : c) {
        report.append("* ").append(o).append("\n");
      }
    }
    report.append("\n");
  }
  public void teacherOutputs(Map<String, TeacherOutput> teacherOutputs) {
    numMailsToSend = teacherOutputs.size();
  }

  public void incSendMailAttempt() {
    sentMailAttempt++;
  }

  public void salaryParsingError(Object[] salaryRow, Exception e) {
    salaryParsingErrors.add(SalaryParsingProblem.create(salaryRow, e));
  }

  public void teacherParsingError(Object[] teacherRow, Exception e) {
    teacherParsingErrors.add(TeacherParsingProblem.create(teacherRow, e));
  }

  public void appInput(String[] args) {
    appInput = Lists.newArrayList(args);
  }

  public void sendMailFailure(Teacher teacher, String subjectLine, String emailBodyText, Exception e) {
    sendMailProblems.add(SendMailProblem.create(teacher, e, subjectLine, emailBodyText));
  }

  public void inputFileError(String message, Exception e) {
    inputFileErrors.add(InputFileError.create(message, e));
  }

  public void unexpectedError(Throwable t) {
    unexpectedErrors.add(t);
  }
}
