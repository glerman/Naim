package view.html;

import org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.util.StringTokenizer;

class HtmlUtils {

  private final static String style = "style";
  private static final String delims = "{}";

  static String inline(final String html) {
    Document doc = Jsoup.parse(html);

    Elements els = doc.select(style);// to get all the style elements
    for (Element e : els) {
      String styleRules = e.getAllElements().get(0).data().replaceAll(
              "\n", "").trim();
      StringTokenizer st = new StringTokenizer(styleRules, delims);
      while (st.countTokens() > 1) {
        String selector = st.nextToken();
        String properties = st.nextToken();
        Elements selectedElements = doc.select(selector);
        for (Element selElem : selectedElements) {
          String oldProperties = selElem.attr(style);
          selElem.attr(style,
                  oldProperties.length() > 0 ?
                          concatenateProperties(oldProperties, properties) :
                          properties);
        }
      }
      e.remove();
    }
    return doc.html();
  }

  static String encodeHebrewWithEntities(String html) {
    Document doc = Jsoup.parse(html);
    doc.select("th, td").forEach(element -> {
      TextNode textNode = (TextNode) element.childNodes().get(0);
      String text = textNode.text();
      String convertedText = convertHebrewText(text);
      textNode.text(convertedText);
    });
    return doc.html().replaceAll("&amp;", "&");
  }

  private static String convertHebrewText(String text) {
    StringBuilder textConversionBuilder = new StringBuilder(text.length());
    text.chars().forEach(codePoint -> {
      String unicodeStr = new String(Character.toChars(codePoint));
      if (isHebrewChar(codePoint)) {
        textConversionBuilder.append(StringEscapeUtils.escapeHtml(unicodeStr));
      } else {
        textConversionBuilder.append(unicodeStr);
      }
    });
    return textConversionBuilder.toString();
  }

  private static boolean isHebrewChar(int codePoint) {
    return codePoint <= (int) 'ת' && codePoint >= (int)'א';
  }

  private static String concatenateProperties(String oldProp, String newProp) {
    oldProp = oldProp.trim();
    if (!newProp.endsWith(";"))
      newProp += ";";
    return newProp + oldProp; // The existing (old) properties should take precedence.
  }
}
