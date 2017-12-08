package com.example.mladen.masterradandroid.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mladen.masterradandroid.R;
import com.example.mladen.masterradandroid.model.CommentModel;

import java.util.ArrayList;
import java.util.List;

public class CommentDataAdapter extends RecyclerView.Adapter<CommentDataAdapter.ViewHolder>{

    private ArrayList<CommentModel> listComment;

    public CommentDataAdapter(ArrayList<CommentModel> listComment) {
        this.listComment = listComment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycle_comment_view, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        viewHolder.naslov.setText(listComment.get(position).titleComment);
        viewHolder.sadrzaj.setText(listComment.get(position).contentComment);
        viewHolder.korisnik.setText(listComment.get(position).fbName);
        viewHolder.datum.setText(listComment.get(position).dateTime);
    }

    @Override
    public int getItemCount() {
        return listComment.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView naslov;
        private TextView sadrzaj;
        private TextView korisnik;
        private TextView datum;

        public ViewHolder(View itemView) {
            super(itemView);

            naslov = itemView.findViewById(R.id.naslov_komentara);
            sadrzaj = itemView.findViewById(R.id.sadrzaj_komentara);
            korisnik = itemView.findViewById(R.id.korisnik);
            datum = itemView.findViewById(R.id.datum);
        }
    }
}
