package com.figengungor.thedictionary.ui.home;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.figengungor.thedictionary.R;
import com.figengungor.thedictionary.data.model.Entry;
import com.figengungor.thedictionary.data.model.Example;
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
                List<Sense> senses = entry.getSenses();
                for (int s = 0; s < senses.size(); s++) {
                    StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
                    SpannableStringBuilder sb_number = new SpannableStringBuilder(s+1+" ");
                    sb_number.setSpan(bss, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    definitionTv.append(sb_number);
                    Sense sense = senses.get(s);
                    List<String> definitions = sense.getDefinitions();
                    if (definitions != null) {
                        for (int i = 0; i < definitions.size(); i++) {
                            definitionTv.append(definitions.get(i) + "\n\n");
                        }
                    }

                    List<String> crossReferenceMarkers = sense.getCrossReferenceMarkers();
                    if (crossReferenceMarkers != null) {

                        if(sense.getDomains()!=null && sense.getDomains().size()>0) {
                            String domains = TextUtils.join(",", sense.getDomains());
                            definitionTv.append(domains + ",");
                        }
                        for (int i = 0; i < crossReferenceMarkers.size(); i++) {
                            definitionTv.append(crossReferenceMarkers.get(i)+"\n\n");
                        }
                    }

                    List<Example> examples = sense.getExamples();
                    if (examples != null) {
                        for (int i = 0; i < examples.size(); i++) {
                            ForegroundColorSpan fcs = new ForegroundColorSpan(ContextCompat.getColor(itemView.getContext(), R.color.colorPrimary));
                            StyleSpan iss = new StyleSpan(android.graphics.Typeface.ITALIC);
                            String example = "\""+examples.get(i).getText()+"\"\n\n";
                            SpannableStringBuilder sb_example = new SpannableStringBuilder(example);
                            sb_example.setSpan(iss, 0, example.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            sb_example.setSpan(fcs, 0, example.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            definitionTv.append(sb_example);
                        }
                    }

                    definitionTv.append("\n");
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
