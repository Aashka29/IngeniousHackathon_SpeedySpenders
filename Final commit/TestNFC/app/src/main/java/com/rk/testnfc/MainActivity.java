package com.rk.testnfc;

import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //    NfcAdapter mNfcAdapter;
    NFCManager nfcMger;
    private TextView t1;
    private TextView desc;
    private TextView quantity;
    private TextView price;
    private TextView names;
    private EditText quan;
    private ImageButton myBag;
    private ImageButton cart;
    private ImageButton back;
    private ArrayList<String> myList = new ArrayList<String>();
    private ArrayList<Integer> myCount = new ArrayList<>();
    private String out;
    private ArrayList<String> name = new ArrayList<>();
    private ArrayList<String> descrip = new ArrayList<>();
    private ArrayList<Integer> cost = new ArrayList<>();
    private ArrayList<String> id = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nfcMger = new NFCManager(this);
        desc = findViewById(R.id.desc);
        quan = findViewById(R.id.qtyval);
        quantity = findViewById(R.id.quantity);
        myBag = findViewById(R.id.mylist);
        cart = findViewById(R.id.addtocart);
        back = findViewById(R.id.back);
        names = findViewById(R.id.names);
        price = findViewById(R.id.price);
        nfcMger = new NFCManager(this);
        quan.setText("1");
        int i;

        AssetManager assetManager = getAssets();
        InputStream inputStream = null;
        try {
            inputStream = assetManager.open("Items.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        try {
            while((line = in.readLine()) != null) {
                String word = line.trim();
                Log.d("word :",word);
                String mid = "hello";
                int ctr=0;
                mid = String.valueOf(word.charAt(0));
                Log.d("Mid :",mid);
                for(i=1;i<word.length();i++){
                    mid=mid+word.charAt(i);
                    if(word.charAt(i+1)=='$')
                    {
                        if(ctr==0)
                        {
                            Log.d("Inner Mid :",mid);
                            id.add(mid);
                            ctr++;
                            i++;
                            mid=String.valueOf(word.charAt(i+1));
                            i++;
                        }
                        else if(ctr==1)
                        {
                            name.add(mid);
                            ctr++;
                            i++;
                            mid=String.valueOf(word.charAt(i+1));
                            i++;
                        }
                        else if(ctr==2)
                        {
                            cost.add(Integer.parseInt(mid));
                            ctr++;
                            i++;
                            mid=String.valueOf(word.charAt(i+1));
                            i++;
                        }
                        else
                        {
                            descrip.add(mid);
                            ctr++;
                            i++;
                            //mid=String.valueOf(word.charAt(i+1));
                            i++;
                        }
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    protected void onResume(){
        super.onResume();

        int temp = nfcMger.verifyNFC();
        if(temp==0) {
            Intent nfcIntent = new Intent(this, getClass());
            nfcIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, nfcIntent, 0);
            IntentFilter[] intentFiltersArray = new IntentFilter[]{};
            String[][] techList = new String[][]{
                    {android.nfc.tech.Ndef.class.getName()},
                    {android.nfc.tech.NdefFormatable.class.getName()}
            };
            NfcAdapter nfcAdpt = NfcAdapter.getDefaultAdapter(this);
            nfcAdpt.enableForegroundDispatch(this, pendingIntent, intentFiltersArray, techList);
        }
        else{
            if(temp==-1)
                Log.d("xx","NFC not supported!");
            else
                Log.d("yy","NFC not enabled!");

        }


    }

    @Override
    public void onNewIntent(Intent intent) {
        int j;
        t1 = findViewById(R.id.tagUID);
        t1.setText(ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID)));
        String ids = (String)t1.getText();
        for(j=0;j<4;j++){
            if(ids.equals(id.get(j))){
                break;
            }
        }
        desc.setText(descrip.get(j));
        names.setText(name.get(j));
        price.setText(cost.get(j).toString());
    }

    private String ByteArrayToHexString(byte [] inarray) {
        int i, j, in;
        String [] hex = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
        out= "";
        for(j = 0 ; j < inarray.length ; ++j)
        {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;
    }


    public void addToCart(View view)
    {
        Toast.makeText(this, "Item Added!", Toast.LENGTH_SHORT).show();
        String temp = quan.getText().toString();
        int var = Integer.parseInt(temp);
        quan.setText("1");
        desc.setText("");
        t1.setText("Item Details");
        myList.add(out);
        myCount.add(var);

    }

    public void viewMyList(View view)
    {
        back.setVisibility(View.VISIBLE);
        myBag.setVisibility(View.INVISIBLE);
        cart.setVisibility(View.INVISIBLE);
        quan.setVisibility(View.INVISIBLE);
        quantity.setVisibility(View.INVISIBLE);
        t1.setVisibility(View.INVISIBLE);
        String t2 = new String();
        names.setVisibility(View.INVISIBLE);
        price.setVisibility(View.INVISIBLE);
        int i,j;
        if(myCount==null){
            desc.setText("Your Cart is Empty");
        }
        for(i=0;i<myList.size();i++)
        {
            for(j=0;j<names.length();j++){
                if(myList.get(i).equals(id.get(j)))
                    break;
            }
            t2 += name.get(j) + "\t" + cost.get(j) + " x " + myCount.get(i).toString() + "\n";
            desc.setText(t2);
        }


    }

    public void backMethod(View view){
        back.setVisibility(View.INVISIBLE);
        myBag.setVisibility(View.VISIBLE);
        cart.setVisibility(View.VISIBLE);
        quan.setVisibility(View.VISIBLE);
        quantity.setVisibility(View.VISIBLE);
        names.setVisibility(View.VISIBLE);
        price.setVisibility(View.VISIBLE);
        names.setText("Name :");
        price.setText("Price :");
        desc.setText("Description");
        t1.setVisibility(View.VISIBLE);
        t1.setText("");
    }


    public void onCheckout(View view)
    {
        cart.setVisibility(View.INVISIBLE);
        myBag.setVisibility(View.INVISIBLE);
        back.setVisibility(View.INVISIBLE);
        price.setVisibility(View.INVISIBLE);
        names.setVisibility(View.INVISIBLE);
        quan.setVisibility(View.INVISIBLE);
        quantity.setVisibility(View.INVISIBLE);
        t1.setVisibility(View.INVISIBLE);
    }
/*    private NfcAdapter nfcAdapter;
    TextView textViewInfo, textViewTagInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewInfo = (TextView)findViewById(R.id.info);
        textViewTagInfo = (TextView)findViewById(R.id.taginfo);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if(nfcAdapter == null){
            Toast.makeText(this,
                    "NFC NOT supported on this devices!",
                    Toast.LENGTH_LONG).show();
            finish();
        }else if(!nfcAdapter.isEnabled()){
            Toast.makeText(this,
                    "NFC NOT Enabled!",
                    Toast.LENGTH_LONG).show();
            finish();
        }
    }*/
/*
    @Override
    protected void onResume() {
        super.onResume();

        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(getIntent().getAction())) {
            Toast.makeText(this,
                    "onResume() - ACTION_TECH_DISCOVERED1",
                    Toast.LENGTH_SHORT).show();

            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            if(tag == null){
                textViewInfo.setText("tag == null");
            }else{
                String tagInfo = tag.toString() + "\n";

                tagInfo += "\nTag Id: \n";
                byte[] tagId = tag.getId();
                tagInfo += "length = " + tagId.length +"\n";
                for(int i=0; i<tagId.length; i++){
                    tagInfo += Integer.toHexString(tagId[i] & 0xFF) + " ";
                }
                tagInfo += "\n";

                String[] techList = tag.getTechList();
                tagInfo += "\nTech List\n";
                tagInfo += "length = " + techList.length +"\n";
                for(int i=0; i<techList.length; i++){
                    tagInfo += techList[i] + "\n ";
                }

                textViewInfo.setText(tagInfo);

                //Only android.nfc.tech.MifareClassic specified in nfc_tech_filter.xml,
                //so must be MifareClassic
                readMifareClassic(tag);
            }
        }else{
            Toast.makeText(this,
                    "onResume() : 1" + action,
                    Toast.LENGTH_SHORT).show();
        }
    }


    public void readMifareClassic(Tag tag){
        MifareClassic mifareClassicTag = MifareClassic.get(tag);

        String typeInfoString = "--- MifareClassic tag ---\n";
        int type = mifareClassicTag.getType();
        switch(type){
            case MifareClassic.TYPE_PLUS:
                typeInfoString += "MifareClassic.TYPE_PLUS\n";
                break;
            case MifareClassic.TYPE_PRO:
                typeInfoString += "MifareClassic.TYPE_PRO\n";
                break;
            case MifareClassic.TYPE_CLASSIC:
                typeInfoString += "MifareClassic.TYPE_CLASSIC\n";
                break;
            case MifareClassic.TYPE_UNKNOWN:
                typeInfoString += "MifareClassic.TYPE_UNKNOWN\n";
                break;
            default:
                typeInfoString += "unknown...!\n";
        }

        int size = mifareClassicTag.getSize();
        switch(size){
            case MifareClassic.SIZE_1K:
                typeInfoString += "MifareClassic.SIZE_1K\n";
                break;
            case MifareClassic.SIZE_2K:
                typeInfoString += "MifareClassic.SIZE_2K\n";
                break;
            case MifareClassic.SIZE_4K:
                typeInfoString += "MifareClassic.SIZE_4K\n";
                break;
            case MifareClassic.SIZE_MINI:
                typeInfoString += "MifareClassic.SIZE_MINI\n";
                break;
            default:
                typeInfoString += "unknown size...!\n";
        }

        int blockCount = mifareClassicTag.getBlockCount();
        typeInfoString += "BlockCount \t= " + blockCount + "\n";
        int sectorCount = mifareClassicTag.getSectorCount();
        typeInfoString += "SectorCount \t= " + sectorCount + "\n";

        textViewTagInfo.setText(typeInfoString);
    }*/
}
