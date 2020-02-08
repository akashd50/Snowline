package com.greymatter.snowline.DataParsers;

import com.greymatter.snowline.Objects.Street;
import static com.greymatter.snowline.Data.Constants.*;

import org.json.JSONException;
import org.json.JSONObject;

public class StreetParser extends JSONParser {
    private Street street;
    public StreetParser(){street = new Street();}

    public Street parseStreetInfo(JSONObject object){
        if(street==null) street = new Street();

        try {
            street.setKey(object.getString(KEY));
            street.setName(object.getString(NAME));
            street.setName(object.getString(TYPE));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return street;
    }
}