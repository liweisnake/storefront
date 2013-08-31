package com.hp.sdf.ngp.android.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.hp.sdf.ngp.ws.mobileclient.model.MobileAsset;

public class AppParcelable implements Parcelable {

	private MobileAsset info;

	public AppParcelable(Parcel source) {
		info = (MobileAsset) source.readValue(MobileAsset.class
				.getClassLoader());
	}

	public AppParcelable(MobileAsset info) {
		this.info = info;
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeValue(info);
	}

	public static final Parcelable.Creator<AppParcelable> CREATOR = new Parcelable.Creator<AppParcelable>() {

		public AppParcelable createFromParcel(Parcel source) {
			return new AppParcelable(source);
		}

		public AppParcelable[] newArray(int size) {
			// return new AppParcelable[size];
			throw new UnsupportedOperationException();
		}

	};

	public MobileAsset getInfo() {
		return info;
	}
}
