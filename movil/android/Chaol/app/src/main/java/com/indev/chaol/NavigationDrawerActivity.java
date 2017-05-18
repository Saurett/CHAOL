package com.indev.chaol;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.indev.chaol.fragments.interfaces.NavigationDrawerInterface;
import com.indev.chaol.models.Administradores;
import com.indev.chaol.models.Bodegas;
import com.indev.chaol.models.Choferes;
import com.indev.chaol.models.Clientes;
import com.indev.chaol.models.DecodeExtraParams;
import com.indev.chaol.models.DecodeItem;
import com.indev.chaol.models.Remolques;
import com.indev.chaol.models.Tractores;
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

        switch (_SESSION_USER.getTipoDeUsuario()) {
            case Constants.FB_KEY_ITEM_TIPO_USUARIO_CLIENTE:
                /**El cliente visualizara menu de fletes y de cuentas**/
                menu.findItem(R.id.menu_item_clientes).setVisible(false);
                menu.findItem(R.id.menu_item_transportistas).setVisible(false);
                menu.findItem(R.id.menu_item_choferes).setVisible(false);
                menu.findItem(R.id.menu_item_tractores).setVisible(false);
                menu.findItem(R.id.menu_item_remolques).setVisible(false);
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
            case R.id.menu_item_bodegas:
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
                    case R.id.item_btn_eliminar_bodega:
                        operation = Constants.WS_KEY_ELIMINAR_BODEGAS;
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

                this.firebaseOperations(operation);

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
        intent.putExtra(Constants.KEY_SESSION_USER, _SESSION_USER);
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

        String firebaseID = (cliente.getFirebaseId() == null) ? user.getUid() : cliente.getFirebaseId();

        /**obtiene la instancia como cliente**/
        DatabaseReference dbCliente =
                FirebaseDatabase.getInstance().getReference()
                        .child(Constants.FB_KEY_MAIN_CLIENTES);

        cliente.setTipoDeUsuario(Constants.FB_KEY_ITEM_TIPO_USUARIO_CLIENTE);
        cliente.setFirebaseId(firebaseID);
        cliente.setEstatus(cliente.getEstatus());
        cliente.setPassword(null);
        cliente.setFechaDeEdicion(DateTimeUtils.getTimeStamp());

        dbCliente.child(cliente.getFirebaseId()).child(Constants.FB_KEY_ITEM_CLIENTE).setValue(cliente, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                pDialog.dismiss();

                if (databaseError == null) {
                    Toast.makeText(getApplicationContext(),
                            "Actualizado correctamente...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Log.i(TAG, "firebaseUpdateCliente: Actualizado correctamente" + user.getUid());
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
        FirebaseUser user = mAuth.getCurrentUser();

        String firebaseID = (transportista.getFirebaseId() == null) ? user.getUid() : transportista.getFirebaseId();

        /**obtiene la instancia como transportista**/
        DatabaseReference dbTransportista =
                FirebaseDatabase.getInstance().getReference()
                        .child("transportistas");

        transportista.setTipoDeUsuario(Constants.FB_KEY_ITEM_TIPO_USUARIO_TRANSPORTISTA);
        transportista.setFirebaseId(firebaseID);
        transportista.setContraseña(null);
        transportista.setEstatus(transportista.getEstatus());
        transportista.setFechaDeEdicion(DateTimeUtils.getTimeStamp());

        dbTransportista.child(transportista.getFirebaseId()).child(Constants.FB_KEY_ITEM_TRANSPORTISTA).setValue(transportista, new DatabaseReference.CompletionListener() {
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
                                    .child(Constants.FB_KEY_MAIN_LISTA_TRANSPORTISTAS);

                    dbListaTransportista.child(transportista.getFirebaseId()).setValue(transportista.getNombre(), new DatabaseReference.CompletionListener() {
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


        Log.i(TAG, "firebaseRegistroTransportista: Registrado correctamente" + user.getUid());
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

        FirebaseUser user = mAuth.getCurrentUser();

        String firebaseID = (chofer.getFirebaseId() == null) ? user.getUid() : chofer.getFirebaseId();

        /**obtiene la instancia como chofer**/
        DatabaseReference dbChofer =
                FirebaseDatabase.getInstance().getReference()
                        .child(Constants.FB_KEY_MAIN_CHOFERES);

        chofer.setTipoDeUsuario(Constants.FB_KEY_ITEM_TIPO_USUARIO_CHOFER);
        chofer.setFirebaseId(firebaseID);
        chofer.setContraseña(null);
        chofer.setFechaDeEdicion(DateTimeUtils.getTimeStamp());

        dbChofer.child(chofer.getFirebaseId()).setValue(chofer, new DatabaseReference.CompletionListener() {
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
                                    .child(Constants.FB_KEY_MAIN_TRANSPORTISTAS);

                    dbTransportista.child(chofer.getFirebaseIdTransportista())
                            .child(Constants.FB_KEY_ITEM_CHOFER).child(chofer.getFirebaseId()).setValue(chofer, new DatabaseReference.CompletionListener() {
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

        administrador.setTipoDeUsuario("administrador");
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

    /**
     * Elimina los objetos deacuerdo a la operacion
     **/
    private void firebaseOperations(int operation) {
        pDialog = new ProgressDialog(NavigationDrawerActivity.this);
        pDialog.setMessage(getString(R.string.default_loading_msg));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        switch (operation) {
            case Constants.WS_KEY_ELIMINAR_CLIENTES:
                this.firebaseDeleteCliente();
                break;
            case Constants.WS_KEY_ELIMINAR_BODEGAS:
                this.firebaseDeleteBodega();
                break;
            case Constants.WS_KEY_ELIMINAR_TRANSPORTISTAS:
                this.firebaseDeleteTransportista();
                break;
            case Constants.WS_KEY_ELIMINAR_CHOFERES:
                this.firebaseDeleteChofer();
                break;
            case Constants.WS_KEY_ELIMINAR_TRACTORES:
                this.firebaseDeleteTractor();
                break;
            case Constants.WS_KEY_ELIMINAR_REMOLQUES:
                this.firebaseDeleteRemolque();
                break;
        }
    }

    /**
     * Elimina especificamente el objeto seleccionado
     **/
    private void firebaseDeleteColaborador() {
        final Choferes chofer = (Choferes) getDecodeItem().getItemModel();

        /**obtiene la instancia del elemento**/
        DatabaseReference dbChofer =
                FirebaseDatabase.getInstance().getReference()
                        .child(Constants.FB_KEY_MAIN_TRANSPORTISTAS)
                        .child(chofer.getFirebaseIdTransportista())
                        .child(Constants.FB_KEY_MAIN_CHOFERES)
                        .child(chofer.getFirebaseId());

        chofer.setEstatus(Constants.FB_KEY_ITEM_ESTATUS_ELIMINADO);
        chofer.setFechaDeEdicion(DateTimeUtils.getTimeStamp());

        dbChofer.setValue(chofer, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                pDialog.dismiss();

                if (databaseError == null) {

                    pDialog = new ProgressDialog(NavigationDrawerActivity.this);
                    pDialog.setMessage(getString(R.string.default_loading_msg));
                    pDialog.setIndeterminate(false);
                    pDialog.setCancelable(false);
                    pDialog.show();

                    /**obtiene la instancia del elemento**/
                    DatabaseReference dbChofer =
                            FirebaseDatabase.getInstance().getReference()
                                    .child(Constants.FB_KEY_MAIN_CHOFERES)
                                    .child(chofer.getFirebaseId());

                    dbChofer.setValue(chofer, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            pDialog.dismiss();
                            if (databaseError == null) {
                                Toast.makeText(getApplicationContext(),
                                        "Eliminado correctamente...", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });

        Log.i(TAG, "firebaseDeleteChofer: Eliminado correctamente" + chofer.getFirebaseId());
    }

    /**
     * Elimina especificamente el objeto seleccionado
     **/
    private void firebaseDeleteCliente() {
        final Clientes cliente = (Clientes) getDecodeItem().getItemModel();

        /**obtiene la instancia del elemento**/
        DatabaseReference dbCliente =
                FirebaseDatabase.getInstance().getReference()
                        .child(Constants.FB_KEY_MAIN_CLIENTES)
                        .child(cliente.getFirebaseId())
                        .child(Constants.FB_KEY_ITEM_CLIENTE);

        cliente.setEstatus(Constants.FB_KEY_ITEM_ESTATUS_ELIMINADO);
        cliente.setFechaDeEdicion(DateTimeUtils.getTimeStamp());

        dbCliente.setValue(cliente, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                pDialog.dismiss();
                if (databaseError == null) {
                    Toast.makeText(getApplicationContext(),
                            "Eliminado correctamente...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Log.i(TAG, "firebaseDeleteCliente: Eliminado correctamente" + cliente.getFirebaseId());
    }

    /**
     * Elimina especificamente el objeto seleccionado
     **/
    private void firebaseDeleteBodega() {
        final Bodegas bodega = (Bodegas) getDecodeItem().getItemModel();

        /**obtiene la instancia del elemento**/
        DatabaseReference dbBodega =
                FirebaseDatabase.getInstance().getReference()
                        .child(Constants.FB_KEY_MAIN_CLIENTES)
                        .child(bodega.getFirebaseIdCliente())
                        .child(Constants.FB_KEY_MAIN_BODEGAS)
                        .child(bodega.getFirebaseIdBodega());

        bodega.setEstatus(Constants.FB_KEY_ITEM_ESTATUS_ELIMINADO);
        bodega.setFechaDeEdicion(DateTimeUtils.getTimeStamp());

        dbBodega.setValue(bodega, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                pDialog.dismiss();
                if (databaseError == null) {
                    Toast.makeText(getApplicationContext(),
                            "Eliminado correctamente...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Log.i(TAG, "firebaseDeleteCliente: Eliminado correctamente" + bodega.getFirebaseIdBodega());
    }

    /**
     * Elimina especificamente el objeto seleccionado
     **/
    private void firebaseDeleteTransportista() {
        final Transportistas transportista = (Transportistas) getDecodeItem().getItemModel();

        /**obtiene la instancia del elemento**/
        DatabaseReference dbTransportista =
                FirebaseDatabase.getInstance().getReference()
                        .child(Constants.FB_KEY_MAIN_TRANSPORTISTAS)
                        .child(transportista.getFirebaseId())
                        .child(Constants.FB_KEY_ITEM_TRANSPORTISTA);

        transportista.setEstatus(Constants.FB_KEY_ITEM_ESTATUS_ELIMINADO);
        transportista.setFechaDeEdicion(DateTimeUtils.getTimeStamp());

        dbTransportista.setValue(transportista, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                pDialog.dismiss();

                if (databaseError == null) {
                    Toast.makeText(getApplicationContext(),
                            "Eliminado correctamente...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Log.i(TAG, "firebaseDeleteTransportista: Eliminado correctamente" + transportista.getFirebaseId());
    }

    /**
     * Elimina especificamente el objeto seleccionado
     **/
    private void firebaseDeleteChofer() {
        final Choferes chofer = (Choferes) getDecodeItem().getItemModel();

        /**obtiene la instancia del elemento**/
        DatabaseReference dbChofer =
                FirebaseDatabase.getInstance().getReference()
                        .child(Constants.FB_KEY_MAIN_TRANSPORTISTAS)
                        .child(chofer.getFirebaseIdTransportista())
                        .child(Constants.FB_KEY_MAIN_CHOFERES)
                        .child(chofer.getFirebaseId());

        chofer.setEstatus(Constants.FB_KEY_ITEM_ESTATUS_ELIMINADO);
        chofer.setFechaDeEdicion(DateTimeUtils.getTimeStamp());

        dbChofer.setValue(chofer, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                pDialog.dismiss();

                if (databaseError == null) {

                    pDialog = new ProgressDialog(NavigationDrawerActivity.this);
                    pDialog.setMessage(getString(R.string.default_loading_msg));
                    pDialog.setIndeterminate(false);
                    pDialog.setCancelable(false);
                    pDialog.show();

                    /**obtiene la instancia del elemento**/
                    DatabaseReference dbChofer =
                            FirebaseDatabase.getInstance().getReference()
                                    .child(Constants.FB_KEY_MAIN_CHOFERES)
                                    .child(chofer.getFirebaseId());

                    dbChofer.setValue(chofer, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            pDialog.dismiss();
                            if (databaseError == null) {
                                Toast.makeText(getApplicationContext(),
                                        "Eliminado correctamente...", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });

        Log.i(TAG, "firebaseDeleteChofer: Eliminado correctamente" + chofer.getFirebaseId());
    }

    /**
     * Elimina especificamente el objeto seleccionado
     **/
    private void firebaseDeleteTractor() {
        Tractores tractor = (Tractores) getDecodeItem().getItemModel();

        /**obtiene la instancia como cliente**/
        DatabaseReference dbTractor =
                FirebaseDatabase.getInstance().getReference()
                        .child(Constants.FB_KEY_MAIN_TRANSPORTISTAS)
                        .child(tractor.getFirebaseIdTransportista())
                        .child(Constants.FB_KEY_MAIN_TRACTORES)
                        .child(tractor.getFirebaseId());

        tractor.setEstatus(Constants.FB_KEY_ITEM_ESTATUS_ELIMINADO);
        tractor.setFechaDeEdicion(DateTimeUtils.getTimeStamp());
        tractor.setFirebaseIdTransportista(null);

        dbTractor.setValue(tractor, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                pDialog.dismiss();

                if (databaseError == null) {
                    Toast.makeText(getApplicationContext(),
                            "Eliminado correctamente...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Log.i(TAG, "firebaseDeleteRemolque: Eliminado correctamente" + tractor.getFirebaseId());
    }

    /**
     * Elimina especificamente el objeto seleccionado
     **/
    private void firebaseDeleteRemolque() {
        Remolques remolque = (Remolques) getDecodeItem().getItemModel();

        /**obtiene la instancia como cliente**/
        DatabaseReference dbRemolque =
                FirebaseDatabase.getInstance().getReference()
                        .child(Constants.FB_KEY_MAIN_TRANSPORTISTAS)
                        .child(remolque.getFirebaseIdTransportista())
                        .child(Constants.FB_KEY_MAIN_REMOLQUES)
                        .child(remolque.getFirebaseId());

        remolque.setEstatus(Constants.FB_KEY_ITEM_ESTATUS_ELIMINADO);
        remolque.setFechaDeEdicion(DateTimeUtils.getTimeStamp());
        remolque.setFirebaseIdTransportista(null);

        dbRemolque.setValue(remolque, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                pDialog.dismiss();

                if (databaseError == null) {
                    Toast.makeText(getApplicationContext(),
                            "Eliminado correctamente...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Log.i(TAG, "firebaseDeleteRemolque: Eliminado correctamente" + remolque.getFirebaseId());
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(drawerView.getWindowToken(), 0);
    }

    @Override
    public void onDrawerClosed(View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }
}
