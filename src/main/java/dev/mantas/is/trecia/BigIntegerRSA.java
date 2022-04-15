package dev.mantas.is.trecia;

import java.math.BigInteger;
import java.util.Optional;
import java.util.Random;

/**
 * https://github.com/Anirban166/RSA-Cryptosystem
 */
public class BigIntegerRSA extends RSAProcessor {

    private static final int MAX_LENGTH = 1 * Byte.SIZE;
    private BigInteger phi;

    // Public key (encryption)
    private BigInteger n, e;

    // Private key (decryption)
    private BigInteger p, q, d;

    public byte[] encryptMessage(byte[] message) {
        return new BigInteger(message).modPow(e, n).toByteArray();
    }

    public byte[] decryptMessage(byte[] message) {
        return new BigInteger(message).modPow(d, n).toByteArray();
    }

    // ======================================================================================================== //

    @Override
    public Optional<PQPair> processPQ(String p_, String q_) {
        Random rng = new Random();

        if (p_.isEmpty() || q_.isEmpty()) {
            this.p = BigInteger.probablePrime(MAX_LENGTH, rng);
            this.q = BigInteger.probablePrime(MAX_LENGTH, rng);
        } else {
            this.p = new BigInteger(p_);
            this.q = new BigInteger(q_);
        }

        this.n = p.multiply(q);
        this.phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        this.e = BigInteger.probablePrime(MAX_LENGTH / 2, rng);

        while (phi.gcd(e).compareTo(BigInteger.ONE) > 0 && e.compareTo(phi) < 0) {
            e = e.add(BigInteger.ONE);
        }

        this.d = e.modInverse(phi);

        return Optional.of(new PQPair(p.toString(), q.toString()));
    }

    @Override
    public String getN() {
        return n.toString();
    }

    @Override
    public Optional<String> setN(String n_) {
        this.n = new BigInteger(n_);
        return Optional.of(n.toString());
    }

    @Override
    public String getPhi() {
        return phi.toString();
    }

    @Override
    public String getPublicKeyComponent() {
        return e.toString();
    }

    @Override
    public Optional<String> setPublicKeyComponent(String e_) {
        this.e = new BigInteger(e_);
        return Optional.of(e.toString());
    }

    @Override
    public void updatePrivateKey() {
        for (int p_ = 0; p_ < 300; p_++) {
            for (int q_ = 0; q_ < 300; q_++) {

                if (this.n.intValue() == p_ * q_) {
                    BigInteger p__ = new BigInteger("" + p_);
                    BigInteger q__ = new BigInteger("" + q_);
                    BigInteger phi = p__.subtract(BigInteger.ONE).multiply(q__.subtract(BigInteger.ONE));
                    this.d = e.modInverse(phi);
                }

            }
        }
    }

    @Override
    public String getPrivateKeyComponent() {
        return d.toString();
    }

    @Override
    public Optional<String> setPrivateKeyComponent(String d_) {
        this.d = new BigInteger(d_);
        return Optional.of(d.toString());
    }

    @Override
    public String encrypt(String plainText) {
        char[] arr = plainText.toCharArray();

        for (int i = 0; i < arr.length; i++) {
            arr[i] = (char) new BigInteger("" + ((int) arr[i])).modPow(e, n).intValue();
        }

        return new String(arr);
    }

    @Override
    public String decrypt(String cipherText) {
        char[] arr = cipherText.toCharArray();

        for (int i = 0; i < arr.length; i++) {
            arr[i] = (char) new BigInteger("" + ((int) arr[i])).modPow(d, n).intValue();
        }

        return new String(arr);
    }

}
