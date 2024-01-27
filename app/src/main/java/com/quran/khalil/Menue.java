package com.quran.khalil;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

public class Menue extends AppCompatActivity {
    MeowBottomNavigation bottomNavigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        bottomNavigation=findViewById(R.id.navi);
        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.baseline_home_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.idea));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.leaderboard));
        bottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.person));
        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
//                Toast.makeText(getApplicationContext(),"Clicked " +item.getId(),Toast.LENGTH_LONG).show();
            }
        });
        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                Fragment fragment;
//                if (item.getId() == 2) {
//                    fragment=new Idea();
//                }else if (item.getId() == 3) {
//                    fragment=new Leaderboard;
//                }else if (item.getId() == 4) {
//                    fragment=new Profile();
//                } else
                    fragment = new Home();
                loadFragment(fragment);
            }
        });
        bottomNavigation.show(1,true);
        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                Toast.makeText(getApplicationContext(), "Reselected " + item.getId(), Toast.LENGTH_LONG).show();
            }
        });

//        bottomNavigation.setCount(4,"5");

    }
    //define a load method to feed the screen
    private void loadFragment(Fragment fragment) {
        //replace the fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frag,fragment, null)
                .commit();
    }
}