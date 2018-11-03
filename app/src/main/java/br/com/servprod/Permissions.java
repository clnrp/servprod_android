package br.com.servprod;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by cleoner on 12/06/17.
 */

public class Permissions extends Activity {
    private static final int REQUEST_PERMISSIONS_CODE = 128;
    private String[] permissions = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.CAMERA,
            Manifest.permission.INTERNET,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.SYSTEM_ALERT_WINDOW,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.READ_PHONE_STATE
    };
    private MaterialDialog materialDialog;
    private Activity activity;

    public Permissions(Activity activity) {
        this.activity=activity;
        showRequest();
    }

    private void showRequest(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION) ||
                ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.RECORD_AUDIO)) {
            callDialog("Para logar é necessário permissões!", this.permissions);
        } else {
            ActivityCompat.requestPermissions(activity, this.permissions, REQUEST_PERMISSIONS_CODE);
        }
        //ActivityCompat.requestPermissions(activity, this.permissions, REQUEST_PERMISSIONS_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch( requestCode ){
            case REQUEST_PERMISSIONS_CODE:
                for( int i = 0; i < permissions.length; i++ ){
                    if( permissions[i].equalsIgnoreCase( Manifest.permission.ACCESS_FINE_LOCATION )
                            && grantResults[i] == PackageManager.PERMISSION_GRANTED ){
                    }else if( permissions[i].equalsIgnoreCase( Manifest.permission.WRITE_EXTERNAL_STORAGE )
                            && grantResults[i] == PackageManager.PERMISSION_GRANTED ){
                    }else if( permissions[i].equalsIgnoreCase( Manifest.permission.READ_EXTERNAL_STORAGE )
                            && grantResults[i] == PackageManager.PERMISSION_GRANTED ){
                    }
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void callDialog(String message, final String[] permissions ){
        materialDialog = new MaterialDialog(activity)
                .setTitle("Permissões")
                .setMessage( message )
                .setPositiveButton("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityCompat.requestPermissions(activity, permissions, REQUEST_PERMISSIONS_CODE);
                        materialDialog.dismiss();
                    }
                });
                /*.setNegativeButton("Cancelar", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        materialDialog.dismiss();
                    }
                });*/
        materialDialog.show();
    }
}
