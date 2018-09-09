package fikrims.io.kamus.feature.detail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.orhanobut.hawk.Hawk;

import butterknife.BindView;
import butterknife.ButterKnife;
import fikrims.io.kamus.R;
import fikrims.io.kamus.data.model.KamusModel;
import fikrims.io.kamus.utils.Constant;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.text_word)
    TextView textWord;

    @BindView(R.id.text_translate)
    TextView textTranslate;

    private KamusModel kamusModel;

    public static void start(Context context){
        context.startActivity(new Intent(context, DetailActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        kamusModel = Hawk.get(Constant.Key.TEXT_KAMUS);
        textWord.setText(kamusModel.getWord());
        textTranslate.setText(kamusModel.getTranslate());
    }
}
