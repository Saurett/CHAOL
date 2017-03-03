package com.indev.chaol.fragments.interfaces;

/**
 * Created by texiumuser on 02/03/2017.
 */

public interface NavigationDrawerInterface {

    /**Permite cambiar de fragmento declarado en el menu**/
    void onChangeMainFragment(int idView);
    /**Permite eliminar todos los fragmentos secundarios que existan**/
    void removeSecondaryFragment();
}
