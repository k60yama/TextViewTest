package com.android.textview;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MainActivity extends Activity implements OnClickListener{

	public CreateItemHelper helper;
	public SQLiteDatabase db;
	public Spinner itemType;
	public EditText itemName;
	public TextView noLabel,no;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ((Button)findViewById(R.id.addButton)).setOnClickListener(this);
        helper = new CreateItemHelper(this);
        
        /*
        //Spinner表示テスト
        Spinner test = (Spinner)findViewById(R.id.itemType);
        ArrayAdapter<String> adapter =(ArrayAdapter<String>)test.getAdapter();
        test.setSelection(adapter.getPosition("その他"));
        */
    }
    
    @Override
    public void onClick(View view){
    	
    	itemType = (Spinner)findViewById(R.id.itemType);
    	itemName = (EditText)findViewById(R.id.itemName);
    	
    	//追加処理(データベースのテーブルにinsert処理)
    	db = helper.getWritableDatabase();
    	db.beginTransaction();
    	
    	ContentValues val = new ContentValues();
    	val.put("itemType", itemType.getSelectedItem().toString());
    	val.put("itemName", itemName.getText().toString());
    	
    	db.insert("item", null, val);
    	db.setTransactionSuccessful();
    	db.endTransaction();
    	
    	
    	TableLayout tablelayout = (TableLayout)findViewById(R.id.itemInfo);
    	tablelayout.removeAllViews();
    	
    	//表示処理(データベースのテーブル情報を取得)
    	db = helper.getReadableDatabase();
    	String columns[] = {"_id", "itemType", "itemName"};
    	Cursor cursor = db.query("item", columns, null, null, null, null, "itemType");
    	
    	tablelayout.setStretchAllColumns(true);
    	TableRow headrow = new TableRow(this);
    	
    	TextView headtxt0 = new TextView(this);
    	headtxt0.setText("登録No");
    	headtxt0.setGravity(Gravity.CENTER_HORIZONTAL);
    	headtxt0.setTextSize(15);
    	headtxt0.setWidth(40);
    	headrow.addView(headtxt0);
    	
    	TextView headtxt1 = new TextView(this);
    	headtxt1.setText("種別");
    	headtxt1.setGravity(Gravity.CENTER_HORIZONTAL);
    	headtxt1.setTextSize(15);
    	headtxt1.setWidth(80);
    	headrow.addView(headtxt1);
    	
    	TextView headtxt2 = new TextView(this);
    	headtxt2.setText("品物名");
    	headtxt2.setGravity(Gravity.CENTER_HORIZONTAL);
    	headtxt2.setTextSize(15);
    	headtxt2.setWidth(80);
    	headrow.addView(headtxt2);
    	
    	tablelayout.addView(headrow);
    	
    	while(cursor.moveToNext()){
    		TableRow row = new TableRow(this);
    		
    		TextView noTxt = new TextView(this);
    		noTxt.setGravity(Gravity.CENTER_HORIZONTAL);
    		noTxt.setText(cursor.getString(0));
    		noTxt.setTextSize(15);
    		
    		TextView itemTypeTxt = new TextView(this);
    		itemTypeTxt.setGravity(Gravity.CENTER_HORIZONTAL);
    		itemTypeTxt.setText(cursor.getString(1));
    		itemTypeTxt.setTextSize(15);
    		
    		TextView itemNameTxt = new TextView(this);
    		itemNameTxt.setGravity(Gravity.CENTER_HORIZONTAL);
    		itemNameTxt.setText(cursor.getString(2));
    		itemNameTxt.setTextSize(15);
    		
    		row.addView(noTxt);
    		row.addView(itemTypeTxt);
    		row.addView(itemNameTxt);
    		
    		//TableRow単位でイベントクリックを設定する
    		row.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View view) {
					TableRow tr = (TableRow)view;
					
					/*
					//TextViewに表示されている文字列取得テスト
					for(int i=0; i<tr.getChildCount(); i++){
						TextView tv = (TextView)tr.getChildAt(i);
						Toast.makeText(MainActivity.this, (i+1) + "回目" + tv.getText().toString(), Toast.LENGTH_SHORT).show();
					}
					*/
					
					//登録No設定
					noLabel = (TextView)findViewById(R.id.noLabel);
					noLabel.setVisibility(View.VISIBLE);
					
					no = (TextView)findViewById(R.id.no);
					String noTxt = ((TextView)tr.getChildAt(0)).getText().toString();
					no.setText(noTxt);
					no.setVisibility(View.VISIBLE);
					
					//種別設定
					ArrayAdapter<String> adapter = (ArrayAdapter<String>)itemType.getAdapter();
					String type = ((TextView)tr.getChildAt(1)).getText().toString();
					itemType.setSelection(adapter.getPosition(type));
					
					//名前設定
					String name = ((TextView)tr.getChildAt(2)).getText().toString();
					itemName.setText(name);
				}
    		});
    		tablelayout.addView(row);
    	}
    }
}
