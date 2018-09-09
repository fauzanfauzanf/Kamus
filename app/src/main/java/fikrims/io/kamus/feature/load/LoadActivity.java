package fikrims.io.kamus.feature.load;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.SQLException;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.orhanobut.hawk.Hawk;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import fikrims.io.kamus.R;
import fikrims.io.kamus.data.database.KamusHelper;
import fikrims.io.kamus.data.model.KamusModel;
import fikrims.io.kamus.feature.main.MainActivity;
import fikrims.io.kamus.utils.AppPreference;

public class LoadActivity extends AppCompatActivity {

    @BindView(R.id.progress)
    ProgressBar progressBar;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        ButterKnife.bind(this);

        context = LoadActivity.this;
        Hawk.init(context).build();

        LoadData data = new LoadData();
        data.execute();
    }

    private class LoadData extends AsyncTask<Void, Integer, Void> {
        KamusHelper kamusHelper;
        AppPreference appPreference;
        double progress;
        double maxprogress = 100;

        @Override
        protected void onPreExecute() {
            kamusHelper = new KamusHelper(context);
            appPreference = new AppPreference(context);
        }

        @SuppressWarnings("WrongThread")
        @Override
        protected Void doInBackground(Void... params) {
            Boolean firstRun = appPreference.getFirstRun();
            if (firstRun) {
                ArrayList<KamusModel> kamusEnglish = preLoadRaw(R.raw.english_indonesia);
                ArrayList<KamusModel> kamusIndonesia = preLoadRaw(R.raw.indonesia_english);

                publishProgress((int) progress);

                try {
                    kamusHelper.open();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                Double progressMaxInsert = 100.0;
                Double progressDiff = (progressMaxInsert - progress) / (kamusEnglish.size() + kamusIndonesia.size());

                kamusHelper.insertTransaction(kamusEnglish, true);
                progress += progressDiff;
                publishProgress((int) progress);

                kamusHelper.insertTransaction(kamusIndonesia, false);
                progress += progressDiff;
                publishProgress((int) progress);

                kamusHelper.close();
                appPreference.setFirstRun(false);

                publishProgress((int) maxprogress);
            } else {
//                tv_load.setVisibility(View.INVISIBLE);
                try {
                    synchronized (this) {
                        this.wait(1000);
                        publishProgress(50);

                        this.wait(300);
                        publishProgress((int) maxprogress);
                    }
                } catch (Exception e) {
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void result) {
            MainActivity.start(context);
        }
    }

    public ArrayList<KamusModel> preLoadRaw(int data) {
        ArrayList<KamusModel> kamusModels = new ArrayList<>();
        BufferedReader reader;
        try {
            Resources res = getResources();
            InputStream raw_dict = res.openRawResource(data);

            reader = new BufferedReader(new InputStreamReader(raw_dict));
            String line = null;
            do {
                line = reader.readLine();
                String[] splitstr = line.split("\t");
                KamusModel kamusModel;
                kamusModel = new KamusModel(splitstr[0], splitstr[1]);
                kamusModels.add(kamusModel);
            } while (line != null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return kamusModels;
    }
}
