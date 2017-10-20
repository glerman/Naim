package view.html;

import org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import sun.text.normalizer.UnicodeSet;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.atomic.AtomicInteger;

public class InlineCss {

  private final static String style = "style";
  private static final String delims = "{}";

  public static String inline(final String html) {
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

  public static String encodeHebrewWithEntities(String html) {
    Document doc = Jsoup.parse(html);
//    AtomicInteger i = new AtomicInteger();
    doc.select("th, td").forEach(element -> {
      //todo: sometimes no child ???
      TextNode textNode = (TextNode) element.childNode(0);
      String text = textNode.text();
      String convertedText = convertHebrewText(text);
      textNode.text("");
      textNode.attr("data", convertedText);
//      i.incrementAndGet();
    });
    return doc.html();
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
