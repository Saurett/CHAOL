package com.indev.chaol;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.Menu;
import android.view.MenuItem;

import com.indev.chaol.utils.Constants;

public class NavigationDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        /**Siempre antes de  "navigationView.setNavigationItemSelectedListener(this)" **/
        this.onPreRender(navigationView);

        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * Carga valores iniciales en vista
     * setMenuTitleColor (Carga el color en los titulos de los grupos)
     */
    public void onPreRender(NavigationView navigationView) {
        Menu menu = navigationView.getMenu();

        onNavigationItemSelected(navigationView.getMenu().getItem(0));

        setMenuTitleColor(menu, R.id.menu_title_administracion);
        setMenuTitleColor(menu, R.id.menu_title_fletes);
        setMenuTitleColor(menu, R.id.menu_title_cuentas);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        /*
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        */

        return super.onOptionsItemSelected(item);
    }

    /**
     * @param menu
     * @param id   Configura el color de los Titulos del menu.
     * @autor saurett / InDev
     */
    private void setMenuTitleColor(Menu menu, int id) {
        MenuItem menuItem = menu.findItem(id);
        SpannableString ss = new SpannableString(menuItem.getTitle());
        ss.setSpan(new TextAppearanceSpan(this, R.style.MenuItemTitleStyle), 0, ss.length(), 0);
        menuItem.setTitle(ss);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_item_inicio:
                setTitle(item.getTitle());
                this.closeFragment(this.getLastFragment());
                this.openFragment(Constants.ITEM_MENU_FRAGMENT.get(id));
                break;
            case R.id.menu_item_clientes:
                setTitle(item.getTitle());
                this.closeFragment(this.getLastFragment());
                break;
            case R.id.menu_item_transportistas:
                setTitle(item.getTitle());
                this.closeFragment(this.getLastFragment());
                break;
            case R.id.menu_item_choferes:
                setTitle(item.getTitle());
                this.closeFragment(this.getLastFragment());
                break;
            case R.id.menu_item_tractores:
                setTitle(item.getTitle());
                this.closeFragment(this.getLastFragment());
                break;
            case R.id.menu_item_remolques:
                setTitle(item.getTitle());
                this.closeFragment(this.getLastFragment());
                break;
            case R.id.menu_item_agenda:
                setTitle(item.getTitle());
                this.closeFragment(this.getLastFragment());
                break;
            case R.id.menu_item_perfil:
                setTitle(item.getTitle());
                this.closeFragment(this.getLastFragment());
                break;
            case R.id.menu_item_cerrar_session:
                finish();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**Valida el tag enviado y cierra si existe el fragmento**/
    private void closeFragment(String tag) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment != null)
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();

    }

    /**Abre el fragmento mediante el tag seleccionado**/
    private void openFragment(String tag) {
        FragmentTransaction mainFragment = getSupportFragmentManager().beginTransaction();
        mainFragment.add(R.id.fragment_main_container, Constants.TAG_FRAGMENT.get(tag), tag);
        mainFragment.addToBackStack(tag);
        mainFragment.commit();
    }

    /**Obtiene el ultimo fragmento mediante almacenamiento por BackStackEntry en fragmentManager**/
    private String getLastFragment() {
        String name = "";

        /**Verifica si existe contenido en BackStackEntry y obtiene el ultimo fragmento**/
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            int index = getSupportFragmentManager().getBackStackEntryCount() - 1;
            FragmentManager.BackStackEntry backEntry = getSupportFragmentManager().getBackStackEntryAt(index);
            String tag = backEntry.getName();
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);

            /**Verifica si el fragmento aun existe en el manager**/
            if (fragment != null) name = fragment.getTag();
        }

        return name;
    }
}
