package com.javalab.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.javalab.models.Contact;
import com.javalab.models.Picture;
import com.javalab.services.ContactService;
import com.javalab.services.ImageService;
import com.javalab.services.PictureService;

public class RecognitionResultActivity extends Activity {

	private static final int NEW_CONTACT_DIALOG_ID = 1;
	private static final int EXISTING_CONTACTS_DIALOG_ID = 2;

	private Button buttonBack;
	private Button buttonNew;
	private Button buttonExists;
	private ImageView imagePicture;

	private Picture picture;

	private ContactService contactService;
	private ImageService imageService;
	private PictureService pictureService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recognition_result_activity);

		picture = (Picture) getIntent().getSerializableExtra("picture");

		contactService = new ContactService(this);
		imageService = new ImageService(this, getWindowManager());
		pictureService = new PictureService(this);

		getViews();

		initView();
	}

	public void getViews() {

		// buttons
		buttonBack = (Button) findViewById(R.id.button_recognition_result_back);
		buttonNew = (Button) findViewById(R.id.button_recognition_result_new);
		buttonExists = (Button) findViewById(R.id.button_recognition_result_exists);

		// image view
		imagePicture = (ImageView) findViewById(R.id.image_recognition_result_picture);
		imagePicture.setAdjustViewBounds(true);

		imagePicture.setImageDrawable(imageService.getDrawableFromLocal(picture.getPictureUri()));
	}

	public void initView() {

		buttonBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});

		buttonNew.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(NEW_CONTACT_DIALOG_ID);
			}
		});

		buttonExists.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(EXISTING_CONTACTS_DIALOG_ID);
			}
		});
	}

	@Override
	protected Dialog onCreateDialog(int id) {

		Dialog dialog = null;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		switch (id) {
		case NEW_CONTACT_DIALOG_ID:

			final View layout = LayoutInflater.from(this).inflate(R.layout.contact_new_dialog, null);

			builder.setView(layout);
			builder.setTitle("Nouveau contact");
			builder.setPositiveButton("Ajouter", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

					// validate fields
					Boolean valid = true;
					EditText firstName = (EditText) layout.findViewById(R.id.edit_contact_first_name);
					EditText lastName = (EditText) layout.findViewById(R.id.edit_contact_last_name);

					String firstNameValue = firstName.getText().toString().trim();
					String lastNameValue = lastName.getText().toString().trim();

					if (firstNameValue.equals("") || lastNameValue.equals("")) {
						Toast.makeText(RecognitionResultActivity.this, "Veuillez renseigner tous les champs",
								Toast.LENGTH_LONG).show();
						return;
					}

					Contact contact = new Contact();
					contact.setFirstName(firstNameValue);
					contact.setLastName(lastNameValue);

					List<Picture> pictures = new ArrayList<Picture>();
					pictures.add(picture);
					contact.setPictures(pictures);

					contactService.addContact(contact);

					Toast.makeText(RecognitionResultActivity.this, "Contact ajouté avec succès", Toast.LENGTH_LONG)
							.show();

					getIntent().putExtra("contact", contact);
					setResult(RESULT_OK, getIntent());
					finish();
				}
			});

			builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});

			dialog = builder.create();
			break;

		case EXISTING_CONTACTS_DIALOG_ID:

			final List<Contact> contacts = contactService.getContacts();
			final CharSequence[] items = new CharSequence[contacts.size()];

			int i = 0;
			for (Contact contact : contacts) {
				items[i] = contact.toString();
				i++;
			}

			builder.setTitle("Contact existant");

			builder.setItems(items, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {

					Contact contact = contacts.get(item);
					picture.setContact(contact);
					pictureService.addPicture(picture);

					Toast.makeText(RecognitionResultActivity.this, "Photo ajoutée au contact avec succès !",
							Toast.LENGTH_LONG).show();

					setResult(RESULT_OK);
					finish();
				}
			});

			builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});

			dialog = builder.create();
			break;
		}

		return dialog;
	}

	public void newContact() {

	}

	public void existingContact() {

	}
}
