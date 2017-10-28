package view.html;

import com.google.common.collect.Lists;
import domain.SalaryInfo;
import j2html.TagCreator;
import j2html.attributes.Attr;
import j2html.tags.ContainerTag;
import j2html.tags.DomContent;
import view.SalaryInfoToOutputRow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import static j2html.TagCreator.body;
import static j2html.TagCreator.each;
import static j2html.TagCreator.head;
import static j2html.TagCreator.html;
import static j2html.TagCreator.meta;
import static j2html.TagCreator.p;
import static j2html.TagCreator.style;
import static j2html.TagCreator.table;
import static j2html.TagCreator.tr;


public class HtmlTableFormatter {

  private static final String[] OUTPUT_COLUMNS = {"תעריף","משתתפים","תאריך","אולם","כיתה"};

  private final Function<SalaryInfo, List<Object>> salaryInfoToOutputRow;

  public HtmlTableFormatter() {
    salaryInfoToOutputRow = new SalaryInfoToOutputRow();
  }

  public String toHtml(Collection<Collection<SalaryInfo>> salariesPerClass) {
    String html = builtHtml(salariesPerClass, Lists.newArrayList(OUTPUT_COLUMNS));
    return HtmlUtils.prepareForEmail(html, "th, td");
  }

  private String builtHtml(Collection<Collection<SalaryInfo>> salariesPerClass, List<String> columnNames) {

    return html(
              meta().attr(Attr.CHARSET, "UTF-8"),
              head(style("table, th, td {\n" +
                              "            border: 1px solid black;\n" +
                              "            text-align: right;\n" +
                              "        }")),
              body(generateClassTables(salariesPerClass, columnNames))
            ).attr(Attr.LANG, "he").
      renderFormatted();
  }

  private DomContent generateClassTables(Collection<Collection<SalaryInfo>> salariesPerClass, List<String> columnNames) {
    return each(salariesPerClass, classSalaries -> generateClassTable(classSalaries, columnNames));
  }

  private String builtEntireHtml(Collection<Collection<SalaryInfo>> salariesPerClass, ArrayList<String> columnNames, String formatMailHeader, String formatMailFooter) {
    return html(
            meta().attr(Attr.CHARSET, "UTF-8"),
            head(
                    style("table, th, td {border: 1px solid black;text-align: right;}").withText("p {text-align: right;}")),
            body(
                    p(formatMailHeader),
                    generateClassTables(salariesPerClass, columnNames),
                    p(formatMailFooter)
            )
    ).attr(Attr.LANG, "he").
            renderFormatted();
  }

  private ContainerTag generateClassTable(Collection<SalaryInfo> teacherSalaries, List<String> columnNamesList) {
    return table(
            tr(each(columnNamesList, TagCreator::th)),
            each(teacherSalaries, salaryInfo -> {
              DomContent tds = each(salaryInfoToOutputRow.apply(salaryInfo),
                      cellDataObject -> TagCreator.td(cellDataObject.toString()));
              return tr(tds);
            })
    ).attr(Attr.STYLE, "width: 100%;margin-bottom: 30px");
  }

  public String toEntireHtml(Collection<Collection<SalaryInfo>> salariesPerClass, String formatMailHeader, String formatMailFooter) {
    String html = builtEntireHtml(salariesPerClass, Lists.newArrayList(OUTPUT_COLUMNS), formatMailHeader, formatMailFooter);
    return HtmlUtils.prepareForEmail(html, "th, td, p");
  }
}
