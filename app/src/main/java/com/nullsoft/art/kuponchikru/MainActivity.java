package com.nullsoft.art.kuponchikru;

/**
 * Created by art on 01.04.16.
 */
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;


public class MainActivity extends AppCompatActivity {

    Drawer mainDrawer;



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
                .withProfileImagesVisible(false)
//                .withCompactStyle(true)
//                .withTextColorRes(R.color.black)
                .addProfiles(
                        new ProfileDrawerItem().withName("Сиротин Артём").withEmail("sirotinart@gmail.com")
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
                .withSelectedItem(6)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_item_offers).withIcon(FontAwesome.Icon.faw_shopping_bag).withSelectable(false).withIdentifier(1),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_history).withIcon(FontAwesome.Icon.faw_archive).withSelectable(false).withIdentifier(2),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_profile).withIcon(FontAwesome.Icon.faw_user).withSelectable(false).withIdentifier(3),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_about).withIcon(FontAwesome.Icon.faw_question_circle).withSelectable(false).withIdentifier(4),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_exit).withIcon(FontAwesome.Icon.faw_sign_out).withSelectable(false).withIdentifier(5)
                )
                .withOnDrawerItemClickListener(new DrawerClickListener())
                .withCloseOnClick(true)
                .build();
        FragmentMain test=new FragmentMain();

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, test).commit();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

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
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    class DrawerClickListener implements Drawer.OnDrawerItemClickListener
    {
        @Override
        public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
            Log.d("Click: ", String.valueOf(position) );
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

            switch (position)
            {
                case 1:
                {

                    toolbar.setTitle(R.string.main_screen_name);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new FragmentMain())
                            //.addToBackStack(null)
                            .commit();
                    break;
                }
                case 2:
                {
                    toolbar.setTitle(R.string.history_screen_name);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new FragmentHistory())
                            //.addToBackStack(null)
                            .commit();
                    break;
                }
                case 3:
                {
                    MainActivity context = (MainActivity) view.getContext();
                    Intent intent = new Intent(context, ProfileActivity.class);
                    startActivity(intent);
                    Log.d("Lol", "lol3");
                    break;
                }
                case 4:
                {
                    //показываем всплывающее окошко с инфой
                    MainActivity context = (MainActivity) view.getContext();
                    AboutDialog dlg1=new AboutDialog();
                    dlg1.show(context.getFragmentManager(),"dlg1");
                    break;
                }
                case 5:
                {
                    //очищаем кэш бд, закрываем приложение
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

    public  void showAddToFavoriteMenu(View v)
    {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.add_to_favorite, popup.getMenu());
        popup.show();
    }


}
