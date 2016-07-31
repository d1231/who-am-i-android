package com.danny.projectt.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.danny.projectt.Key;
import com.danny.projectt.R;
import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;

public class KeyboardAdapter2 extends RecyclerView.Adapter {

    private static final int TYPE_NORMAL = 1;

    private final List<Key> values;

    private LayoutInflater inflater;

    private KeyboardAdapter.KeyClickedListener listener;

    public KeyboardAdapter2(Context context, KeyboardAdapter.KeyClickedListener listener) {

        inflater = LayoutInflater.from(context);
        this.listener = listener;

        final Key[] values = Key.values();
        this.values = Lists.newArrayList(Arrays.asList(values));

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        switch (viewType) {
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
                ((ClickableKeyViewHolder) holder).bind(values.get(position), listener);
                break;
            default:
                throw new IllegalStateException("Unknown view type");

        }

    }

    @Override
    public int getItemViewType(int position) {

        return TYPE_NORMAL;
    }

    @Override
    public int getItemCount() {

        return values.size();
    }

    public void addIncorrectGuess(Key key) {

        removeKey(key);

    }

    private void removeKey(Key key) {

        final int i = values.indexOf(key);

        notifyItemRemoved(i);
        values.remove(i);
    }

    public void addCorrectGuess(Key key) {

        removeKey(key);
    }

}
