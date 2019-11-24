package com.docs.telemedicine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView mMainNav;
    private FrameLayout mMainFrame;
    private Profile_Frag profileFragment;
    private Meetings_Frag meetingsFragment;
    private Records_FRag recordsFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMainFrame = (FrameLayout)findViewById(R.id.main_frame);
        mMainNav = (BottomNavigationView)findViewById((R.id.main_nav));
        profileFragment = new Profile_Frag();
        recordsFragment = new Records_FRag();
        meetingsFragment = new Meetings_Frag();
        setFragment(profileFragment);
        mMainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
    switch (menuItem.getItemId())
{
    case (R.id.nav_profile):{

        setFragment(profileFragment);
        return true;
    }
    case (R.id.nav_meetings):{

        setFragment(meetingsFragment);
        return true;
    }
    case (R.id.nav_records):{

        setFragment(recordsFragment);
        return true;
    }
    default :
        return false;
}

            }
        });
    }

    private void setFragment(Fragment inputFragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame,inputFragment);
        fragmentTransaction.commit();
    }
}
