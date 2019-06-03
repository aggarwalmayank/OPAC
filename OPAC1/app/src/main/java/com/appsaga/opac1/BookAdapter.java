package com.appsaga.opac1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class BookAdapter extends ArrayAdapter<BookInformation> {

    public BookAdapter(Context context, ArrayList<BookInformation> books) {
        super(context, 0, books);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View bookitemview = convertView;

        if (bookitemview == null) {
            bookitemview = LayoutInflater.from(getContext()).inflate(
                    R.layout.book_view, parent, false);
        }

        BookInformation current_book = getItem(position);

        TextView name = bookitemview.findViewById(R.id.book_name);
        TextView author = bookitemview.findViewById(R.id.author);
        TextView publisher = bookitemview.findViewById(R.id.publisher);
        TextView call = bookitemview.findViewById(R.id.call_no);
        TextView available=bookitemview.findViewById(R.id.available);

        name.setText(current_book.getName());
        author.setText(current_book.getAuthor());
        publisher.setText(current_book.getPublisher());
        call.setText("Call No.\n"+current_book.getCall_no());

        return bookitemview;
    }
}
