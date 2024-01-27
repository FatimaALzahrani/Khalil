package com.quran.khalil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quran.khalil.SimilarityItem;

import java.util.List;
public class SimilarityAdapter extends RecyclerView.Adapter<SimilarityAdapter.ViewHolder> {

    private List<SimilarityItem> similarityList;
    private OnItemClickListener onItemClickListener;
    private List<SimilarityItem> similarityList2;
    private OnItemClickListener onItemClickListener2;

    // Interface for item click
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }


    public SimilarityAdapter(List<SimilarityItem> similarityList) {
        this.similarityList = similarityList;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_similarity, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SimilarityItem similarityItem = similarityList.get(position);
        String[] surahNames = new String[]{
                "الفاتحة", "البقرة", "آل عمران", "النساء", "المائدة", "الأنعام", "الأعراف", "الأنفال", "التوبة", "يونس",
                "هود", "يوسف", "الرعد", "ابراهيم", "الحجر", "النحل", "الإسراء", "الكهف", "مريم", "طه",
                "الأنبياء", "الحج", "المؤمنون", "النور", "الفرقان", "الشعراء", "النمل", "القصص", "العنكبوت", "الروم",
                "لقمان", "السجدة", "الأحزاب", "سبإ", "فاطر", "يس", "الصافات", "ص", "الزمر", "غافر",
                "فصلت", "الشورى", "الزخرف", "الدخان", "الجاثية", "الأحقاف", "محمد", "الفتح", "الحجرات", "ق",
                "الذاريات", "الطور", "النجم", "القمر", "الرحمن", "الواقعة", "الحديد", "المجادلة", "الحشر", "الممتحنة",
                "الصف", "الجمعة", "المنافقون", "التغابن", "الطلاق", "التحريم", "الملك", "القلم", "الحاقة", "المعارج",
                "نوح", "الجن", "المزمل", "المدثر", "القيامة", "الإنسان", "المرسلات", "النبإ", "النازعات", "عبس",
                "التكوير", "الإنفطار", "المطففين", "الإنشقاق", "البروج", "الطارق", "الأعلى", "الغاشية", "الفجر", "البلد",
                "الشمس", "الليل", "الضحى", "الشرح", "التين", "العلق", "القدر", "البينة", "الزلزلة", "العاديات",
                "القارعة", "التكاثر", "العصر", "الهمزة", "الفيل", "قريش", "الماعون", "الكوثر", "الكافرون", "النصر",
                "المسد", "الإخلاص", "الفلق", "الناس"
        };
        holder.surahNumTextView.setText("سورة " + surahNames[Integer.parseInt(String.valueOf(similarityItem.getSurahNum()))]);
        holder.ayahNumTextView.setText("آية  " + similarityItem.getAyahNum());
        holder.ayahTextView.setText("﴿" + similarityItem.getAyahText()+"﴾");
    }

    @Override
    public int getItemCount() {
        return similarityList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView surahNumTextView;
        TextView ayahNumTextView;
        TextView ayahTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            surahNumTextView = itemView.findViewById(R.id.surahNumTextView);
            ayahNumTextView = itemView.findViewById(R.id.ayahNumTextView);
            ayahTextView = itemView.findViewById(R.id.ayahTextView);

            // Set an onClickListener for the itemView
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onItemClickListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}