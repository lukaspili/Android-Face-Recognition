package com.javalab.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.javalab.models.Contact;

public class ContactEditActivity extends Activity {

	private static String TAG = "Contact Edit Activity";

	private EditText editContactFirstName;
	private EditText editContactLastName;
	private Button buttonCancel;
	private Button buttonSave;

	private Contact contact;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_edit_activity);
		Log.i(TAG, "Activity created");

		contact = (Contact) savedInstanceState.getSerializable("contact");

		getViews();

		initView();
	}

	private void getViews() {
		editContactFirstName = (EditText) findViewById(R.id.edit_contact_first_name);
		editContactLastName = (EditText) findViewById(R.id.edit_contact_last_name);
		buttonCancel = (Button) findViewById(R.id.button_contact_edit_cancel);
		buttonSave = (Button) findViewById(R.id.button_contact_edit_save);
	}

	private void initView() {

		// button cancel click listener
		buttonCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Log.i(TAG, "Button cancel clicked, return to the contact detail activity without applying any changes");

				setResult(RESULT_CANCELED);
				finish();
			}
		});

		// button save click listener
		buttonSave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Log.i(TAG,
						"Button save clicked, save any changes made to the contact and return to the contact detail activity");

				getIntent().putExtra("contact", contact);
				setResult(RESULT_OK, getIntent());
				finish();
			}
		});

		// update edit texts with contact
		editContactFirstName.setText(contact.getFirstName());
		editContactLastName.setText(contact.getLastName());
	}
}
