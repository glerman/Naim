package view;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class FormattedOutput {

  public abstract String subject();
  public abstract String entireHtml(); //header+table+footer

  public static FormattedOutput create(String subject, String entireHtml) {
    return new AutoValue_FormattedOutput(subject, entireHtml);
  }
}
