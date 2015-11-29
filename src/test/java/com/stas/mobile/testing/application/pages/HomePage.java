
package com.stas.mobile.testing.application.pages;

import com.stas.mobile.testing.framework.device.controls.Button;
import com.stas.mobile.testing.framework.device.controls.Image;
import com.stas.mobile.testing.framework.device.controls.TextBox;


public class HomePage extends AbstractApplicationUI
{
    // BEGIN: constants
    public static final String COFFEE = "Coffee";
    private static final String ID_HOME_LOGO = "com.wholefoods.wholefoodsmarket:id/header_wfm_logo";
    private static final String ID_SERCH_BOX = "com.wholefoods.wholefoodsmarket:id/etHomeSearch";
    private static final String ID_SERCH_BUTTON = "com.wholefoods.wholefoodsmarket:id/imgSearch";
    // END: constants

    // BEGIN: fields, getters and setters
    private Image homeLogo = new Image(ID_HOME_LOGO);
    private TextBox searchBox = new TextBox(ID_SERCH_BOX);
    private Button searchBtn = new Button(ID_SERCH_BUTTON);

    public Image getHomeLogo()
    {
        return homeLogo;
    }

    public TextBox getSearchBox()
    {
        return searchBox;
    }

    public Button getSearchButton()
    {
        return searchBtn;
    }

    // END: fields, getters and setters

    // BEGIN: construction
    public HomePage(ApplicationUIManager appUIManager)
    {
        super(appUIManager);
    }

    // END: construction

    // BEGIN: methods
    public SearchResultPage SearchForRecipes(String recipe)
    {
        searchBox.enterText(recipe);
        searchBtn.tap();
        return new SearchResultPage(getApplicationUIManager());
    }

    // END: methods

    @Override
    public HomePage open()
    {
        if (!isOpened())
            getApplicationUIManager().getMainMenuUI().getHomeTextView().tap();
        return this;
    }

    @Override
    public boolean isOpened()
    {
        return homeLogo.isDisplayed();
    }
}
