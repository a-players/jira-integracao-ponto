package ut.com.mindproapps.jira.integracaoponto;

import com.mindproapps.jira.integracaoponto.api.MyPluginComponent;
import com.mindproapps.jira.integracaoponto.impl.MyPluginComponentImpl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MyComponentUnitTest
{
    @Test
    public void testMyName()
    {
        MyPluginComponent component = new MyPluginComponentImpl(null);
        assertEquals("names do not match!", "myComponent",component.getName());
    }
}