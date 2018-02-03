package soulstudios.caloriecounter;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.fitbit.api.services.UserService;
import com.fitbit.authentication.AuthenticationHandler;
import com.fitbit.authentication.AuthenticationManager;
import com.fitbit.authentication.AuthenticationResult;
import com.fitbit.authentication.Scope;

import java.util.Set;

import soulstudios.caloriecounter.R;

public class StartActivity extends AppCompatActivity implements AuthenticationHandler {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    public void onLoginClick(View view){
       AuthenticationManager.login(this);
    }

    public void onLogoutClick(View view){
        AuthenticationManager.logout(this);
    }

    public void onTreadmill(View view){
        Intent intent = new Intent(this,SetupActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        AuthenticationManager.onActivityResult(requestCode, resultCode, data, this);
    }

    @Override
    public void onAuthFinished(AuthenticationResult result) {
        if(result.isSuccessful()){
            Intent intent = new Intent(this,FBMenuActivity.class);
            startActivity(intent);
        }else{
            displayAuthError(result);
        }
    }

//    private class OnTokenAquired implements AccountManagerCallback<Bundle> {
//        @Override
//        public void run(AccountManagerFuture<Bundle> future) {
//            try {
//                Bundle bundle = future.getResult();
//                AppMain.token = bundle.getString(AccountManager.KEY_AUTHTOKEN);
//
//                Intent launch = (Intent) future.getResult().get(AccountManager.KEY_INTENT);
//                if(launch != null){
//                    startActivityForResult(launch,0);
//                }
//            }catch(Exception e){
//                Log.w("Error",e.toString());
//            }
//        }
//    }

    private void displayAuthError(AuthenticationResult authenticationResult) {
        String message = "";

        switch (authenticationResult.getStatus()) {
            case dismissed:
                message = getString(R.string.login_dismissed);
                break;
            case error:
                message = authenticationResult.getErrorMessage();
                break;
            case missing_required_scopes:
                Set<Scope> missingScopes = authenticationResult.getMissingScopes();
                String missingScopesText = TextUtils.join(", ", missingScopes);
                message = getString(R.string.missing_scopes_error) + missingScopesText;
                break;
        }

        new AlertDialog.Builder(this)
                .setTitle(R.string.login_title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .create()
                .show();
    }
}
