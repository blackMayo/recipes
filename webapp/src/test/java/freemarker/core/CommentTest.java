package freemarker.core;

import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.BooleanModel;
import freemarker.template.Template;
import freemarker.template.TemplateHashModel;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.StringWriter;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * created: 6/26/14 7:41 AM
 *
 * @author jgruber
 */
public class CommentTest {


  @Test
  public void testEmpty() throws Exception {
    test("", null, null);
  }

  @Test
  public void testNull() throws Exception {
    test(null, null, null);
  }

  @Test
  public void testSimpleExample() throws Exception {
    test(" @ftlvariable name=\"self\" type=\"java.lang.String\" ", "self", String.class);
  }

  @Test
  public void testGeneric() throws Exception {
    test(" @ftlvariable name=\"obj\" type=\"java.util.List<java.lang.Integer>\" ", "obj", List.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalClass() throws Exception {
    new Comment(" @ftlvariable name=\"obj\" type=\"java.i.don.T.know.you\" ");
  }

  @Test
  public void testOtherTicks() throws Exception {
    test(" @ftlvariable name='self' type='java.lang.String' ", "self", String.class);
  }

  @Test
  public void testNoWarn() throws Exception {
    Environment e = new Environment(mock(Template.class), mock(TemplateHashModel.class), new StringWriter());
    Comment c = spy(new Comment("some text"));
    c.accept(e);
    verify(c, times(0)).warn(anyString());
  }

  @Test
  public void testWarnWrongType() throws Exception {
    TemplateHashModel hashModel = mock(TemplateHashModel.class);
    Mockito.doReturn(new BooleanModel(true, mock(BeansWrapper.class))).when(hashModel).get("self");
    Environment e = new Environment(mock(Template.class), hashModel, new StringWriter());
    Comment c = spy(new Comment("@ftlvariable name='self' type='java.lang.String'"));
    c.accept(e);
    verify(c, times(1)).warn("Variable self should be instance of class java.lang.String, but is class java.lang.Boolean at in nameless template at line 0, column 0");
  }

  @Test
  public void testParamValue() throws Exception {
    Comment c = new Comment("some text");
    assertEquals(1, c.getParameterCount());
    assertEquals("some text", c.getParameterValue(0));
    assertEquals(ParameterRole.CONTENT, c.getParameterRole(0));
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testParamValueIOOB() throws Exception {
    new Comment("some text").getParameterValue(2);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testParamRoleIOOB() throws Exception {
    new Comment("some text").getParameterRole(2);
  }

  @Test
  public void testDump() throws Exception {
    assertEquals("<#--x-->", new Comment("x").dump(true));
    assertEquals("comment \"x\"", new Comment("x").dump(false));
  }

  @Test
  public void testNodeSymbol() throws Exception {
    assertEquals("#--...--", new Comment("y").getNodeTypeSymbol());
  }

  private void test(String input, String expectedName, Class<?> expectedClass) {
    Comment c = new Comment(input);
    assertEquals(expectedName, c.ftlvariableName);
    assertEquals(expectedClass, c.ftlvariableType);
    assertEquals(input, c.text);
    assertEquals(input, c.getText());
  }

}
