package view.html;

import org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import javax.validation.constraints.Null;
import java.util.StringTokenizer;

public class HtmlUtils {

  private final static String style = "style";
  private static final String delims = "{}";

  public static String prepareForEmail(final String html, String tagsToSearch) {
    Document doc = htmlToDocument(html);

    inlineCss(doc);
    encodeHebrewToEntities(doc, tagsToSearch);
    return toHtml(doc);
  }

  private static void inlineCss(Document doc) {
    Elements els = doc.select(style);// to get all the style elements
    for (Element e : els) {
      String styleRules = e.getAllElements().get(0).data().replaceAll("\n", "").trim();
      StringTokenizer st = new StringTokenizer(styleRules, delims);
      while (st.countTokens() > 1) {
        String selector = st.nextToken();
        String properties = st.nextToken();
        Elements selectedElements = doc.select(selector);
        for (Element selElem : selectedElements) {
          String oldProperties = selElem.attr(style);
          selElem.attr(style, oldProperties.length() > 0 ?
                  concatenateProperties(oldProperties, properties) :
                  properties);
        }
      }
      e.remove();
    }
  }

  private static String toHtml(Document doc) {
    String s = doc.html().replaceAll("\\\\n", "\n");
    return s.replaceAll("&amp;", "&");
  }

  private static Document htmlToDocument(String html) {
    if (html == null) {
      throw new NullPointerException();
    }
    Document doc = Jsoup.parse(html);
    doc.outputSettings().prettyPrint(false); //makes html() preserve linebreaks and spacing
    doc.select("br").append("\\n");
    doc.select("p").prepend("\\n\\n");
    return doc;
  }

  private static void encodeHebrewToEntities(Document doc, String tagsToSearch) {
    doc.select(tagsToSearch).forEach(element -> {
      element.textNodes().forEach(textNode -> {
        String text = textNode.text();
        String convertedText = convertHebrewText(text);
        textNode.text(convertedText);
      });
    });
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
