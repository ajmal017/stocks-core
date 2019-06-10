package org.cerion.stocklist.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Tools {

    public static String getURL(String theUrl)
    {
        String sResult = null;

        try
        {
            URL gotoUrl = new URL(theUrl);

            try (InputStreamReader isr = new InputStreamReader(gotoUrl.openStream());
                 BufferedReader in = new BufferedReader(isr))
            {
                StringBuffer sb = new StringBuffer();
                String inputLine;

                //grab the contents at the URL
                while ((inputLine = in.readLine()) != null)
                    sb.append(inputLine+"\r\n");

                sResult = sb.toString();
            }
            catch (IOException e)
            {
                sResult = "";
            }
        }
        catch (MalformedURLException mue) {
            mue.printStackTrace();
        }

        return sResult;
    }


    public static Response getURL(String url, String cookie)
    {
        Response result = new Response();

        try
        {
            URL gotoUrl = new URL(url);
            URLConnection conn = gotoUrl.openConnection();

            if (cookie != null)
                conn.setRequestProperty("Cookie", cookie);

            conn.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuffer sb = new StringBuffer();
            //DataInputStream dis = new DataInputStream(conn.getInputStream());

            result.headers = conn.getHeaderFields();

            String data;
            while ((data = in.readLine()) != null) {
                // System.out.println(urlData);
                if (sb.length() > 0)
                    sb.append("\r\n" + data);
                else
                    sb.append(data);
            }

            result.result = sb.toString();
        }
        catch (MalformedURLException mue)
        {
            mue.printStackTrace();
        }
        catch (IOException e)
        {
            result.result = "";
        }

        return result;
    }
}
