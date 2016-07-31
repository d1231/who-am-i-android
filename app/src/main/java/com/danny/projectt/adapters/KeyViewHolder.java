package com.danny.projectt.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.danny.projectt.Key;
import com.danny.projectt.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class KeyViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.letter)
    TextView textView;

    public KeyViewHolder(View itemView) {

        super(itemView);

        ButterKnife.bind(this, itemView);

    }

    public void bind(Key key) {

        textView.setText(key.name());

    }
}
