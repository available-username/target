package se.thirdbase.target.fragment.weapon;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.thirdbase.target.R;
import se.thirdbase.target.db.PrecisionSeriesContract;
import se.thirdbase.target.db.TargetDBHelper;
import se.thirdbase.target.model.Ammunition;
import se.thirdbase.target.model.Weapon;
import se.thirdbase.target.model.precision.PrecisionSeries;
import se.thirdbase.target.util.PaletteGenerator;

/**
 * Created by alexp on 3/3/16.
 */
public class WeaponDisplayAmmunitionDistributionFragment extends Fragment {

    private static final String BUNDLE_TAG_WEAPON = "BUNDLE_TAG_WEAPON";

    private SQLiteDatabase mSQLiteDatabase;

    private Weapon mWeapon;

    public static WeaponDisplayAmmunitionDistributionFragment newInstance(Weapon weapon) {
        Bundle arguments = new Bundle();

        arguments.putParcelable(BUNDLE_TAG_WEAPON, weapon);

        WeaponDisplayAmmunitionDistributionFragment fragment = new WeaponDisplayAmmunitionDistributionFragment();
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        TargetDBHelper helper = TargetDBHelper.getInstance(context);
        mSQLiteDatabase = helper.getReadableDatabase();

        Bundle arguments = getArguments();
        mWeapon = arguments.getParcelable(BUNDLE_TAG_WEAPON);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.weapons_display_ammunition_distribution_layout, container, false);

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }

        PieChart ammoChart = (PieChart)view.findViewById(R.id.weapon_display_ammunition_distribution_layout_chart);
        PieData ammoData = getAmmunitionDistribution();
        ammoChart.setData(ammoData);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(BUNDLE_TAG_WEAPON, mWeapon);
    }

    private void onRestoreInstanceState(Bundle bundle) {
        mWeapon = bundle.getParcelable(BUNDLE_TAG_WEAPON);
    }

    private PieData getAmmunitionDistribution() {

        /* Fetch PrecisionSeries produced with this weapon */
        String selection = PrecisionSeriesContract.PrecisionSeriesEntry.COLUMN_NAME_WEAPON + "=?";
        String[] args = { String.format("%d", mWeapon.getDBHandle()) };
        List<PrecisionSeries> precisionSeries = PrecisionSeries.fetchSelection(mSQLiteDatabase,
                selection, args, null, null, null, null);

        Map<String, Integer> map = new HashMap<>();

        for (PrecisionSeries series : precisionSeries) {
            Ammunition ammo = series.getAmmunition();
            String label = ammo.getMakeAndName();
            int bulletCount = series.getBulletHoles().size();

            if (map.containsKey(label)) {
                bulletCount +=  map.get(label);
            }

            map.put(label, bulletCount);
        }

        int idx = 0;
        List<Entry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        for (String label : map.keySet()) {
            Entry entry = new Entry(map.get(label).intValue(), idx++);
            entries.add(entry);
            labels.add(label);
        }

        String ammoString = getResources().getString(R.string.ammunition);
        PieDataSet dataSet = new PieDataSet(entries, ammoString);
        dataSet.setColors(PaletteGenerator.generate(Color.MAGENTA, entries.size()));

        return new PieData(labels, dataSet);
    }

}
