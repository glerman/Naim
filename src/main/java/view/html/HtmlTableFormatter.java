package view.html;

import com.google.common.collect.Lists;
import domain.SalaryInfo;
import j2html.TagCreator;

import java.util.Collection;
import java.util.List;

import static j2html.TagCreator.*;

public class HtmlTableFormatter {

  private static final String tableCaption = "דו״ח שיעורים";

  String toHtml(Collection<SalaryInfo> teacherSalaries, String[] columnNames) {
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
                      table(text("style=\"width:100%\""),
                            caption(tableCaption),
                            tr(each(columnNamesList, TagCreator::text))
                      )

              )
            ).renderFormatted();
    /*
    StringBuilder htmlBuilder = new StringBuilder();
    htmlBuilder.append("<!DOCTYPE html>").
            append("<html>").
            append("<head>").
              append("<style>").
                append("table, th, td ").append("{").
                append("boarder: 1px solid black;").
                append("text-align: right;").
                append("}").
                append("caption {").
                append("display: table-caption;").
                append("text-align: right;").
                append("}").
              append("</style>").
              append("</head>").
            append("<body>").
              append("<table style=\"width:100%\">").
                append("<caption>").append(tableCaption).append("</caption>").
                append("<tr>");
    Lists.newArrayList(columnNames).forEach(columnName -> htmlBuilder.append("<th>").append(columnName).append("</th>"));
                htmlBuilder.append("</tr>").
                        append("</table>").
                        append("</body>").
                        append("</html>");



    return htmlBuilder.toString();
    */
  }

  /*
  <!DOCTYPE html>
<html>
<head>
    <style>
        table, th, td {
            border: 1px solid black;
            text-align: right;
        }
        caption {
            display: table-caption;
            text-align: right;
        }
    </style>
</head>
<body>

<table style="width:100%">
    <caption>דו״ח שיעורים</caption>
    <tr>
        <th>גיל</th>
        <th>שם משפחה</th>
        <th>שם פרטי</th>
    </tr>
    <tr>
        <td>50</td>
        <td>משפחה2</td>
        <td>שם1</td>
    </tr>
    <tr>
        <td>94</td>
        <td>משפחה2</td>
        <td>שם2</td>
    </tr>
    <tr>
        <td>80</td>
        <td>משפחה3</td>
        <td>שם3</td>
    </tr>
</table>

</body>
</html>

   */
}
