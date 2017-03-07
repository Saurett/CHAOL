package com.indev.chaol.fragments.interfaces;

import com.indev.chaol.models.DecodeItem;

/**
 * Created by texiumuser on 02/03/2017.
 */

public interface NavigationDrawerInterface {

    /**Permite cambiar de fragmento declarado en el menu**/
    void onChangeMainFragment(int idView);
    /**Permite eliminar todos los fragmentos secundarios que existan**/
    void removeSecondaryFragment();
    /**Permite mostrar el dialogo de prguntas**/
    void showQuestion(DecodeItem decodeItem);
}
