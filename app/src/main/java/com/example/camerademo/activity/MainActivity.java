package com.example.camerademo.activity;

import com.example.camerademo.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

public class MainActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ViewUtils.inject(this);
		
		init();
	}
	private void init() {
		
	}
	
	//运用xUtils第三方开发包注解方式实现事件绑定
	@OnClick({R.id.btn_scan})
	public void onMyClick(View view) {
		switch (view.getId()) {
		//case R.id.btn_photo: //相机拍照
//			if(isHasCamera()) {
//				toastMessage(MediaStore.ACTION_IMAGE_CAPTURE);
//				goTakePhoto();
//			} else {
//				toastMessage(R.string.msg_no_camera);
//			}
			//gotoActivity(CameraActivity.class);
			//break;
		case R.id.btn_scan: //相机扫码
			gotoActivity(CaptureActivity.class);
			break;

		default:
			break;
		}
	}

	private void goTakePhoto() {
		Intent intent = new Intent();
		intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		gotoActivity(intent);
	}
}
