package br.com.servprod;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    AutoCompleteTextView autoCompleteTextViewAdd;
    EditText editTextEmail;
    EditText editTextUserId;
    EditText editTextName;
    ListView listView;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private List<String> names = new ArrayList<String>();

    private static final String[] keywords = new String[] {
            "limpesa de estofado", "lavagem de carro", "fotografia", "filmagem", "pedreiro", "diarista", "encanador"
    };

    public UserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_user, container, false);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, keywords);

        autoCompleteTextViewAdd = (AutoCompleteTextView) view.findViewById(R.id.user_autoCompleteTextView);
        autoCompleteTextViewAdd.setAdapter(adapter);
        editTextUserId = (EditText) view.findViewById(R.id.user_editTextUserId);
        editTextName = (EditText) view.findViewById(R.id.user_editTextName);
        editTextEmail = (EditText) view.findViewById(R.id.user_editTextEmail);
        listView = (ListView) view.findViewById(R.id.user_listView);

        Button buttonAdd = (Button) view.findViewById(R.id.user_buttonAdd);
        buttonAdd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String tmp = autoCompleteTextViewAdd.getText().toString();
                if(tmp.length() > 5) {
                    names.add(tmp);
                    updateList();
                    autoCompleteTextViewAdd.setText("");
                }
            }
        });

        Button buttonOk = (Button) view.findViewById(R.id.user_buttonOk);
        buttonOk.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                /*
                List<Object> vect = new Vector<Object>();
                vect.add("Element1");
                vect.add("Element2");
                vect.add("Element3");

                PropertyInfo tabProp =new PropertyInfo();
                tabProp.setName("LIST_PARAM");
                tabProp.setValue(vect);*/
            }
        });

        ContentResolver contentResolver = getActivity().getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Profile.CONTENT_URI, null, null, null, null);
        if(cursor.getCount()>0) {
            cursor.moveToFirst();
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Profile.DISPLAY_NAME));
            editTextUserId.setText(name);
            editTextName.setText(name);
        }
        editTextUserId.setText("joão");
        editTextName.setText("joão");

        String email=getEmail();
        editTextEmail.setText(email);
        return view;
    }

    private String getEmail() {
        try {
            AccountManager accountManager = AccountManager.get(getActivity().getApplicationContext());
            Account[] accounts = accountManager.getAccountsByType("com.google");
            if (accounts.length > 0) {
                Account account = accounts[0];
                return account.name;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
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
                viewHolder.thumbnail = (ImageView) convertView.findViewById(R.id.list_item_user_keywords_thumbnail);
                viewHolder.title = (TextView) convertView.findViewById(R.id.list_item_user_keywords_text);
                viewHolder.button = (Button) convertView.findViewById(R.id.list_item_user_keywords_button);
                convertView.setTag(viewHolder);
            }
            mainViewholder = (ViewHolder) convertView.getTag();
            mainViewholder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    names.remove(position);
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

        listView.setAdapter(new UserFragment.ListAdaper(getActivity().getApplicationContext(), R.layout.list_item_user_keywords, names));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(UsersActivity.this, "CPF:"+u_cpf.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
