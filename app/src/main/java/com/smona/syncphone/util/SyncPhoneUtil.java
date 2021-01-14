package com.smona.syncphone.util;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.util.Log;

import com.smona.syncphone.bean.PhoneBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SyncPhoneUtil {

    public static <T> void runOnWork(Context context, AsyncListener listener, List<PhoneBean> dataList) {
        Observable.just(dataList).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .map(key -> {
                    try {
                        Thread.sleep(1000);
                        return processPhoneList(context, dataList);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                }).subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Boolean t) {
                listener.onResult(t);
            }

            @Override
            public void onError(Throwable e) {
                listener.onResult(false);
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private static boolean processPhoneList(Context context, List<PhoneBean> list) throws RemoteException, OperationApplicationException {
        Set<String> listContact = queryAllPhone(context);
        if (listContact != null) {
            for (PhoneBean phoneBean : list) {
                if (listContact.contains(phoneBean.getName() + "_" + phoneBean.getTel())) {
                    Log.e("motianhu", "phoneBean: " + phoneBean);
                }
            }
        }
        batchInsertContacts(context, list);
        listContact = queryAllPhone(context);
        if (listContact == null) {
            return false;
        }
        List<PhoneBean> retainPhones = new ArrayList<>();
        for (PhoneBean phoneBean : list) {
            if (listContact.contains(phoneBean.getName() + "_" + phoneBean.getTel())) {
                continue;
            }
            Log.e("motianhu", "phoneBean: " + phoneBean);
            retainPhones.add(phoneBean);
        }
        Log.e("motianhu", "retainPhones: " + retainPhones + ", list: " + list.size() + ", listContact: " + listContact.size());
        if (retainPhones.size() > 0) {
            batchInsertContacts(context, retainPhones);
        }
        return true;
    }

    public static Set<String> queryAllPhone(Context context) {
        Uri uri = Uri.parse("content://com.android.contacts/contacts"); //访问raw_contacts表
        ContentResolver resolver = context.getContentResolver();
        //获得_id属性
        Cursor cursor = resolver.query(uri, new String[]{ContactsContract.Data._ID}, null, null, null);
        if (cursor == null) {
            return null;
        }
        Set<String> set = new HashSet<>();
        try {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                uri = Uri.parse("content://com.android.contacts/contacts/" + id + "/data");
                Cursor cursor2 = resolver.query(uri, new String[]{ContactsContract.Data.DATA1, ContactsContract.Data.MIMETYPE}, null, null, null);
                String name = "";
                String phone = "";
                while (cursor2.moveToNext()) {
                    String data = cursor2.getString(cursor2.getColumnIndex("data1"));
                    if (cursor2.getString(cursor2.getColumnIndex("mimetype")).equals("vnd.android.cursor.item/name")) {       //如果是名字
                        name = data;
                    } else if (cursor2.getString(cursor2.getColumnIndex("mimetype")).equals("vnd.android.cursor.item/phone_v2")) {  //如果是电话

                        phone = data;
                    }
                }
                cursor2.close();
                set.add(name + "_" + phone);
            }
        } finally {
            cursor.close();
        }
        return set;
    }

    private static boolean batchInsertContacts(Context context, List<PhoneBean> list) throws
            RemoteException, OperationApplicationException {
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();
        int rawContactInsertIndex;
        for (PhoneBean contact : list) {
            rawContactInsertIndex = ops.size(); // 有了它才能给真正的实现批量添加

            ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                    .withYieldAllowed(true).build());

            // 添加姓名
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, contact.getName())
                    .withYieldAllowed(true).build());
            // 添加号码
            ops.add(ContentProviderOperation
                    .newInsert(android.provider.ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, contact.getTel())
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.LABEL, "")
                    .withYieldAllowed(true).build());
        }
        ContentProviderResult[] contentProviderResults = context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
        return true;
    }

    private static boolean batchInsertContacts1(Context context, List<PhoneBean> list) {
        ContentValues[] dataList = new ContentValues[list.size() * 2];
        for (int i = 0; i < list.size(); i++) {
            ContentValues nameValue = new ContentValues();
            //向RawContacts.CONTENT_URI执行一个空值插入
            //目的是获取系统返回的parseId
            Uri uri = context.getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, nameValue);
            long parseId = ContentUris.parseId(uri);
            nameValue.clear();

            //联系人绑定parseId
            nameValue.put(ContactsContract.Data.RAW_CONTACT_ID, parseId);
            //设置内容类型
            nameValue.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
            //设置联系人名字
            nameValue.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, list.get(i).getName());
            //向联系人Uri添加联系人名字
            dataList[2 * i] = nameValue;

            ContentValues phoneValue = new ContentValues();
            phoneValue.put(ContactsContract.Data.RAW_CONTACT_ID, parseId);
            phoneValue.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
            //设置联系人的电话号码
            phoneValue.put(ContactsContract.CommonDataKinds.Phone.NUMBER, list.get(i).getTel());
            //设置电话类型为手机
            phoneValue.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
            dataList[2 * i + 1] = phoneValue;
        }
        int size = context.getContentResolver().bulkInsert(ContactsContract.Data.CONTENT_URI, dataList);
        Log.e("motianhu", "level " + list.size() + ", result: " + size);
        return true;
    }

}
