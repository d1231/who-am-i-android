package com.whomi.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.whomi.Key;
import com.whomi.R;

import butterknife.BindView;
import butterknife.ButterKnife;

class KeyViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.letter)
    Button button;

    KeyViewHolder(View itemView) {

        super(itemView);

        ButterKnife.bind(this, itemView);

    }

    public void bind(Key key) {

        button.setText(key.name());

        button.setEnabled(false);
        button.setAlpha(0.7f);
    }

    public void bind(Key key, KeyboardAdapter.KeyClickedListener listener) {

        button.setText(key.name());
        button.setOnClickListener(v -> listener.onKeyClicked(key));

    }
}
