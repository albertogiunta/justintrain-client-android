package com.jaus.albertogiunta.justintrain_oraritreni;

import android.os.Bundle;

public interface BasePresenter {

    /**
     * Should be called in the onDestroy method.
     * Sets the view to null
     */
    void unsubscribe();

    /**
     * Should be called in the onrResume method.
     * It receives a bundle and sets the state of the presenter to it if any interesting value is present.
     * @param bundle
     */
    void setState(Bundle bundle);

    /**
     * Should be called in the onRestoreInstanceState method.
     * Creates a bundle if true and fills it with the presenter state if present
     * @param bundle
     * @return the previous bundle with overridden values (if present)
     */
    Bundle getState(Bundle bundle);

}
