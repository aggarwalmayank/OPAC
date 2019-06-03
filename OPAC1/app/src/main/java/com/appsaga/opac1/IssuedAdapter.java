package com.appsaga.opac1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class IssuedAdapter extends ArrayAdapter<IssuedBooks> {

    public IssuedAdapter(Context context, ArrayList<IssuedBooks> issuedBooks) {
        super(context, 0, issuedBooks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View issueview = convertView;

        if (issueview == null) {
            issueview = LayoutInflater.from(getContext()).inflate(
                    R.layout.issue_view, parent, false);
        }

        IssuedBooks current_issued_book = getItem(position);

        TextView accession = issueview.findViewById(R.id.accession);
        TextView book_name = issueview.findViewById(R.id.book_name);
        TextView author = issueview.findViewById(R.id.author);
        TextView publisher = issueview.findViewById(R.id.publisher);

        accession.setText(current_issued_book.getAccession());
        book_name.setText(current_issued_book.getName());
        author.setText(current_issued_book.getAuthor_name());
        publisher.setText(current_issued_book.getPublisher_name());

        return issueview;
    }
}
