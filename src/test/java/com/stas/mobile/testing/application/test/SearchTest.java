
package com.stas.mobile.testing.application.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.stas.mobile.testing.application.pages.ApplicationUIManager;
import com.stas.mobile.testing.application.pages.HomePage;
import com.stas.mobile.testing.framework.test.BaseAppiumTest;


public class SearchTest extends BaseAppiumTest
{

    @Test
    public void testBasicSearch()
    {
        String expectedResultNumer = "25";
        String actualResultNumer;
        ApplicationUIManager appUIMng = new ApplicationUIManager();
        actualResultNumer = appUIMng.getHomeUI()
            .SearchForRecipes(HomePage.COFFEE)
            .getSearchResultNumberTextTextView().getStringValue();

        Assert.assertEquals(actualResultNumer, expectedResultNumer);
    }

}
