package com.healthcare.userservice.common.utils;

import lombok.experimental.UtilityClass;

import java.io.*;
import java.util.Base64;

@UtilityClass
public final class SerializationUtils {

    public static <T> String serialize(T objectToBeSerialized) {
        byte[] byteArray = toByteStream(objectToBeSerialized);
        return toBase64(byteArray);
    }

    public static <T> T deserialize(String base64String, Class<T> clazz) {
        byte[] byteArray = toByteArray(base64String);
        return toObject(byteArray, clazz);
    }

    public static <T> byte[] toByteStream(T objectToBeSerialized) {

        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteArrayOutputStream);
            out.writeObject(objectToBeSerialized);
            out.close();
            byteArrayOutputStream.close();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return new byte[0];
    }

    public static <T> T toObject(byte[] byteArray, Class<T> clazz) {

        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
            ObjectInputStream ois = new ObjectInputStream(bais);
            T obj = clazz.cast(ois.readObject());
            ois.close();
            return obj;
        } catch (ClassNotFoundException | IOException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static String toBase64(byte[] byteArray) {
        return Base64.getEncoder().encodeToString(byteArray);
    }

    public static byte[] toByteArray(String base64String) {
        return Base64.getUrlDecoder().decode(base64String);
    }

    public static String toByteArrayToString(String base64String) {
        return new String(Base64.getUrlDecoder().decode(base64String));
    }

}