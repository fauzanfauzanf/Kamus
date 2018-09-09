package fikrims.io.kamus.feature.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fikrims.io.kamus.R;
import fikrims.io.kamus.data.database.KamusHelper;
import fikrims.io.kamus.data.model.KamusModel;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MaterialSearchBar.OnSearchActionListener, MainPresenter.MainListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindView(R.id.search_bar)
    MaterialSearchBar search;

    @BindView(R.id.list_search)
    RecyclerView recycler_search;

    @BindView(R.id.text_status)
    TextView textStatus;

    private KamusHelper kamusHelper;
    private MainAdapter adapter;
    private ArrayList<KamusModel> list = new ArrayList<>();
    private boolean isEnglish = true;
    private Context context;
    private MainPresenter mainPresenter;

    public static void start(Context context){
        context.startActivity(new Intent(context, MainActivity.class));
        ((Activity)context).finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
        context = MainActivity.this;

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        search.setOnSearchActionListener(this);

        kamusHelper = new KamusHelper(this);
        mainPresenter = new MainPresenter(context, this, kamusHelper);

        setupRecyclerView();

        navigationView.getMenu().getItem(0).setChecked(true);

        mainPresenter.doSearch("", isEnglish);
    }

    void setupRecyclerView(){
        adapter = new MainAdapter(context);
        recycler_search.setLayoutManager(new LinearLayoutManager(this));
        recycler_search.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_english_indo) {
            // Handle the camera action
            isEnglish = true;
            mainPresenter.doSearch("", true);
        } else if (id == R.id.nav_indo_english) {
            isEnglish = false;
            mainPresenter.doSearch("", false);
        }

        if (isEnglish) textStatus.setText(getResources().getString(R.string.english_indonesia));
        else textStatus.setText(getResources().getString(R.string.indonesia_english));

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onSearchStateChanged(boolean enabled) {

    }

    @Override
    public void onSearchConfirmed(CharSequence text) {
        mainPresenter.doSearch(String.valueOf(text), isEnglish);
    }

    @Override
    public void onButtonClicked(int buttonCode) {

    }

    @Override
    public void getSearchResult(List<KamusModel> list) {
        adapter.setKamusResult(list);
    }
}
