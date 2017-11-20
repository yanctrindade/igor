package br.com.yimobile.igor.screens.container;

import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;
import java.util.Calendar;
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
import database.Notifications;
import database.Session;
import database.User;

import static br.com.yimobile.igor.screens.auth.LoginActivity.dateToString;
import static br.com.yimobile.igor.screens.auth.LoginActivity.stringToDate;


public class ContainerActivity extends AppCompatActivity
        implements AdventuresFragment.NewAdventureOnClickListener,
        NewAdventureFragment.CreateAdventureOnClickListener,
        SessionsFragment.ResumeOnClickListener,
        EditAdventureNameFragment.EditAdventureOnClickListener,
        PlayersFragment.PlayersOnClickListener,
        NewSessionFragment.NewSessionOnClickListener {

    private static final String TAG = ContainerActivity.class.getSimpleName();
    private Toolbar toolbar;
    private Drawer navDrawer;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private User user;
    private List<Adventure> userAdventures;
    private String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    private SwipeRefreshLayout swipeNotifyContainer;
    private RelativeLayout notification;
    private TextView Title, noNotification;
    static List<Notifications> arrayNotifications;
    public static MenuItem menuItem;
    NotificationAdapter notificationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        user = getUserDatabase();
        setupToolbar();
        setupNavDrawer();

        Title = findViewById(R.id.Title);
        noNotification = findViewById(R.id.noNotification);
        Title.setVisibility(View.INVISIBLE);
        notification = findViewById(R.id.notification_center);
        notification.setVisibility(View.INVISIBLE);

        if(user != null){
            arrayNotifications = getUser().getNotifications();
        } else{
            arrayNotifications = new ArrayList<>();
        }

        RecyclerView notList = findViewById(R.id.notificationList);
        notList.setHasFixedSize(false);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        notList.setLayoutManager(llm);

        notificationAdapter = new NotificationAdapter(arrayNotifications, this);
        notList.setAdapter(notificationAdapter);

        swipeNotifyContainer = findViewById(R.id.notification_refresh_scroll);

        if(swipeNotifyContainer != null) {
            swipeNotifyContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);

            swipeNotifyContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    getUserDatabase();
                }
            });
        }

        updateNotification();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, new AdventuresFragment());
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        inflater.inflate(R.menu.notifications_menu, menu);

        menuItem = menu.findItem(R.id.notifications);
        int i = countUnread();
        if(i > 0) {
            menuItem.setIcon(buildCounterDrawable(countUnread(), R.drawable.nav_drawer_notifications_selected));
        } else {
            menuItem.setIcon(buildCounterDrawable(0, R.drawable.nav_drawer_notifications));
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                navDrawer.openDrawer();
                return true;
            case R.id.notifications:
                if(notification.getVisibility() == View.VISIBLE) {
                    notification.setVisibility(View.INVISIBLE);
                    Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_out_down);
                    notification.setAnimation(animation);
                } else {
                    notification.setVisibility(View.VISIBLE);
                    Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_in_down);
                    notification.setAnimation(animation);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed(){
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            Log.i("MainActivity", "popping backstack");
            fm.popBackStack();
        } else {
            Log.i("MainActivity", "nothing on backstack, calling super");
            super.onBackPressed();
        }
    }

    private void updateNotification() {
        if (arrayNotifications == null || arrayNotifications.size() == 0) {
            //Log.d("Notification: ", "VAZIO");
            Title.setVisibility(View.INVISIBLE);
            noNotification.setVisibility(View.VISIBLE);
        } else {
            //Log.d("Notification: ", arrayNotifications.toString());
            Title.setVisibility(View.VISIBLE);
            noNotification.setVisibility(View.INVISIBLE);
        }
    }

    public static int countUnread(){
        int contador = 0;
        if(arrayNotifications != null) {
            Notifications current;
            for (int i = 0; i < arrayNotifications.size(); i++) {
                current = arrayNotifications.get(i);
                if (current.getDataRecebimento() == null)
                    contador++;
            }
        }
        return contador;
    }

    private void updateNotificationCounter(){
        if(notificationAdapter != null){
            notificationAdapter.clear();
            notificationAdapter.addAll(arrayNotifications);
        }

        updateNotification();

        int i = countUnread();
        if(countUnread() > 0) {
            menuItem.setIcon(buildCounterDrawable(countUnread(), R.drawable.nav_drawer_notifications_selected));
        } else{
            menuItem.setIcon(buildCounterDrawable(countUnread(), R.drawable.nav_drawer_notifications));
        }
    }

    private Drawable buildCounterDrawable(int count, int backgroundImageId) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.unread_background, null);
        view.setBackgroundResource(backgroundImageId);

        if (count == 0) {
            View counterTextPanel = view.findViewById(R.id.counterValuePanel);
            counterTextPanel.setVisibility(View.GONE);
        } else {
            TextView textView = view.findViewById(R.id.count);
            textView.setText(String.valueOf(count));
        }

        view.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        return new BitmapDrawable(getResources(), bitmap);
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

    public Fragment getVisibleFragment(){
        android.support.v4.app.FragmentManager fragmentManager = ContainerActivity.this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if(fragments != null){
            for(Fragment fragment : fragments){
                if(fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        return null;
    }

    public void setUser(User user){
        this.user = user;
    }

    public User getUser(){
        if(user == null) return getUserDatabase();
        Log.d("USERV", user.getEmail());
        return user;
    }

    public String getUid(){
        return uid;
    }

    private User getUserDatabase(){
        Query dataUser = mDatabase.child("users").orderByKey().equalTo(uid);
        dataUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    user = singleSnapshot.getValue(User.class);
                    if (user != null) {
                        boolean removed = false;
                        for(int i = 0; i < user.getNotifications().size(); i++){
                            Notifications notification = user.getNotifications().get(i);
                            Calendar dataAgenda = stringToDate(notification.getDataAgenda(), "dd/MM/yyyy");
                            Calendar dataAtual = Calendar.getInstance();
                            dataAtual.add(Calendar.DATE, -1);
                            if(dataAtual.after(dataAgenda)){
                                user.removeNotification(i);
                                removed = true;
                            }
                        }
                        if(removed) {
                            Map<String, Object> postValues = new HashMap<>();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                postValues.put(snapshot.getKey(), snapshot.getValue());
                            }

                            postValues.put("notifications", user.getNotifications());
                            mDatabase.child("users").child(uid).updateChildren(postValues);
                        }
                    }

                    arrayNotifications = getUser().getNotifications();

                    Fragment fragment = getVisibleFragment();
                    if(fragment instanceof AdventuresFragment){
                        getUserAdventuresDatabase();
                    } else if(fragment instanceof AccountFragment){
                        ((AccountFragment) fragment).fillFragment(user);
                    }

                    updateNotificationCounter();
                    swipeNotifyContainer.setRefreshing(false);

                    Log.d("USERN", user.getEmail());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("ERROR", "onCancelled", databaseError.toException());
            }
        });

        return user;
    }

    public List<Adventure> getUserAdventures(){
        if(userAdventures == null) {
            userAdventures = new ArrayList<>();
            return getUserAdventuresDatabase();
        }
        return userAdventures;
    }

    public void refreshUserAdventure(){
        getUserAdventuresDatabase();
    }

    private List<Adventure> getUserAdventuresDatabase(){
        if(user != null) {
            List<String> aventuras = user.getAventuras();
            if (aventuras != null && !aventuras.isEmpty()) {
                userAdventures.clear();
                Log.d("ADVLIST", aventuras.size() + " TAMANHO");
                for (int i = aventuras.size() - 1; i >= 0; i--) {
                    final int finalI = i;
                    if(aventuras.get(i) != null) {
                        mDatabase.child("adventure").child(aventuras.get(i))
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Adventure adventure = dataSnapshot.getValue(Adventure.class);
                                        userAdventures.add(adventure);
                                        if (finalI == 0) {
                                            Fragment fragment = getVisibleFragment();
                                            if (fragment instanceof AdventuresFragment) {
                                                ((AdventuresFragment) fragment).fillFragment(userAdventures);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });
                    }
                }
            }
        }
        return userAdventures;
    }

    private void setNewNotification(final String nomeSessao, final String dataAgenda, final Adventure adventure){
        Log.d(TAG, "Notification Created");

        for(final String userID : adventure.getJogadores()) {

            mDatabase.child("users").child(userID)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User aux = dataSnapshot.getValue(User.class);
                            Map<String, Object> postValues = new HashMap<>();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                postValues.put(snapshot.getKey(), snapshot.getValue());
                            }

                            if (aux != null) {
                                Notifications notification = new Notifications(userID,
                                        adventure.getMestre(), adventure.getNome(), nomeSessao, dataAgenda,
                                        dateToString(Calendar.getInstance(), "dd/MM/yyyy"), null);

                                if(userID.equals(uid)) {
                                    user.addNotification(notification);
                                    arrayNotifications = user.getNotifications();
                                    updateNotificationCounter();
                                }
                                aux.addNotification(notification);
                                postValues.put("notifications", aux.getNotifications());

                                mDatabase.child("users").child(userID).updateChildren(postValues);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

        }

    }

    public void setNotificationReceived(final Notifications notification, final int position){
        Log.d(TAG, "Notification Received");

        mDatabase.child("users").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, Object> postValues = new HashMap<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            postValues.put(snapshot.getKey(), snapshot.getValue());
                        }

                        user.changeNotification(position, notification);
                        arrayNotifications = user.getNotifications();
                        updateNotificationCounter();

                        postValues.put("notifications", user.getNotifications());

                        mDatabase.child("users").child(uid).updateChildren(postValues);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

    }

    @Override
    public void onNewAdventureClicked() {
        Log.d(TAG, "New Adventure Button Clicked");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, new NewAdventureFragment()).commit();
    }

    @Override
    public void onAdventureItemClicked(int itemPosition, Adventure adventure) {
        Log.d(TAG, "Selected item " + itemPosition);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        SessionsFragment sf = new SessionsFragment();
        ft.replace(R.id.container, sf).commit();
        sf.SetAdventure(adventure);
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

            List<String> jog = new ArrayList<>();
            List<Session> ses = new ArrayList<>();
            jog.add(uid);

            Adventure adv = new Adventure(name, "", uid,  jog, ses);
            mDatabase.child("adventure").child(name).setValue(adv);

            userAdventures.add(0, adv);
            adventuresFragment.fillFragment(userAdventures);

            mDatabase.child("users").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User active = dataSnapshot.getValue(User.class);
                        List<String> aventuras = active != null ? active.getAventuras() : null;
                        Map<String, Object> postValues = new HashMap<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            postValues.put(snapshot.getKey(), snapshot.getValue());
                        }
                        if(aventuras == null) aventuras = new ArrayList<>();
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
    public void onPlayersPressed(Adventure adventure){
        Log.d(TAG, "Players Pressed");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        PlayersFragment pf = new PlayersFragment();
        ft.replace(R.id.container, pf).commit();
        pf.SetAdventure(adventure);
    }

    @Override
    public void onEditAdventurePressed(Adventure adventure){
        Log.d(TAG, "Edit Adventure Pressed");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        EditAdventureNameFragment eanf = new EditAdventureNameFragment();
        ft.replace(R.id.container, eanf).addToBackStack(null).commit();
        eanf.SetAdventure(adventure);
    }

    @Override
    public void newSessionPressed(Adventure adventure) {
        Log.d(TAG, "Create new session clicked");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        NewSessionFragment nsf = new NewSessionFragment();
        ft.replace(R.id.container, nsf).addToBackStack(null).commit();
        nsf.SetAdventure(adventure);
    }

    @Override
    public void onSessionCreated(final String name, final String data, final Adventure adventure) {
        Log.d(TAG, "Session Created");
        Session session = new Session(name, data);
        adventure.addSessao(session);
        mDatabase.child("adventure").child(adventure.getNome())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, Object> postValues = new HashMap<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            postValues.put(snapshot.getKey(), snapshot.getValue());
                        }
                        postValues.put("sessoes", adventure.getSessoes());
                        mDatabase.child("adventure").child(adventure.getNome()).updateChildren(postValues);
                        setNewNotification(name, data, adventure);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });

    }

    public void newPlayerPressed() {
        Log.d(TAG, "Create new player clicked");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, new NewPlayerFragment()).commit();
    }

    @Override
    public void onAdventureEdited(Adventure adventure, final String name) {
        Log.d(TAG, "Adventure Edited");
        final String oldName = adventure.getNome();
        adventure.setNome(name);
        mDatabase.child("adventure").child(name).setValue(adventure);
        mDatabase.child("adventure").child(oldName).removeValue();

        mDatabase.child("users").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User active = dataSnapshot.getValue(User.class);
                        List<String> aventuras = active.getAventuras();
                        Map<String, Object> postValues = new HashMap<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            postValues.put(snapshot.getKey(), snapshot.getValue());
                        }
                        if(aventuras == null) aventuras = new ArrayList<>();
                        for(int i = 0; i < aventuras.size(); i++){
                            if(aventuras.get(i).equals(oldName)){
                                aventuras.remove(i);
                                break;
                            }
                        }
                        aventuras.add(name);
                        postValues.put("aventuras", aventuras);
                        mDatabase.child("users").child(uid).updateChildren(postValues);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
    }

    @Override
    public void onResumePressed(Adventure adventure){
        Log.d(TAG, "Resume Adventure Pressed");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        SessionsFragment sf = new SessionsFragment();
        ft.replace(R.id.container, sf).commit();
        sf.SetAdventure(adventure);
    }

}
