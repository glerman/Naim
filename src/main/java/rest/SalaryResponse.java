package rest;

public class SalaryResponse {

  private String salariesPreview;
  private String report;

  public SalaryResponse(String salariesPreview, String report) {
    this.salariesPreview = salariesPreview;
    this.report = report;
  }

  //Serialization ctor
  public SalaryResponse() {
  }

  public String getSalariesPreview() {
    return salariesPreview;
  }

  public void setSalariesPreview(String salariesPreview) {
    this.salariesPreview = salariesPreview;
  }

  public String getReport() {
    return report;
  }

  public void setReport(String report) {
    this.report = report;
  }
}
