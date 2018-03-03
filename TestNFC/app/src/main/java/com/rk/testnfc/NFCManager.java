package com.rk.testnfc;

import android.app.Activity;
import android.nfc.NfcAdapter;

/**
 * Created by RK on 03-03-2018.
 */

public class NFCManager {
    private Activity activity;
    private NfcAdapter nfcAdpt;

    public NFCManager(Activity activity){
        this.activity = activity;
    }

    public int verifyNFC(){
        nfcAdpt = NfcAdapter.getDefaultAdapter(activity);

        if(nfcAdpt==null)
            return -1;
        if(!nfcAdpt.isEnabled())
            return -2;

        return 0;

    }

}
