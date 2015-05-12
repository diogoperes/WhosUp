package com.whosup.android.whosup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.whosup.android.whosup.utils.SPreferences;
import com.whosup.drawer.FragmentDrawer;
import com.whosup.drawer.fragments.Friends;
import com.whosup.drawer.fragments.CreateInviteActivity;
import com.whosup.drawer.fragments.Messages;
import com.whosup.listview.Invite;
import com.whosup.listview.InviteAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener, AdapterView.OnItemClickListener {

    private ArrayList<Invite> inviteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inviteList = new ArrayList<>();

        //TEST INVITES
        inviteList.add(new Invite(R.drawable.ic_launcher, "Antonio", "Colombo", "3"));
        inviteList.add(new Invite(R.mipmap.ic_home, "Nuno", "Vasco da Gama", "8"));
        inviteList.add(new Invite(R.drawable.ic_launcher, "Diogo", "Horta", "0,5"));
        inviteList.add(new Invite(R.mipmap.ic_home, "Jose", "Benfica", "3"));
        inviteList.add(new Invite(R.drawable.ic_launcher, "Mario", "Mafra", "18"));

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get the ListView by Id and instantiate the adapter with
        // invite data and then set it the ListView
        ListView inviteListView = (ListView) findViewById(R.id.list_invites);
        InviteAdapter adapter = new InviteAdapter(this, inviteList);
        inviteListView.setAdapter(adapter);
        // Set the onItemClickListener on the ListView to listen for items clicks
        inviteListView.setOnItemClickListener(this);

        FragmentDrawer drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                Toast.makeText(MainActivity.this.getApplicationContext(), "jowf", Toast.LENGTH_LONG).show();
                return true;
            case R.id.action_exit:
                System.exit(0);
                return true;
            case R.id.action_logout:
                SPreferences.getInstance().discardLoginCredentials(getApplicationContext());
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new CreateInviteActivity();
                title = getString(R.string.create_invite);
                break;
            case 1:
                fragment = new Friends();
                title = getString(R.string.title_friends);
                break;
            case 2:
                fragment = new Messages(    );
                title = getString(R.string.title_messages);
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            // set the toolbar title
            setTitle(title);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Invite selectedInvite = inviteList.get(position);
        Toast.makeText(this, "You've selected :\n Invite from : " + selectedInvite.getFrom() + "\n Place : " + selectedInvite.getPlace(), Toast.LENGTH_SHORT).show();
    }
}
