package view.html;

import com.google.common.collect.Lists;
import domain.SalaryInfo;
import j2html.TagCreator;
import j2html.attributes.Attr;
import j2html.tags.DomContent;
import view.SalaryInfoToOutputRow;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import static j2html.TagCreator.*;

public class HtmlTableFormatter {

  private static final String tableCaption = "דו״ח שיעורים";

  private final Function<SalaryInfo, List<Object>> salaryInfoToOutputRow;

  public HtmlTableFormatter() {
    salaryInfoToOutputRow = new SalaryInfoToOutputRow().
            andThen(salaryRow -> Lists.reverse(Lists.newArrayList(salaryRow)));//todo: ugly reversal of data to fit column names
  }

  public String toHtml(Collection<SalaryInfo> teacherSalaries, String[] columnNames) {
    List<String> columnNamesList = Lists.newArrayList(columnNames);

    return
            html(
              head(
                      style("table, th, td {\n" +
                              "            border: 1px solid black;\n" +
                              "            text-align: right;\n" +
                              "        }\n" +
                              "        caption {\n" +
                              "            display: table-caption;\n" +
                              "            text-align: right;\n" +
                              "        }")
              ),
              body(
                      table(caption(tableCaption),
                            tr(each(columnNamesList, TagCreator::th)),
                            each(teacherSalaries, salaryInfo -> {
                              DomContent tds = each(salaryInfoToOutputRow.apply(salaryInfo), cellDataObject -> TagCreator.td(cellDataObject.toString()));
                              return tr(tds);
                            })
                      ).attr(Attr.STYLE, "width:100%").attr(Attr.ALIGN, "right")
              )
            ).renderFormatted();
  }
}
