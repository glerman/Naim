package domain;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public class Message {

  private String teacherName;
  private String message;

  public Message(String teacherName, String message) {
    this.teacherName = teacherName;
    this.message = message;
  }

  public Message() {
  }

  public String getTeacherName() {
    return teacherName;
  }

  public void setTeacherName(String teacherName) {
    this.teacherName = teacherName;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
            .add("teacherName", teacherName)
            .add("message", message)
            .toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Message message1 = (Message) o;
    return Objects.equal(teacherName, message1.teacherName) &&
            Objects.equal(message, message1.message);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(teacherName, message);
  }
}
