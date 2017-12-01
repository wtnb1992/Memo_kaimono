package com.example.repens.memo_kaimono;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
  Created by repens on 2017/06/06.
 */

public class EditActivity extends Activity {
    String mFileName = "";

    boolean mNotSave = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //別アクティビティ使うならここ変更
        setContentView(R.layout.activity_edit);
//        setContentView(R.layout.sample);


        //タイトルと内容の入力用EditTextを取得
        EditText eTxtTitle = (EditText) findViewById(R.id.eTxtTitle);
        EditText eTxtContent = (EditText) findViewById(R.id.eTxtContent);

        //情報受け取り後設定
        Intent intent = getIntent();
        String name = intent.getStringExtra("NAME");
        if (name != null) {
            mFileName = name;
            eTxtTitle.setText(intent.getStringExtra("TITLE"));
            eTxtContent.setText(intent.getStringExtra("CONTENT"));
        }
    }


    public void onPause() {
        super.onPause();

        //削除は保存しない
        if (mNotSave) {
            return;
        }

        // タイトル、内容を取得
        EditText eTxtTitle = (EditText) findViewById(R.id.eTxtTitle);
        EditText eTxtContent = (EditText) findViewById(R.id.eTxtContent);
        String title = eTxtTitle.getText().toString();
        String content = eTxtContent.getText().toString();

        // タイトル、内容が空白の場合、保存しない
        if (title.isEmpty() && content.isEmpty()) {
            Toast.makeText(this, R.string.msg_destruction, Toast.LENGTH_SHORT).show();
            deleteFile(mFileName);
            return;
        }

        // ファイル名を生成  ファイル名 ： yyyyMMdd_HHmmssSSS.txt
        // （既に保存されているファイルは、そのままのファイル名とする）
        if (mFileName.isEmpty()) {
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmssSSS", Locale.JAPAN);
            mFileName = sdf.format(date) + ".txt";
        }

        // 保存
        OutputStream out = null;
        PrintWriter writer = null;
        try {
            out = this.openFileOutput(mFileName, Context.MODE_PRIVATE);
            writer = new PrintWriter(new OutputStreamWriter(out, "UTF-8"));
            // タイトル書き込み
            writer.println(title);
            // 内容書き込み
            writer.print(content);
            writer.close();
            out.close();
        } catch (Exception e) {
            Toast.makeText(this, "File save error!", Toast.LENGTH_LONG).show();
        }
    }

    //ボタン押されたら
    public void onClick(View view) {
        switch (view.getId()) {
            //保存
            case R.id.button2:
                finish();
                break;
            //削除
            case R.id.button3:
                new AlertDialog.Builder(this)
                        .setMessage("内容を削除してもよろしいですか？")
                        .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // はいを押すと削除
                                //やりかたわからないからタイトルと内容にnull代入して削除
                                EditText eTxtTitle = (EditText) findViewById(R.id.eTxtTitle);
                                EditText eTxtContent = (EditText) findViewById(R.id.eTxtContent);
                                eTxtTitle.setText(null);
                                eTxtContent.setText(null);
                                finish();
                            }
                        })
                        //いいえ押すと戻る
                        .setNegativeButton("いいえ", null)
                        .show();
                break;
        }
    }
}


/**参考
　　http://androidguide.nomaki.jp/html/memo_app/memo_app_java_edit.html
 */