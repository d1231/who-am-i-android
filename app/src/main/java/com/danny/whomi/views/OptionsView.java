package com.danny.whomi.views;

import com.danny.whomi.model.objects.GameOptions;

import rx.Observable;

public interface OptionsView extends BaseView {

    Observable<Void> startGameClickOBservable();

    void bindOptions(GameOptions gameOptions);

    Observable<Integer> minYearChangesObservable();

    Observable<MultiSelectClick> multiSelectClick();

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
    }
}
