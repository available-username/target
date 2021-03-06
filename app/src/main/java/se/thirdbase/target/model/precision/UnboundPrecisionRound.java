package se.thirdbase.target.model.precision;

import android.os.Parcel;

import java.util.List;

/**
 * Created by alex on 4/4/16.
 */
public class UnboundPrecisionRound extends PrecisionRound {

    private static final int MAX_NBR_SERIES = Integer.MAX_VALUE;

    public UnboundPrecisionRound() {
        super(false);
    }

    public UnboundPrecisionRound(List<PrecisionSeries> precisionSeries, String notes) {
        super(precisionSeries, false, notes);
    }

    public UnboundPrecisionRound(List<PrecisionSeries> precisionSeries, String notes, long timestamp) {
        super(precisionSeries, false, notes, timestamp);
    }

    protected UnboundPrecisionRound(Parcel in) {
        super(in);
    }

    public static final Creator<UnboundPrecisionRound> CREATOR = new Creator<UnboundPrecisionRound>() {
        @Override
        public UnboundPrecisionRound createFromParcel(Parcel in) {
            return new UnboundPrecisionRound(in);
        }

        @Override
        public UnboundPrecisionRound[] newArray(int size) {
            return new UnboundPrecisionRound[size];
        }
    };


    @Override
    public int getMaxNbrSeries() {
        return MAX_NBR_SERIES;
    }
}
