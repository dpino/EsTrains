package com.example.helloandroid;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class HelloAndroid extends ListActivity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		String[] elements = { "amir", "servando", "diego" };

		ArrayAdapter<?> adapter = new ArrayAdapter(this,
				android.R.layout.simple_list_item_1, elements);

		setListAdapter(adapter);

		// TextView tv = new TextView(this);
		// tv.setText("Hello, Android");
		// setContentView(tv);
	}
}