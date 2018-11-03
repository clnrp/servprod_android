package br.com.servprod;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
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
import android.widget.Toast;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class UserActivity extends AppCompatActivity {

    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };

    AutoCompleteTextView autoCompleteTextViewAdd;
    EditText editTextEmail;
    EditText editTextUserId;
    EditText editTextName;
    ListView listView;
    ImageView imageView;
    CropImageView cropImageView;

    private Bitmap bitmap = null;
    private File file;
    private Uri imageToUploadUri;
    private View mProgressView;
    private View mLoginFormView;
    private UserInfoTask mAuthTask = null;

    private String user_name, first_name, email, password;
    private Vector list = new Vector();

    private final String NAMESPACE = "http://192.168.25.9/servprod/";
    private final String URL = "http://192.168.25.9/servprod/service1.php";
    private final String METHOD_NAME = "add_user";
    private final String SOAP_ACTION = "http://192.168.25.9/servprod/service1.php#"+METHOD_NAME;

    private final int REQUEST_CAMERA = 0;
    private final int REQUEST_LIBRARY = 1;

    private Location location = null;

    private static final String[] keywords = new String[] {
            "limpesa de estofado", "lavagem de carro", "fotografia", "filmagem", "pedreiro", "diarista", "encanador"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, keywords);

        autoCompleteTextViewAdd = (AutoCompleteTextView) findViewById(R.id.user_a_autoCompleteTextView);
        autoCompleteTextViewAdd.setAdapter(adapter);
        editTextUserId = (EditText) findViewById(R.id.user_a_editTextUserId);
        editTextName = (EditText) findViewById(R.id.user_a_editTextName);
        editTextEmail = (EditText) findViewById(R.id.user_a_editTextEmail);
        listView = (ListView) findViewById(R.id.user_a_listView);
        imageView  = (ImageView) findViewById(R.id.user_a_imageView);
        //cropImageView = (CropImageView) findViewById(R.id.user_a_cropImageView);

        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Profile.CONTENT_URI, null, null, null, null);
        if(cursor.getCount()>0) {
            cursor.moveToFirst();
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Profile.DISPLAY_NAME));
            editTextUserId.setText(name);
            editTextName.setText(name);
        }
        editTextUserId.setText("joão");
        editTextName.setText("joão");

        email=getEmail();
        editTextEmail.setText(email);

        Button buttonAdd = (Button) findViewById(R.id.user_a_buttonAdd);
        buttonAdd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String tmp = autoCompleteTextViewAdd.getText().toString();
                if(tmp.length() > 5) {
                    list.add(tmp);
                    updateList();
                    autoCompleteTextViewAdd.setText("");
                }
            }
        });

        Button buttonOk = (Button) findViewById(R.id.user_a_buttonOk);
        buttonOk.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(UserActivity.this, "sendfile..", Toast.LENGTH_SHORT).show();
                mAuthTask = new UserInfoTask(editTextUserId.getText().toString(),editTextName.getText().toString(),editTextEmail.getText().toString(), "123", list);
                mAuthTask.execute((Void) null);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                /*Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                File f = new File(Environment.getExternalStorageDirectory(), "image_test.jpg");
                takePicture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                imageToUploadUri = Uri.fromFile(f);
                startActivityForResult(takePicture, REQUEST_CAMERA);*/

                //CropImage.startPickImageActivity(UserActivity.this);
                CropImage.activity(null)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(UserActivity.this);

                /*Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);

                intent.setType("image/*");
                intent.putExtra("crop", "true");
                intent.putExtra("scale", true);
                intent.putExtra("outputX", 500);
                intent.putExtra("outputY", 500);
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("return-data", true);

                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 1);*/

                //SelectImage();
            }
        });

        GPSReader();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(bitmap != null){
            imageView.setImageBitmap(bitmap);
        }

    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserInfoTask extends AsyncTask<Void, Void, Boolean> {

        private String user_name;
        private String first_name;
        private String email;
        private String password;
        private Vector list;

        UserInfoTask(String user_name, String first_name, String email, String password, Vector list) {
            this.user_name = user_name;
            this.first_name = first_name;
            this.email = email;
            this.password = password;
            this.list = list;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("user", user_name);
                request.addProperty("first_name", first_name);
                request.addProperty("email", email);
                request.addProperty("password", password);
                request.addProperty("list", list);

                if(bitmap != null) {
                    //byte[] data=Base64.decode(image);
                    //final Bitmap bitmap = BitmapFactory.decodeFile( file.getAbsolutePath() );
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 60, out);
                    byte[] byte_img = out.toByteArray();
                    String strBase64 = Base64.encodeToString(byte_img, Base64.DEFAULT);
                    request.addProperty("image", strBase64);
                }

                if(location != null){
                    request.addProperty("latitude", location.getLatitude());
                    request.addProperty("longitude", location.getLongitude());
                }
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                //envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);


                try {
                    androidHttpTransport.call(SOAP_ACTION, envelope);
                    Boolean res = (Boolean) envelope.getResponse();

                    if(res == false){
                        return false;
                    }
                    //SoapObject primitive = (SoapObject) envelope.bodyIn;
                    //Object obj = primitive.getProperty(0);
                    //Vector s = ((Vector) obj);
                    //Log.i("ws", s.toString());
                    //return false;
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            //showProgress(false);

            if (success) {
                //Intent intent = new Intent();
                //intent.setClass(getApplicationContext(), MainActivity.class);
                //intent.putExtra("info", true);
                //startActivity(intent);
            } else {
                //mPasswordView.setError(getString(R.string.error_incorrect_password));
                //mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            //showProgress(false);
        }
    }

    private String getEmail() {
        try {
            AccountManager accountManager = AccountManager.get(getApplicationContext());
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
                    list.remove(position);
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

        listView.setAdapter(new ListAdaper(getApplicationContext(), R.layout.list_item_user_keywords, list));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(UsersActivity.this, "CPF:"+u_cpf.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri).start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageView.setImageURI(result.getUri());
                bitmap = getBitmap(result.getUri().getPath());
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }

        /*if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CAMERA) {
            final Bundle extras = data.getExtras();

            if (extras != null) {
                Bitmap photo = extras.getParcelable("data");
                imageView.setImageBitmap(photo);
            }
        }*/


        /*if(requestCode == REQUEST_CAMERA && imageToUploadUri != null){
            Uri selectedImage = imageToUploadUri;
            getContentResolver().notifyChange(selectedImage, null);
            bitmap = getBitmap(imageToUploadUri.getPath());
            Log.i("ServProd", String.valueOf(bitmap.getWidth())+"-"+String.valueOf(bitmap.getHeight()));
            if(bitmap != null){
                imageView.setImageBitmap(bitmap);
            }else{
                Toast.makeText(this,"Error while capturing Image",Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this,"Error while capturing Image",Toast.LENGTH_LONG).show();
        }*/

    }

    private Bitmap getBitmap(String path) {

        Uri uri = Uri.fromFile(new File(path));
        InputStream in = null;
        try {
            final int IMAGE_MAX_SIZE = 1200000; // 1.2MP
            in = getContentResolver().openInputStream(uri);

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, o);
            in.close();


            int scale = 1;
            while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) > IMAGE_MAX_SIZE) {
                scale++;
            }
            Log.d("", "scale = " + scale + ", orig-width: " + o.outWidth + ", orig-height: " + o.outHeight);

            Bitmap b = null;
            in = getContentResolver().openInputStream(uri);
            if (scale > 1) {
                scale--;
                // scale to max possible inSampleSize that still yields an image
                // larger than target
                o = new BitmapFactory.Options();
                o.inSampleSize = scale;
                b = BitmapFactory.decodeStream(in, null, o);

                // resize to desired dimensions
                int height = b.getHeight();
                int width = b.getWidth();
                Log.d("", "1th scale operation dimenions - width: " + width + ", height: " + height);

                double y = Math.sqrt(IMAGE_MAX_SIZE
                        / (((double) width) / height));
                double x = (y / height) * width;

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x,
                        (int) y, true);
                b.recycle();
                b = scaledBitmap;

                System.gc();
            } else {
                b = BitmapFactory.decodeStream(in);
            }
            in.close();

            Log.d("", "bitmap size - width: " + b.getWidth() + ", height: " + b.getHeight());
            return b;
        } catch (IOException e) {
            Log.e("", e.getMessage(), e);
            return null;
        }
    }

    private void SelectImage() {
        final CharSequence[] items = { "Câmera", "Biblioteca",
                "Cancelar" };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                //boolean result=permissions.checkPermission(this);
                if (items[item].equals("Câmera")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Biblioteca")) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select File"), REQUEST_LIBRARY);
                } else if (items[item].equals("Cancelar")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    public void GPSReader() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT > 23) {
            } else {
                return;
            }
        }
        LocationManager lManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener lListener = new LocationListener() {
            public void onLocationChanged(Location loc) {
                if(loc.getAccuracy() > 0) {
                    location = loc;
                }
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        };
        lManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, lListener);

        boolean isOn = lManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!isOn) {
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
    }
}
