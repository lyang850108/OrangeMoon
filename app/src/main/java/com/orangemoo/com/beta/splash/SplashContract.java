package com.orangemoo.com.beta.splash;

import android.content.Context;


import com.orangemoo.com.beta.tutorial.TutorialItem;

import java.util.ArrayList;


/**
 * @author rebeccafranks
 * @since 15/11/05.
 */
public interface SplashContract {
    interface View {
        void loadTutorial();

        void loadMainScreen();
    }

    interface UserActionsListener {
        void loadSplash();

        ArrayList<TutorialItem> getTutorialItems(Context context);

        void finishedTutorial();
    }
}
