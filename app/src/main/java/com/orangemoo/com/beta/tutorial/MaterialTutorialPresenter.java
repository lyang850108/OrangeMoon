package com.orangemoo.com.beta.tutorial;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.view.View;


import com.orangemoo.com.beta.app.App;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yang on 16/8/15.
 */
public class MaterialTutorialPresenter implements MaterialTutorialContract.UserActionsListener {
    private final Context context;
    private MaterialTutorialContract.View tutorialView;
    private List<MaterialTutorialFragment> fragments;
    private List<TutorialItem> tutorialItems;

    public MaterialTutorialPresenter(Context context, MaterialTutorialContract.View tutorialView) {
        this.tutorialView = tutorialView;
        this.context = context;
    }


    @Override
    public void loadViewPagerFragments(List<TutorialItem> tutorialItems) {
        fragments = new ArrayList<>();
        this.tutorialItems = tutorialItems;
        for (int i = 0; i < tutorialItems.size(); i++) {
            MaterialTutorialFragment helpTutorialImageFragment;
            helpTutorialImageFragment = MaterialTutorialFragment.newInstance(tutorialItems.get(i), i);
            fragments.add(helpTutorialImageFragment);
        }
        tutorialView.setViewPagerFragments(fragments);
    }

    @Override
    public void doneOrSkipClick() {
        tutorialView.showEndTutorial();
    }

    @Override
    public void nextClick() {
        tutorialView.showNextTutorial();
    }

    @Override
    public void onPageSelected(int pageNo) {
        if (pageNo >= fragments.size() - 1) {
            tutorialView.showDoneButton();
        } else {
            tutorialView.showSkipButton();
        }
    }

    @Override
    public void transformPage(View page, float position) {
        int pagePosition = (int) page.getTag();
        if (position <= -1.0f || position >= 1.0f) {

        } else if (position == 0.0f)
            tutorialView.setBackgroundColor(App.getsContext().getResources().getColor(tutorialItems.get(pagePosition).getBackgroundColor()));
        else {
            fadeNewColorIn(pagePosition, position);
        }
    }

    @Override
    public int getNumberOfTutorials() {
        if (tutorialItems != null) {
            return tutorialItems.size();
        }
        return 0;
    }

    private void fadeNewColorIn(int index, float multiplier) {
        if (multiplier < 0) {

            int colorStart = App.getsContext().getResources().getColor(tutorialItems.get(index).getBackgroundColor());
            if (index + 1 == fragments.size()) {
                tutorialView.setBackgroundColor(colorStart);
                return;
            }
            int colorEnd = App.getsContext().getResources().getColor(tutorialItems.get(index + 1).getBackgroundColor());
            int colorToSet = (int) (new ArgbEvaluator().evaluate(Math.abs(multiplier), colorStart, colorEnd));
            tutorialView.setBackgroundColor(colorToSet);
        }

    }

}
