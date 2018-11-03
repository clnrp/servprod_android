package br.com.servprod;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    AutoCompleteTextView autoCompleteTextViewSearch;
    MultiAutoCompleteTextView multiAutoCompleteTextViewSearch;
    ListView listView;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private List<String> nomes = new ArrayList<String>();

    private static final String[] keywords = new String[] {
            "limpesa de estofado", "lavagem de carro", "fotografia", "filmagem", "pedreiro", "diarista", "encanador"
    };

    private OnFragmentInteractionListener mListener;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, keywords);

        View view = inflater.inflate(R.layout.fragment_search, container, false);
        autoCompleteTextViewSearch = (AutoCompleteTextView) view.findViewById(R.id.search_autoCompleteTextView);
        autoCompleteTextViewSearch.setAdapter(adapter);
        listView = (ListView) view.findViewById(R.id.search_listView);

        //multiAutoCompleteTextViewSearch = (MultiAutoCompleteTextView) view.findViewById(R.id.search_multiAutoCompleteText);
        //multiAutoCompleteTextViewSearch.setAdapter(adapter);
        //multiAutoCompleteTextViewSearch.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        Button buttonAdd = (Button) view.findViewById(R.id.search_buttonAdd);
        buttonAdd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String tmp = autoCompleteTextViewSearch.getText().toString();
                if(tmp.length() > 5) {
                    nomes.add(tmp);
                    updateList();
                    autoCompleteTextViewSearch.setText("");
                }
            }
        });

        Button buttonSearch = (Button) view.findViewById(R.id.search_buttonSearch);
        buttonSearch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Fragment fragment = new SearchResultFragment();
                android.support.v4.app.FragmentTransaction fragmentTrasaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTrasaction.replace(R.id.content_frame, fragment);
                fragmentTrasaction.commit();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        updateList();
        super.onStart();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteractionSearch(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteractionSearch(Uri uri);
    }

    private class ListAdaper extends ArrayAdapter<String> {
        private int layout;
        private List<String> mObjects;
        private ListAdaper(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            mObjects = objects;
            layout = resource;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder mainViewholder = null;
            if(convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.thumbnail = (ImageView) convertView.findViewById(R.id.list_item_search_thumbnail);
                viewHolder.title = (TextView) convertView.findViewById(R.id.list_item_search_text);
                viewHolder.button = (Button) convertView.findViewById(R.id.list_item_search_button);
                convertView.setTag(viewHolder);
            }
            mainViewholder = (ViewHolder) convertView.getTag();
            mainViewholder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nomes.remove(position);
                    updateList();
                }
            });
            mainViewholder.title.setText(getItem(position));

            return convertView;
        }
    }

    public class ViewHolder {
        ImageView thumbnail;
        TextView title;
        Button button;
    }

    private void updateList(){
        /*Cursor cursor = getContentResolver().query(users, null, null, null, null);
        if(cursor!=null){
            if (cursor.moveToFirst()){
                do {
                    u_nomes.add(cursor.getString(cursor.getColumnIndex("nome")));
                }while(cursor.moveToNext());
            }
        }*/

        listView.setAdapter(new ListAdaper(getActivity().getApplicationContext(), R.layout.list_item_search_a1, nomes));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(UsersActivity.this, "CPF:"+u_cpf.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
