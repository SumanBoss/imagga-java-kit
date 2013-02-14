package net.jakobnielsen.imagga.crop_slice.convert;

import net.jakobnielsen.imagga.convert.Converter;
import net.jakobnielsen.imagga.convert.ConverterException;
import net.jakobnielsen.imagga.crop_slice.bean.Cropping;
import net.jakobnielsen.imagga.crop_slice.bean.Region;
import net.jakobnielsen.imagga.crop_slice.bean.SmartCropping;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.ArrayList;
import java.util.List;

import static net.jakobnielsen.imagga.convert.ConverterTools.getInteger;
import static net.jakobnielsen.imagga.convert.ConverterTools.getLong;
import static net.jakobnielsen.imagga.convert.ConverterTools.getString;

public class SmartCroppingConverter implements Converter<List<SmartCropping>> {

    private static final String SMART_CROPPINGS = "smart_croppings";

    private static final String CROPPINGS = "croppings";

    @Override
    public List<SmartCropping> convert(String jsonString) {
        if (jsonString == null) {
            throw new ConverterException("The given JSON string is null");
        }

        JSONObject json = (JSONObject) JSONValue.parse(jsonString);
        if (!json.containsKey(SMART_CROPPINGS)) {
            throw new ConverterException(SMART_CROPPINGS + " key missing from json : " + jsonString);
        }
        JSONArray jsonArray = (JSONArray) json.get(SMART_CROPPINGS);

        List<SmartCropping> smartCroppings = new ArrayList<SmartCropping>();

        for (Object aJsonArray : jsonArray) {

            JSONObject smartCroppingObject = (JSONObject) aJsonArray;

            String url = getString("url", smartCroppingObject);

            List<Cropping> croppings = new ArrayList<Cropping>();

            if (smartCroppingObject.containsKey(CROPPINGS)) {

                JSONArray croppingsArray = (JSONArray) smartCroppingObject.get(CROPPINGS);

                for (Object aCroppingsArray : croppingsArray) {

                    JSONObject croppingObject = (JSONObject) aCroppingsArray;

                    croppings.add(new Cropping(
                            getInteger("target_width", croppingObject),
                            getInteger("target_height", croppingObject),
                            new Region(
                                    getLong("x1", croppingObject),
                                    getLong("y1", croppingObject),
                                    getLong("x2", croppingObject),
                                    getLong("y2", croppingObject)
                            )));
                }
                smartCroppings.add(new SmartCropping(url, croppings));
            }
        }

        return smartCroppings;
    }


}
