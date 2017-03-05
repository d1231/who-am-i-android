package com.danny.whomi.fragments;

public class DialogResult {

    public static final DialogResult POSITIVE_CLICKED = new DialogResult(1);

    public static final DialogResult NEGATIVE_CLICKED = new DialogResult(0);

    private int result;

    public DialogResult(int result) {

        this.result = result;
    }

    @Override
    public int hashCode() {

        return result;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DialogResult that = (DialogResult) o;

        return result == that.result;

    }
}
