package logic;

import com.google.common.collect.Sets;

import java.util.Set;

public class ProblemTracker {

  private final Set<String> teachersWithoutEmail;

  public ProblemTracker() {
    this.teachersWithoutEmail = Sets.newHashSet();
  }

  public void addTeacherWithoutEmail(final String teacherName) {
    teachersWithoutEmail.add(teacherName);
  }

  public void appendReport(final StringBuilder report) {

    report.append("Number of teachers without emails: ").append(teachersWithoutEmail.size());
    if (!teachersWithoutEmail.isEmpty()) {
      report.append("The teachers without emails are: ").append(teachersWithoutEmail);
    }
  }
}
