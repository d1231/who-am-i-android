package com.whomi.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.whomi.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by danny on 10-Mar-17.
 */
public class NationViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.nation)
    TextView nationTv;

    @BindView(R.id.nation_checkbox)
    CheckBox checkBox;

    public NationViewHolder(View view) {
        super(view);

        ButterKnife.bind(this, view);

    }

    public void bind(String nation, Boolean value, NationAdapter.NationListener listener) {

        nationTv.setText(nation);
        checkBox.setChecked(value);

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            listener.checked(nation, isChecked);
        });
    }

}
