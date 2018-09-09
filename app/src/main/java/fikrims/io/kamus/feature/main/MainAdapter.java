package fikrims.io.kamus.feature.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.orhanobut.hawk.Hawk;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fikrims.io.kamus.R;
import fikrims.io.kamus.data.model.KamusModel;
import fikrims.io.kamus.feature.detail.DetailActivity;
import fikrims.io.kamus.utils.Constant;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private Context context;
    private List<KamusModel> list;

    MainAdapter(Context context){
        this.list = new ArrayList<>();
        this.context = context;
    }

    public void setKamusResult(List<KamusModel> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_kamus, null);
        // create ViewHolder
        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapter.ViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_word)
        TextView textWord;

        @BindView(R.id.text_translate)
        TextView textTranslate;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(KamusModel kamusModel){
            textWord.setText(kamusModel.getWord());
            textTranslate.setText(kamusModel.getTranslate());

            itemView.setOnClickListener(view -> {
                Hawk.put(Constant.Key.TEXT_KAMUS, kamusModel);
                DetailActivity.start(context);
            });
        }
    }
}
