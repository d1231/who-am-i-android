package com.whomi.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.whomi.R;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Created by danny on 10-Mar-17.
 */
public class NationAdapter extends RecyclerView.Adapter<NationViewHolder> {

    private final LayoutInflater inflater;
    private List<NationValue> nationsStatusList;
    private NationListener listener;

    public NationAdapter(Context context, Map<String, Boolean> nationsStatusMap, NationListener listener) {

        inflater = LayoutInflater.from(context);
        this.listener = listener;
        this.nationsStatusList = Lists.newArrayList();

        Set<Map.Entry<String, Boolean>> entries = nationsStatusMap.entrySet();
        for (Map.Entry<String, Boolean> entry : entries) {
            nationsStatusList.add(NationValue.create(entry.getKey(), entry.getValue()));
        }
    }

    @Override
    public NationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = inflater.inflate(R.layout.item_options_nation, parent, false);
        return new NationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NationViewHolder holder, int position) {

        NationValue nationSetting = nationsStatusList.get(position);
        holder.bind(nationSetting.nation, nationSetting.value, listener);

    }

    @Override
    public int getItemCount() {
        return nationsStatusList.size();
    }

    public void setChecked(String value) {

        for (int i = 0; i < nationsStatusList.size(); i++) {
            NationValue nation = nationsStatusList.get(i);
            if (Objects.equals(nation.nation, value)) {
                nation.value = true;
                notifyItemChanged(i);
                break;
            }
        }

    }

    private static class NationValue {

        private String nation;

        private boolean value;

        private NationValue(String nation, boolean value) {
            this.nation = nation;
            this.value = value;
        }


        public static NationValue create(String nation, boolean value) {
            return new NationValue(nation, value);
        }
    }

    public interface NationListener {
        void checked(String nation, boolean isChecked);
    }
}
