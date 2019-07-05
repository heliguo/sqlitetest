package com.example.sqlitetest;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sqlitetest.model.Const;
import com.example.sqlitetest.utils.SPUtils;
import com.example.sqlitetest.view.ServerSettingDialog;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private EditText nameEditText, passwordEditText;
    private CheckBox   saveCheck;
    private Button     loginBtn;
    private TextView   setting;
    private String     config;
    private JSONObject jo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setting = (TextView) findViewById(R.id.login_setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertEdit();
            }
        });
        nameEditText = (EditText) findViewById(R.id.name);
        passwordEditText = (EditText) findViewById(R.id.password);
        saveCheck = (CheckBox) findViewById(R.id.save_password);
        loginBtn = (Button) findViewById(R.id.log_in);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginActivity();
            }
        });
        String password = SPUtils.getString(LoginActivity.this, Const.SP_SERVER_PASSWORD, null);
        String name = SPUtils.getString(LoginActivity.this, Const.SP_SERVER_NAME, null);
        boolean is = SPUtils.getBoolean(LoginActivity.this, Const.SP_SERVER_ISCHECKED, false);
        if (is && name != null && password != null) {
            nameEditText.setText(name);
            passwordEditText.setText(password);
            saveCheck.setChecked(true);
        }
    }

    private void alertEdit() {
        final ServerSettingDialog myDialog = new ServerSettingDialog(this);

        String a = SPUtils.getString(this, Const.SP_SERVER_CONFIG, Const.DEFAULT_SERVER_HOST);
        myDialog.setMessage(a);
        myDialog.setTitle("服务器地址");
        myDialog.setCancelable(false);
        myDialog.setConfirmOnClickListener("保存", new ServerSettingDialog.ConfirmOnClickListener() {
            @Override
            public void onConfirmClick() {
                if (myDialog.getMsgStr() == null || myDialog.getMsgStr().equals("")) {
                    myDialog.setMessage(Const.DEFAULT_SERVER_HOST);
                    return;
                } else {
                    config = myDialog.getMsgStr();
                    SPUtils.setString(LoginActivity.this, Const.SP_SERVER_CONFIG, config);

                    myDialog.dismiss();
                }

            }
        });
        myDialog.setCancelOnClickListener("取消", new ServerSettingDialog.CancelOnClickListener() {
            @Override
            public void onCancelClick() {
                config = SPUtils.getString(LoginActivity.this, Const.SP_SERVER_CONFIG, Const.DEFAULT_SERVER_HOST);
                myDialog.dismiss();
            }
        });
        myDialog.show();
    }


    private void loginActivity() {
        if (nameEditText.getText().toString().trim().equals("")) {
            Toast.makeText(LoginActivity.this, "账号不能为空", Toast.LENGTH_LONG).show();
        } else {
            if (passwordEditText.getText().toString().trim().equals("")) {
                Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_LONG).show();
                return;
            }
            final String userName = nameEditText.getText().toString().trim();
            final String pasword = passwordEditText.getText().toString().trim();
            if (saveCheck.isChecked()) {
                SPUtils.setString(LoginActivity.this, Const.SP_SERVER_NAME, userName);
                SPUtils.setString(LoginActivity.this, Const.SP_SERVER_PASSWORD, pasword);
                SPUtils.setBoolean(LoginActivity.this,Const.SP_SERVER_ISCHECKED,saveCheck.isChecked());
            }

            final String password = lock(pasword);
            ThreadPoolUtil.execute(new Runnable() {
                @Override
                public void run() {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("user", userName);
                    map.put("password", password);
                    try {
                        jo = checkLogin(map);
                        final String obj = jo.getString("obj");
                        final String status = jo.getString("success");
                        if (!status.equals("true")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoginActivity.this, obj, Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            /**存储登录信息*/
                            JSONObject loginResult = jo.optJSONArray("rows").getJSONObject(0);
                            UserInfo userInfo = new UserInfo();
                            userInfo.setUserCode(loginResult.optString("USERCODE"));
                            userInfo.setUserName(loginResult.optString("USERNAME"));
                            userInfo.setUserId(loginResult.optString("USERID"));
                            userInfo.setDeptCode(loginResult.optString("DEPTCODE"));
                            userInfo.setDeptId(loginResult.optString("DEPTID"));
                            userInfo.setDeptName(loginResult.optString("DEPTNAME"));
                            HashMap<String, String> cardData = new HashMap<>();
                            JSONArray jsonArray = jo.optJSONArray("rows");
                            if (jsonArray == null) {
                                return;
                            }
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Iterator<String> it = jsonObject.keys();
                                while (it.hasNext()) {
                                    String key = it.next();
                                    String value = jsonObject.getString(key);
                                    cardData.put(key, value);
                                }
                            }
                            Log.d("123456789", "run:  " + jsonArray.length());
                            String username = cardData.get("USERNAME");
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("name", username);
                            startActivity(intent);
                            finish();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    }


    private String lock(String password) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("md5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] bs = md5.digest(password.getBytes());
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < bs.length; i++) {
            int val = ((int) bs[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        password = hexValue.toString();

        return password;
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    public JSONObject checkLogin(HashMap<String, String> params) throws IOException, JSONException {

        config = "192.168.27.11:8084";

        String downloadurl = "http://" + config + "/android/androidAction!login.action";
        String result;
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(downloadurl);
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
        HttpParams para = httpclient.getParams();
        HttpConnectionParams.setConnectionTimeout(para, 6000);
        HttpConnectionParams.setSoTimeout(para, 6000);
        HttpResponse response;
        response = httpclient.execute(httppost);
        int statusCode = response.getStatusLine().getStatusCode();
        JSONObject jo = null;
        if (statusCode == 200) {
            result = retrieveInputStream(response.getEntity());
            if (result == null) {
                return null;
            }
            jo = new JSONObject(result);
        }
        return jo;
    }

    public String retrieveInputStream(HttpEntity httpEntity) throws UnsupportedEncodingException, IllegalStateException, IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(
                httpEntity.getContent(), HTTP.UTF_8);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public JSONObject check(HashMap<String, String> params) throws IOException, JSONException {



        return null;
    }


}
