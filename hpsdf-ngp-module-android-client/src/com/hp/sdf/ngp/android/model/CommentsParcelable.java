package com.hp.sdf.ngp.android.model;

import android.os.Parcel;
import android.os.Parcelable;

public class CommentsParcelable implements Parcelable {

	private Long assetId;

	public Long getAssetId() {
		return assetId;
	}

	public void setAssetId(Long assetId) {
		this.assetId = assetId;
	}

	public static final Parcelable.Creator<CommentsParcelable> CREATOR = new Parcelable.Creator<CommentsParcelable>() {

		public CommentsParcelable createFromParcel(Parcel source) {
			return new CommentsParcelable(source);
		}

		public CommentsParcelable[] newArray(int size) {
			throw new UnsupportedOperationException();
		}
	};

	public CommentsParcelable(Parcel source) {
		assetId = (Long) source.readValue(Long.class.getClassLoader());
	}

	public CommentsParcelable(Long assetId) {
		this.assetId = assetId;
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int paramInt) {
		dest.writeValue(assetId);
	}

}
