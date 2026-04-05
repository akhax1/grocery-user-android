package com.kkgrocery.user;

import android.content.Intent;
import android.net.Uri;
import com.kkgrocery.user.web.WebFragment;

import android.content.ActivityNotFoundException;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.view.MenuItem;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;
import com.onesignal.OneSignal;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private final Context context = this;
    private Fragment currentFragment;
    private Listener listener;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        OneSignal.getDeviceState();
         Objects.requireNonNull(OneSignal.getDeviceState()).getUserId();
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
//                WindowManager.LayoutParams.FLAG_SECURE);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);




        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

         if (savedInstanceState != null) {
            currentFragment = getSupportFragmentManager().getFragment(savedInstanceState, "currentFragment");
        } else {
             OneSignal.getDeviceState();
             String userIdd = OneSignal.getDeviceState().getUserId();
            currentFragment = WebFragment.newInstance( "https://groceryup.in/app/index.php?onesignal_id=" + userIdd, "mailto:,tel:,upi:,market:,play.google,vid:,whatsapp:", false, true, false);

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.frameLayoutMain, currentFragment);
            fragmentTransaction.commit();
        }

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        getSupportFragmentManager().putFragment(outState, "currentFragment", currentFragment);
    }

    @Override
    public void onBackPressed() {
//        finish();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }

        if (listener != null) {
            if (listener.onBackPressed()) return;
        }

        super.onBackPressed();{
            finish();
        };
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        OneSignal.getDeviceState();
        String userIdd = OneSignal.getDeviceState().getUserId();
		switch (id) {

			case R.id.section1: replaceFragment(WebFragment.newInstance( "https://groceryup.in/app/index.php?onesignal_id=" + userIdd, "mailto:,tel:,upi:,market:,play.google,vid:,whatsapp:", false, true, false)); break;
			case R.id.section2:
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/")));
				break;
		case R.id.nav_rate: startRate(); break;
		}

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutMain, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        currentFragment = fragment;
    }

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		if (currentFragment instanceof WebFragment) {
			currentFragment.onActivityResult(requestCode, resultCode, intent);
		}
	}

	public void startRate() {
		Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
		Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
		// To count with Play market backstack, After pressing back button,
		// to taken back to our application, we need to add following flags to intent.
		goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
			Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
			Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
		try {
			context.startActivity(goToMarket);
		} catch (ActivityNotFoundException e) {
			context.startActivity(new Intent(Intent.ACTION_VIEW,
				Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
		}
	}


    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        if (fragment instanceof Listener)
            listener = (Listener) fragment;
        else
            listener = null;
    }

    public interface Listener {
        boolean onBackPressed();
    }

//    @Override
//    public void onBackPressed() {
//
//        finish();
//
//    }
}


