package com.jaus.albertogiunta.justintrain_oraritreni.utils.constants;

public enum ENUM_CATEGORIES {

    FR("Frecciarossa"), FB("Frecciabianca"), FA("Frecciargento"), IC("Intercity"), REG("Regionale"), RV("Regionale Veloce"), BUS("Bus");

    private final String alias;

    ENUM_CATEGORIES(String alias) {
        this.alias = alias;
    }

    public String getAlias() {
        return alias;
    }
}
