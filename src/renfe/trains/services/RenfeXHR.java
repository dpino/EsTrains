package renfe.trains.services;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;

/**
 *
 * @author Diego Pino <dpino@igalia.com>
 *
 */
public class RenfeXHR {

    private static final String TAG = "RenfeXHR";

    private static final String BASE_URL = "http://pinowsky.appspot.com/trains";

    private InputStream response;

    private String responseAsString;

    public RenfeXHR() {

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

    private String joinParameters(Map<String, String> params) {
        String result = "";

        Set<String> keys = params.keySet();
        for (String each : keys) {
            result += each + "=" + params.get(each) + "&";
        }
        if (result.length() > 0) {
            result = "?" + result.substring(0, result.length() - 1);
        }
        return result;
    }

    public void execute(Map<String, String> params) {
        try {
            String address = BASE_URL + joinParameters(params);
            URL url = new URL(address);
            response = url.openStream();
            // Log.d(TAG, getResponseAsString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String toString(InputStream inputStream) {
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
