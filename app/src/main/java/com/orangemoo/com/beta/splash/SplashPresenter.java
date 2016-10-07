package com.orangemoo.com.beta.splash;

import android.content.Context;


import com.orangemoo.com.beta.R;
import com.orangemoo.com.beta.app.App;
import com.orangemoo.com.beta.tutorial.TutorialItem;
import com.orangemoo.com.beta.util.PreferenceUtils;

import java.util.ArrayList;


/**
 * @author rebeccafranks
 * @since 15/11/05.
 */
public class SplashPresenter implements SplashContract.UserActionsListener {
    /*private final SettingsRepository settingsRepository;
    private final SplashContract.View splashView;

    public SplashPresenter(SplashContract.View view, SettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
        splashView = view;
    }*/

    private final SplashContract.View splashView;

    public SplashPresenter(SplashContract.View view) {
        splashView = view;
    }

    @Override
    public void loadSplash() {
        if (PreferenceUtils.isFirstLaunch(App.getsContext())) {
            splashView.loadTutorial();
        } else {
            splashView.loadMainScreen();
        }

        //splashView.loadTutorial();
    }

    @Override
    public ArrayList<TutorialItem> getTutorialItems(Context context) {
        TutorialItem tutorialItem1 = new TutorialItem(context.getString(R.string.slide_1_african_story_books), context.getString(R.string.slide_1_african_story_books_subtitle),
                R.color.slide_1, R.mipmap.tut_page_1_front,  R.mipmap.tut_page_1_background);

        TutorialItem tutorialItem2 = new TutorialItem(context.getString(R.string.slide_2_volunteer_professionals), context.getString(R.string.slide_2_volunteer_professionals_subtitle),
                R.color.slide_2,  R.mipmap.tut_page_2_front,  R.mipmap.tut_page_2_background);

        TutorialItem tutorialItem3 = new TutorialItem(context.getString(R.string.slide_3_download_and_go), context.getString(R.string.slide_3_download_and_go_subtitle),
                R.color.slide_3, R.mipmap.tut_page_3_foreground,  R.mipmap.tut_page_3_background);

        TutorialItem tutorialItem4 = new TutorialItem(context.getString(R.string.slide_4_different_languages), context.getString(R.string.slide_4_different_languages_subtitle),
                R.color.slide_4,  R.mipmap.tut_page_4_foreground,  R.mipmap.tut_page_4_background);

        ArrayList<TutorialItem> tutorialItems = new ArrayList<>();
        tutorialItems.add(tutorialItem1);
        tutorialItems.add(tutorialItem2);
        tutorialItems.add(tutorialItem3);
        tutorialItems.add(tutorialItem4);

        return tutorialItems;
    }

    @Override
    public void finishedTutorial() {
        //settingsRepository.setIsFirstTime(false);
        splashView.loadMainScreen();
    }
}
