package se.thirdbase.target.fragment;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import se.thirdbase.target.R;
import se.thirdbase.target.adapter.StatisticsPrecisionRoundListAdapter;
import se.thirdbase.target.db.PrecisionDBHelper;
import se.thirdbase.target.db.PrecisionRoundContract;
import se.thirdbase.target.model.PrecisionRound;

/**
 * Created by alexp on 2/24/16.
 */
public class StatisticsPrecisionFragment extends StatisticsBaseFragment {

    public static final String TAG = StatisticsPrecisionFragment.class.getSimpleName();

    public static StatisticsBaseFragment newInstance() {
        return new StatisticsPrecisionFragment();
    }

    private Button mRoundsProgressButton;
    private ListView mRoundsListView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    protected void onOverview() {
        super.onOverview();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.statistics_precision_layout, container, false);

        List<PrecisionRound> precisionRounds = getPrecisionRounds();

        PrecisionRound[] precisionRoundsArray = new PrecisionRound[precisionRounds.size()];
        precisionRounds.toArray(precisionRoundsArray);

        mRoundsListView = (ListView) view.findViewById(R.id.statistics_precision_layout_rounds_list);

        StatisticsPrecisionRoundListAdapter adapter = new StatisticsPrecisionRoundListAdapter(getContext(), R.layout.statistics_precision_list_row, precisionRoundsArray);

        mRoundsListView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private List<PrecisionRound> getPrecisionRounds() {
        PrecisionDBHelper helper = PrecisionDBHelper.getInstance(getContext());
        SQLiteDatabase db = helper.getReadableDatabase();


        String[] columns = {
                PrecisionRoundContract.PrecisionRoundEntry._ID,
                PrecisionRoundContract.PrecisionRoundEntry.COLUMN_NAME_DATE_TIME
        };

        Cursor cursor = db.query(PrecisionRoundContract.TABLE_NAME,
                columns, //columns *
                null, //selection *
                null, //selectionArgs
                null, //groupBy
                null, //having
                String.format("datetime(%s) DESC", PrecisionRoundContract.PrecisionRoundEntry.COLUMN_NAME_DATE_TIME), //orderBy,
                "10");

        List<PrecisionRound> precisionRounds = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            try {
                while (!cursor.isAfterLast()) {
                    int id = cursor.getInt(cursor.getColumnIndex(PrecisionRoundContract.PrecisionRoundEntry._ID));

                    PrecisionRound precisionRound = PrecisionRoundContract.retrievePrecisionRound(db, id);

                    precisionRounds.add(precisionRound);
                    
                    cursor.moveToNext();
                }
            } finally {
                cursor.close();
            }
        }

        return precisionRounds;
    }
}
