package com.example.android.inventorytracker;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventorytracker.Data.InventoryContract;
import com.example.android.inventorytracker.Data.InventoryDbHelper;

public class InventoryActivity extends AppCompatActivity {

    //Adapter for the list of items
    InventoryCursorAdapter cursorAdapter;

    //Cursor that iterates through table
    private Cursor cursor;

    //Dbhelper
    private InventoryDbHelper mDbhelper;

    //ListView for the list of items
    private ListView itemListView;

    //Empty TextView if nothing to show
    TextView emptyTextView;

    Button saleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory_list);

        itemListView = (ListView) findViewById(R.id.inventory_list);

        //Set the TextView with id empty to an empty view.
        emptyTextView = (TextView) findViewById(R.id.empty);
        itemListView.setEmptyView(emptyTextView);

        //SALE button displayed in InventoryActivity
        saleButton = (Button) findViewById(R.id.sale_button);

        //Setup FAB to open editorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newItemIntent = new Intent(InventoryActivity.this, NewItemActivity.class);
                startActivity(newItemIntent);
            }
        });

//        saleButton.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                View parentRow = (View) v.getParent();
//                final int position = itemListView.getPositionForView(parentRow);
//                saleUpdate(position);
//            }
//        });

        //Setup onClickListener for when an item is clicked
        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                long viewId = view.getId();
                if (viewId == R.id.sale_button) {
                    Toast.makeText(InventoryActivity.this, "uhhh", Toast.LENGTH_SHORT).show();

                    saleUpdate(position);
                } else {
                    Intent editIntent = new Intent(InventoryActivity.this, EditorActivity.class);
                    editIntent.putExtra("item_id", position);
                    startActivity(editIntent);
                }
            }
        });

        mDbhelper = new InventoryDbHelper(this);
        displayDatabaseInfo();
    }

    private void displayDatabaseInfo() {
        InventoryDbHelper mDbHelper = new InventoryDbHelper(this);

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String[] project = {
            InventoryContract.InventoryEntry._ID,
            InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME,
            InventoryContract.InventoryEntry.COLUMN_PRODUCT_PRICE,
            InventoryContract.InventoryEntry.COLUMN_PRODUCT_QUANTITY
        };

        cursor = db.query(
                InventoryContract.InventoryEntry.TABLE_NAME,
                project,
                null,
                null,
                null,
                null,
                null);

        itemListView = (ListView) findViewById(R.id.inventory_list);

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                cursorAdapter = new InventoryCursorAdapter(
                        InventoryActivity.this,
                        cursor);
                itemListView.setAdapter(cursorAdapter);
            }
        });
    }
    private void saleUpdate(int position) {
        Toast.makeText(InventoryActivity.this, position, Toast.LENGTH_SHORT).show();




    }


}
