package com.jasminesodhi.randomhacksofkindness;

import android.graphics.Color;
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
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by jasminesodhi on 16/07/17.
 */

public class GraphActivity extends Fragment {

    PieChart pieChart;
    static String url, tokenString;

    FancyButton donate;
    int amount;

    TextView amountTextView;
    String amountToBePaid;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_graph,container,false);

        amountToBePaid = "Amount to be paid by you is Rs. ";
        amountTextView = (TextView) view.findViewById(R.id.donate_text_view);

        donate = (FancyButton) view.findViewById(R.id.button_donate);
        url = "http://twentyeight10.tech/rhok.php/get";

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            int token = bundle.getInt("Token", 0000);
            //Log.d("Token Graph Activity", Integer.toString(token));

            tokenString = Integer.toString(token);
            displayPieChart(view);
        }

        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                url = "http://twentyeight10.tech/rhok.php/donate";
                StringRequest stringRequest = new StringRequest (Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //Log.d("Result response 1", response);

                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    //Log.d("JSON Object", jsonObject.toString());
                                    amount = jsonObject.getInt("unpaid_amount");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                showJSON(response);
                                //Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //Log.d("Result response 2", error.toString());
                                //Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                            }
                        }) {
                    @Override
                    protected Map<String,String> getParams(){
                        Map<String,String> params = new HashMap<String, String>();
                        params.put("token", tokenString);
                        params.put("amount", Integer.toString(amount));
                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                requestQueue.add(stringRequest);
                Toast.makeText(getActivity(), "Donation made!", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    void displayPieChart(View view) {

        pieChart = (PieChart) view.findViewById(R.id.pie_chart);
        pieChart.getDescription().setEnabled(false);

        StringRequest stringRequest = new StringRequest (Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //Log.d("Result response 3", response);

                        showJSON(response);
                        //Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        //Log.d("Result response 4", error.toString());
                        //Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
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

    private void showJSON(String json) {

        try {

            JSONObject jsonObject = new JSONObject(json);
            Log.d("json values", jsonObject.toString());

            float unpaidTrips = (float) jsonObject.getDouble("unpaid_amount");
            float paidTrips = (float) jsonObject.getDouble("paid_amount");

            Log.d("Values", Double.toString(unpaidTrips) + " " + Double.toString(paidTrips));

            //Log.d("Abcd", Double.toString(unpaidTrips) + Double.toString(paidTrips));

            pieChart.setUsePercentValues(true);
            List<PieEntry> entries = new ArrayList<>();

            float percent_unpaid = ((unpaidTrips * 100) / (unpaidTrips + paidTrips));
            float percent_paid = 100 - percent_unpaid;

            //Log.d("kuchbhi", percent_unpaid + " " + percent_unpaid);

            entries.add(new PieEntry(percent_unpaid, "Unpaid Amt."));
            entries.add(new PieEntry(percent_paid, "Paid Amt."));

            PieDataSet pieDataSet = new PieDataSet(entries, "Results");
            pieDataSet.setDrawIcons(false);
            pieDataSet.setSliceSpace(3f);
            pieDataSet.setIconsOffset(new MPPointF(0, 40));
            pieDataSet.setSelectionShift(5f);

            PieData data = new PieData(pieDataSet);
            data.setValueFormatter(new PercentFormatter());
            data.setValueTextSize(11f);
            data.setValueTextColor(Color.WHITE);

            pieChart.setData(data);
            pieChart.highlightValues(null);

            pieChart.invalidate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
