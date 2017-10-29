package domain;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public class Teacher {

  private String name;
  private String email;
  private String message;

  public Teacher(String name, String email) {
    this.name = name;
    this.email = email;
  }

  public Teacher() {
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Teacher teacher = (Teacher) o;
    return Objects.equal(name, teacher.name) &&
            Objects.equal(email, teacher.email) &&
            Objects.equal(message, teacher.message);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(name, email, message);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
            .add("name", name)
            .add("email", email)
            .add("message", message)
            .toString();
  }
}
