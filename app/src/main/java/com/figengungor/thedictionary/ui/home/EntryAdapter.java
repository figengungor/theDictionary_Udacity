package com.figengungor.thedictionary.ui.home;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.figengungor.thedictionary.R;
import com.figengungor.thedictionary.data.model.Entry;
import com.figengungor.thedictionary.data.model.LexicalEntry;
import com.figengungor.thedictionary.data.model.Sense;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by figengungor on 5/22/2018.
 */
public class EntryAdapter extends
        RecyclerView.Adapter<EntryAdapter.EntryViewHolder> {

    private List<LexicalEntry> items;
    private ItemListener itemListener;

    public EntryAdapter(List<LexicalEntry> items) {
        this.items = items;
    }

    @Override
    public EntryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EntryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_entry, parent, false));
    }

    @Override
    public void onBindViewHolder(EntryViewHolder holder, int position) {
        final LexicalEntry item = items.get(position);
        holder.bindItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class EntryViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.lexicalCategoryTv)
        TextView lexicalCategoryTv;
        @BindView(R.id.definitionTv)
        TextView definitionTv;

        public EntryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindItem(LexicalEntry item) {
            lexicalCategoryTv.setText(item.getLexicalCategory());
            List<Entry> entries = item.getEntries();
            for (Entry entry : entries) {
                for (Sense sense : entry.getSenses()) {
                    List<String> definitions = sense.getDefinitions();
                    if (definitions != null) {
                        for (int i = 0; i < definitions.size(); i++)
                            definitionTv.append("* " + sense.getDefinitions().get(i) + "\n");
                    }

                }
            }
        }
    }

    public interface ItemListener {
        void onItemClicked(LexicalEntry item);
    }

    public void setItemListener(ItemListener itemListener) {
        this.itemListener = itemListener;
    }

}
