package com.kvs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Dialog, der um den gewünschten Vorgang mit einem vorhandenen Leistungsnachweis bittet.
 */

public class LnDialog extends DialogFragment {

    Leistungsnachweis ln;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        ln = bundle.getParcelable("Leistungsnachweis");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Was möchten Sie tun?")
                .setItems(R.array.lnOptionen, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch(i) {
                            case 0:
                                break;
                            case 1:
                                ((SchuelerV)getActivity()).deleteLn(ln);
                                break;
                        }
                    }
                });
        return builder.create();
    }

}
