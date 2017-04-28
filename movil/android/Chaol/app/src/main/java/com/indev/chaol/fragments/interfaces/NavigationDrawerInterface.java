package com.indev.chaol.fragments.interfaces;

import com.indev.chaol.models.Choferes;
import com.indev.chaol.models.Clientes;
import com.indev.chaol.models.DecodeItem;
import com.indev.chaol.models.Transportistas;

/**
 * Created by texiumuser on 02/03/2017.
 */

public interface NavigationDrawerInterface {

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
    /**Permite actualizar usuarios tipo cliente**/
    void updateUserCliente(Clientes cliente);
    /**Permite actualizar usuarios tipo transportista**/
    void updateUserTransportista(Transportistas transportista);
    /**Permite actualizar usuarios tipo chofer**/
    void updateUserChofer(Choferes chofer);
}
