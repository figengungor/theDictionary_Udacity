package com.figengungor.thedictionary.ui.history;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.figengungor.thedictionary.R;
import com.figengungor.thedictionary.data.local.AppDatabase;
import com.figengungor.thedictionary.data.local.SearchHistoryEntry;
import com.figengungor.thedictionary.ui.home.HomeActivity;
import com.nikhilpanju.recyclerviewenhanced.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryActivity extends AppCompatActivity {

    HistoryViewModel viewModel;
    RecyclerTouchListener onTouchListener;
    HistoryAdapter historyAdapter;
    @BindView(R.id.historyRv)
    RecyclerView historyRv;
    @BindView(R.id.messageLayout)
    ConstraintLayout messageLayout;
    @BindView(R.id.messageTv)
    TextView messageTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);
        init();
    }

    private void init(){
        viewModel = ViewModelProviders.of(this,
                new HistoryViewModelFactory(getApplication(), AppDatabase.getInstance(getApplication())))
                .get(HistoryViewModel.class);

        viewModel.getHistoryList().observe(this, new Observer<List<SearchHistoryEntry>>() {
            @Override
            public void onChanged(@Nullable List<SearchHistoryEntry> searchHistoryEntries) {
                if (searchHistoryEntries.size() == 0) {
                    displayEmptyLayout();
                } else {
                    displayHistoryList(searchHistoryEntries);
                }
            }
        });

        setupUI();
    }

    private void setupUI() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        historyAdapter = new HistoryAdapter(new ArrayList<SearchHistoryEntry>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(HistoryActivity.this);
        historyRv.setLayoutManager(layoutManager);
        onTouchListener = new RecyclerTouchListener(this, historyRv);
        onTouchListener
                .setClickable(new RecyclerTouchListener.OnRowClickListener() {
                    @Override
                    public void onRowClicked(int position) {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra(HomeActivity.EXTRA_SEARCH_HISTORY_ENTRY, historyAdapter.getItem(position).getEntry());
                        setResult(Activity.RESULT_OK,returnIntent);
                        onBackPressed();
                    }

                    @Override
                    public void onIndependentViewClicked(int independentViewID, int position) {
                    }
                })
                .setLongClickable(true, new RecyclerTouchListener.OnRowLongClickListener() {
                    @Override
                    public void onRowLongClicked(int position) {
                    }
                })
                .setSwipeOptionViews(R.id.add)
                .setSwipeable(R.id.rowFG, R.id.rowBG, new RecyclerTouchListener.OnSwipeOptionsClickListener() {
                    @Override
                    public void onSwipeOptionClicked(int viewID, int position) {
                        if (viewID == R.id.add) {
                            viewModel.deleteSearchHistoryEntry(historyAdapter.getItem(position));
                        }
                    }
                });
    }

    private void displayHistoryList(List<SearchHistoryEntry> searchHistoryEntries) {
        messageLayout.setVisibility(View.GONE);
        historyRv.setVisibility(View.VISIBLE);
        historyAdapter = new HistoryAdapter(searchHistoryEntries);
        historyRv.setAdapter(historyAdapter);
    }

    private void displayEmptyLayout() {
        messageTv.setText(getString(R.string.no_history_record));
        messageLayout.setVisibility(View.VISIBLE);
        historyRv.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_history, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.item_delete_all:
                showDeleteDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.are_you_sure));
        builder.setMessage(getString(R.string.all_history_will_be_deleted));

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                viewModel.deleteAllHistory();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        historyRv.addOnItemTouchListener(onTouchListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        historyRv.removeOnItemTouchListener(onTouchListener);
    }
}
