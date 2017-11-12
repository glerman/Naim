package view.html;

import com.google.common.collect.Lists;
import domain.SalaryInfo;
import j2html.TagCreator;
import j2html.attributes.Attr;
import j2html.tags.ContainerTag;
import j2html.tags.DomContent;
import view.SalaryInfoToOutputRow;
import view.SentenceContainer;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static j2html.TagCreator.body;
import static j2html.TagCreator.br;
import static j2html.TagCreator.each;
import static j2html.TagCreator.head;
import static j2html.TagCreator.html;
import static j2html.TagCreator.meta;
import static j2html.TagCreator.p;
import static j2html.TagCreator.style;
import static j2html.TagCreator.table;
import static j2html.TagCreator.text;
import static j2html.TagCreator.tr;

public class HtmlOutputFormatter {

  private static final String[] OUTPUT_COLUMNS = {"תעריף","משתתפים","תאריך","אולם","כיתה"};

  private final Function<String, String> htmlTransformer;
  private final Function<SalaryInfo, List<Object>> salaryInfoToOutputRow;

  public HtmlOutputFormatter(Function<String, String> htmlTransformer) {
    this.htmlTransformer = htmlTransformer;
    salaryInfoToOutputRow = new SalaryInfoToOutputRow();
  }


  private DomContent generateClassTables(Collection<Collection<SalaryInfo>> salariesPerClass, List<String> columnNames) {
    return each(salariesPerClass, classSalaries -> generateClassTable(classSalaries, columnNames));
  }

  private String builtEntireHtml(Collection<Collection<SalaryInfo>> salariesPerClass, List<String> columnNames,
                                 SentenceContainer formatMailHeader, SentenceContainer formatMailFooter) {
    return html(
            meta().attr(Attr.CHARSET, "UTF-8"),
            head(
                    style("table, th, td {border: 1px solid black;text-align: right;}").withText("p {text-align: right;}")),
            body(
                    p(sentenceContainerToDomArray(formatMailHeader)),
                    generateClassTables(salariesPerClass, columnNames),
                    p(sentenceContainerToDomArray(formatMailFooter))
            )
    ).attr(Attr.LANG, "he").
            renderFormatted();
  }

  private DomContent[] sentenceContainerToDomArray(SentenceContainer sentenceContainer) {
    List<DomContent> collect = sentenceContainer.sentences().stream().
            map(sentence -> Lists.newArrayList(text(sentence), br())).
            flatMap(Collection::stream).
            collect(Collectors.toList());
    return collect.toArray(new DomContent[collect.size()]);
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

  public String toEntireHtml(Collection<Collection<SalaryInfo>> salariesPerClass, SentenceContainer formatMailHeader, SentenceContainer formatMailFooter) {
    String html = builtEntireHtml(salariesPerClass, Lists.newArrayList(OUTPUT_COLUMNS), formatMailHeader, formatMailFooter);
    return htmlTransformer.apply(html);
  }

}
