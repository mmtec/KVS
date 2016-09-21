package com.kvs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

public class LnEDialog extends DialogFragment {

    private NumberPicker np;
    private Spinner spinner;
    private DatePicker dp;
    private ArrayAdapter<CharSequence> adapter;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.ln_e_dialog, null);

        np = (NumberPicker) view.findViewById(R.id.numberPicker);
        spinner = (Spinner) view.findViewById(R.id.spinner);
        dp = (DatePicker) view.findViewById(R.id.datePicker);

        np.setMinValue(1);
        np.setMaxValue(6);

        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.lnArten, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        builder.setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ((SchuelerV)getActivity()).lnHinzufuegen(np, spinner, dp);
                    }
                })
                .setNegativeButton("Abbruch", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getDialog().cancel();
                    }
                });
        return builder.create();
    }

}