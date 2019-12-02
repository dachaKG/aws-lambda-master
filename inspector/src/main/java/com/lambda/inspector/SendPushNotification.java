package com.lambda.inspector;

import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class SendPushNotification {

    private static final long serialVersionUID = -8022560668279505764L;

    // Method to send Notifications from server to client end.
    public final static String AUTH_KEY_FCM = "AAAACtLB908:APA91bGfdf6_Ars8KF4tvV48MsZ7RuN7RUx6nc-TGmVKsnWMcRiW4PNSGtlW4FFbEaHsq-RqzEpS-1VRexOu_cNvY-ChY7R9CP-_b2oeNsUn1j4TW0Jg5liUbG1nHV8wNYntvTVOMmcH";
    public final static String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";
//    public final static String DEVICE_ID = "clwt5mHLrc8:APA91bEULpfTE0bJb72WWDA0HfYw5WKRKGzCmv9bNuRkNnIm7TSxs4Isaqs343tE966gh6gMhitiyjOBSo6gg4RDu_skhOLFKXFdx4-Mh15Qc2pfcTNSA2YMb577CThreBcRoXfzqQJ_";
    public final static String DEVICE_ID = "cSbM-rwaGyI:APA91bGaxfYe5qakQK9PyRvoLhv4qk0UK-k5zPhXZ6mzC9jCAFnmaLPs2ufmim7UIGOUiZB_pXuMJq-DLKW2CWOnabT_ngTT9znQ4T3crM6lo3rf80dSWqDl1qTSaoJ5Bd_1wf3rQM31";
            //    public final static String DEVICE_ID = "dBkyD1Pgaps:APA91bFhUWgBhzKelc_GmXjiSgGX8XTXOGUdMhMBZBIxjNDBZtsDb_o_KmLa6Ype1vTUi-Dmr0bf0WhJr_fPoY2FHraHyWtVtsx7xPmtIpExmQfrJtuI8dGXEBFo983v5R59FOBfdPiS";

    public String execute(String title, String body, String runArn) {
        String DeviceIdKey = DEVICE_ID;
        String authKey = AUTH_KEY_FCM;
        String FMCurl = API_URL_FCM;

        try {
            URL url = new URL(FMCurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "key=" + authKey);
            conn.setRequestProperty("Content-Type", "application/json");
            System.out.println(DeviceIdKey);
            JSONObject data = new JSONObject();
            data.put("to", DeviceIdKey.trim());
            JSONObject info = new JSONObject();
            info.put("title", title); // Notification title
//            info.put("title", "FCM Notification Title"); // Notification title
            info.put("body", body); // Notification body
            if(!runArn.isEmpty()) {
                info.put("runArn", runArn);
                System.out.println("run arn is : " + runArn);
            }

            data.put("data", info);
            System.out.println(data.toString());
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data.toString());
            wr.flush();
            wr.close();

            int responseCode = conn.getResponseCode();
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            System.out.println("BufferReader: " + in);
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Input line: " + inputLine);
                response.append(inputLine);
            }
            in.close();
            System.out.println("Response: " + response);
            return response.toString();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }

        return "Mistake";
    }

}
