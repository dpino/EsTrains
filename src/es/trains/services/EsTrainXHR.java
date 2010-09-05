package es.trains.services;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;

/**
 *
 * @author Diego Pino <dpino@igalia.com>
 *
 */
public class EsTrainXHR {

    private static final String TAG = "EsTrainXHR";

    private static final String BASE_URL = "http://pinowsky.appspot.com/trains";

    private InputStream response;

    private String responseAsString;

    public EsTrainXHR() {

    }

    public String getResponseAsString() {
        if (responseAsString == null) {
            responseAsString = toString(response);
        }
        return responseAsString;
    }

    public InputStream getResponse() {
        return response;
    }

    public void execute(Map<String, String> params) {
        try {
            String address = BASE_URL + joinParameters(params);
            URL url = new URL(address);
            response = url.openStream();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String joinParameters(Map<String, String> params) {
        String result = "";

        Set<String> keys = params.keySet();
        for (String each : keys) {
            result += each + "=" + encode(params.get(each)) + "&";
        }
        if (result.length() > 0) {
            result = "?" + result.substring(0, result.length() - 1);
        }
        return result;
    }

    private String encode(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    private String toString(InputStream inputStream) {
        if (inputStream == null) {
            return "";
        }

        StringWriter writer = new StringWriter();
        try {
            IOUtils.copy(inputStream, writer);
            return writer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
