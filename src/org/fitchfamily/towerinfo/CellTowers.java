package org.fitchfamily.towerinfo;

import android.content.Context;
import android.telephony.CellInfo;
import android.telephony.CellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.util.Log;
import java.util.List;

class currentCellTower {
    Context mContext;
    private static final String TAG = "towerInfo.currentCellTower";

    private List<CellInfo> allCells;
    private List<NeighboringCellInfo> neighborCells;

    public currentCellTower(Context context) {
        mContext = context;
        allCells = null;
        neighborCells = null;
    }

    public CellSpec getCellSpecs() {
        if (mContext == null) {
            Log.d(TAG, "Context is NULL.");
            return null;
        }
        TelephonyManager tm=(TelephonyManager)mContext.getSystemService(mContext.TELEPHONY_SERVICE);
        if (tm == null) {
            Log.d(TAG, "TelephonyManager is NULL.");
            return null;
        }

        String mncString = tm.getNetworkOperator();
        if ((mncString == null) || (mncString.length() < 5) || (mncString.length() > 6)) {
            Log.d(TAG, "mncString is NULL or not recognized.");
            return null;
        }
        int mcc = Integer.parseInt(mncString.substring(0,3));
        int mnc = Integer.parseInt(mncString.substring(3));

        try {
            allCells = tm.getAllCellInfo();
        } catch (NoSuchMethodError e) {
            allCells = null;
        }
        neighborCells = tm.getNeighboringCellInfo();
        CellLocation cellLocation = tm.getCellLocation();
        if (cellLocation == null) {
            Log.d(TAG, "cellLocation is NULL.");
            return null;
        }
        if (cellLocation instanceof GsmCellLocation) {
            GsmCellLocation gsmCellLocation = (GsmCellLocation) cellLocation;
            int cid = gsmCellLocation.getCid();
            int lac = gsmCellLocation.getLac();
            int psc = gsmCellLocation.getPsc();
            if (psc == -1) {
                return new CellSpec(Radio.GSM, mcc, mnc, lac, cid);
            } else if ((cid != -1) && (lac != -1)) {
                CellSpec cellSpec = new CellSpec(Radio.UMTS, mcc, mnc, lac, cid);
                cellSpec.setPsc(psc);
                return cellSpec;
            }
        }
        Log.d(TAG, "Not connected to network or using LTE, which is not supported for SDK <= 16");
        return null;
    }

    @Override public String toString() {
        String rslt = "";

        if (allCells != null) {
            rslt += "getAllCellInfo(): " +allCells.toString() + "\n\n";
        }
        if (allCells != null) {
            rslt += "getNeighboringCellInfo(): " +neighborCells.toString() + "\n\n";
        }
        CellSpec curCell = getCellSpecs();
        if (curCell == null) {
            rslt += "No current Cell Information\n";
        } else {
            rslt += curCell.toString();
        }
        return rslt;
    }

}
