package view;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class FormattedOutput_2 {

  public abstract String subject();
  public abstract SentenceContainer header();
  public abstract String salaryTablesHtml();
  public abstract SentenceContainer footer();
  public abstract String entireHtml(); //header+table+footer

  public static FormattedOutput_2 create(String subject, SentenceContainer header, String salaryTablesHtml, SentenceContainer footer, String entireHtml) {
    return new AutoValue_FormattedOutput_2(subject, header, salaryTablesHtml, footer, entireHtml);
  }
}
