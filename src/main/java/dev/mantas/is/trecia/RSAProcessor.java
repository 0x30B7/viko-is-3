package dev.mantas.is.trecia;

import java.util.Optional;

public abstract class RSAProcessor {

    public record PQPair(String p, String q) { }

    public abstract Optional<PQPair> processPQ(String p, String q);

    public abstract String getN();

    public abstract Optional<String> setN(String n);

    public abstract String getPhi();

    public abstract String getPublicKeyComponent();

    public abstract Optional<String> setPublicKeyComponent(String e);

    public abstract void updatePrivateKey();

    public abstract String getPrivateKeyComponent();

    public abstract Optional<String> setPrivateKeyComponent(String d);

    public abstract String encrypt(String plainText);

    public abstract String decrypt(String cipherText);

}
