package com.jaus.albertogiunta.justintrain_oraritreni.trainDetails;

import com.jaus.albertogiunta.justintrain_oraritreni.BasePresenter;
import com.jaus.albertogiunta.justintrain_oraritreni.BaseView;
import com.jaus.albertogiunta.justintrain_oraritreni.data.Journey;
import com.jaus.albertogiunta.justintrain_oraritreni.data.News;
import com.jaus.albertogiunta.justintrain_oraritreni.data.Train;
import com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.ENUM_ERROR_BTN_STATUS;

import java.util.List;

public interface TrainDetailsContract {

    interface Presenter extends BasePresenter {

        void updateRequested();

        void refreshRequested();

        void onFavouriteButtonClick();

        void onDoubleAdditionConfirmation();

        boolean isSolutionPreferred();

        Journey.Solution getSolution();

        List<Object> getFlatTrainList();

        Train getTrainForAdapterPosition(int position);

        Integer getTrainIndexForAdapterPosition(int position);

        void onNotificationRequested(int position);

        void onNewsUpdateRequested(String trainId);

        String getShareableString();

        String getShareableString(int index);

        boolean isOnlyTrainSearch();
    }

    interface View extends BaseView {

        void setFavouriteButtonStatus(boolean isPreferred);

        void setShareButton(List<String> trainCatAndId);

        /**
         * Hides everything and shows a loader
         */
        void showProgress();

        /**
         * Hides the loader and shows everything else
         */
        void hideProgress();


        void updateTrainDetails();

        void showNewsDialog(News news);

        void showAddSolutionAndJourneyToFavouritesDialog();

        /**
         * Hides everything and shows an error message with an action button
         *
         * @param tvMessage message to be shown
         * @param btnMessage text for the button
         * @param intent action to execute on button press
         */
        void showErrorMessage(String tvMessage, String btnMessage, ENUM_ERROR_BTN_STATUS intent);

    }

}
