package com.example.android.paktw.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.paktw.R;
import com.example.android.paktw.activity.DetailDiseaseActivity;
import com.example.android.paktw.model.Disease;

import java.util.ArrayList;
import java.util.List;


public class DiseaseAdapter extends RecyclerView.Adapter<DiseaseAdapter.ViewHolder> {

    private final LayoutInflater mInflater;
    private List<Disease> disease;
    Context mcontext;
    DatabaseHelper db;

    public static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener{

        CardView cv;
        private final TextView name_disease;
        private final TextView short_detail;
        private ClickListener clickListener;

        ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv_disease);
            name_disease = (TextView) itemView.findViewById(R.id.name_disease);
            short_detail = (TextView) itemView.findViewById(R.id.desc_disease);

            // We set listeners to the whole item view, but we could also
            // specify listeners for the title or the icon.
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void bind(Disease ds) {
            name_disease.setText(ds.getName());
            short_detail.setText(ds.getPenyebab_disease().substring(0,23)+ "...(Lihat detail)");
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

    public DiseaseAdapter(Context context, List<Disease> models) {
        mInflater = LayoutInflater.from(context);
        disease = new ArrayList<>(models);
        this.mcontext = context;
    }

    public DiseaseAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.mcontext = context;
    }
    
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = mInflater.inflate(R.layout.card_view_disease, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.name_disease.setText(disease.get(position).getName());
        holder.short_detail.setText(disease.get(position).getPenyebab_disease().substring(0,23)+ "...(Lihat detail)");
//        final Disease model = disease.get(position);
//
//        holder.bind(model);
        holder.setClickListener(new ViewHolder.ClickListener() {
            @Override
            public void onClick(View v, int position, boolean isLongClick) {
                mcontext = v.getContext();
//                Toast.makeText(mcontext, "tes " + disease.get(position).getName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mcontext, DetailDiseaseActivity.class);
                intent.setClass(mcontext, DetailDiseaseActivity.class);
                intent.putExtra("id_disease",disease.get(position).getId_disease());
                mcontext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return disease.size();
    }

    public void animateTo(List<Disease> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<Disease> newModels) {
        for (int i = disease.size() - 1; i >= 0; i--) {
            final Disease model = disease.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Disease> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final Disease model = newModels.get(i);
            if (!disease.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Disease> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final Disease model = newModels.get(toPosition);
            final int fromPosition = disease.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public Disease removeItem(int position) {
        final Disease model = disease.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, Disease model) {
        disease.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Disease model = disease.remove(fromPosition);
        disease.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    public List<Disease> searchDisease(List<Disease> disease, String query) {
        query = query.toLowerCase();

        final List<Disease> filteredDisease = new ArrayList<>();
        for (Disease model : disease) {
            final String text = (model.getName()+ " " +model.getGejala_disease()).toLowerCase();
            if (text.contains(query)) {
                filteredDisease.add(model);
            }
        }
        return filteredDisease;
    }

    public void setGejala(String query, DiseaseAdapter mAdapter, List<Disease> mModels){
        final List<Disease> filteredDisease = searchDisease(mModels, query);
        mAdapter.animateTo(filteredDisease);
    }

    public void initializeData(){
        db = new DatabaseHelper(mcontext);
        if(db.getAllDisease().size()==0){
            db.createDisease(
                    1,
                    "Bercak Daun Cercospora",
                    "cendawan (jamur) Cercospora carotae (Pass.) Solheim",
                    "Pada daun-daun yang sudah tua timbul bercak-bercak berwarna coklat muda atau putih dengan pinggiran berwarna coklat tua sampai hitam.",
                    "(1) disinfeksi benih dengan larutan fungisida yang mengandung tembaga klorida satu permil selama 5 menit\n" +
                            "(2) pergiliran tanaman dengan jenis lain yang tidak sefamili\n" +
                            "(3) pembersihan sisa-sisa tanaman dari sekitar kebun\n" +
                            "(4) penyemprotan fungisida yang mangkus dan sangkil seperti Dithane M-45 0,2%");

            db.createDisease(
                    2,
                    "Nematoda Bintil Akar",
                    "Mikro organisme nematoda Sista (Heterodera carotae)",
                    "Umbi dan akar tanaman wortel menjadi salah bentuk, berbenjol-benjol abnormal",
                    "Melakukan pergiliran tanaman dengan jenis lain yang tidak sefamili, pemberaan lahan dan penggunaan nematisida seperti Rugby 10 G atau Rhocap 10 G");

            db.createDisease(
                    3,
                    "Busuk Alternaria",
                    "Cendawan Alternaria dauci Kuhn",
                    "Pada daun terjadi bercak-bercak kecil, berwarna coklat tua sampi hitam yang dikelilingi oleh jaringan berwarna hijau-kuning (klorotik). Pada umbi ada gejala bercak-bercak tidak beraturan bentuknya, kemudian membusuk berwarna hitam sampai hitam kelam.",
                    "(1) disinfeksi benih dengan larutan fungisida yang mengandung tembaga klorida satu permil selama 5 menit\n " +
                            "(2) pergiliran tanaman dengan jenis lain yang tidak sefamili\n" +
                            "(3) pembersihan sisa-sisa tanaman dari sekitar kebun\n" +
                            "(4) penyemprotan fungisida yang mangkus dan sangkil seperti Dithane M-45 0,2%");

            db.createDisease(
                    4,
                    "Ulat tanah (Agrotis ipsilon Hufn)",
                    "Memiliki warna kecoklatan hingga berwarna hitam, dengan panjang antar 4-5  cm dan hidup di dalam tanah",
                    "Menyerang bagian pucuk akar atau pusat tanamana yang masih mudah, yang mengakibatkan tanaman menjadi layu, kering dan mati.",
                    "Non kimiawi : melakukan pengumpulan atau pemungutan ulat, lalu membakarnya.\n" +
                            "Kimiawi : melakukan penyemprotan insektisida Furadan atau Indofuradan sebelum tanam dan sesudah tanam.");

            db.createDisease(
                    5,
                    "Kutu Daun (Aphid, Aphis spp.)",
                    "Memiliki warna hijau hingga berwarna kehitaman, bentuk badan kecil dan juga memiliki bulu halus di sekitar tubuh",
                    "Menyerang permukaan daun hingga layu, menguning, berlubang dan juga mati serta kering.",
                    "Non kimiawi : melakukan sanitasi kebun atau lahan, perserampakan penanaman dan juga penjarangan tanaman.\n" +
                            "Kimiawi : melakukan penyemprotan dengan insektisida berbahan aktif sesuai dengan petunjuk.");

            db.createDisease(
                    6,
                    "Lalat atau Magot (Psile rosae)",
                    "Memiliki warna tubuh kehitaman dan juga kehijauan, terdapat belang-belang pada tubuh berwrna kecoklatan mudah dan hitam",
                    "menyerang buah yang ada di dalam sehingg berlubang, membusuk bagian dalam dan juga buah tidak normal.",
                    "Non kimiawi : membuat perangkap lalat, melakukan sanitasi kebun atau lahan\n" +
                            "Kimiawi : melakukan penyemprotan insektisida Decis dan lainnya sesuai dengan petunjuk.\n");

        }
    }
}
