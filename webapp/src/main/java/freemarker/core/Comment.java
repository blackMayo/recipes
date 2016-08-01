package freemarker.core;

import com.google.common.base.Throwables;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModelException;
import freemarker.template.utility.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Replacement of original freemarker comment implementation. This new implementation is now able to do some dynamic
 * type checking based on intellj's "@ftlvaraible" comment.
 * <br>
 * Goal: Find and reduce errors in freemarker templates.
 * <br>
 * Evil: Class replacement (no freemarker extension points available). This may lead to class loading issues.
 *
 * @author jgruber
 */
@SuppressWarnings("deprecation")
public class Comment extends TemplateElement {

  private static final Pattern FTL_VARIABLE =
      Pattern.compile("\\s*@ftlvariable\\s+name=[\"'](.+?)[\"']\\s+type=[\"'](.+?)(<.+)?[\"']\\s*");
  private static final Logger LOGGER = LoggerFactory.getLogger(Comment.class);
  private static final int VAR_NAME_INDEX = 1;
  private static final int VAR_TYPE_INDEX = 2;

  final String text;
  final String ftlvariableName;
  final Class<?> ftlvariableType;

  Comment(String text) {
    this.text = text;
    if (text != null) {
      Matcher matcher = FTL_VARIABLE.matcher(text);
      if (matcher.matches()) {
        this.ftlvariableName = matcher.group(VAR_NAME_INDEX);
        this.ftlvariableType = classOf(matcher.group(VAR_TYPE_INDEX));
        return;
      }
    }
    this.ftlvariableName = null;
    this.ftlvariableType = null;
  }

  @Override
  TemplateElement[] accept(Environment environment) throws TemplateException, IOException {
    if (ftlvariableName != null) {
      checkFtlVariable(environment);
    }
    return new TemplateElement[0];
  }

  private void checkFtlVariable(Environment env) {
    try {
      Object value = env.__getitem__(ftlvariableName);
      if (value != null && !ftlvariableType.isInstance(value)) {
        warn("Variable " + ftlvariableName + " should be instance of " + ftlvariableType
            + ", but is " + value.getClass()
            + " at " + getStartLocation()
        );
      }
    } catch (TemplateModelException e) {
      throw Throwables.propagate(e);
    }
  }


  void warn(String msg) {
    LOGGER.error(msg);
  }

  @Override
  protected String dump(boolean canonical) {
    if (canonical) {
      return "<#--" + text + "-->";
    } else {
      return "comment " + StringUtil.jQuote(text.trim());
    }
  }

  @Override
  String getNodeTypeSymbol() {
    return "#--...--";
  }

  @Override
  int getParameterCount() {
    return 1;
  }

  @Override
  Object getParameterValue(int idx) {
    if (idx != 0) {
      throw new IndexOutOfBoundsException();
    }
    return text;
  }

  @Override
  ParameterRole getParameterRole(int idx) {
    if (idx != 0) {
      throw new IndexOutOfBoundsException();
    }
    return ParameterRole.CONTENT;
  }

  public String getText() {
    return text;
  }

  @SuppressWarnings("unchecked")
  private static Class<Object> classOf(String className) {
    try {
      return (Class<Object>) Class.forName(className);
    } catch (ClassNotFoundException e) {
      throw new IllegalArgumentException("Unable to compile template", e);
    }
  }

  @Override
  boolean isNestedBlockRepeater() {
    return false;
  }
}
