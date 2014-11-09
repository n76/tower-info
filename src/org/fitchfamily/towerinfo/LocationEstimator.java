package org.fitchfamily.towerinfo;

import android.content.Context;
import java.io.File;
import android.os.Environment;

class LocationEstimator {
    private Context mContext;
    private currentCellTower towerInfo;
    private CellLocationFile cellLocations;

    public LocationEstimator(Context context) {
        mContext = context;
        towerInfo = new currentCellTower(mContext);
        cellLocations = new CellLocationFile(new File(Environment.getExternalStorageDirectory(), ".nogapps/lacells.db"));
        cellLocations.open();
    }

    public LocationSample getLocationEstimate() {
        return cellLocations.getLocation(towerInfo.getCellSpecs());
    }

    @Override public String toString() {
        if (mContext == null) {
            return "mContext is null";
        } else {
            LocationSample loc = getLocationEstimate();
            String rslt = "Cell Tower: "+towerInfo.toString();
            if (loc != null) {
                rslt += "\n\nLocation: " + loc.toString();
            }
            return rslt;
        }
    }
}
