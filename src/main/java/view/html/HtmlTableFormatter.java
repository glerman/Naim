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

import static j2html.TagCreator.body;
import static j2html.TagCreator.each;
import static j2html.TagCreator.head;
import static j2html.TagCreator.html;
import static j2html.TagCreator.meta;
import static j2html.TagCreator.style;
import static j2html.TagCreator.table;
import static j2html.TagCreator.tr;


public class HtmlTableFormatter {

  private final Function<SalaryInfo, List<Object>> salaryInfoToOutputRow;

  public HtmlTableFormatter() {
    salaryInfoToOutputRow = new SalaryInfoToOutputRow();
  }

  public String toHtml(Collection<Collection<SalaryInfo>> salariesPerClass, String[] columnNames) {
    String html = builtHtml(salariesPerClass, Lists.newArrayList(columnNames));
    String inlined = HtmlUtils.inline(html);
    return HtmlUtils.encodeHebrewWithEntities(inlined);
  }

  private String builtHtml(Collection<Collection<SalaryInfo>> salariesPerClass, List<String> columnNames) {

    return html(
              meta().attr(Attr.CHARSET, "UTF-8"),
              head(style("table, th, td {\n" +
                              "            border: 1px solid black;\n" +
                              "            text-align: right;\n" +
                              "        }")),
              body(each(salariesPerClass,
                      classSalaries -> tableTagWithColumnAndDataRows(classSalaries, columnNames)))
            ).attr(Attr.LANG, "he").
      renderFormatted();
  }

  private ContainerTag tableTagWithColumnAndDataRows(Collection<SalaryInfo> teacherSalaries, List<String> columnNamesList) {
    return table(
            tr(each(columnNamesList, TagCreator::th)),
            each(teacherSalaries, salaryInfo -> {
              DomContent tds = each(salaryInfoToOutputRow.apply(salaryInfo),
                      cellDataObject -> TagCreator.td(cellDataObject.toString()));
              return tr(tds);
            })
    ).attr(Attr.STYLE, "width: 100%;margin-bottom: 30px");
  }
}
