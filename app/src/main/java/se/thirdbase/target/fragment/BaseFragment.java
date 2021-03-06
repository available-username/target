package se.thirdbase.target.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;

import se.thirdbase.target.activity.StateListener;

/**
 * Created by alexp on 2/12/16.
 */
abstract class BaseFragment extends Fragment {

    private StateListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof StateListener) {
            mListener = (StateListener)context;
        }
    }

    protected void onStartup() {
        if (mListener != null) {
            mListener.onStartup();
        }
    }

    protected void onPrecision() {
        if (mListener != null) {
            mListener.onPrecision();
        }
    }

    protected void onWeapons() {
        if (mListener != null) {
            mListener.onWeapons();
        }
    }

    protected void onAmmunition() {
        if (mListener != null) {
            mListener.onAmmunition();
        }
    }

    protected void onStatistics() {
        if (mListener != null) {
            mListener.onStatistics();
        }
    }
}
