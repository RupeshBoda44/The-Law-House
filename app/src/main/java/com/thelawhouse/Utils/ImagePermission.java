package com.thelawhouse.Utils;

import android.os.Environment;

public interface ImagePermission {
    int CAMERA_CAPTURE = 100;
    int GALLERY = 101;
    int CROP_CODE = 203;
    String SDCARD = Environment.getExternalStorageDirectory() + "/Goyoga/";
    int PERMISSION_FOR_CAMERA = 11;

}
