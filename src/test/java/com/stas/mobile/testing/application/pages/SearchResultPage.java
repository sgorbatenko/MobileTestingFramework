
package com.stas.mobile.testing.application.pages;

import com.stas.mobile.testing.framework.device.controls.Text;

public class SearchResultPage extends AbstractApplicationUI
{

    // BEGIN: constants
    private static final String ID_SEARCH_RESULT_NUMBER_TEXTVIEW = "com.wholefoods.wholefoodsmarket:id/searchResultsNumber";
    // END: constants

    // BEGIN: fields, getters and setters
    private Text searchResultNumberText = new Text(
        ID_SEARCH_RESULT_NUMBER_TEXTVIEW);

    public Text getSearchResultNumberTextTextView()
    {
        return searchResultNumberText;
    }

    // END: fields, getters and setters

    // BEGIN: construction
    public SearchResultPage(ApplicationUIManager appUIManager)
    {
        super(appUIManager);
        // TODO-DJ Auto-generated constructor stub
    }

    // END: construction

    // BEGIN: methods

    @Override
    public SearchResultPage open()
    {
        return this;
    }

    @Override
    public boolean isOpened()
    {
        return (searchResultNumberText.isDisplayed() && searchResultNumberText
            .isEnabled());
    }
    // END: methods
}
