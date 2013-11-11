/*
 * Copyright 2013 Aleksey Kislin
 * Copyright 2013 Michal Švirec
 *
 * This file is part of aRevelation.
 *
 * aRevelation is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aRevelation is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aRevelation.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.marmalade.aRevelation;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.Key;
import java.security.MessageDigest;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Random;
import java.util.zip.DataFormatException;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;

/**
 * Author: <a href="mailto:alexey.kislin@gmail.com">Alexey Kislin</a>
 * Date: 6/26/13
 * Time: 2:44 AM
 */
public class Cryptographer {

    final static byte[] MAGIC_STRING_DATA_VERSION_2 = new byte[] {'r', 'v', 'l', 0, 2, 0};
    final static byte[] VERSION_0_4_7 = new byte[] {0, 4, 7};

    /**
     * Main decpypt method
     * @param file
     * @param password
     * @return
     * @throws Exception
     */
    public static String decrypt(File file, String password) throws Exception {
        RandomAccessFile f = new RandomAccessFile(file.getAbsoluteFile(), "r");
        byte[] fileData = new byte[(int)f.length()];
        f.read(fileData);
        return decrypt(fileData, password);
    }

    public static String decrypt(byte[] fileData, String password) throws Exception {
        byte[] header;
        header = Arrays.copyOfRange(fileData, 0, 36);

        byte[] iv = null;
        byte[] salt = null;

        if(Arrays.equals(Arrays.copyOfRange(header, 0, 6), MAGIC_STRING_DATA_VERSION_2)) {
            if(Arrays.equals(VERSION_0_4_7, Arrays.copyOfRange(header, 6, 9))) {
                salt = Arrays.copyOfRange(header, 12, 20);
                iv = Arrays.copyOfRange(header, 20, 36);
                Cipher cypher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                SecretKeyFactory scf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
                KeySpec ks = new PBEKeySpec(password.toCharArray(), salt, 12000, 256);
                SecretKey s = scf.generateSecret(ks);                                           // Bottleneck (12k)
                Key k = new SecretKeySpec(s.getEncoded(),"AES");
                cypher.init(Cipher.DECRYPT_MODE, k, new IvParameterSpec(iv));

                byte[] input = Arrays.copyOfRange(fileData, 36, fileData.length);
                byte[] compressedData = cypher.doFinal(input);

                byte[] hash256 = Arrays.copyOfRange(compressedData, 0, 32);

                compressedData = Arrays.copyOfRange(compressedData, 32, compressedData.length);

                compressedData = addPadding(compressedData);

                MessageDigest md = MessageDigest.getInstance("SHA-256");
                md.update(compressedData);
                byte[] computedHash = md.digest();

                if(!Arrays.equals(computedHash, hash256)) {
                    throw new Exception("Invalid data");
                }

                byte[] result = decompress(compressedData);
                return new String(result);
            }
        }

        throw new Exception("Unknown file format");
    }

    public static byte[] encrypt(String xmlData, String password) throws Exception {
        if(password == null || password =="")
            throw new Exception("Password cannot be empty");

        Random r = new Random();

        byte[] salt = new byte[8];
        r.nextBytes(salt);

        byte[] compressedData = compress(xmlData);

        byte[] compressedDataWithPadlen = addPadding(compressedData);

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(compressedDataWithPadlen);
        byte[] computedHash = md.digest();

        byte[] hashAndData = concatenateByteArrays(computedHash, compressedData);

        byte[] iv = new byte[16];
        r.nextBytes(iv);

        Cipher cypher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        SecretKeyFactory scf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

        KeySpec ks = new PBEKeySpec(password.toCharArray(), salt, 12000, 256);
        SecretKey s = scf.generateSecret(ks);
        Key k = new SecretKeySpec(s.getEncoded(), "AES");

        cypher.init(Cipher.ENCRYPT_MODE, k, new IvParameterSpec(iv));

        byte[] encrypted= new byte[cypher.getOutputSize(hashAndData.length)];

        int enc_len = cypher.update(hashAndData, 0, hashAndData.length, encrypted, 0);
        enc_len += cypher.doFinal(encrypted, enc_len);

        byte[] a1 = concatenateByteArrays(MAGIC_STRING_DATA_VERSION_2, VERSION_0_4_7);
        a1 = concatenateByteArrays(a1, new byte[] {0,0,0});
        a1 = concatenateByteArrays(a1, salt);
        a1 = concatenateByteArrays(a1, iv);
        a1 = concatenateByteArrays(a1, encrypted);

        return a1;
    }

    private static String bytArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder();
        for(byte b: a)
            sb.append(String.format("%02x", b&0xff));
        return sb.toString();
    }

    private static byte[] decompress(byte[] inputData) throws DataFormatException, IOException {
        Inflater decompressor = new Inflater();
        decompressor.setInput(inputData);
        ByteArrayOutputStream bos = new ByteArrayOutputStream(inputData.length);
        byte[] buf = new byte[1024];
        while (!decompressor.finished()) {
            int count = decompressor.inflate(buf);
            bos.write(buf, 0, count);
        }
        bos.close();
        return bos.toByteArray();
    }

    private static byte[] compress(String text) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            OutputStream out = new DeflaterOutputStream(baos);
            out.write(text.getBytes("UTF-8"));
            out.close();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        return baos.toByteArray();
    }

    private static byte[] concatenateByteArrays(byte[] a, byte[] b) {
        byte[] result = new byte[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

    private static byte[] addPadding (byte[] a) {
        byte padlen = (byte)(16 - (a.length % 16));
        if (padlen == 0)
            padlen = 16;

        byte[] b = new byte[a.length + padlen];
        System.arraycopy(a, 0, b, 0, a.length);
        for (int i = 0; i < padlen; i++)
            b[a.length + i] = padlen;
        return b;
    }
}
