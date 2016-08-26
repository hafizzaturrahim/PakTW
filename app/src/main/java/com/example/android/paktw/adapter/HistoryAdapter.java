package com.example.android.paktw.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.paktw.R;
import com.example.android.paktw.activity.DetailEventActivity;
import com.example.android.paktw.model.History;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder>{
    List<History> history;
    List<History> history2;
    Context mcontext;
    static int total_data = 0;
    RecyclerView rv;

    public HistoryAdapter(RecyclerView rv){
        this.rv = rv;
    }

    public HistoryAdapter(List<History> history, Context context){
        this.history = history;
         mcontext = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener, View.OnLongClickListener {
        CardView cv;
        private ClickListener clickListener;
        TextView HistoryName;
        TextView HistoryDate;
        Button Delete;

        ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv_history);
            HistoryName = (TextView)itemView.findViewById(R.id.history_name);
            HistoryDate = (TextView)itemView.findViewById(R.id.history_date);
            Delete = (Button)itemView.findViewById(R.id.button_delete);

            // We set listeners to the whole item view, but we could also
            // specify listeners for the title or the icon.
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        /* Setter for listener. */
        public void setClickListener(ClickListener clickListener) {
            this.clickListener = clickListener;
        }

        @Override
        public void onClick(View v) {
            clickListener.onClick(v, getAdapterPosition(), false);
        }

        @Override
        public boolean onLongClick(View v) {
            clickListener.onClick(v, getAdapterPosition(), true);
            return true;
        }

        public interface ClickListener {
            void onClick(View v, int position, boolean isLongClick);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_history, viewGroup, false);
        ViewHolder evh = new ViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(ViewHolder historyViewHolder, final int position) {
        setHistoryData(historyViewHolder,position);

    }

    @Override
    public int getItemCount() {
        return history.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void setHistoryData(ViewHolder historyViewHolder, final int position){
        final DatabaseHelper db = new DatabaseHelper(mcontext);

        historyViewHolder.HistoryName.setText(history.get(position).getName_event());
        historyViewHolder.HistoryDate.setText(history.get(position).getDate());
        historyViewHolder.Delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                db.changeStatusEvent(history.get(position).getId_event(), history.get(position).getId_calendar());
                db.deleteHistory(history.get(position).getId_history());
                Toast.makeText(mcontext, "Delete " + history.get(position).getName_event(), Toast.LENGTH_SHORT).show();
            }
        });

        historyViewHolder.setClickListener(new ViewHolder.ClickListener() {
            @Override
            public void onClick(View v, int position, boolean isLongClick) {
                mcontext = v.getContext();
                Toast.makeText(mcontext, "tes " + history.get(position).getName_event(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mcontext, DetailEventActivity.class);
                intent.setClass(mcontext, DetailEventActivity.class);
                intent.putExtra("isHistory", true);
                intent.putExtra("id_event",history.get(position).getId_event());
                mcontext.startActivity(intent);
            }
        });
    }


}

