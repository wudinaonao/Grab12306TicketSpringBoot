package com.naonao.grab12306ticket.version.springboot.util;

import com.naonao.grab12306ticket.version.springboot.exception.InvalidCiphertext;
import com.naonao.grab12306ticket.version.springboot.exception.RSAException;
import com.naonao.grab12306ticket.version.springboot.service.tools.GeneralTools;
import com.naonao.grab12306ticket.version.springboot.util.base.AbstractUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: SpringBoot
 * @description: RSA util class
 * @author: Wen lyuzhao
 * @create: 2019-05-27 23:12
 **/
@Slf4j
@Component
public class RSAUtil extends AbstractUtil {

    public static final String CHARSET = "UTF-8";
    public static final String RSA_ALGORITHM = "RSA";
    public static final String RSA_ALGORITHM_SIGN = "SHA256WithRSA";

    private String publicKeyString;
    private String privateKeyString;

    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;


    /**
     * initialization rsa public and private key
     * @param publicKeyPath     publicKeyPath
     * @param privateKeyPath    privateKeyPath
     */
    public RSAUtil(@Value("${setting.encryption.rsa.publicFilePath}")String publicKeyPath,
                   @Value("${setting.encryption.rsa.privateFilePath}")String privateKeyPath) {
        publicKeyString = GeneralTools
                .readFileText(publicKeyPath)
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replace("\n", "")
                .trim();
        privateKeyString = GeneralTools.readFileText(privateKeyPath)
                .replace("-----BEGIN RSA PRIVATE KEY-----", "")
                .replace("-----END RSA PRIVATE KEY-----", "")
                .replace("\n", "")
                .trim();
        try {
            if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
                Security.addProvider(new BouncyCastleProvider());
            }
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
            // get the public key object by x509 encoded key instruction
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKeyString));
            this.publicKey = (RSAPublicKey) keyFactory.generatePublic(x509KeySpec);
            // get the private key object by PKCS#8 encoded key instruction
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKeyString));
            this.privateKey = (RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
        } catch (Exception e) {
            throw new RSAException("not support key ---> " + e.getMessage());
        }
    }



    /**
     * create public and private key
     * @param keySize   key size, general set is 2048
     * @return          Map
     */
    public static Map<String, String> createKeys(int keySize){
        // create a KeyPairGenerator object for RSA algorithm
        KeyPairGenerator kpg;
        try{
            kpg = KeyPairGenerator.getInstance(RSA_ALGORITHM);
        }catch(NoSuchAlgorithmException e){
            throw new IllegalArgumentException("No such algorithm-->[" + RSA_ALGORITHM + "]");
        }
        // initialization KeyPairGenerator object and set key size
        kpg.initialize(keySize);
        // generator key
        KeyPair keyPair = kpg.generateKeyPair();
        // get public key
        Key publicKey = keyPair.getPublic();
        String publicKeyStr = Base64.encodeBase64URLSafeString(publicKey.getEncoded());
        // get private key
        Key privateKey = keyPair.getPrivate();
        String privateKeyStr = Base64.encodeBase64URLSafeString(privateKey.getEncoded());
        Map<String, String> keyPairMap = new HashMap<String, String>(16);
        keyPairMap.put("publicKey", publicKeyStr);
        keyPairMap.put("privateKey", privateKeyStr);

        return keyPairMap;
    }

    public String publicEncrypt(String data){
        try{
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] bytes = rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET), publicKey.getModulus().bitLength());
            if (bytes == null){
                return null;
            }
            return Base64.encodeBase64URLSafeString(bytes);
        }catch(Exception e){
            log.error(AN_EXCEPTION_OCCURRED_WHILE_ENCRYPTING_DATA);
            log.error(e.getMessage());
            return null;
        }
    }
    public String privateDecrypt(String data){
        if (data == null){
            throw new InvalidCiphertext(CIPHER_TEXT_IS_NULL);
        }
        try{
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] bytes = rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.decodeBase64(data), publicKey.getModulus().bitLength());
            if (bytes == null){
                return null;
            }
            return new String(bytes, CHARSET);
        }catch(Exception e){
            log.error(AN_EXCEPTION_OCCURRED_WHILE_DECRYPTING_DATA);
            log.error(e.getMessage());
            return null;
        }
    }


    private static byte[] rsaSplitCodec(Cipher cipher, int opmode, byte[] datas, int keySize){
        int maxBlock = 0;
        if(opmode == Cipher.DECRYPT_MODE){
            maxBlock = keySize / 8;
        }else{
            maxBlock = keySize / 8 - 11;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] buff;
        int i = 0;
        try{
            while(datas.length > offSet){
                if(datas.length-offSet > maxBlock){
                    buff = cipher.doFinal(datas, offSet, maxBlock);
                }else{
                    buff = cipher.doFinal(datas, offSet, datas.length-offSet);
                }
                out.write(buff, 0, buff.length);
                i++;
                offSet = i * maxBlock;
            }
            return out.toByteArray();
        }catch(Exception e){
            log.error(AN_EXCEPTION_OCCURRED_WHILE_ENCRYPTING_AND_DECRYPTING_DATA);
            log.error(e.getMessage());
        }finally {
            IOUtils.closeQuietly(out);
        }
        return null;
    }

}
