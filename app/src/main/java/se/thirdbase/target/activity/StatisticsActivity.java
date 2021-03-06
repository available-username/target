package se.thirdbase.target.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import se.thirdbase.target.R;
import se.thirdbase.target.fragment.precision.PrecisionHitDistributionFragment;
import se.thirdbase.target.fragment.precision.PrecisionRoundSummaryFragment;
import se.thirdbase.target.fragment.precision.PrecisionScoreDistributionFragment;
import se.thirdbase.target.fragment.statistics.StatisticsPrecisionFragment;
import se.thirdbase.target.fragment.statistics.StatisticsPrecisionProgressFragment;
import se.thirdbase.target.model.precision.PrecisionRound;

/**
 * Created by alexp on 2/24/16.
 */
public class StatisticsActivity extends BaseActivity implements StatisticsStateListener, PrecisionRoundSummaryFragment.PrecisionRoundSummaryListener {

    private static final String BACK_STACK_TAG_PRECISION_FRAGMENT = "BACK_STACK_TAG_PRECISION_FRAGMENT";
    private static final String BACK_STACK_TAG_PRECISION_PROGRESS_FRAGMENT = "BACK_STACK_TAG_PRECISION_PROGRESS_FRAGMENT";
    private static final String BACK_STACK_TAG_PRECISION_ROUND_SUMMARY_FRAGMENT = "BACK_STACK_TAG_PRECISION_ROUND_SUMMARY_FRAGMENT";
    private static final String BACK_STACK_TAG_PRECISION_ROUND_SCORE_DISTRIBUTION = "BACK_STACK_TAG_PRECISION_ROUND_SCORE_DISTRIBUTION";
    private static final String BACK_STACK_TAG_PRECISION_ROUND_HIT_DISTRIBUTION = "BACK_STACK_TAG_PRECISION_ROUND_HIT_DISTRIBUTION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        if (savedInstanceState == null) {
            onOverview();
        }
    }

    @Override
    public int getLayoutContainerId() {
        return R.id.statistics_layout_id;
    }

    @Override
    public void onOverview() {
        Fragment fragment = StatisticsPrecisionFragment.newInstance();
        displayFragment(fragment, false, BACK_STACK_TAG_PRECISION_FRAGMENT);
    }

    @Override
    public void onPrecision() {

    }

    @Override
    public void onPrecisionProgress() {
        Fragment fragment = StatisticsPrecisionProgressFragment.newInstance();
        displayFragment(fragment, true, BACK_STACK_TAG_PRECISION_PROGRESS_FRAGMENT);
    }

    @Override
    public void onPrecisionRoundSummary(PrecisionRound precisionRound) {
        Fragment fragment = PrecisionRoundSummaryFragment.newInstance(precisionRound);
        displayFragment(fragment, true, BACK_STACK_TAG_PRECISION_ROUND_SUMMARY_FRAGMENT);
    }

    @Override
    public void onPrecisionRoundScoreDistribution(PrecisionRound precisionRound) {
        Fragment fragment = PrecisionScoreDistributionFragment.newInstance(precisionRound);
        displayFragment(fragment, true, BACK_STACK_TAG_PRECISION_ROUND_SCORE_DISTRIBUTION);
    }

    @Override
    public void onPrecisionRoundHitDistribution(PrecisionRound precisionRound) {
        Fragment fragment = PrecisionHitDistributionFragment.newInstance(precisionRound);
        displayFragment(fragment, true, BACK_STACK_TAG_PRECISION_ROUND_HIT_DISTRIBUTION);
    }
}
