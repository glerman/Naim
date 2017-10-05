package logic;

import com.google.common.collect.Sets;
import domain.TeacherOutput;

import java.util.Map;
import java.util.Set;

public class ReportAggregator {

  public static final ReportAggregator instance = new ReportAggregator();

  private final Set<String> teachersWithoutEmail;
  private int numMailsToSend;
  private int sentMailAttempt;

  private ReportAggregator() {
    this.teachersWithoutEmail = Sets.newHashSet();
  }

  public void addTeacherWithoutEmail(final String teacherName) {
    teachersWithoutEmail.add(teacherName);
  }

  public String report() {
    StringBuilder report = new StringBuilder();

    report.append("Total mails to send: ").append(numMailsToSend).append("\n");
    report.append("Send mail attempts: ").append(sentMailAttempt).append("\n");
    report.append("Number of teachers without emails: ").append(teachersWithoutEmail.size()).append("\n");
    if (!teachersWithoutEmail.isEmpty()) {
      report.append("The teachers without emails are: ").append(teachersWithoutEmail).append("\n");
    }
    return report.toString();
  }

  public void teacherOutputs(Map<String, TeacherOutput> teacherOutputs) {
    numMailsToSend = teacherOutputs.size();
  }

  public void incSendMailAttempt() {
    sentMailAttempt++;
  }

  public void salaryParsingError(Object[] salaryRow, Exception e) {
//    salaryParsingErrors.add(Pair)
  }
}
