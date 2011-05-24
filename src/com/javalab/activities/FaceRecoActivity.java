package com.javalab.activities;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.javalab.models.Contact;
import com.javalab.services.CameraService;
import com.javalab.services.ContactService;

public class FaceRecoActivity extends Activity {

	private static final String TAG = "FaceReco Activity";

	private static final int CONTACT_DETAIL_ACTIVITY_ID = 1;

	private Button buttonNewPicture;
	private ListView listContacts;
	private ArrayAdapter<Contact> listContactsAdapter;

	private List<Contact> contacts;

	private ContactService contactService;
	private CameraService cameraService;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.face_reco_activity);
		Log.i(TAG, "Activity created");

		contactService = new ContactService(this);
		cameraService = new CameraService(this);

		contacts = contactService.getContacts();

		getViews();

		initView();
	}

	private void getViews() {
		buttonNewPicture = (Button) findViewById(R.id.button_new_picture);
		listContacts = (ListView) findViewById(R.id.list_contacts);
	}

	private void initView() {

		// button new picture click listener
		buttonNewPicture.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Log.i(TAG, "Button new picture clicked, start the camera");
				startCamera();
			}
		});

		// list contacts adapter
		listContactsAdapter = new ArrayAdapter<Contact>(this, R.layout.list_contacts, R.id.label_list_contacts_element,
				contacts);
		listContacts.setAdapter(listContactsAdapter);

		// list contacts click listener
		listContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View parent, int position, long id) {
				Log.i(TAG, "List contacts element clicked, show the contact detail");

				Contact contact = (Contact) listContacts.getItemAtPosition(position);

				Intent intent = new Intent(FaceRecoActivity.this, ContactDetailActivity.class);
				intent.putExtra("contact", contact);
				intent.putExtra("id", id);

				startActivityForResult(intent, CONTACT_DETAIL_ACTIVITY_ID);
			}

		});
	}

	private void startCamera() {

		// cameraService.startCamera();
		cameraService.startCameraAsLocal(R.drawable.angelina1);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {

		// result from contact detail activity
		case CONTACT_DETAIL_ACTIVITY_ID:

			// modifications done
			if (requestCode == RESULT_OK) {

				Bundle extras = data.getExtras();
				Integer id = extras.getInt("id");
				Contact contact = (Contact) extras.get("contact");

				// replace the local list
				contacts.set(id, contact);
				contactService.update(contact);
			}

			break;

		case CameraService.CAMERA_ACTIVITY_ID:

			if (resultCode == RESULT_OK) {
				Toast.makeText(this, "Envoi de la photo en cours", Toast.LENGTH_LONG).show();

				cameraService.getCameraResult();
				// cameraService.getCameraResultWithSending();

				Log.i(TAG, "Picture was uploaded, print the result in the Recognition result activity");
			}

			else {
				Toast.makeText(this, "Picture was not taken", Toast.LENGTH_LONG).show();
			}

			break;

		case CameraService.RECOGNITION_RESULT_ACTIVITY_ID:

			if (resultCode == RESULT_OK) {

				if (null != data) {

					if (null != data.getExtras()) {

						if (null != data.getExtras().get("contact")) {

							Contact contact = (Contact) data.getExtras().get("contact");
							contacts.add(contact);
							listContactsAdapter.notifyDataSetChanged();
						}
					}
				}
			}
		}
	}
}
