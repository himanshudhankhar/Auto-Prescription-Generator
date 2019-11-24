package com.docs.telemedicine;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 */
public class Records_FRag extends Fragment {


    public Records_FRag() {
        // Required empty public constructor
    }
   private  Button getButton;
    private EditText meetingKey;
    OkHttpClient client;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view= inflater.inflate(R.layout.fragment_records__frag, container, false);


        getButton=(Button)view.findViewById(R.id.getButton);
        meetingKey = (EditText)view.findViewById(R.id.getPrescription);
        getButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key_entered = meetingKey.getText().toString().trim();
                if(key_entered.length()==0){
                    Toast.makeText(getContext(),"Meeting key not entered",Toast.LENGTH_LONG).show();
                    return;
                }
                client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://5fc0c10b.ngrok.io/getPrescription")
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override public void onFailure(Call call, IOException e) {

                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getContext(), "Failed to connect server", Toast.LENGTH_LONG).show();
                                meetingKey.setText("");
                            }
                        });
                        e.printStackTrace();
                    }
                    @Override public void onResponse(Call call, Response response) throws IOException {
                        try (ResponseBody responseBody = response.body()) {
                            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);


                            final   String resp = responseBody.string();

                            if (resp.matches("wrong_otp")) {
                                getActivity().runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(getContext(), "otp is wrong", Toast.LENGTH_LONG).show();
                                        meetingKey.setText("");
                                    }});
                                return;
                            } else if (resp.matches("Prescription not ready")) {
                                getActivity().runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(getContext(), "Prescription not ready", Toast.LENGTH_SHORT).show();
                                        meetingKey.setText("");
                                    }});
                                return;
                            } else {
                                getActivity().runOnUiThread(new Runnable() {
                                    public void run() {
                                        meetingKey.setText("");
                                        //make a new activity here and run the video calling functionality
                                        //resp is the seesion key , token key
                                        JSONObject prescription = new JSONObject();
                                        try{
                                            prescription=new JSONObject(resp);
                                        }catch(JSONException e){
                                            Toast.makeText(getContext(),"Response invalid",Toast.LENGTH_LONG).show();
                                            e.printStackTrace();
                                            return;
                                        }




                                        Intent intent = new Intent(getContext(),ShowPrecription.class );

                                        JSONObject jsonObject = null;
                                        try {
                                            jsonObject  = new JSONObject(resp);

                                            if(jsonObject==null){
                                                try{
                                                    jsonObject=new JSONObject("{disease:null,symptoms:null,dosage:null}");
                                                }catch(JSONException ee){
                                                    ee.getStackTrace();
                                                }
                                            }
                                            intent.putExtra("disease",jsonObject.getString("disease"));

                                            intent.putExtra("symptoms",jsonObject.getString("symptoms"));
                                            intent.putExtra("dosage",jsonObject.getString("dosage"));
//intent.putExtra("dosage",jsonObject.getJSONArray("dosage"));
                                            startActivity(intent);
                                        }catch(JSONException ee){
                                            ee.getStackTrace();
                                        }
                                    }});
                                return;
                            }

                        }
                    }
                });

                //make a http request to backened to get the prescription


            }
        });


    return view;
    }





}
