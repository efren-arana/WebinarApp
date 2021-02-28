package com.company.webinarapp.API;

public class ApiUtils {

        private ApiUtils() {}

        public static final String BASE_URL = "https://spring-boot-webinar.herokuapp.com/api/v1/";

        public static APIService getAPIService() {

            return RetrofitClient.getClient(BASE_URL).create(APIService.class);
        }
}
