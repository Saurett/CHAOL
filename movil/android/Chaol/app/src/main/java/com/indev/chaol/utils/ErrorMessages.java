package com.indev.chaol.utils;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

/**
 * Created by jvier on 18/04/2017.
 */

public class ErrorMessages {

    public static String showErrorMessage(Exception exception) {
        String message;

        try {
            throw exception;
        } catch (FirebaseAuthInvalidCredentialsException e) {
            message = FirebaseAuthInvalidCredentialsExceptionMessage(exception.getMessage());
        } catch (FirebaseAuthInvalidUserException e) {
          message = FirebaseAuthInvalidUserExceptionMessage(exception.getMessage());
        } catch (FirebaseException e) {
            message = FirebaseExceptionMessage(exception.getMessage());
        } catch (Exception e) {
            message = "Se ha detectado un error, " + exception.getMessage();
        }

        return message;
    }

    private static String FirebaseExceptionMessage(String exceptionMessage) {
        String message;

        switch (exceptionMessage) {
            case "An internal error has occurred. [ WEAK_PASSWORD  ]":
                message = "La contraseña es muy corta.";
                break;
            case "An internal error has occurred. [ INVALID_EMAIL ]":
                message = "El correo electronico  no es valido.";
                break;
            case "The email address is already in use by another account.":
                message = "El correo electronico ya esta registrado en otra cuenta.";
                break;
            default:
                message = "Se ha detectado un error, " + exceptionMessage;
                break;
        }

        return message;
    }

    private static String FirebaseAuthInvalidCredentialsExceptionMessage(String exceptionMessage) {
        String message;

        switch (exceptionMessage) {
            case "The email address is badly formatted.":
                message = "El correo electronico  no es valido.";
                break;
            case "The password is invalid or the user does not have a password.":
                message = "La contraseña es invalida.";
                break;
            default:
                message = "Se ha detectado un error, " + exceptionMessage;
                break;
        }

        return message;
    }

    private static String FirebaseAuthInvalidUserExceptionMessage(String exceptionMessage) {
        String message;

        switch (exceptionMessage) {
            case "There is no user record corresponding to this identifier. The user may have been deleted.":
                message = "Sus credenciales no son validas.";
                break;
            default:
                message = "Se ha detectado un error, " + exceptionMessage;
                break;
        }

        return message;
    }
}
