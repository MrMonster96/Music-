package com.example.onlymusic;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongHolder> {

    ArrayList<SongInfo> songInfos;
    Context context;
    OnitemClickListener onitemClickListener;

    SongAdapter(Context context,ArrayList<SongInfo> songInfos)
    {
        this.context=context;
        this.songInfos=songInfos;
    }
    public interface OnitemClickListener
    {
        void onItemClick(Button b, View v, SongInfo obj, int position);
    }
    public void setOnitemClickListener(OnitemClickListener onitemClickListener){
        this.onitemClickListener = onitemClickListener;
    }



    @NonNull
    @Override
    public SongHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
     View myview = LayoutInflater.from(context).inflate(R.layout.row_song,viewGroup,false);
        return new SongHolder(myview);
    }

    @Override
    public void onBindViewHolder(@NonNull final SongHolder songHolder,final int i) {
       final SongInfo c = songInfos.get(i);

        songHolder.songName.setText(c.songName);
        songHolder.artistName.setText(c.artistName);
        songHolder.btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onitemClickListener!=null){
                    onitemClickListener.onItemClick(songHolder.btnAction,v,c,i);

                }
            }
        });



    }

    @Override
    public int getItemCount() {

        return songInfos.size();
    }

    public class SongHolder extends RecyclerView.ViewHolder{
        TextView songName,artistName;
        Button btnAction;
        public SongHolder(@NonNull View itemView) {
            super(itemView);
            songName = itemView.findViewById(R.id.tvSongName);
            artistName= itemView.findViewById(R.id.tvArtistName);
            btnAction = itemView.findViewById(R.id.btnAction);


        }
    }


}
