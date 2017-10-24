package view.texttable;

import dnl.utils.text.table.TextTable;
import domain.SalaryInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import view.ViewTestDataGenerator;

import javax.swing.table.TableModel;
import javax.swing.text.View;
import java.util.ArrayList;

@Ignore
public class SalariesTextTableFormatterTest {

  private SalariesTextTableFormatter formatter;

  @Before
  public void setUp() throws Exception {
    formatter = new SalariesTextTableFormatter();
  }

  @Test
  public void test() throws Exception {

    TextTable textTable = formatter.toTextTable(ViewTestDataGenerator.salaries, ViewTestDataGenerator.columnNames);
    TableModel tableModel = textTable.getTableModel();

    //check output dimensions
    Assert.assertEquals(ViewTestDataGenerator.columnNames.length, tableModel.getColumnCount());
    Assert.assertEquals(ViewTestDataGenerator.salaries.size(), tableModel.getRowCount());

    //check output values
    for (int rowIndex = 0; rowIndex < ViewTestDataGenerator.salaries.size(); rowIndex++) {
      Object className = tableModel.getValueAt(rowIndex, 0);
      Object room = tableModel.getValueAt(rowIndex, 1);
      Object date = tableModel.getValueAt(rowIndex, 2);
      Object attendees = tableModel.getValueAt(rowIndex, 3);
      Object rate = tableModel.getValueAt(rowIndex, 4);

      SalaryInfo salaryInfo = ViewTestDataGenerator.salaries.get(rowIndex);
      Assert.assertEquals(salaryInfo.getClassName(), className);
      Assert.assertEquals(salaryInfo.getRoom(), room);
      Assert.assertEquals(salaryInfo.getDate(), date);
      Assert.assertEquals(salaryInfo.getAttendees(), attendees);
      Assert.assertEquals(salaryInfo.getPayment(), rate);
    }
    textTable.printTable();
  }
}
