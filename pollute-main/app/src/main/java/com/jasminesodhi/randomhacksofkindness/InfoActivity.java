package com.jasminesodhi.randomhacksofkindness;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jasminesodhi on 16/07/17.
 */

public class InfoActivity extends Fragment {

    public static String url, tokenString;
    TextView totalDistance, carbonFootprint, totalAmount, totalTrips, unpaidAmount;

    public static String totalDistanceString, carbonFootprintString, totalAmountString, totalTripsString, unpaidAmountString;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_info,container,false);
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        totalDistance = (TextView) getView().findViewById(R.id.total_distance);
        carbonFootprint = (TextView) getView().findViewById(R.id.carbon_footprint);
        totalAmount = (TextView) getView().findViewById(R.id.total_amount);
        totalTrips = (TextView) getView().findViewById(R.id.total_trips);
        unpaidAmount = (TextView) getView().findViewById(R.id.unpaid_amount);

        url = "http://twentyeight10.tech/rhok.php/get";

        totalDistanceString = "Total Distance in kms: "; carbonFootprintString = "Carbon Footprint: ";
        totalAmountString = "Total Amount: Rs. "; totalTripsString = "Total Trips: ";
        unpaidAmountString = "Unpaid Amount: Rs. ";

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            int token = bundle.getInt("Token", 0000);
            Log.d("Token Graph Activity", Integer.toString(token));

            tokenString = Integer.toString(token);
            displayValues();
        }
    }

    public void displayValues() {

        StringRequest stringRequest = new StringRequest (Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("Result response 3", response);

                        showJSON(response);
                        setTexts();
                        //Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d("Result response 4", error.toString());
                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("token", tokenString);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    public void showJSON(String json) {

        try {

            JSONObject jsonObject = new JSONObject(json);

            totalDistanceString += jsonObject.getString("total_distance");
            carbonFootprintString += jsonObject.getString("carbon_footprint");
            totalAmountString += jsonObject.getString("paid_amount");
            totalTripsString += jsonObject.getString("total_trips");
            unpaidAmountString += jsonObject.getString("unpaid_amount");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTexts(){

        totalDistance.setText(totalDistanceString);
        carbonFootprint.setText(carbonFootprintString);
        totalAmount.setText(totalAmountString);
        totalTrips.setText(totalTripsString);
        unpaidAmount.setText(unpaidAmountString);
    }
}
