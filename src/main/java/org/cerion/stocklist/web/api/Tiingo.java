package org.cerion.stocklist.web.api;

import org.cerion.stocklist.model.Symbol;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Tiingo {

    private static final String API_TOKEN = "e113430723313f765f12a4d130acd1c5aac78eee";

    public Symbol getSymbol(String symbol) {
        String url = "https://api.tiingo.com/tiingo/daily/" + symbol + "/prices";

        String data = getData(url);

        return null;
    }



    private static String getData(String targetURL) {
        URL url;
        HttpURLConnection connection = null;
        try {
            //Create connection
            url = new URL(targetURL);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");

            connection.setRequestProperty("Content-type", "application/json");
            connection.setRequestProperty("Authorization", "Token e113430723313f765f12a4d130acd1c5aac78eee");

            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();

        } catch (Exception e) {

            e.printStackTrace();
            return null;

        } finally {

            if(connection != null) {
                connection.disconnect();
            }
        }
    }

}
