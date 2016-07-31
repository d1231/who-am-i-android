package com.danny.projectt.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.danny.projectt.Key;
import com.danny.projectt.R;
import com.google.common.collect.Sets;

import java.util.Set;

public class KeyboardAdapter extends RecyclerView.Adapter {

    private static final int TYPE_CORRECT = 0;

    private static final int TYPE_NORMAL = 1;

    private static final int TYPE_INCORRECT = 2;

    private final Key[] values;

    private final Set<Key> correctGuesses;

    private final Set<Key> incorrectGuesses;

    private LayoutInflater inflater;

    private KeyClickedListener listener;

    public KeyboardAdapter(Context context, KeyClickedListener listener) {

        inflater = LayoutInflater.from(context);
        this.listener = listener;

        values = Key.values();

        correctGuesses = Sets.newHashSet();
        incorrectGuesses = Sets.newHashSet();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        switch (viewType) {
            case TYPE_CORRECT:
                final View view = inflater.inflate(R.layout.key_item_correct, parent, false);
                return new KeyViewHolder(view);
            case TYPE_INCORRECT:
                final View view2 = inflater.inflate(R.layout.key_item_incorrect, parent, false);
                return new KeyViewHolder(view2);
            case TYPE_NORMAL:
                final View view3 = inflater.inflate(R.layout.key_item, parent, false);
                return new ClickableKeyViewHolder(view3);
            default:
                throw new IllegalStateException("Unknown view type");
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final int viewType = getItemViewType(position);

        switch (viewType) {
            case TYPE_NORMAL:
                ((ClickableKeyViewHolder) holder).bind(values[position], listener);
                break;
            case TYPE_INCORRECT:
            case TYPE_CORRECT:
                ((KeyViewHolder) holder).bind(values[position]);
                break;
            default:
                throw new IllegalStateException("Unknown view type");

        }

    }

    @Override
    public int getItemViewType(int position) {

        final Key key = values[position];

        if (correctGuesses.contains(key)) {
            return TYPE_CORRECT;
        } else if (incorrectGuesses.contains(key)) {
            return TYPE_INCORRECT;
        } else {
            return TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {

        return values.length;
    }

    public void addIncorrectGuess(Key key) {

        incorrectGuesses.add(key);

        notifyItemChanged(key.ordinal());

    }

    public void addCorrectGuess(Key key) {

        correctGuesses.add(key);

        notifyItemChanged(key.ordinal());

    }

    public interface KeyClickedListener {

        void onKeyClicked(Key key);

    }
}
