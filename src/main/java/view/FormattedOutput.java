package view;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class FormattedOutput {

  public abstract String subject();
  public abstract String header();
  public abstract String salaryTablesHtml();
  public abstract String footer();

  public static FormattedOutput create(String subject, String header, String salaryTablesHtml, String footer) {
    return new AutoValue_FormattedOutput(subject, header, salaryTablesHtml, footer);
  }


}
