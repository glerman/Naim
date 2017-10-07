package view.html;

import com.google.common.collect.Lists;
import domain.SalaryInfo;
import j2html.TagCreator;
import j2html.attributes.Attr;
import j2html.tags.ContainerTag;
import j2html.tags.DomContent;
import view.SalaryInfoToOutputRow;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import static j2html.TagCreator.*;


//todo: hebrew table text in gmails show up as ???
//todo: need to find a lib to inline the css before sending the mail
//todo: make sure the data and columns are aligned
public class HtmlTableFormatter {

  private final Function<SalaryInfo, List<Object>> salaryInfoToOutputRow;

  public HtmlTableFormatter() {
    salaryInfoToOutputRow = new SalaryInfoToOutputRow().
            andThen(salaryRow -> Lists.reverse(Lists.newArrayList(salaryRow)));//todo: ugly reversal of data to fit column names
  }

  public String toHtml(Collection<Collection<SalaryInfo>> salariesPerClass, String[] columnNames) {
    List<String> columnNamesList = Lists.newArrayList(columnNames);

    return html(
              meta().attr(Attr.CHARSET, "UTF-8"),
              head(style("table, th, td {\n" +
                              "            border: 1px solid black;\n" +
                              "            text-align: right;\n" +
                              "        }")),
              body(each(salariesPerClass,
                      classSalaries -> tableTagWithColumnAndDataRows(classSalaries, columnNamesList)))
            ).attr(Attr.LANG, "he").
      renderFormatted();
  }

  private ContainerTag tableTagWithColumnAndDataRows(Collection<SalaryInfo> teacherSalaries, List<String> columnNamesList) {
    return table(
            tr(each(columnNamesList, TagCreator::th)),
            each(teacherSalaries, salaryInfo -> {
              DomContent tds = each(salaryInfoToOutputRow.apply(salaryInfo), cellDataObject -> TagCreator.td(cellDataObject.toString()));
              return tr(tds);
            })
    ).attr(Attr.STYLE, "width: 100%;margin-bottom: 30px");
  }
}
