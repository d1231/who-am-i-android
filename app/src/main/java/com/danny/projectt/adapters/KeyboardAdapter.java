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

    private static final int TYPE_NORMAL = 1;

    private static final int TYPE_GUESSED = 2;

    private final Key[] values;

    private final Set<Key> guesses;

    private LayoutInflater inflater;

    private KeyClickedListener listener;

    public KeyboardAdapter(Context context, KeyClickedListener listener) {

        inflater = LayoutInflater.from(context);
        this.listener = listener;

        values = Key.values();

        guesses = Sets.newHashSet();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        switch (viewType) {
            case TYPE_NORMAL:
            case TYPE_GUESSED:
                final View view = inflater.inflate(R.layout.key_item, parent, false);
                return new KeyViewHolder(view);
            default:
                throw new IllegalStateException("Unknown view type");
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final int viewType = getItemViewType(position);

        switch (viewType) {
            case TYPE_NORMAL:
                ((KeyViewHolder) holder).bind(values[position], listener);
                break;
            case TYPE_GUESSED:
                ((KeyViewHolder) holder).bind(values[position]);
                break;
            default:
                throw new IllegalStateException("Unknown view type");

        }

    }

    @Override
    public int getItemViewType(int position) {

        final Key key = values[position];

        if (guesses.contains(key)) {
            return TYPE_GUESSED;
        } else {
            return TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {

        return values.length;
    }

    public void addIncorrectGuess(Key key) {

        itemGuessed(key);

    }

    private void itemGuessed(Key key) {

        guesses.add(key);

        notifyItemChanged(key.ordinal());
    }

    public void addCorrectGuess(Key key) {

        itemGuessed(key);

    }

    public interface KeyClickedListener {

        void onKeyClicked(Key key);

    }
}
