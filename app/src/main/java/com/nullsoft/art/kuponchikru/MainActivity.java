package com.nullsoft.art.kuponchikru;

/**
 * Created by art on 01.04.16.
 */

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;


public class MainActivity extends AppCompatActivity {

    Drawer mainDrawer;

    private FragmentMain fragmentMain;
    private FragmentHistory fragmentHistory;
    private FragmentFavorites fragmentFavorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.main_screen_name);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .withProfileImagesVisible(true)
//                .withCompactStyle(false)
//                .withTextColorRes(R.color.black)
                .addProfiles(
                        new ProfileDrawerItem().withName(UserController.getController().getUserName())
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .withSelectionListEnabled(false)
                .build();

        mainDrawer=new DrawerBuilder()
                .withActivity(this)
                .withFullscreen(true)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .withAccountHeader(headerResult)
                .withSelectedItem(7)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_item_offers).withIcon(FontAwesome.Icon.faw_shopping_bag).withSelectable(false).withIdentifier(1),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_history).withIcon(FontAwesome.Icon.faw_archive).withSelectable(false).withIdentifier(2),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_favorites).withIcon(FontAwesome.Icon.faw_star).withSelectable(false).withIdentifier(3),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_profile).withIcon(FontAwesome.Icon.faw_user).withSelectable(false).withIdentifier(4),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_about).withIcon(FontAwesome.Icon.faw_question_circle).withSelectable(false).withIdentifier(5),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_exit).withIcon(FontAwesome.Icon.faw_sign_out).withSelectable(false).withIdentifier(6)
                )
                .withOnDrawerItemClickListener(new DrawerClickListener())
                .build();

        ProgressBar mProgress = (ProgressBar) findViewById(R.id.progressBar);
        mProgress.getIndeterminateDrawable().setColorFilter(Color.WHITE,android.graphics.PorterDuff.Mode.SRC_IN);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,getFragmentMain()).commit();

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    class DrawerClickListener implements Drawer.OnDrawerItemClickListener
    {
        @Override
        public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

            mainDrawer.closeDrawer();


            switch (position)
            {
                case 1:
                {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,getFragmentMain())
                            .commit();
                    break;
                }
                case 2:
                {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,getFragmentHistory())
                            .commit();
                    break;
                }
                case 3:
                {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,getFragmentFavorites())
                            .commit();
                    break;
                }
                case 4:
                {
                    MainActivity context = (MainActivity) view.getContext();
                    Intent intent = new Intent(context, ProfileActivity.class);
                    startActivity(intent);
                    Log.d("Lol", "lol3");
                    break;
                }
                case 5:
                {
                    MainActivity context = (MainActivity) view.getContext();
                    AboutDialog dlg1=new AboutDialog();
                    dlg1.show(context.getFragmentManager(),"dlg1");
                    break;
                }
                case 7:
                {
                    UserController.getController().logout();
                    finish();
                    break;
                }
            }
            return false;
        }
    }

    @Override
    public void onBackPressed()
    {
        if(mainDrawer.isDrawerOpen())
        {
            mainDrawer.closeDrawer();
        }
        else
        {
            super.onBackPressed();
        }
    }

    private FragmentMain getFragmentMain()
    {
        if (fragmentMain==null)
        {
            fragmentMain=new FragmentMain();
        }

        return fragmentMain;
    }

    private FragmentHistory getFragmentHistory()
    {
        if (fragmentHistory==null)
        {
            fragmentHistory=new FragmentHistory();
        }

        return fragmentHistory;
    }

    private FragmentFavorites getFragmentFavorites()
    {
        if (fragmentFavorites==null)
        {
            fragmentFavorites=new FragmentFavorites();
        }

        return fragmentFavorites;
    }
}
