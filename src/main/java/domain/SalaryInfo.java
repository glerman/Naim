package domain;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public class SalaryInfo {

  private int teacherId;
  private String className;
  private String room;
  private String date;
  private int attendees;
  private double ratePerAttendee;
  private Object[] salaryRow;

  public SalaryInfo(int teacherId, String className, String room,
                    String date, int attendees, double ratePerAttendee, Object[] salaryRow) {
    this.teacherId = teacherId;
    this.className = className;
    this.room = room;
    this.date = date;
    this.attendees = attendees;
    this.ratePerAttendee = ratePerAttendee;
    this.salaryRow = salaryRow;
  }

  public SalaryInfo() {
  }

  public Object[] getSalaryRow() {
    return salaryRow;
  }

  public void setSalaryRow(Object[] salaryRow) {
    this.salaryRow = salaryRow;
  }

  public int getTeacherId() {
    return teacherId;
  }

  public void setTeacherId(int teacherId) {
    this.teacherId = teacherId;
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public String getRoom() {
    return room;
  }

  public void setRoom(String room) {
    this.room = room;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public int getAttendees() {
    return attendees;
  }

  public void setAttendees(int attendees) {
    this.attendees = attendees;
  }

  public double getRatePerAttendee() {
    return ratePerAttendee;
  }

  public void setRatePerAttendee(double ratePerAttendee) {
    this.ratePerAttendee = ratePerAttendee;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
            .add("teacherId", teacherId)
            .add("className", className)
            .add("room", room)
            .add("date", date)
            .add("attendees", attendees)
            .add("ratePerAttendee", ratePerAttendee)
            .add("salaryRow", salaryRow)
            .toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SalaryInfo that = (SalaryInfo) o;
    return teacherId == that.teacherId &&
            attendees == that.attendees &&
            Double.compare(that.ratePerAttendee, ratePerAttendee) == 0 &&
            Objects.equal(className, that.className) &&
            Objects.equal(room, that.room) &&
            Objects.equal(date, that.date) &&
            Objects.equal(salaryRow, that.salaryRow);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(teacherId, className, room, date, attendees, ratePerAttendee, salaryRow);
  }
}
