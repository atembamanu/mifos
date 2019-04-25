package org.mifos.mobilebanking.ui.fragments;


import android.content.SharedPreferences;
import android.net.SSLCertificateSocketFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.json.JSONException;
import org.json.JSONObject;
import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.utils.Constants;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * A simple {@link Fragment} subclass.
 */
public class StkPushFragment extends Fragment {
    private long accountId;
    View rootView;
    private  static final String urlAdress="https://techsavanna.net:3000/UIPSTKPush/webresources/uipstkpush/mpesa/push";

    TextView accountidtxt;
    EditText amountedt,phoneedt;
    Button makepaymentbtn;
    public StkPushFragment() {
        // Required empty public constructor
    }

    public static StkPushFragment newInstance(Long accountId){
        StkPushFragment transferFragment = new StkPushFragment();
        Bundle args = new Bundle();
        args.putLong(Constants.ACCOUNT_ID, accountId);
        // args.putString(Constants.TRANSFER_TYPE, transferType);
        transferFragment.setArguments(args);
        return transferFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            accountId = getArguments().getLong(Constants.ACCOUNT_ID);
            // transferType = getArguments().getString(Constants.TRANSFER_TYPE);
        }
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.fragment_stk_push, container, false);
        amountedt=rootView.findViewById(R.id.amount);
        phoneedt=rootView.findViewById(R.id.phoneno);
        accountidtxt=rootView.findViewById(R.id.accounttxt);
        makepaymentbtn=rootView.findViewById(R.id.paymentbtn);

        // accountidtxt.setText((int) accountId);
        accountidtxt.setText(String.valueOf(accountId));

        makepaymentbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payLoan(accountId);
            }
        });
        return rootView;
    }

    private void payLoan(final long accountId) {

        final String amount=amountedt.getText().toString();
        final String phone=phoneedt.getText().toString();

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    URL url = new URL(urlAdress);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    if (conn instanceof HttpsURLConnection) {
                        HttpsURLConnection httpsConn = (HttpsURLConnection) conn;
                        httpsConn.setSSLSocketFactory(SSLCertificateSocketFactory.getInsecure(0, null));
                        httpsConn.setHostnameVerifier(new AllowAllHostnameVerifier());
                    }
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept","application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("phone", phone);
                    jsonParam.put("amount", amount);
                    jsonParam.put("accountReference", accountId);
                    jsonParam.put("transactionDesc", "Subscription");

                    Log.i("JSON", jsonParam.toString());
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                    os.writeBytes(jsonParam.toString());

                    os.flush();
                    os.close();
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    System.out.println("Output from Server .... \n");
                    //   System.out.println("jsonnn"+br.readLine());

                    // String json = null;

                    //System.out.println("CheckoutRequestID :"+ br.readLine());
                    try {
                        JSONObject ns = new JSONObject(br.readLine());
                        String checkout=ns.getString("CheckoutRequestID");



                    }catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.i("MSG new" , conn.getInputStream().toString());


                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

    }

}