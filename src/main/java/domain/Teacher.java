package domain;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public class Teacher {

  private String name;
  private String email;

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

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
            .add("name", name)
            .add("email", email)
            .toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Teacher teacher = (Teacher) o;
    return Objects.equal(name, teacher.name) &&
            Objects.equal(email, teacher.email);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(name, email);
  }
}
