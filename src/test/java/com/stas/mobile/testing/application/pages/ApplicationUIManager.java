
package com.stas.mobile.testing.application.pages;

import com.stas.mobile.testing.framework.device.DeviceControl;


public class ApplicationUIManager
{
    // BEGIN: constants

    // END: constants

    // BEGIN: fields, getters and setters
    private DeviceControl deviceControl = new DeviceControl();
    private HomePage homeUI;
    private MainMenuPage menuUI;

    /**
     * Gets the home ui.
     *
     * @return the administration ui.
     */
    public HomePage getHomeUI()
    {
        if (homeUI == null)
        {
            homeUI = new HomePage(this);
        }
        return homeUI.open();
    }

    /**
     * Gets the main menu ui.
     *
     * @return the main menu ui.
     */
    public MainMenuPage getMainMenuUI()
    {
        if (menuUI == null)
        {
            menuUI = new MainMenuPage(this);
        }
        return menuUI.open();
    }
    // END: fields, getters and setters

    // BEGIN: construction

    // END: construction

    // BEGIN: methods

    // END: methods
}
