package com.indev.chaol;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.indev.chaol.fragments.ChoferesFragment;
import com.indev.chaol.fragments.ClientesFragment;
import com.indev.chaol.fragments.RemolquesFragment;
import com.indev.chaol.fragments.TractoresFragment;
import com.indev.chaol.fragments.TransportistasFragment;
import com.indev.chaol.fragments.interfaces.NavigationDrawerInterface;
import com.indev.chaol.models.Administradores;
import com.indev.chaol.models.Choferes;
import com.indev.chaol.models.Clientes;
import com.indev.chaol.models.DecodeExtraParams;
import com.indev.chaol.models.DecodeItem;
import com.indev.chaol.models.Transportistas;
import com.indev.chaol.models.Usuarios;
import com.indev.chaol.utils.Constants;
import com.indev.chaol.utils.DateTimeUtils;

import java.util.List;

public class NavigationDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NavigationDrawerInterface, DialogInterface.OnClickListener, DrawerLayout.DrawerListener {

    private static final String TAG = NavigationDrawerActivity.class.getName();

    /**
     * Variable que almacena el ultimo item que fue seleccionado en el navigation
     **/
    private static MenuItem lastMenuItem;
    /**
     * Variable global para tener acceso al navigationDrawer
     **/
    private static NavigationView navigationView;
    /**
     * Variable globar para tener acceso a los datos (Basicos) de la session
     */
    private static Usuarios _SESSION_USER;
    private ProgressDialog pDialog;
    private static DecodeItem _decodeItem;

    /*** Declaraciones para Firebase**/
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

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

        drawer.setDrawerListener(this);


        _SESSION_USER = (Usuarios) getIntent().getExtras().getSerializable(Constants.KEY_SESSION_USER);

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        /**Obtiene la instancia compartida del objeto FirebaseAuth**/
        mAuth = FirebaseAuth.getInstance();
        /**Responde a los cambios de estato en la session**/
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

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

        /**Carga las opciones que debe de ver cada usuario en el menu**/
        onPreRenderSessionMenu(menu);

        onNavigationItemSelected((lastMenuItem != null) ? lastMenuItem : navigationView.getMenu().getItem(0));

        setMenuTitleColor(menu, R.id.menu_title_administracion);
        setMenuTitleColor(menu, R.id.menu_title_fletes);
        setMenuTitleColor(menu, R.id.menu_title_cuentas);
    }

    /**
     * Administra las opciones del menu por al tipo de usuario
     * Nota: El admin visualizara todas las opciones del menu
     **/
    private void onPreRenderSessionMenu(Menu menu) {

        switch (_SESSION_USER.getTipoUsuario()) {
            case Constants.FB_KEY_ITEM_TIPO_USUARIO_CLIENTE:
                /**El cliente visualizara menu de fletes y de cuentas**/
                menu.findItem(R.id.menu_title_administracion).setVisible(false);
                break;
            case Constants.FB_KEY_ITEM_TIPO_USUARIO_TRANSPORTISTA:
                /**El cliente visualizara menu de administracion,fletes y de cuentas**/
                //Nota: Solo mostrar en admnistracion / choferes, tractores, remolques
                menu.findItem(R.id.menu_item_clientes).setVisible(false);
                menu.findItem(R.id.menu_item_transportistas).setVisible(false);
                break;
            case Constants.FB_KEY_ITEM_TIPO_USUARIO_CHOFER:
                /**El cliente visualizara menu de fletes y de cuentas**/
                menu.findItem(R.id.menu_title_administracion).setVisible(false);
                break;
            default:
                /**Sin restricciones para el admin**/
                break;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            //finish();
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

        switch (id) {
            case R.id.action_home:
                onChangeMainFragment(R.id.menu_item_inicio);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        lastMenuItem = item;

        switch (id) {
            case R.id.menu_item_inicio:
                setTitle(item.getTitle());
                this.closeFragment(this.getLastFragment());
                this.openFragment(Constants.ITEM_FRAGMENT.get(id));
                break;
            case R.id.menu_item_clientes:
                setTitle(item.getTitle());
                this.closeFragment(this.getLastFragment());
                this.openFragment(Constants.ITEM_FRAGMENT.get(id));
                break;
            case R.id.menu_item_transportistas:
                setTitle(item.getTitle());
                this.closeFragment(this.getLastFragment());
                this.openFragment(Constants.ITEM_FRAGMENT.get(id));
                break;
            case R.id.menu_item_choferes:
                setTitle(item.getTitle());
                this.closeFragment(this.getLastFragment());
                this.openFragment(Constants.ITEM_FRAGMENT.get(id));
                break;
            case R.id.menu_item_tractores:
                setTitle(item.getTitle());
                this.closeFragment(this.getLastFragment());
                this.openFragment(Constants.ITEM_FRAGMENT.get(id));
                break;
            case R.id.menu_item_remolques:
                setTitle(item.getTitle());
                this.closeFragment(this.getLastFragment());
                this.openFragment(Constants.ITEM_FRAGMENT.get(id));
                break;
            case R.id.menu_item_agenda:
                setTitle(item.getTitle());
                this.closeFragment(this.getLastFragment());
                this.openFragment(Constants.ITEM_FRAGMENT.get(id));
                break;
            case R.id.menu_item_perfil:
                setTitle(item.getTitle());
                this.closeFragment(this.getLastFragment());
                this.openFragment(Constants.ITEM_FRAGMENT.get(id));
                break;
            case R.id.menu_item_cerrar_session:
                /**Si se crean mas elementos al cerrar session, se creara un metodo**/
                lastMenuItem = null;
                FirebaseAuth.getInstance().signOut();
                finish();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    /**
     * Valida el tag enviado y cierra si existe el fragmento
     **/
    private void closeFragment(String tag) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment != null)
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();

    }

    /**
     * Abre el fragmento mediante el tag seleccionado
     **/
    private void openFragment(String tag) {
        FragmentTransaction mainFragment = getSupportFragmentManager().beginTransaction();
        mainFragment.replace(R.id.fragment_main_container, Constants.TAG_FRAGMENT.get(tag), tag);
        mainFragment.addToBackStack(tag);
        mainFragment.commit();
    }

    /**
     * Obtiene el ultimo fragmento mediante almacenamiento por BackStackEntry en fragmentManager
     **/
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

    /**
     * Interface la cual permite abrir un fragmento en una vista principal del menu
     **/
    @Override
    public void onChangeMainFragment(int idView) {
        try {
            MenuItem menuItem = navigationView.getMenu().findItem(idView);
            onNavigationItemSelected(menuItem);
            /**Forza el checkItem solo debe usarse de forma manual**/
            navigationView.setCheckedItem(idView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Interface para remover fragmentos secundarios
     **/
    @Override
    public void removeSecondaryFragment() {
        List<String> secondaryFragments = Constants.SECONDARY_TAG_FRAGMENTS;

        for (String tag :
                secondaryFragments) {
            closeFragment(tag);
        }
    }

    @Override
    public void showQuestion() {
        AlertDialog.Builder ad = new AlertDialog.Builder(this);

        ad.setTitle("Eliminar");
        ad.setMessage("¿Esta seguro que desea elminar?");
        ad.setCancelable(false);
        ad.setNegativeButton("Cancelar", this);
        ad.setPositiveButton("Aceptar", this);
        ad.show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        int operation = 0;

        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                switch (this.getDecodeItem().getIdView()) {
                    case R.id.item_btn_eliminar_cliente:
                        operation = Constants.WS_KEY_ELIMINAR_CLIENTES;
                        break;
                    case R.id.item_btn_eliminar_transportista:
                        operation = Constants.WS_KEY_ELIMINAR_TRANSPORTISTAS;
                        break;
                    case R.id.item_btn_eliminar_chofer:
                        operation = Constants.WS_KEY_ELIMINAR_CHOFERES;
                        break;
                    case R.id.item_btn_eliminar_tractor:
                        operation = Constants.WS_KEY_ELIMINAR_TRACTORES;
                        break;
                    case R.id.item_btn_eliminar_remolque:
                        operation = Constants.WS_KEY_ELIMINAR_REMOLQUES;
                        break;
                }

                AsyncCallWS wsDeleteClientes = new AsyncCallWS(operation);
                wsDeleteClientes.execute();

                break;
        }
    }

    @Override
    public void openExternalActivity(int action, Class<?> externalActivity) {
        DecodeExtraParams extraParams = new DecodeExtraParams();

        extraParams.setTituloActividad(getString(Constants.TITLE_ACTIVITY.get(this.getDecodeItem().getIdView())));
        extraParams.setTituloFormulario(getString(Constants.TITLE_FORM_ACTION.get(action)));
        extraParams.setAccionFragmento(action);
        extraParams.setFragmentTag(Constants.ITEM_FRAGMENT.get(this.getDecodeItem().getIdView()));
        extraParams.setDecodeItem(this.getDecodeItem());

        Intent intent = new Intent(this, externalActivity);
        intent.putExtra(Constants.KEY_MAIN_DECODE, extraParams);
        startActivity(intent);
    }

    @Override
    public void setDecodeItem(DecodeItem decodeItem) {
        _decodeItem = decodeItem;
    }

    @Override
    public DecodeItem getDecodeItem() {
        return _decodeItem;
    }

    @Override
    public void updateUserCliente(Clientes cliente) {
        pDialog = new ProgressDialog(NavigationDrawerActivity.this);
        pDialog.setMessage(getString(R.string.default_loading_msg));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        this.firebaseUpdateCliente(cliente);
    }

    private void firebaseUpdateCliente(Clientes cliente) {
        FirebaseUser user = mAuth.getCurrentUser();

        /**obtiene la instancia como cliente**/
        DatabaseReference dbCliente =
                FirebaseDatabase.getInstance().getReference()
                        .child("clientes");

        cliente.setTipoUsuario("cliente");
        cliente.setFirebaseId(user.getUid());
        cliente.setEstatus("activo");
        cliente.setContraseña(null);
        cliente.setFechaDeEdicion(DateTimeUtils.getTimeStamp());

        dbCliente.child(user.getUid()).child("cliente").setValue(cliente, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                pDialog.dismiss();

                if (databaseError == null) {
                    Toast.makeText(getApplicationContext(),
                            "Actualizado correctamente...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Log.i(TAG,"firebaseUpdateCliente: Actualizado correctamente" + user.getUid());
    }

    @Override
    public void updateUserTransportista(Transportistas transportista) {
        pDialog = new ProgressDialog(NavigationDrawerActivity.this);
        pDialog.setMessage(getString(R.string.default_loading_msg));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        this.firebaseUpdateTransportista(transportista);
    }

    private void firebaseUpdateTransportista(final Transportistas transportista) {
        final FirebaseUser user = mAuth.getCurrentUser();

        /**obtiene la instancia como transportista**/
        DatabaseReference dbTransportista =
                FirebaseDatabase.getInstance().getReference()
                        .child("transportistas");

        transportista.setTipoUsuario("transportista");
        transportista.setFirebaseId(user.getUid());
        transportista.setContraseña(null);
        transportista.setEstatus("activo");
        transportista.setFechaDeEdicion(DateTimeUtils.getTimeStamp());

        dbTransportista.child(user.getUid()).child("transportista").setValue(transportista, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                pDialog.dismiss();

                pDialog = new ProgressDialog(NavigationDrawerActivity.this);
                pDialog.setMessage(getString(R.string.default_loading_msg));
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(false);
                pDialog.show();

                if (databaseError == null) {

                    /**obtiene la instancia como listaDeTransportistas**/
                    DatabaseReference dbListaTransportista =
                            FirebaseDatabase.getInstance().getReference()
                                    .child("listaDeTransportistas");

                    dbListaTransportista.child(user.getUid()).setValue(transportista.getNombre(), new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            pDialog.dismiss();

                            if (databaseError == null) {
                                Toast.makeText(getApplicationContext(),
                                        "Actualizado correctamente...", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });


        Log.i(TAG,"firebaseRegistroTransportista: Registrado correctamente" + user.getUid());
    }

    @Override
    public void updateUserChofer(Choferes chofer) {
        pDialog = new ProgressDialog(NavigationDrawerActivity.this);
        pDialog.setMessage(getString(R.string.default_loading_msg));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        this.firebaseUpdateChofer(chofer);
    }

    private void firebaseUpdateChofer(final Choferes chofer) {

        final FirebaseUser user = mAuth.getCurrentUser();

        /**obtiene la instancia como chofer**/
        DatabaseReference dbChofer =
                FirebaseDatabase.getInstance().getReference()
                        .child("choferes");

        chofer.setTipoUsuario("chofer");
        chofer.setFirebaseId(user.getUid());
        chofer.setContraseña(null);
        chofer.setFechaDeEdicion(DateTimeUtils.getTimeStamp());

        dbChofer.child(user.getUid()).setValue(chofer, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                pDialog.dismiss();

                if (databaseError == null) {

                    pDialog = new ProgressDialog(NavigationDrawerActivity.this);
                    pDialog.setMessage(getString(R.string.default_loading_msg));
                    pDialog.setIndeterminate(false);
                    pDialog.setCancelable(false);
                    pDialog.show();

                    /**obtiene la instancia como transportista**/
                    DatabaseReference dbTransportista =
                            FirebaseDatabase.getInstance().getReference()
                                    .child("transportistas");

                    dbTransportista.child(chofer.getFirebaseIdTransportista())
                            .child("chofer").child(user.getUid()).setValue(chofer, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            pDialog.dismiss();

                            if (databaseError == null) {
                                Toast.makeText(getApplicationContext(),
                                        "Actualizado correctamente...", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        Log.i(TAG, "firebaseRegistroChoferes: Actualizado correctamente" + user.getUid());

    }

    @Override
    public void updateUserAdministrador(Administradores administrador) {
        pDialog = new ProgressDialog(NavigationDrawerActivity.this);
        pDialog.setMessage(getString(R.string.default_loading_msg));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        this.firebaseUpdateAdministrador(administrador);
    }

    private void firebaseUpdateAdministrador(final Administradores administrador) {

        final FirebaseUser user = mAuth.getCurrentUser();

        /**obtiene la instancia como chofer**/
        DatabaseReference dbAdministrador =
                FirebaseDatabase.getInstance().getReference()
                        .child("administradores");

        administrador.setTipoUsuario("administrador");
        administrador.setFirebaseId(user.getUid());
        administrador.setContraseña(null);
        administrador.setFechaDeEdicion(DateTimeUtils.getTimeStamp());

        dbAdministrador.child(user.getUid()).setValue(administrador, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                pDialog.dismiss();

                if (databaseError == null) {
                    Toast.makeText(getApplicationContext(),
                            "Actualizado correctamente...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Log.i(TAG, "firebaseRegistroChoferes: Actualizado correctamente" + user.getUid());

    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(drawerView.getWindowToken(), 0);
    }

    @Override
    public void onDrawerClosed(View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }


    private class AsyncCallWS extends AsyncTask<Void, Void, Boolean> {

        private Integer webServiceOperation;
        private String textError;

        public AsyncCallWS(Integer wsOperation) {
            webServiceOperation = wsOperation;
            textError = "";
        }

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(NavigationDrawerActivity.this);
            pDialog.setMessage(getString(R.string.default_loading_msg));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            Boolean validOperation = false;

            try {
                switch (webServiceOperation) {
                    case Constants.WS_KEY_ELIMINAR_CLIENTES:
                        //TODO Eliminar desde el servidor
                        validOperation = true;
                        break;
                    case Constants.WS_KEY_ELIMINAR_TRANSPORTISTAS:
                        //TODO Eliminar desde el servidor
                        validOperation = true;
                        break;
                    case Constants.WS_KEY_ELIMINAR_CHOFERES:
                        //TODO Eliminar desde el servidor
                        validOperation = true;
                        break;
                    case Constants.WS_KEY_ELIMINAR_TRACTORES:
                        //TODO Eliminar desde el servidor
                        validOperation = true;
                        break;
                    case Constants.WS_KEY_ELIMINAR_REMOLQUES:
                        //TODO Eliminar desde el servidor
                        validOperation = true;
                        break;
                }
            } catch (Exception e) {
                textError = e.getMessage();
                validOperation = false;
            }

            return validOperation;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            try {
                pDialog.dismiss();
                if (success) {
                    switch (webServiceOperation) {
                        case Constants.WS_KEY_ELIMINAR_CLIENTES:
                            ClientesFragment.deleteItem(getDecodeItem());
                            break;
                        case Constants.WS_KEY_ELIMINAR_TRANSPORTISTAS:
                            TransportistasFragment.deleteItem(getDecodeItem());
                            break;
                        case Constants.WS_KEY_ELIMINAR_CHOFERES:
                            ChoferesFragment.deleteItem(getDecodeItem());
                            break;
                        case Constants.WS_KEY_ELIMINAR_TRACTORES:
                            TractoresFragment.deleteItem(getDecodeItem());
                            break;
                        case Constants.WS_KEY_ELIMINAR_REMOLQUES:
                            RemolquesFragment.deleteItem(getDecodeItem());
                            break;
                    }
                } else {
                    String tempText = (textError.isEmpty() ? "La lista  se encuentra vacía" : textError);
                    Toast.makeText(getApplicationContext(), tempText, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
