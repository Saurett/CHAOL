package com.indev.chaol.fragments.interfaces;

import com.indev.chaol.models.Administradores;
import com.indev.chaol.models.Bodegas;
import com.indev.chaol.models.Choferes;
import com.indev.chaol.models.Clientes;
import com.indev.chaol.models.DecodeItem;
import com.indev.chaol.models.Fletes;
import com.indev.chaol.models.MainFletes;
import com.indev.chaol.models.Remolques;
import com.indev.chaol.models.Tractores;
import com.indev.chaol.models.Transportistas;

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
    /**Permite crear usuarios tipo colaboradores**/
    void createUserColaborador(Administradores colaborador);
    /**Permite crear usuarios tipo cliente**/
    void createUserCliente(Clientes cliente);
    /**Permite crear usuarios tipo transportista**/
    void createUserTransportista(Transportistas transportista);
    /**Permite crear usuarios tipo chofer**/
    void createUserChofer(Choferes chofer);
    /**Permite crear tractores**/
    void createTractores(Tractores tractor);
    /**Permite editar tractores**/
    void updateTractores(Tractores tractor);
    /**Permite crear remolques**/
    void createRemolques(Remolques remolque);
    /**Permite editar remolques**/
    void updateRemolques(Remolques remolque);
    /**Permite crear bodegas**/
    void createBodegas(Bodegas bodega);
    /**Permite editar bodegas**/
    void updateBodega(Bodegas bodega);
    /**Permite editar administrador**/
    void updateAdministrador(Administradores colaborador);
    /**Permite editar clientes**/
    void updateCliente(Clientes cliente);
    /**Permite editar bodegas**/
    void updateTransportista(Transportistas transportista);
    /**Permite editar bodegas**/
    void updateChofer(Choferes chofer);
    /**Permite crear la solicitud de cotizacion**/
    void createSolicitudCotizacion(Fletes flete, Bodegas _bodegaCargaActual, Bodegas _bodegaDescargaActual);
    /**Permite actualizar la solicitud de cotizacion**/
    void updateSolicitudCotizacion(Fletes flete);
    /**Permite actualizar la solicitud de transportista**/
    void updateSolicitudTransportistaInteresado(Fletes flete, Transportistas transportistaInteresado);
    /**Permite actualizar la solicitud de transportista**/
    void removeSolicitudTransportistaInteresado(Fletes flete, String firebaseIDTransportistaInteresado);
    /**Permite actualizar la solicitud de transportistas**/
    void removeSolicitudTransportistaSeleccionado(Fletes flete, String firebaseIDTransportistaInteresado);
    /**Permite crear la solicitud de Equipo**/
    void createSolicitudEquipo(MainFletes mainFletes);
    /**Permite actualizar la solicitud de equipo**/
    void updateSolicitudEquipo(MainFletes mainFlete, MainFletes _oldMainFlete);
    /**Permite actualizar la solicitud de envio**/
    void updateSolicitudEnvio(MainFletes mainFletes);
}
