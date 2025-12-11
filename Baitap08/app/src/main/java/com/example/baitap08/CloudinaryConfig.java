package com.example.baitap08;

import android.content.Context;
import com.cloudinary.android.MediaManager;
import java.util.HashMap;
import java.util.Map;

public class CloudinaryConfig {
    private static boolean isInitialized = false;

    public static void init(Context context) {
        if (isInitialized) {
            return;
        }

        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dpwsigzh3"); // <-- THAY THẾ BẰNG CLOUD NAME CỦA BẠN
        config.put("api_key", "942196361244221");          // <-- THAY THẾ BẰNG API KEY CỦA BẠN
        config.put("api_secret", "cKUkKbvOkFpye0IAavbWujPsZlw");    // <-- THAY THẾ BẰNG API SECRET CỦA BẠN
        MediaManager.init(context, config);

        isInitialized = true;
    }
}
