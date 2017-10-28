package view;

import com.google.auto.value.AutoValue;

import java.util.List;

@AutoValue
public abstract class SentenceContainer {

  public abstract List<String> sentences();

  public static SentenceContainer create(List<String> sentences) {
    return new AutoValue_SentenceContainer(sentences);
  }


}
