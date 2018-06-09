package com.figengungor.thedictionary.ui.home;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.figengungor.thedictionary.R;
import com.figengungor.thedictionary.data.DataManager;
import com.figengungor.thedictionary.data.model.LexicalEntry;
import com.figengungor.thedictionary.ui.history.HistoryActivity;
import com.figengungor.thedictionary.ui.widget.SearchHistoryWidgetProvider;
import com.figengungor.thedictionary.utils.KeyboardUtils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = HomeActivity.class.getSimpleName();
    public static final String EXTRA_SEARCH_HISTORY_ENTRY = "history_entry";
    private static final int REQUEST_CODE_HISTORY = 1;
    private static final String KEY_RECYCLERVIEW_STATE = "recyclerview_state";
    private FirebaseAnalytics firebaseAnalytics;
    private HomeViewModel viewModel;
    private Parcelable recyclerViewState;

    @BindView(R.id.searchEt)
    EditText searchEt;

    @BindView(R.id.entriesRv)
    RecyclerView entriesRv;

    @BindView(R.id.messageLayout)
    ConstraintLayout messageLayout;

    @BindView(R.id.messageTv)
    TextView messageTv;

    @BindView(R.id.loadingPw)
    ProgressWheel loadingPw;

    @BindView(R.id.adView)
    AdView adView;

    @OnClick(R.id.searchBtn)
    public void search() {
        String word = searchEt.getText().toString();
        if (TextUtils.isEmpty(word)) {
            Toast.makeText(this, getString(R.string.empty_search_text), Toast.LENGTH_SHORT).show();
        } else {
            viewModel.search(word);
            KeyboardUtils.hideSoftKeyboard(this);
        }
        firebaseAnalytics.logEvent("search_btn", null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            recyclerViewState = savedInstanceState.getParcelable(KEY_RECYCLERVIEW_STATE);
        }

        viewModel = ViewModelProviders.of(this,
                new HomeViewModelFactory(getApplication(), DataManager.getInstance(getApplication())))
                .get(HomeViewModel.class);

        viewModel.getLexicalEntries().observe(this, lexicalEntries -> {
            if (lexicalEntries != null) showEntries(lexicalEntries);
            else entriesRv.setVisibility(View.GONE);
        });

        viewModel.getIsLoading().observe(this, isLoading -> {
            Log.d(TAG, "onChanged: getIsLoading -> " + isLoading);
            showLoadingIndicator(isLoading);
        });

        viewModel.getError().observe(this, throwable -> {
            Log.d(TAG, "onChanged: getError -> " + throwable);
            if (throwable != null) showError(throwable);
            else messageLayout.setVisibility(View.GONE);

        });

        // Obtain the FirebaseAnalytics instance.
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // Add a banner ad
        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        setupUI();
    }

    private void setupUI() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        entriesRv.setLayoutManager(linearLayoutManager);
        if(getIntent().hasExtra(SearchHistoryWidgetProvider.EXTRA_SEARCH_ENTRY)){
            String entry = getIntent().getExtras().getString(SearchHistoryWidgetProvider.EXTRA_SEARCH_ENTRY);
            searchEt.setText(entry);
            search();
        }
    }


    private void showEntries(List<LexicalEntry> lexicalEntries) {
        entriesRv.setVisibility(View.VISIBLE);
        messageLayout.setVisibility(View.GONE);
        EntryAdapter adapter = new EntryAdapter(lexicalEntries);
        entriesRv.setAdapter(adapter);
        if (recyclerViewState != null) {
            entriesRv.getLayoutManager().onRestoreInstanceState(recyclerViewState);
            recyclerViewState = null;
        }
    }

    private void showLoadingIndicator(Boolean isLoading) {
        if (isLoading) {
            loadingPw.setVisibility(View.VISIBLE);
        } else {
            loadingPw.setVisibility(View.GONE);
        }
    }

    private void showError(Throwable throwable) {
        entriesRv.setVisibility(View.GONE);
        messageLayout.setVisibility(View.VISIBLE);
        messageTv.setText(throwable.getMessage());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.item_history:
                startActivityForResult(new Intent(HomeActivity.this, HistoryActivity.class), REQUEST_CODE_HISTORY);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                firebaseAnalytics.logEvent("history_menu", null);
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_HISTORY && resultCode == RESULT_OK) {
            String searchEntry = data.getExtras().getString(EXTRA_SEARCH_HISTORY_ENTRY);
            searchEt.setText(searchEntry);
            search();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Parcelable state = entriesRv.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(KEY_RECYCLERVIEW_STATE, state);
    }

    /**
     * Called when leaving the activity
     */
    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    /**
     * Called when returning to the activity
     */
    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }
}
