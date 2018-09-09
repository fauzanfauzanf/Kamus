package fikrims.io.kamus.feature.main;

import android.content.Context;
import android.database.SQLException;

import java.util.List;

import fikrims.io.kamus.R;
import fikrims.io.kamus.data.database.KamusHelper;
import fikrims.io.kamus.data.model.KamusModel;

public class MainPresenter {

    private final Context context;
    private final MainListener mainListener;
    private final KamusHelper kamusHelper;

    public MainPresenter(Context context, MainListener mainListener, KamusHelper kamusHelper) {
        this.context = context;
        this.mainListener = mainListener;
        this.kamusHelper = kamusHelper;
    }

    public interface MainListener{
        void getSearchResult(List<KamusModel> list);
    }

    void doSearch(String search, boolean isEnglish){
        try {
            kamusHelper.open();
            if (search.isEmpty()) {
                mainListener.getSearchResult(kamusHelper.getAllData(isEnglish));
            } else {
                mainListener.getSearchResult(kamusHelper.getDataByName(search, isEnglish));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            kamusHelper.close();
        }
    }
}
