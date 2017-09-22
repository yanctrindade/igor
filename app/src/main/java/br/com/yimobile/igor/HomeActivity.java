package br.com.yimobile.igor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity{

    Drawer result;
    AdventuresRecyclerViewAdapter adventuresRecyclerViewAdapter;
    RecyclerView recyclerView;
    ArrayList<String> adventuresArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.loginatention);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.adventures_recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adventuresArrayList.add("Teste 1");
        adventuresArrayList.add("Teste 2");
        adventuresArrayList.add("Teste 3");
        adventuresRecyclerViewAdapter = new AdventuresRecyclerViewAdapter(adventuresArrayList, this);
        recyclerView.setAdapter(adventuresRecyclerViewAdapter);

        result = new DrawerBuilder().withActivity(this).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                result.openDrawer();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
