package com.appsaga.opac1;

import android.content.Context;
import android.icu.util.ValueIterator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ReserveAdapter extends ArrayAdapter<Copies> {

    public ReserveAdapter(Context context, ArrayList<Copies> to_reserve) {
        super(context, 0,to_reserve);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        View reserve_view = convertView;

        if (reserve_view == null) {
            reserve_view = LayoutInflater.from(getContext()).inflate(
                    R.layout.reserve_view, parent, false);
        }

        Copies current_copy = getItem(position);

        TextView accession = reserve_view.findViewById(R.id.accession);
        TextView code = reserve_view.findViewById(R.id.code);
        TextView type = reserve_view.findViewById(R.id.type);
        TextView status = reserve_view.findViewById(R.id.status);
        TextView issued_by = reserve_view.findViewById(R.id.issued_by);
        final CheckBox check = reserve_view.findViewById(R.id.check);

        accession.setText(current_copy.getAccession());
        code.setText(current_copy.getCode());
        type.setText(current_copy.getType());
        status.setText(current_copy.getStatus());
        issued_by.setText(current_copy.getIssued_by());

        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                for(int i=0;i<parent.getChildCount();i++)
                {
                    if(i!=position) {
                        View view = parent.getChildAt(i);
                        CheckBox checkBox = view.findViewById(R.id.check);
                        checkBox.setChecked(false);
                    }
                }
            }
        });

       /* reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(int i=0;i<parent.getChildCount();i++)
                {
                    View view = parent.getChildAt(i);
                    CheckBox checkBox = view.findViewById(R.id.check);
                    if(checkBox.isChecked())
                    {
                        TextView textView = view.findViewById(R.id.accession);
                        String name = textView.getText().toString();
                        Toast.makeText(getContext(),"Reserving "+name,Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });*/

        return reserve_view;
    }
}
