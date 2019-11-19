package com.kevnlan.application.loaninterestcalculator;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {

    private Button btnCalc;
    private Button btnAmort;

    AdView mAdView;
    InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAdView = (AdView) findViewById(R.id.adView);
        MobileAds.initialize(this,"ca-app-pub-2142473192633532~1231076023");

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        prepareAd();

        /*
        ScheduledExecutorService scheduledExecutorService =
                Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (interstitialAd.isLoaded()){
                            interstitialAd.show();
                        }
                        else {
                            Log.d("TAG","Interstitial ad not loaded");
                        }
                        prepareAd();
                    }
                });
            }
        },10,10, TimeUnit.SECONDS); */

        final EditText P = findViewById(R.id.txtPrincipal);
        final EditText I = findViewById(R.id.txtInterest);
        final EditText Y = findViewById(R.id.txtYears);
        final EditText TI = findViewById(R.id.txtInt);

        final EditText result = findViewById(R.id.txtEMI);


        btnCalc = findViewById(R.id.btnCalc);
        btnCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String st1 = P.getText().toString();
                String st2 = I.getText().toString();
                String st3 = Y.getText().toString();

                if (TextUtils.isEmpty(st1)) {
                    P.setError("Enter Prncipal Amount");
                    P.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(st2)) {
                    I.setError("Enter Interest Rate");
                    I.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(st3)) {
                    Y.setError("Enter Years");
                    Y.requestFocus();
                    return;
                }

                float p = Float.parseFloat(st1);
                float i = Float.parseFloat(st2);
                float y = Float.parseFloat(st3);

                float Principal = calPric(p);

                float Rate = calInt(i);

                float Months = calMonth(y);

                float Dvdnt = calDvdnt( Rate, Months);

                float FD = calFinalDvdnt (Principal, Rate, Dvdnt);

                float D = calDivider(Dvdnt);

                float emi = calEmi(FD, D);

                float TA = calTa (emi, Months);

                float ti = calTotalInt(TA, Principal);

                float total = calTotalAmount(Principal,TA);


                result.setText(String.valueOf(emi));


                TI.setText(String.valueOf(ti));


            }
        });
    }

    public void prepareAd(){
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-2142473192633532/1203155860");
        interstitialAd.loadAd(new AdRequest.Builder().build());
    }

    public void onBackPressed(){
        if (interstitialAd.isLoaded()){
            interstitialAd.show();

            interstitialAd.setAdListener(new AdListener(){
                @Override
                public void onAdClosed(){
                    super.onAdClosed();
                    finish();
                }
            });
        }
        else {
            super.onBackPressed();
        }
    }

    public  float calPric(float p) {

        return p;

    }

    public  float calInt(float i) {

        return i/12/100;

    }

    public  float calMonth(float y) {

        return y * 12;

    }

    public  float calDvdnt(float Rate, float Months) {

        return (float) (Math.pow(1+Rate, Months));

    }

    public  float calFinalDvdnt(float Principal, float Rate, float Dvdnt) {

        return Principal * Rate * Dvdnt;

    }

    public  float calDivider(float Dvdnt) {

        return Dvdnt-1;

    }

    public  float calEmi(float FD, Float D) {

        return FD/D;

    }

    public  float calTa(float emi, Float Months) {

        return emi*Months;

    }

    public  float calTotalInt(float TA, float Principal) {

        return TA - Principal;

    }

    public float calTotalAmount(float Pricipal,float TA){
        return Pricipal + TA;
    }

}
