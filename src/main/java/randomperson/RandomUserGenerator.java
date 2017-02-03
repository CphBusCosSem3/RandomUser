package randomperson;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Niki on 2017-02-03.
 *
 * @author Niki
 */
public class RandomUserGenerator {

    private static final Gson GSON = new Gson();

    public RandomUserGenerator() {
    }

    private static RandomUser callRest(String server, String restResource,
                                       String parameter, String mime, String
                                               method) {
        String data = "";

        RandomUser user = null;
        try {
            URL e = new URL(server + restResource + parameter);
            HttpURLConnection conn = (HttpURLConnection) e.openConnection();
            try {
                conn.setRequestMethod(method);
                conn.setRequestProperty("Accept", mime);
                if (conn.getResponseCode() != 200) {
                    throw new RuntimeException("Failed: HTTP Response Code= "
                                                       + conn.getResponseCode
                            ());
                }

                Reader r = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                user = parseUser(r);
                conn.disconnect();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } catch (RuntimeException | IOException var9) {
            System.out.println("Error: " + var9);
        }
        System.out.println(data);

        return user;
    }

    private static RandomUser parseUser(Reader r) {
        //GSON.
        RandomUser user = new RandomUser();
        JsonReader jr = GSON.newJsonReader(r);

        try {
            jr.beginObject(); // {
            jr.skipValue(); // results
            jr.beginArray(); // [
            jr.beginObject(); // {

            jr.skipValue(); // Gender
            jr.skipValue(); // Gender:String

            jr.skipValue(); // Name

            jr.beginObject(); // {
            jr.skipValue(); // title
            jr.skipValue(); // title:string
            jr.skipValue(); // first
            user.setFirstName(jr.nextString()); // first:string
            jr.skipValue(); // last
            user.setLastName(jr.nextString()); // last:string
            jr.endObject(); // }

            jr.skipValue(); // Location
            //System.out.println(jr.nextName());
            jr.beginObject(); // {
            jr.skipValue(); // street
            user.setStreet(jr.nextString()); // street:string
            jr.skipValue(); // city
            user.setCity(jr.nextString()); // city:string
            jr.skipValue(); // state
            jr.skipValue(); // state:string
            jr.skipValue(); // postcode
            user.setZip(jr.nextString()); // postcode:string
            jr.endObject(); // }

            jr.skipValue(); // email
            user.setEmail(jr.nextString()); // email:string

            jr.skipValue(); // login
            jr.beginObject(); // {
            jr.skipValue(); // username
            user.setUsername(jr.nextString()); // username:string
            jr.skipValue(); // password
            user.setPassword(jr.nextString()); // password:string

            jr.skipValue();
            jr.skipValue();
            jr.skipValue();
            jr.skipValue();
            jr.skipValue();
            jr.skipValue();
            jr.skipValue();
            jr.skipValue();

            jr.endObject(); // } (of login)

            jr.skipValue();
            jr.skipValue();
            jr.skipValue();
            jr.skipValue();

            jr.skipValue(); // phone
            user.setPhone(jr.nextString()); // phone:string
            jr.skipValue(); // cell
            user.setCell(jr.nextString()); // cell:string
        } catch (IOException e) {
            e.printStackTrace();
        }

        return user;
    }

    public static void main(String[] args) {
        try {
            System.out.println(getRandomUser());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static RandomUser getRandomUser() throws InterruptedException {
        Thread.sleep((long) (3000 + (int) (Math.random() * 6000.0D)));
        return callRest("https://api.randomuser.me", "/", "",
                        "application/json", "GET");
    }
}
