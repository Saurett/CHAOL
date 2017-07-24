package com.indev.chaol.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.indev.chaol.R;


/**
 * Created by saurett on 24/02/2017.
 */

public class ContactFragment extends Fragment implements View.OnClickListener {

    private TextInputEditText txtTelefono, txtCorreoElectronico;
    private Button btnCall, btnEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);

        txtTelefono = (TextInputEditText) view.findViewById(R.id.txt_contact_telefono);
        txtCorreoElectronico = (TextInputEditText) view.findViewById(R.id.txt_contact_correo);

        btnCall = (Button) view.findViewById(R.id.btn_call);
        btnEmail = (Button) view.findViewById(R.id.btn_send_email);

        txtTelefono.setTag(txtTelefono.getKeyListener());
        txtTelefono.setKeyListener(null);

        txtCorreoElectronico.setTag(txtCorreoElectronico.getKeyListener());
        txtCorreoElectronico.setKeyListener(null);

        btnCall.setOnClickListener(this);
        btnEmail.setOnClickListener(this);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_call:
                String posted_by = "6251365750";

                String uri = "tel:" + posted_by.trim();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(uri));
                startActivity(intent);
                break;
            case R.id.btn_send_email:
                String to = "Chaolapp@gmail.com";
                String subject = "[CHAOL] Contacto";
                //String message = "Message to send";

                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
                //email.putExtra(Intent.EXTRA_CC, new String[]{ to});
                //email.putExtra(Intent.EXTRA_BCC, new String[]{to});
                email.putExtra(Intent.EXTRA_SUBJECT, subject);
                //email.putExtra(Intent.EXTRA_TEXT, message);

                // This type is needed to prompt only email client chooser
                email.setType("message/rfc822");

                try {
                    startActivity(Intent.createChooser(email, "Choose an Email client :"));
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getActivity(), "Es necesario tener una aplicación de Email ...", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
