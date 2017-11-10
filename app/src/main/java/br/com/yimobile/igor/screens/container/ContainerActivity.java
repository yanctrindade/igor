package br.com.yimobile.igor.screens.container;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.yimobile.igor.R;
import br.com.yimobile.igor.screens.container.account.AccountFragment;
import br.com.yimobile.igor.screens.container.adventures.AdventuresFragment;
import br.com.yimobile.igor.screens.container.adventures.andamento.SessionsFragment;
import br.com.yimobile.igor.screens.container.adventures.andamento.newPlayer.NewPlayerFragment;
import br.com.yimobile.igor.screens.container.adventures.andamento.newSession.NewSessionFragment;
import br.com.yimobile.igor.screens.container.adventures.newAdventure.NewAdventureFragment;
import br.com.yimobile.igor.screens.container.adventures.andamento.EditAdventureNameFragment;
import br.com.yimobile.igor.screens.container.adventures.andamento.PlayersFragment;
import br.com.yimobile.igor.screens.container.books.BooksFragment;
import br.com.yimobile.igor.screens.container.notifications.NotificationsFragment;
import br.com.yimobile.igor.screens.container.settings.SettingsFragment;
import database.Adventure;
import database.Session;
import database.User;

public class ContainerActivity extends AppCompatActivity
        implements AdventuresFragment.NewAdventureOnClickListener,
        NewAdventureFragment.CreateAdventureOnClickListener,
        SessionsFragment.ResumeOnClickListener,
        EditAdventureNameFragment.EditAdventureOnClickListener,
        PlayersFragment.PlayersOnClickListener {

    private static final String TAG = ContainerActivity.class.getSimpleName();
    private Toolbar toolbar;
    private Drawer navDrawer;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_home);

        setupToolbar();
        setupNavDrawer();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, new AdventuresFragment());
        ft.commit();
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
                navDrawer.openDrawer();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.nav_drawer_menu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void setupNavDrawer() {
        //if you want to update the items at a later time it is recommended to keep it in a variable
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1)
                .withName(R.string.nav_drawer_adventures)
                .withIcon(R.drawable.nav_drawer_adventures)
                .withTextColor(getResources().getColor(R.color.white))
                .withSelectedIcon(R.drawable.nav_drawer_adventures_selected);
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(2)
                .withName(R.string.nav_drawer_books)
                .withTextColor(getResources().getColor(R.color.white))
                .withIcon(R.drawable.nav_drawer_books)
                .withSelectedIcon(R.drawable.nav_drawer_books_selected);
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withIdentifier(3)
                .withName(R.string.nav_drawer_account)
                .withIcon(R.drawable.nav_drawer_account)
                .withTextColor(getResources().getColor(R.color.white))
                .withSelectedIcon(R.drawable.nav_drawer_account_selected);
        PrimaryDrawerItem item4 = new PrimaryDrawerItem().withIdentifier(4)
                .withName(R.string.nav_drawer_notifications)
                .withIcon(R.drawable.nav_drawer_notifications)
                .withTextColor(getResources().getColor(R.color.white))
                .withSelectedIcon(R.drawable.nav_drawer_notifications_selected);
        PrimaryDrawerItem item5 = new PrimaryDrawerItem().withIdentifier(5)
                .withName(R.string.nav_drawer_settings)
                .withIcon(R.drawable.nav_drawer_settings)
                .withTextColor(getResources().getColor(R.color.white))
                .withSelectedIcon(R.drawable.nav_drawer_settings_selected);
        PrimaryDrawerItem item6 = new PrimaryDrawerItem().withIdentifier(6)
                .withName(R.string.nav_drawer_exit)
                .withIcon(R.drawable.newadv_fechar)
                .withTextColor(getResources().getColor(R.color.white))
                .withSelectedIcon(R.drawable.newadv_fechar);

        //create the drawer and remember the `Drawer` navDrawer object
        navDrawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withSliderBackgroundDrawable(getResources().getDrawable(R.drawable.nav_drawer_bg))
                .addDrawerItems(
                        item1,
                        item2,
                        item3,
                        item4,
                        item5,
                        item6
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                        switchFragment(position);
                        Log.d(TAG, position + " Clicked");
                        navDrawer.closeDrawer();
                        return true;
                    }
                })
                .build();
    }

    private void switchFragment(int position) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        switch (position) {
            case 0:
                ft.replace(R.id.container, new AdventuresFragment()).commit();
                break;
            case 1:
                ft.replace(R.id.container, new BooksFragment()).commit();
                break;
            case 2:
                ft.replace(R.id.container, new AccountFragment()).commit();
                break;
            case 3:
                ft.replace(R.id.container, new NotificationsFragment()).commit();
                break;
            case 4:
                ft.replace(R.id.container, new SettingsFragment()).commit();
                break;
            case 5:
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                finish();
                break;
            default:
                break;
        }
    }


    @Override
    public void onNewAdventureClicked() {
        Log.d(TAG, "New Adventure Button Clicked");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, new NewAdventureFragment()).commit();
    }

    @Override
    public void onAdventureItemClicked(int itemPosition) {
        Log.d(TAG, "Selected item " + itemPosition);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, new SessionsFragment()).commit();
    }

    @Override
    public void onAdventureCreated(final String name) {
        Log.d(TAG, "Adventure Created");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, new AdventuresFragment(), "AdventuresFragment").commit();
        getSupportFragmentManager().executePendingTransactions();

        AdventuresFragment adventuresFragment = (AdventuresFragment)
                getSupportFragmentManager().findFragmentByTag("AdventuresFragment");
        if (adventuresFragment != null) {

            List<String> jog = new ArrayList<String>();
            List<Session> ses = new ArrayList<Session>();
            final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            jog.add(uid);
            //ses.add(new Session("",""));

            Adventure adv = new Adventure(name, "", uid,  jog, ses);
            mDatabase.child("adventure").child(name).setValue(adv);

            adventuresFragment.addNewAdventure(adv);

            mDatabase.child("users").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User active = dataSnapshot.getValue(User.class);
                        List<String> aventuras = active.getAventuras();
                        Map<String, Object> postValues = new HashMap<String,Object>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            postValues.put(snapshot.getKey(), snapshot.getValue());
                        }
                        if(aventuras == null) aventuras = new ArrayList<String>();
                        aventuras.add(name);
                        postValues.put("aventuras", aventuras);
                        mDatabase.child("users").child(uid).updateChildren(postValues);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
        }
    }

    @Override
    public void onPlayersPressed(){
        Log.d(TAG, "Players Pressed");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, new PlayersFragment()).commit();
    }

    @Override
    public void onEditAdventurePressed(){
        Log.d(TAG, "Edit Adventure Pressed");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, new EditAdventureNameFragment()).commit();
    }

    @Override
    public void newSessionPressed() {
        Log.d(TAG, "Create new session clicked");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, new NewSessionFragment()).commit();
    }

    public void newPlayerPressed() {
        Log.d(TAG, "Create new player clicked");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, new NewPlayerFragment()).commit();
    }

    @Override
    public void onAdventureEdited(String name) {
        Log.d(TAG, "Adventure Edited");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, new SessionsFragment(), "SessionsFragment").commit();
        getSupportFragmentManager().executePendingTransactions();

        SessionsFragment adventuresFragment = (SessionsFragment)
                getSupportFragmentManager().findFragmentByTag("SessionsFragment");
        if (adventuresFragment != null) {
            adventuresFragment.changeAdventureName(name);
        }
    }

    @Override
    public void onResumePressed(){
        Log.d(TAG, "Resume Adventure Pressed");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, new SessionsFragment()).commit();
    }

}
