package com.indev.chaol.fragments.interfaces;

import com.indev.chaol.models.DecodeItem;

/**
 * Created by texiumuser on 02/03/2017.
 */

public interface MainRegisterInterface {

    /**Permite cambiar de fragmento declarado en el menu**/
    void onChangeMainFragment(int idView);
    /**Permite eliminar todos los fragmentos secundarios que existan**/
    void removeSecondaryFragment();
    /**Permite mostrar el dialogo de preguntas**/
    void showQuestion();
    /**Permte abrir una actividad externa enviando parametros en el DecodeItem**/
    void openExternalActivity(int action, Class<?> externalActivity);
    /**Permite transferir los valores seleccionados en DecodeItem*/
    void setDecodeItem(DecodeItem decodeItem);
    /**Permite obtener los ultimos valores seleccionados en DecodeItem**/
    DecodeItem getDecodeItem();
}
