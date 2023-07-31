package com.umc.commonplant.global.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Configuration
public class FCMConfig {
    private final ClassPathResource firebaseResource = new ClassPathResource(
            "firebase/commonplant-8463a-firebase-adminsdk-me65f-52b19519c4.json");

    @Bean
    FirebaseApp firebaseApp() throws IOException {
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(
                        firebaseResource.getInputStream()))
                .build();

        return FirebaseApp.initializeApp(options);
    }

    @Bean
    FirebaseMessaging firebaseMessaging() throws IOException {
        return FirebaseMessaging.getInstance(firebaseApp());
    }
}

//@Configuration
//public class FCMConfig {
//
//    @Bean
//    FirebaseMessaging firebaseMessaging() throws IOException {
//        ClassPathResource resource = new ClassPathResource("firebase/commonplant-8463a-firebase-adminsdk-me65f-52b19519c4.json");
//
//        InputStream refreshToken = resource.getInputStream();
//
//        FirebaseApp firebaseApp = null;
//        List<FirebaseApp> firebaseAppList = FirebaseApp.getApps();
//
//        if(firebaseAppList != null && !firebaseAppList.isEmpty()) {
//            for(FirebaseApp app : firebaseAppList) {
//                if(app.getName().equals(FirebaseApp.DEFAULT_APP_NAME)){
//                    firebaseApp = app;
//                }
//            }
//        } else {
//            FirebaseOptions options = FirebaseOptions.builder()
//                    .setCredentials(GoogleCredentials.fromStream(refreshToken))
//                    .build();
//
//            firebaseApp = FirebaseApp.initializeApp(options);
//        }
//
//        return FirebaseMessaging.getInstance(firebaseApp);
//    }
//}
