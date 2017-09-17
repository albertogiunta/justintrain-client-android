package com.jaus.albertogiunta.justintrain_oraritreni.utils.constants;

import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_V0.PREFIX_PREF_JOURNEY;
import static com.jaus.albertogiunta.justintrain_oraritreni.utils.constants.CONST_SP_V0.PREFIX_REC_JOURNEY;

public enum ENUM_HOME_HEADER {

    FAVOURITES("Preferiti", PREFIX_PREF_JOURNEY),
    RECENT("Recenti", PREFIX_REC_JOURNEY);

    private final String title;
    private final String type;

    ENUM_HOME_HEADER(String title, String type) {
        this.title = title;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }
}
