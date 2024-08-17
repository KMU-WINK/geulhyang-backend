package bootcamp.geulhyang.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class OpenApi {
    public String getOpenApi(String requestUrl) throws Exception{
        String protocol = "GET";
        String json;

        URL url = new URL(requestUrl);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod(protocol);
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));

        json = br.readLine();

        return json;
    }
}
