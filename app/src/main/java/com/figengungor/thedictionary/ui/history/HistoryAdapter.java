package com.figengungor.thedictionary.ui.history;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.figengungor.thedictionary.R;
import com.figengungor.thedictionary.data.local.SearchHistoryEntry;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by figengungor on 5/29/2018.
 */
public class HistoryAdapter extends
        RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private List<SearchHistoryEntry> items;
    private boolean showDelete;

    public HistoryAdapter(List<SearchHistoryEntry> items, boolean showDelete) {
        this.items = items;
        this.showDelete = showDelete;
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HistoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false));
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        final SearchHistoryEntry item = items.get(position);
        holder.historyTv.setText(item.getEntry());
        if(showDelete){
            holder.deleteBtn.setVisibility(View.VISIBLE);
        } else {
            holder.deleteBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.historyTv)
        TextView historyTv;
        @BindView(R.id.deleteBtn)
        ImageButton deleteBtn;

        public HistoryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public SearchHistoryEntry getItem(int position) {
        return items.get(position);
    }
}
