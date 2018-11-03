package br.com.servprod;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.List;
import java.util.Vector;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchResultFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ListView listView;
    private Vector<ListSearchResult> list = new Vector<ListSearchResult>();

    private SearchResultTask mSearchResultTask = null;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private final String NAMESPACE = "http://192.168.25.9/servprod/";
    private final String URL = "http://192.168.25.9/servprod/service1.php";
    private final String METHOD_NAME = "search";
    private final String SOAP_ACTION = "http://192.168.25.9/servprod/service1.php#"+METHOD_NAME;

    public SearchResultFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchResultFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchResultFragment newInstance(String param1, String param2) {
        SearchResultFragment fragment = new SearchResultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mSearchResultTask = new SearchResultTask("item");
        mSearchResultTask.execute((Void) null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_result, container, false);

        listView = (ListView) view.findViewById(R.id.search_result_listView);

        return view;
    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        updateList();
        super.onStart();
    }

    public class SearchResultTask extends AsyncTask<Void, Void, Boolean> {

        private final String Item;
        String aux;

        SearchResultTask(String item) {
            Item = item;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {

                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("item", "lavagem de carro");

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                //envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

                try {
                    androidHttpTransport.call(SOAP_ACTION, envelope);
                    String res = (String) envelope.getResponse();

                    JSONArray Providers = new JSONArray(res);
                    for (int i = 0; i < Providers.length(); i++) {
                        ListSearchResult listSearchResult = new ListSearchResult();
                        JSONObject Provider = Providers.getJSONObject(i);
                        listSearchResult.name = Provider.getString("first_name");
                        listSearchResult.scored = Provider.getString("password");
                        listSearchResult.distance = "50 Km";
                        list.add(listSearchResult);
                        Log.i("", Provider.getString("first_name"));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                Thread.sleep(2000);
            } catch (Exception e) {
                return false;
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mSearchResultTask = null;
            //showProgress(false);

            if (success) {
                getActivity().runOnUiThread(new Runnable(){
                    public void run(){
                        updateList();
                    }
                });
            } else {

                //mPasswordView.setError(getString(R.string.error_incorrect_password));
                //mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mSearchResultTask = null;
            //showProgress(false);
        }
    }

    public class ListSearchResult {

        private String name;
        private String scored;
        private String distance;

    }

    private class ListAdaper extends ArrayAdapter<ListSearchResult> {
        private int layout;
        private List<ListSearchResult> mlistSearchResult;
        private ListAdaper(Context context, int resource, List<ListSearchResult> objects) {
            super(context, resource, objects);
            mlistSearchResult = objects;
            layout = resource;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder mainViewholder = null;
            if(convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.thumbnail = (ImageView) convertView.findViewById(R.id.list_item_search_result_thumbnail);
                viewHolder.name = (TextView) convertView.findViewById(R.id.list_item_search_result_name);
                viewHolder.scored = (TextView) convertView.findViewById(R.id.list_item_search_result_scored);
                viewHolder.distance = (TextView) convertView.findViewById(R.id.list_item_search_result_distance);
                convertView.setTag(viewHolder);
            }
            mainViewholder = (ViewHolder) convertView.getTag();
            mainViewholder.name.setText(getItem(position).name);
            mainViewholder.scored.setText(getItem(position).scored);
            mainViewholder.distance.setText(getItem(position).distance);
            return convertView;
        }
    }

    public class ViewHolder {
        ImageView thumbnail;
        TextView name;
        TextView scored;
        TextView distance;
    }

    private void updateList(){

        listView.setAdapter(new ListAdaper(getActivity().getApplicationContext(), R.layout.list_item_search_result, list));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(UsersActivity.this, "CPF:"+u_cpf.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
