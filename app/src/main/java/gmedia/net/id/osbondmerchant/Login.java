package gmedia.net.id.osbondmerchant;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {
    private Button login;
    private EditText username, password;
    private SessionManager session;
    private Proses proses;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        session = new SessionManager(getApplicationContext());
        session.checkLogin();
        proses = new Proses(Login.this);
        login = findViewById(R.id.btnLogin);
        username = (EditText) findViewById(R.id.form_username);
        password = (EditText) findViewById(R.id.form_password);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nama = username.getText().toString();
                String pass = password.getText().toString();
                if (nama.trim().length() > 0 && pass.trim().length() > 0) {
                    prepareDataLogin();
                } else {
                    Toast.makeText(getApplicationContext(), "Silahkan isi username & password anda", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void prepareDataLogin() {
        proses.ShowDialog();
        final JSONObject jBody = new JSONObject();
        try {
            jBody.put("username", username.getText().toString());
            jBody.put("password", password.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ApiVolley request = new ApiVolley(Login.this, jBody, "POST", URL.Login, "", "", 0, new ApiVolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                proses.DismissDialog();
                try {
                    JSONObject object = new JSONObject(result);
                    String status = object.getJSONObject("metadata").getString("status");
                    String message = object.getJSONObject("metadata").getString("message");
                    String isimessage = "" + message.contains("Welcome");
                    if (status.equals("200")) {
                        if (isimessage.equals("true")) {
                            JSONObject response = object.getJSONObject("response");
                            String token = response.getString("token");
                            String uid = response.getString("uid");
                            String cabang = response.getString("cabang");
                            session.createLoginSession(token, uid, cabang);
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String result) {
                proses.DismissDialog();
                Toast.makeText(getApplicationContext(), "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Klik sekali lagi untuk keluar", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
/*private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;*/
/*mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {

                Intent intent = new Intent(Login.this,MainActivity.class);
                startActivity(intent);
                *//*
 * The following method, "handleShakeEvent(count):" is a stub //
 * method you would use to setup whatever you want done once the
 * device has been shook.
 *//*
//                handleShakeEvent(count);
            }
        });*/
        /*logo = (ImageView)findViewById(R.id.imgLogoOsbond);
        Drawable logoOsbond = getResources().getDrawable(R.drawable.logo_osbond);
        int widthOri = logoOsbond.getIntrinsicWidth();
        int heightOri = logoOsbond.getIntrinsicHeight();
        int customWidth = widthOri+((50*widthOri)/100);
        int customHeight = heightOri+((50*heightOri)/100);
        Picasso.with(Login.this).load(R.drawable.logo_osbond).resize(customWidth,customHeight).into(logo);*/
        /*@Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }*/