package com.example.contacts;

import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.SaveDateListener {

    Contact currentContact;
    ImageButton listImageButton, mapImageButton, settingsImageButton, saveButton;
    Button changeBirthdayButton;
    ToggleButton editToggle;
    EditText nameEditText, addressEditText, cityEditText,
            stateEditText, zipEditText, homeEditText,
            cellEditText, emaiEditText;
    TextView birthdayText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initLayoutComponents();
        NavButtonsInitializer.initNavButtons(listImageButton, mapImageButton, settingsImageButton, this);
        initToggleButton();
        setForEditing(false);
        initChangeBirthdayButton();
        currentContact = new Contact();
        initTextChangedEvents();
        initSaveButton();
    }

    private void initSaveButton() {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean wasSuccessful = false;
                ContactDataSource dataSource = new ContactDataSource(MainActivity.this);
                try {
                    dataSource.open();
                    if (currentContact.getContactID() == -1) {
                        wasSuccessful = dataSource.insertContact(currentContact);
                        if(wasSuccessful){
                            int newId = dataSource.getLastContact();
                            currentContact.setContactID(newId);
                        }
                    } else {
                        wasSuccessful = dataSource.updateContact(currentContact);
                    }
                } catch (Exception e) {
                }
                if (wasSuccessful) {
                    editToggle.toggle();
                    setForEditing(false);
                }
            }
        });
    }

    private void initTextChangedEvents() {
        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("Before", nameEditText.getText().toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("on", nameEditText.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("After", nameEditText.getText().toString());
                currentContact.setContactName(nameEditText.getText().toString());
            }
        });
        addressEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                currentContact.setStreetAddress(addressEditText.getText().toString());
            }
        });
        cityEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                currentContact.setCity(cityEditText.getText().toString());
            }
        });
        stateEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                currentContact.setState(stateEditText.getText().toString());
            }
        });
        zipEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                currentContact.setZipcode(zipEditText.getText().toString());
            }
        });
        homeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                currentContact.setPhoneNumber(homeEditText.getText().toString());
            }
        });
        cellEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                currentContact.setCellNumber(cellEditText.getText().toString());
            }
        });
        emaiEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                currentContact.setEMail(emaiEditText.getText().toString());
            }
        });
        homeEditText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        cellEditText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
    }

    private void initToggleButton() {
        editToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setForEditing(editToggle.isChecked());
            }
        });
    }

    private void setForEditing(boolean checked) {
        nameEditText.setEnabled(checked);
        addressEditText.setEnabled(checked);
        cityEditText.setEnabled(checked);
        stateEditText.setEnabled(checked);
        zipEditText.setEnabled(checked);
        homeEditText.setEnabled(checked);
        cellEditText.setEnabled(checked);
        emaiEditText.setEnabled(checked);
        changeBirthdayButton.setEnabled(checked);
        saveButton.setEnabled(checked);
        if (checked) {
            nameEditText.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.showSoftInput(nameEditText, 0);
        }
    }

    private void initChangeBirthdayButton() {
        changeBirthdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                DatePickerDialog dialog = new DatePickerDialog();
                dialog.show(fm, "DatePicker");
            }
        });
    }

    private void initLayoutComponents() {
        listImageButton = findViewById(R.id.imageButtonList);
        mapImageButton = findViewById(R.id.imageButtonMap);
        settingsImageButton = findViewById(R.id.imageButtonSettings);
        editToggle = findViewById(R.id.toggleButtonEdit);
        nameEditText = findViewById(R.id.editName);
        addressEditText = findViewById(R.id.editAddress);
        cityEditText = findViewById(R.id.editCity);
        stateEditText = findViewById(R.id.editState);
        zipEditText = findViewById(R.id.editZip);
        homeEditText = findViewById(R.id.editHome);
        cellEditText = findViewById(R.id.editCell);
        emaiEditText = findViewById(R.id.editEmail);
        changeBirthdayButton = findViewById(R.id.btnBirthday);
        saveButton = findViewById(R.id.imageButtonSave);
        birthdayText = findViewById(R.id.textViewBirthday);
    }

    @Override
    public void didFinishDatePickerDialog(Calendar selectedDate) {
        birthdayText.setText(DateFormat.format("dd/MM/yyyy", selectedDate));
        currentContact.setBirthday(selectedDate);
    }
}