package view.texttable;

import com.google.common.collect.Lists;
import dnl.utils.text.table.TextTable;
import domain.SalaryInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.swing.table.TableModel;
import java.util.ArrayList;

public class SalariesTextTableFormatterTest {


  private SalariesTextTableFormatter formatter;
  private ArrayList<SalaryInfo> salaryInfos;
  private String[] columnNames = {"כיתה", "אולם", "תאריך", "משתתפים", "תעריף"};

  @Before
  public void setUp() throws Exception {
    formatter = new SalariesTextTableFormatter();

    SalaryInfo salaryInfo1 = new SalaryInfo(1, "teacher1", "class1", "room1", "01/09", 5, 80, null);
    SalaryInfo salaryInfo2 = new SalaryInfo(2, "teacher2", "class2", "room2", "02/09", 6, 90, null);
    SalaryInfo salaryInfo3 = new SalaryInfo(3, "teacher3", "class3", "room3", "03/09", 7, 100, null);
    salaryInfos = Lists.newArrayList(salaryInfo1, salaryInfo2, salaryInfo3);
  }

  @Test
  public void test() throws Exception {

    TextTable textTable = formatter.toTextTable(salaryInfos, columnNames);
    TableModel tableModel = textTable.getTableModel();

    //check output dimensions
    Assert.assertEquals(columnNames.length, tableModel.getColumnCount());
    Assert.assertEquals(salaryInfos.size(), tableModel.getRowCount());

    //check output values
    for (int rowIndex = 0; rowIndex < salaryInfos.size(); rowIndex++) {
      Object className = tableModel.getValueAt(rowIndex, 0);
      Object room = tableModel.getValueAt(rowIndex, 1);
      Object date = tableModel.getValueAt(rowIndex, 2);
      Object attendees = tableModel.getValueAt(rowIndex, 3);
      Object rate = tableModel.getValueAt(rowIndex, 4);

      SalaryInfo salaryInfo = salaryInfos.get(rowIndex);
      Assert.assertEquals(salaryInfo.getClassName(), className);
      Assert.assertEquals(salaryInfo.getRoom(), room);
      Assert.assertEquals(salaryInfo.getDate(), date);
      Assert.assertEquals(salaryInfo.getAttendees(), attendees);
      Assert.assertEquals(salaryInfo.getPayment(), rate);
    }
  }
}