package com.example.repens.memo_kaimono;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
//リストビューアダプタ変更で表示行調整
    //List View用アダプタ
    SimpleAdapter mAdapter = null;
    //List View に設定するデータ
    List<Map<String, String>> mList = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //ここから理解必須

        //アダプタ用リストを作成
        mList = new ArrayList<Map<String, String>>();
        //アダプタ作成
        mAdapter = new SimpleAdapter(
                this,
                mList,
                android.R.layout.simple_list_item_2,
                new String[]{"title", "content"},
                new int[]{android.R.id.text1, android.R.id.text2}
        );
        ListView list = (ListView) findViewById(R.id.listView);
        list.setAdapter(mAdapter);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(
                    AdapterView<?> parent, View view, int pos, long id) {
                //編集画面にデータを渡す準備(セット、表示)
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra("NAME", mList.get(pos).get("filename"));
                intent.putExtra("TITLE", mList.get(pos).get("title"));
                intent.putExtra("CONTENT", mList.get(pos).get("content"));
                startActivity(intent);

            }
        });
        //コンテキストメニューに登録
        registerForContextMenu(list);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //リストデータクリア
        mList.clear();

        //ファイル一覧取得
        String savePath = this.getFilesDir().getPath().toString();
        File[] files = new File(savePath).listFiles();
        //降順ソート
        Arrays.sort(files, Collections.reverseOrder());
        //listviewアダプタにセット
        for (int i = 0; i < files.length; i++) {
            String fileName = files[i].getName();
            if (files[i].isFile() && fileName.endsWith(".txt")){
            String title = null;
            String content = null;

                //読み込み
            try {
                //オープン
                InputStream in = this.openFileInput(fileName);
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                //タイトル読み込み
                title = reader.readLine();
                //内容読み込み
                char[] buf = new char[(int) files[i].length()];
                int num = reader.read(buf);
                content = new String(buf, 0, num);
                //クローズ
                reader.close();
                in.close();
            } catch (Exception e) {
                Toast.makeText(this, "内容が記載されていないメモがあります", Toast.LENGTH_LONG).show();
            }
            //アダプターにセット
            Map<String, String> map = new HashMap<String, String>();
            map.put("filename", fileName);
            map.put("title", title);
            map.put("content", content);
            mList.add(map);
        }
    }
    //変更反映
    mAdapter.notifyDataSetChanged();
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
            Intent intent = new Intent(MainActivity.this ,EditActivity .class);
                startActivity(intent);
                mAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }
}
