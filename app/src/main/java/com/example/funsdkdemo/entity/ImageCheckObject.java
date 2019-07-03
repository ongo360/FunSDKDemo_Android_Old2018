package com.example.funsdkdemo.entity;

import android.widget.ImageView;

public class ImageCheckObject {
	int src[] = new int[2];
	int value[] = new int[2];
	int index = 0;

	public ImageCheckObject() {
	}

	public ImageCheckObject(int src0, int value0, int src1, int value1) {
		src[0] = src0;
		src[1] = src1;
		value[0] = value0;
		value[1] = value1;
	}

	public int GetValue() {
		return value[index];
	}

	public int SetValue(ImageView image, int nValue) {
		if (nValue == value[1]) {
			index = 1;
		} else {
			index = 0;
		}
		image.setImageResource(src[index]);
		return value[index];
	}

	public int SetValue2(ImageView image) {
		index = index == 0 ? 1 : 0;
		image.setImageResource(src[index]);
		return value[index];
	}
}
