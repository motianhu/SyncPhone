package com.smona.syncphone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.smona.http.business.BaseResponse;
import com.smona.http.wrapper.OnResultListener;
import com.smona.syncphone.bean.PhoneBean;
import com.smona.syncphone.bean.PhoneList;
import com.smona.syncphone.bean.ReqPhone;
import com.smona.syncphone.model.PhoneModel;
import com.smona.syncphone.util.AsyncListener;
import com.smona.syncphone.util.SyncPhoneUtil;

import java.util.ArrayList;
import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends AppCompatActivity {

    private EditText input_account;
    private EditText input_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.query_phone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(input_account.getText().toString())) {
                    showToast("请输入账号密码");
                    return;
                }
                if (TextUtils.isEmpty(input_password.getText().toString())) {
                    showToast("请输入账号密码");
                    return;
                }

                //insertContacts(false, 1, "","",getLocalPhone());
                requestPhoneList(1, input_account.getText().toString(), input_password.getText().toString());
            }
        });
        input_account = findViewById(R.id.input_account);
        input_password = findViewById(R.id.input_password);
        //http://tools.zsliangxiangshe.com/openapi/pull-verify-data
    }

    private List<PhoneBean> getLocalPhone() {
        List<PhoneBean> dataList = new ArrayList<>();
        dataList.add(createPhone("Moth", "13111111111"));
        dataList.add(createPhone("Moth", "13111111112"));
        dataList.add(createPhone("Moth1", "13211111111"));
        dataList.add(createPhone("Moth2", "13211111111"));
        dataList.add(createPhone("Moth3", "13211111113"));
        dataList.add(createPhone("Moth3", "13211111113"));
        dataList.add(createPhone("Moth4", "132111"));
        dataList.add(createPhone("Moth5", "abcd"));
        dataList.add(createPhone("", "131112233"));
        dataList.add(createPhone("Moth6", ""));
        return dataList;
    }

    private PhoneBean createPhone(String name, String phone) {
        PhoneBean phoneBean = new PhoneBean();
        phoneBean.setName(name);
        phoneBean.setTel(phone);
        return phoneBean;
    }

    private void requestPhoneList(int page, String account, String password) {
        PhoneModel phoneModel = new PhoneModel();
        ReqPhone reqPhone = new ReqPhone();
        reqPhone.setPage(page);
        reqPhone.setPage_size(500);
        reqPhone.setUsername(account);
        reqPhone.setPassword(password);
        phoneModel.requestPhoneList(reqPhone, new OnResultListener<BaseResponse<PhoneList>>() {
            @Override
            public void onSuccess(BaseResponse<PhoneList> phoneListBaseResponse) {
                if (isFinish()) {
                    return;
                }
                List<PhoneBean> dataList = phoneListBaseResponse.data.getItems();
                if (dataList == null || dataList.size() == 0) {
                    return;
                }
                insertContacts(phoneListBaseResponse.data.isHasNext(), page, account, password, dataList);
            }

            @Override
            public void onError(String stateCode, String errorInfo) {
                if (isFinish()) {
                    return;
                }
                showToast(errorInfo);
            }
        });
    }

    private void insertContacts(boolean hasNext, int page, String account, String password, List<PhoneBean> dataList) {
        SyncPhoneUtil.runOnWork(MainActivity.this, new AsyncListener() {
            @Override
            public void onResult(boolean success) {
                if (success) {
                    if(hasNext) {
                        requestPhoneList(page + 1, account, password);
                    } else {
                        showToast("数据插入完成");
                    }
                } else {
                    insertContacts(hasNext, page, account, password, dataList);
                }
            }
        }, dataList);
    }

    private void showToast(String errorInfo) {
        Toast.makeText(MainActivity.this, errorInfo, Toast.LENGTH_SHORT).show();
    }

    private boolean isFinish() {
        return isFinishing() || isDestroyed();
    }

    private void addContact() {
        if (hasDeniedPermission(this, getPermissions())) {
            ActivityCompat.requestPermissions(this, getPermissions(), 10);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    private String[] getPermissions() {
        return new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS};
    }

    private static boolean hasDeniedPermission(Context context, String... permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED) {
                return true;
            }
        }
        return false;
    }

    @NeedsPermission({Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS})
    public void addContact1() {
    }
}