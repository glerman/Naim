package domain;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public class Teacher {

  private int id;
  private String name;
  private String email;

  public Teacher(int id, String name, String email) {
    this.id = id;
    this.name = name;
    this.email = email;
  }

  public Teacher() {
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
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
            .add("id", id)
            .add("name", name)
            .add("email", email)
            .toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Teacher teacher = (Teacher) o;
    return id == teacher.id &&
            Objects.equal(name, teacher.name) &&
            Objects.equal(email, teacher.email);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id, name, email);
  }
}
