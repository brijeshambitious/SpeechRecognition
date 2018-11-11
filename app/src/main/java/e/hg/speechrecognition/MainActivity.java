package e.hg.speechrecognition;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final int SPEAK_REQUEST = 1;

    private static final int RECORD_REQUEST_CODE = 101;
    private static final int STORAGE_REQUEST_CODE = 102;

    private TextView text;
    private ImageButton speakbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        text = findViewById(R.id.txt);
        speakbutton = findViewById(R.id.speakbutton);


        speakbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUserPermsions();




            }


        });

   }

   private void checkUserPermsions(){
       if ( Build.VERSION.SDK_INT >= 23){
           if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) !=
                   PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                   PackageManager.PERMISSION_GRANTED ){
               requestPermissions(new String[]{
                               android.Manifest.permission.RECORD_AUDIO,android.Manifest.permission.READ_EXTERNAL_STORAGE}, RECORD_REQUEST_CODE);
               return ;
           }
       }

       listToUserVoice();

   }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RECORD_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    listToUserVoice();
                } else {
                    // Permission Denied
                    setupPermissions();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    private void setupPermissions() {
        int permission = ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i("PermissionDemo", "Permission to record denied");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(this);
                builder.setMessage("Permission to access the microphone is required for this app to record audio.")
                        .setTitle("Permission required");
                builder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //Log.i(TAG, "Clicked");
                                checkUserPermsions();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                Toast.makeText(getApplicationContext(),"Allow Access to Permission in Settings",Toast.LENGTH_LONG).show();
               Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getApplicationContext().getPackageName(), null);
                intent.setData(uri);
                MainActivity.this.startActivity(intent);

            }

            }
        }

//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//
//
//        switch (requestCode) {
//            case RECORD_REQUEST_CODE:
//                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    listToUserVoice();
//
//
//                } else {
//
//                    }
//                break;
//
//                default:
//                    super.onRequestPermissionsResult(requestCode,permissions,grantResults);
//            }
//        }
//
//
//    private void makeRequest() {
//        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO,},
//                RECORD_REQUEST_CODE);
//
//
//    }



    private void listToUserVoice() {
        Intent voiceIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        voiceIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Voice Search");
        voiceIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        voiceIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 10);

        startActivityForResult(voiceIntent, SPEAK_REQUEST);
    }


//   public void speakButtonClicked(View v)
//    {
//       listToUserVoice();
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SPEAK_REQUEST && resultCode == RESULT_OK)

        {
            ArrayList<String> voiceWord = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            if (!voiceWord.isEmpty())
            {
                String Query = voiceWord.get(0);
                text.setText(Query);

            }

//            for (String userword : voiceWord)
//
//            {
//                text.setText(userword);
//
//            }
        }
    }

}