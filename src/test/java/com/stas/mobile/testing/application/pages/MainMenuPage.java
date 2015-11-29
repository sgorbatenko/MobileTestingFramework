
package com.stas.mobile.testing.application.pages;

import com.stas.mobile.testing.framework.device.controls.Text;


public class MainMenuPage extends AbstractApplicationUI
{
    // BEGIN: constants
    private static final String ID_HOME_TEXTVIEW = "com.wholefoods.wholefoodsmarket:id/idHomeLayout";
    private static final String ID_FIND_STORE_TEXTVIEW = "com.wholefoods.wholefoodsmarket:id/idFindStoreLayout";
    // END: constants

    // BEGIN: fields, getters and setters
    private Text homeText = new Text(ID_HOME_TEXTVIEW);
    private Text findStoreText = new Text(ID_FIND_STORE_TEXTVIEW);

    public Text getHomeTextView()
    {
        return homeText;
    }

    public Text getFindStoreView()
    {
        return homeText;
    }

    // TO-DO: Add other menu items

    // END: fields, getters and setters

    // BEGIN: construction
    public MainMenuPage(ApplicationUIManager appUIManager)
    {
        super(appUIManager);
    }

    // END: construction

    // BEGIN: methods

    // END: methods

    @Override
    public MainMenuPage open()
    {
        if (!isOpened())
            getMenuButton().tap();
        return this;
    }

    @Override
    public boolean isOpened()
    {
        return (getMenuButton().isDisplayed() && getMenuButton().isEnabled());
    }

}
