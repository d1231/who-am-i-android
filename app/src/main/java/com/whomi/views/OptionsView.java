package com.whomi.views;

import java.util.Map;

import rx.Observable;

public interface OptionsView extends BaseView {

    Observable<Void> startGameButton();

    Observable<MultiSelectClick> nationStatusChanged();

    void showNations(Map<String, Boolean> nations);

    void unableToUnchecked(String value);


    class MultiSelectClick {

        private String value;

        private boolean isChecked;

        public MultiSelectClick(String value, boolean isChecked) {


            this.value = value;
            this.isChecked = isChecked;
        }

        public String getValue() {

            return value;
        }

        public boolean isChecked() {

            return isChecked;
        }

        @Override
        public String toString() {
            return "MultiSelectClick{" +
                    "value='" + value + '\'' +
                    ", isChecked=" + isChecked +
                    '}';
        }
    }
}
