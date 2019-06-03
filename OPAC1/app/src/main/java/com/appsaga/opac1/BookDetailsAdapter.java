package com.appsaga.opac1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class BookDetailsAdapter extends ArrayAdapter<Copies> {

    public BookDetailsAdapter(Context context, ArrayList<Copies> copies) {
        super(context, 0, copies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View bookdetailview = convertView;

        if (bookdetailview == null) {
            bookdetailview = LayoutInflater.from(getContext()).inflate(
                    R.layout.book_detail_view, parent, false);
        }

        Copies current_copy = getItem(position);

        TextView accession = bookdetailview.findViewById(R.id.accession);
        TextView code = bookdetailview.findViewById(R.id.code);
        TextView type = bookdetailview.findViewById(R.id.type);
        TextView status = bookdetailview.findViewById(R.id.status);
        TextView reserved_by = bookdetailview.findViewById(R.id.reserved_by);

        accession.setText(current_copy.getAccession());
        code.setText(current_copy.getCode());
        type.setText(current_copy.getType());
        status.setText(current_copy.getStatus());
        reserved_by.setText(current_copy.getReserved());

        return bookdetailview;
    }
}
