package report;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import domain.Teacher;
import domain.TeacherOutput;
import report.pojo.SalaryParsingProblem;
import report.pojo.SendMailProblem;
import report.pojo.TeacherParsingProblem;

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

  private ReportAggregator() {
    teachersWithoutEmail = Sets.newHashSet();
    salaryParsingErrors = Sets.newHashSet();
    teacherParsingErrors = Sets.newHashSet();
    sendMailProblems = Sets.newHashSet();
  }

  public void addTeacherWithoutEmail(final String teacherName) {
    teachersWithoutEmail.add(teacherName);
  }

  public String report() {
    StringBuilder report = new StringBuilder();

    reportCollectionLineByLine(report, "App input: ", appInput);
    reportProblemsCollection(report, "Salary parsing errors: ", "The salary parsing errors were: ", salaryParsingErrors);
    reportProblemsCollection(report, "Teacher parsing errors: ", "The teacher parsing errors were: ", teacherParsingErrors);
    reportNumber(report, "Total mails to send: ", numMailsToSend);
    reportProblemsCollection(report, "Number of teachers without emails (send wasn't attempted): ", "The teachers without emails are: ", teachersWithoutEmail);
    reportNumber(report, "Send mail attempts: ", sentMailAttempt);
    reportProblemsCollection(report, "Send mail failures: ", "Problems with sending mails were: ", sendMailProblems);
    return report.toString();
  }

  private void reportNumber(StringBuilder report, String numberPrefix, Number number) {
    report.append(numberPrefix).append(number).append("\n");
  }

  private void reportProblemsCollection(StringBuilder report, String sizePrefix, String collectionPrefix, Collection<?> collection) {
    report.append(sizePrefix).append(collection.size()).append("\n");
    reportCollectionLineByLine(report, collectionPrefix, collection);
  }

  private void reportCollectionLineByLine(StringBuilder report, String collectionPrefix, Collection<?> c) {
    if (c == null || c.isEmpty()) {
      return;
    }
    report.append(collectionPrefix).append("\n");
    for (Object o : c) {
      report.append(o).append("\n");
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
}
