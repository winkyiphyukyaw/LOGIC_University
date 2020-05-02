package com.example.logic_university;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.cert.CertPathValidatorException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class disbursementDetailActivity extends AppCompatActivity
//        implements  AsyncTaskGET.IServerResponse3
        {
    String jsonString;
    ListView listview;
    DisbursementDetailAdapter disbursementDetailAdapter;
    String url = "";
    JSONObject jsonObject1;
    JSONObject jsonObject2;
    TextView departName;
    TextView collectionPoint;
    TextView repName;
    TextView email;
    JSONArray array1;
    JSONArray array2;
    String disbursementID;
    Button updateBtn;
    Button OTPbtn;
    TextView otp;
    public static int clickTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disbursement_detail);

        updateBtn=(Button)findViewById(R.id.update_save);
        OTPbtn=(Button)findViewById(R.id.generateOTP);
        departName = (TextView) findViewById(R.id.disbursement_department);
        collectionPoint = (TextView) findViewById(R.id.collectin_point);
        repName = (TextView) findViewById(R.id.representative_name);
        email = (TextView) findViewById(R.id.email);
        disbursementID=getIntent().getExtras().getString("disbursmentID");
        jsonString = getIntent().getExtras().getString("list_details");
        listview = (ListView) findViewById(R.id.listView3);
        otp=(TextView)findViewById(R.id.otp);
        disbursementDetailAdapter = new DisbursementDetailAdapter(this, R.layout.row_for_disbursement_details);

        try {
            jsonObject1 = new JSONObject(jsonString);
            array1 = jsonObject1.getJSONArray("model1");
            array2 = jsonObject1.getJSONArray("model2");

            departName.setText(array1.getJSONObject(0).getString("Departmentname").toString());
            repName.setText(array1.getJSONObject(0).getString("UserName").toString());
            collectionPoint.setText(array1.getJSONObject(0).getString("CollectionPoint").toString());
            email.setText(array1.getJSONObject(0).getString("EmailID").toString());
            String OTP=array1.getJSONObject(0).getString("OTP").toString();
            //otp.setText(array1.getJSONObject(0).getString("OTP").toString());
            if(OTP.compareTo("0")==0){
                OTPbtn.setEnabled(true);
            }
            else{
                OTPbtn.setEnabled(false);
                otp.setText(OTP);
            }
            for (int i = 0; i < array2.length(); i++) {
                jsonObject2 = array2.getJSONObject(i);
                ListofDisbursementItem item = new ListofDisbursementItem(
                        jsonObject2.getString("ItemID"),
                        jsonObject2.getString("ItemName"),
                        jsonObject2.getString("ActualQty"),
                        jsonObject2.getString("DeliveredQty")
                );
                disbursementDetailAdapter.add(item);
            }
            listview.setAdapter(disbursementDetailAdapter);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickTime==1){
//                    updateBtn.setEnabled(false);
                    Toast.makeText(disbursementDetailActivity.this,"Updated Already",Toast.LENGTH_LONG).show();
                }
                else{
                    ClickUpdateButton();
                }
            }
        });



        OTPbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClickGenerateOTP();
//                FetchOtp();
            }
        });

    }
//    private void FetchOtp(){
//        AsyncTaskGET fetchOTPtask=new AsyncTaskGET();
//        Command3 cmd_otp=new Command3(disbursementDetailActivity.this,"http://10.0.2.2:62084/api/Disbursement/getOTP");
//        fetchOTPtask.execute(cmd_otp);
//    }
    private void ClickGenerateOTP(){
        String url ="http://10.0.2.2:62084/api/Disbursement/validateOTP";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject JO=new JSONObject(response);
                            String otp1=JO.get("model").toString();
                            otp.setText(otp1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(disbursementDetailActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
//                params.put("DisbursementID",Integer.parseInt(disbursementID.getText().toString()));
                params.put("DisbursementID",disbursementID);
//                params.put("Username",itemID.getText().toString());
//                params.put("EmailID",wantedQty.getText().toString());
//                params.put("CollectionPoint",DeliveredQty.getText().toString());
//                params.put("Reason", CertPathValidatorException.Reason.getText().toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void ClickUpdateButton() {//the flow should be "click raise update button->
        JSONObject JSONestimate = new JSONObject();
        final JSONArray myarray = new JSONArray();
        for (int j = 0; j < disbursementDetailAdapter.getCount(); j++) {
            ListofDisbursementItem listofDisbursementItem = (ListofDisbursementItem) disbursementDetailAdapter.getItem(j);
            RequisitionDetails item = new RequisitionDetails(Integer.parseInt(disbursementID), listofDisbursementItem.getItemCode(), Integer.parseInt(listofDisbursementItem.getReceivedQty()), "pending");
            try {
                JSONestimate.put("data:" + String.valueOf(j + 1), item.getReqDetJSONObject());
                myarray.put(JSONestimate);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        clickTime=1;
        String url="http://10.0.2.2:62084/api/Disbursement/UpdateQuantity";
//        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.POST, url,
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(this,response.toString(),Toast.LENGTH_LONG).show();
                        // parseData(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected HashMap<String,String> getParams(){

                HashMap<String,String> params = new HashMap<String,String>();
//                ArrayList<RequisitionDetails> DISBURSEMENTList = new ArrayList<RequisitionDetails>();
//                params.put("receivedQtyUpdate",DISBURSEMENTList.toString());
                params.put("receievedQtyUpdate",myarray.toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        Toast.makeText(this,"Update Successfully",Toast.LENGTH_LONG).show();
        updateBtn.setEnabled(false);
    }

//    @Override
//    public void onServerResponse(JSONObject jsonObject)  {
//        try{
//        String otp_num=jsonObject.getString("model");
//        otp.setText(otp_num);}
//        catch(Exception e){e.printStackTrace();}
//    }

    private class DisbursementDetailAdapter extends ArrayAdapter {

        List list=new ArrayList();
        public DisbursementDetailAdapter(Context context, int resource)
        {
            super(context, resource);
        }

        public void add(ListofDisbursementItem object) {
            super.add(object);
            list.add(object);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            DetailHolder holder=new DetailHolder();
            View row=convertView;
            if(row==null){
                LayoutInflater layoutInflater=(LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row=layoutInflater.inflate(R.layout.row_for_disbursement_details,parent,false);
                holder.itemCode=row.findViewById(R.id.ITEMCODE);
                holder.stationery_description=row.findViewById(R.id.DESCRIPTION);
                holder.required_Qty=row.findViewById(R.id.REQUIREDqty);
                holder.receivedQty=row.findViewById(R.id.RECEIVEDqty);
                holder.createDiscrepancyBtn=row.findViewById(R.id.CreateDiscrepancyBtn);
                row.setTag(holder);
            }
            else{
                holder=(DetailHolder)row.getTag();
            }
            final ListofDisbursementItem listItem=(ListofDisbursementItem) this.getItem(position);
            holder.itemCode.setText(listItem.getItemCode());
            holder.stationery_description.setText(listItem.getStationeryDescription());
            holder.required_Qty.setText(String.valueOf(listItem.getRequiredQty()));

            final DetailHolder finalHolder = holder;
            holder.createDiscrepancyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//
                           String received_qty=finalHolder.receivedQty.getText().toString();
//                    if(received_qty>listItem.getRequiredQty()){
//                        Toast.makeText(disbursementDetailActivity.this,"received Qty cannot be over Required Qty",Toast.LENGTH_LONG).show();
//                    }
//                    else{
                    Intent createDiscrepacyIntent=new Intent(disbursementDetailActivity.this,CreateDiscrepancyActivity.class);
                    createDiscrepacyIntent.putExtra("disbursmentID",disbursementID);
                    createDiscrepacyIntent.putExtra("ItemCode",listItem.getItemCode());
                    createDiscrepacyIntent.putExtra("RequiredQty",listItem.getRequiredQty());
                    createDiscrepacyIntent.putExtra("ReceivedQty",received_qty);
                    startActivity(createDiscrepacyIntent);
//                    }
                }
            });
            return row;
        }
        class DetailHolder{
            TextView itemCode,stationery_description,required_Qty;
            EditText receivedQty;
            Button createDiscrepancyBtn;
        }
    }
}

