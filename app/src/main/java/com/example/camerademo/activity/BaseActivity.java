package com.example.camerademo.activity;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.camerademo.R;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Activity����
 * @author wushengjun
 * @date 2016��8��11��
 */
public class BaseActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	public void gotoActivity(Intent intent) {
		this.startActivity(intent);
	}
	public void gotoActivity(Class<? extends Activity> clz) {
		Intent intent = new Intent(this, clz);
		this.startActivity(intent);
	}
	
	public void toastMessage(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}
	public void toastMessage(int resId) {
		Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * �ж��Ƿ��������ͷ
	 * @return
	 */
	public boolean isHasCamera() {
		PackageManager pm = getPackageManager();
//		toastMessage("��"+pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)+
//				", ǰ��"+pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT));
		return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)
				|| pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT);
	}
	
	/**
	 * �ж�����ͷ�Ƿ����
	 * @return
	 */
	public boolean isCameraCanUse() {
		Camera mCamera = null;
	    try {
	        mCamera = Camera.open();
	        Camera.Parameters mParameters = mCamera.getParameters();
	        mCamera.setParameters(mParameters);
	    } catch (Exception e) {
	    	toastMessage(R.string.msg_no_camera);
	        onBackPressed();
	        return false;
	    } finally {
	    	if(mCamera != null) {
	        	mCamera.release();
	        	mCamera = null;
	    	}
	    }
	    return true;
	}
	
	/**
	 * 返回指定时间戳指定格式的时间字符串
	 * @param pattern 格式
	 * @param time 时间戳
	 * @return
	 */
	public String getDateFormat(String pattern, long time) {
		SimpleDateFormat sdf = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
		sdf.applyPattern(pattern);
		Date date = new Date(time);
		return sdf.format(date);
	}
}
