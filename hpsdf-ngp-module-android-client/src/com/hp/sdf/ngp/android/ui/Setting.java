package com.hp.sdf.ngp.android.ui;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.hp.sdf.ngp.android.R;

public class Setting extends PreferenceActivity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.preferences);

	}

}