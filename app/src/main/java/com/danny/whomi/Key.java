package com.danny.whomi;

public enum Key {

    Q('q'), W('w'), E('e'), R('r'), T('t'), Y('y'), U('u'), I('i'), O('o'), P('p'),
    A('a'), S('s'), D('d'), F('f'), G('g'), H('h'), J('j'), K('k'), L('l'),
    Z('z'), X('x'), C('c'), V('v'), B('b'), N('n'), M('m');

    private final char letter;

    Key(char letter) {

        this.letter = letter;
    }

    public static Key get(char c) {

        final char upperCase = Character.toUpperCase(c);
        final String name = String.valueOf(upperCase);

        return Key.valueOf(name);

    }

    public char getLetter() {

        return letter;
    }
}
