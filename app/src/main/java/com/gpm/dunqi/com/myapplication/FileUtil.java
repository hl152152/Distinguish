package com.gpm.dunqi.com.myapplication;

import android.content.Context;

import java.io.File;

public class FileUtil {

    public static File getSaveFile(Context context) {
        return new File(context.getFilesDir(), "pic.jpg");
    }

}
