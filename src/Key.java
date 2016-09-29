import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;

public class Key {
    private Poly[] secret;
    private Poly[] shared;
    private Poly key;
    private Poly error;
    private BigInteger modulus;
    private int dimention;
    private Poly ring;

    public Key(int dimention, Poly ring, BigInteger modulus, Poly[] shared) {
        this.dimention = dimention;
        this.ring = ring;
        this.modulus = modulus;
        this.shared = shared;
        this.secret = generateRandomMatrix(dimention, modulus, ring);
        int error_size = 0;
        Poly[] temp = generateRandomMatrix(error_size, modulus, ring);
        this.error = new Poly();
        for (int i = 0; i < error_size; i++) {
            this.error = this.error.plus(temp[i]);
        }

        this.key = new Poly();

        for (int i = 0; i < this.dimention; i++) {
            key = key.plus(shared[i].times(secret[i]));
        }

        key = key.mod(modulus).mod(ring);

        this.key = key.plus(error);
    }

    public Poly calculateKey(Poly[] secret) {
        Poly result = new Poly();
        for (int i = 0; i < this.dimention; i++) {
            result = result.plus(secret[i].times(this.key)).mod(modulus).mod(ring);
        }

        return result;
    }

    public String reconcile(Poly result) {
        String str = "";

        for (int i = 0; i < result.monos.length; i++) {
            if (result.monos[i].abs().compareTo(this.modulus.divide(BigInteger.valueOf(4))) > 0 && result.monos[i].abs().compareTo(this.modulus.multiply(BigInteger.valueOf(3)).divide(BigInteger.valueOf(4))) < 0) {
                str += "1";
            } else {
                str += "0";
            }
        }

        return str;
    }

    public Poly[] getSecret() {
        return secret;
    }

    public Poly getKey() {
        return key;
    }

    public String toString() {
        String str = "";
        str += "secret=" + Arrays.toString(secret);
        str += "shared=" + Arrays.toString(shared);
        str += "key=" + key + "\n";
        str += "error=" + error + "\n";
        str += "dimention=" + dimention + "\n";
        return str;
    }
    public static Poly[] generateRandomMatrix(int dimention, BigInteger modulus, Poly ring) {
        Poly[] result = new Poly[dimention];

        SecureRandom random = new SecureRandom();

        for (int i = 0; i < dimention; i++) {
            Poly p = new Poly();
            for (int j = 0; j < dimention; j++) {
                if (dimention < 4) {
                    p = p.plus(new Poly(BigInteger.probablePrime(16, random), j));
                } else {
                    p = p.plus(new Poly(BigInteger.probablePrime(dimention / 2, random), j));
                }
            }

            p = p.mod(modulus).mod(ring);
            result[i] = p;
        }

        return result;
    }

}
