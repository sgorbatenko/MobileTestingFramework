
package com.stas.mobile.testing.application.pages;

import com.stas.mobile.testing.framework.device.controls.Button;


public abstract class AbstractApplicationUI
{

    static final String ID_MENU_BUTTON = "com.wholefoods.wholefoodsmarket:id/hamburger_icon";

    private Button menuBtn = new Button(ID_MENU_BUTTON);
    private ApplicationUIManager appUIMng;

    public Button getMenuButton()
    {
        return menuBtn;
    }

    public ApplicationUIManager getApplicationUIManager()
    {
        return appUIMng;
    }

    public AbstractApplicationUI(ApplicationUIManager appUIManager)
    {
        appUIMng = appUIManager;
    }

    public abstract AbstractApplicationUI open();

    public abstract boolean isOpened();
}
