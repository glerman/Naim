package report;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import domain.Teacher;
import domain.TeacherOutput;
import report.pojo.GeneralProblem;
import report.pojo.DomainObjectParsingProblem;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

public class ReportAggregator {

  public static final ReportAggregator instance = new ReportAggregator();

  final Set<String> teachersWithoutEmail;
  final Set<DomainObjectParsingProblem> salaryParsingErrors;
  final Set<DomainObjectParsingProblem> teacherParsingErrors;
  int numMailsToSend;
  int sentMailAttempt;
  private List<String> appInput;
  private List<String> userInputErrors;
  private Set<GeneralProblem> sendMailProblems;
  private Set<GeneralProblem> generalProblems;
  private Set<GeneralProblem> formattingErrors;
  private Set<Throwable> unexpectedErrors;

  private ReportAggregator() {
    teachersWithoutEmail = Sets.newHashSet();
    salaryParsingErrors = Sets.newHashSet();
    teacherParsingErrors = Sets.newHashSet();
    sendMailProblems = Sets.newHashSet();
    generalProblems = Sets.newHashSet();
    unexpectedErrors = Sets.newHashSet();
    formattingErrors = Sets.newHashSet();
    userInputErrors = Lists.newArrayList();
  }

  public void addTeacherWithoutEmail(final String teacherName) {
    teachersWithoutEmail.add(teacherName);
  }

  public String report() {
    StringBuilder report = new StringBuilder();

    reportCollectionLineByLine(appInput, "App input: ", report);
    reportProblemsCollection(userInputErrors, "User input errors: ", "User input errors were: ", report);
    reportProblemsCollection(unexpectedErrors, "Unexpected errors: ", "Unexpected errors were: ", report);
    reportProblemsCollection(generalProblems, "Input/Output errors: ", "Input/Output errors are: ", report);
    reportProblemsCollection(salaryParsingErrors, "Salary parsing errors: ", "The salary parsing errors were: ", report);
    reportProblemsCollection(teacherParsingErrors, "Teacher parsing errors: ", "The teacher parsing errors were: ", report);
    reportNumber(numMailsToSend, "Total mails to send: ", report);
    reportProblemsCollection(teachersWithoutEmail, "Number of teachers without emails (send wasn't attempted): ", "The teachers without emails are: ", report);
    reportNumber(sentMailAttempt, "Send mail attempts: ", report);
    reportProblemsCollection(sendMailProblems, "Send mail failures: ", "Problems with sending mails were: ", report);
    reportProblemsCollection(formattingErrors, "Formatting errors: ", "Formatting erros were: ", report);
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
        final String objectString;
        if (o instanceof Throwable) {
          ByteArrayOutputStream out = new ByteArrayOutputStream();
          ((Throwable) o).printStackTrace(new PrintStream(out));
          objectString = out.toString();
        } else {
          objectString = o.toString();
        }
        report.append("* ").append(objectString).append("\n");
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
    salaryParsingErrors.add(DomainObjectParsingProblem.create(salaryRow, e));
  }

  public void teacherParsingError(Object[] teacherRow, Exception e) {
    teacherParsingErrors.add(DomainObjectParsingProblem.create(teacherRow, e));
  }

  public void appInput(String[] args) {
    appInput = Lists.newArrayList(args);
  }

  public void sendMailFailure(String message, Teacher teacher, String subjectLine, Exception e) {
    StringBuilder sb = new StringBuilder();
    sb.append(message).append(" ").append(teacher).append(" ").append(subjectLine).append(" ").append(e);
    String extendedErrorMessage = sb.toString();
    sendMailProblems.add(GeneralProblem.create(extendedErrorMessage, e));
  }

  public void ioError(String message, Exception e) {
    generalProblems.add(GeneralProblem.create(message, e));
  }

  public void userInputError(String message) {
    userInputErrors.add(message);
  }

  public void unexpectedError(Throwable t) {
    unexpectedErrors.add(t);
  }

  public void formattingError(String teacherName, Exception e) {
    formattingErrors.add(GeneralProblem.create(teacherName, e));
  }
}
