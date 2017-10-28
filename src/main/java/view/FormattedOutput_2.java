package view;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class FormattedOutput_2 {

  public abstract String subject();
  public abstract String header();
  public abstract String salaryTablesHtml();
  public abstract String footer();
  public abstract String entireHtml(); //header+table+footer

  public static FormattedOutput_2 create(String subject, String header, String salaryTablesHtml, String footer, String entireHtml) {
    return new AutoValue_FormattedOutput_2(subject, header, salaryTablesHtml, footer, entireHtml);
  }

}
