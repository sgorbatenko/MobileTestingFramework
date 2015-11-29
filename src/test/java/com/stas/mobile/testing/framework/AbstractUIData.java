
package com.stas.mobile.testing.framework;

import java.util.HashSet;

import com.stas.mobile.testing.framework.queryhelpers.WebElementQueryHelper;
import com.stas.mobile.testing.framework.synchronization.AjaxHelper;
import com.stas.mobile.testing.framework.util.environment.EnvironmentUtil;
import com.stas.mobile.testing.framework.util.logger.LogController;

public abstract class AbstractUIData
    implements UIData
{
    protected AjaxHelper syncHelper;
    private LogController logger = new LogController(AbstractUIData.class);
    protected EnvironmentUtil env = new EnvironmentUtil();
    private WebElementQueryHelper queryHelper;
    protected HashSet<UIData> children = new HashSet();

    public HashSet<UIData> getChildren()
    {
        return this.children;
    }

    public void addChild(UIData child)
    {
        this.children.add(child);
    }

    public String getAbsoluteSelector()
    {
        return doGetAbsoluteSelector(this);
    }

    private String doGetAbsoluteSelector(UIData chunk)
    {
        String result = chunk.getSelector();
        UIData parent = chunk.getParent();
        if (parent != null)
        {
            String prefix = parent.getSelector();
            if (prefix != null)
            {
                result = prefix + " " + result;
            }
        }
        return result;
    }
}
