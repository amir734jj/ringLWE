import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;

public class Main {

    // test client
    public static void main(String[] args) {
        int dimention = 32;
        BigInteger modulus = BigInteger.valueOf(2).pow(32).subtract(BigInteger.ONE);
        Poly ring = new Poly(BigInteger.ONE, 1024).plus(new Poly(BigInteger.ONE, 0));

        Poly[] shared = Key.generateRandomMatrix(dimention, modulus, ring);

        System.out.println("modulus=\t" + modulus);
        System.out.println("ring=\t" + ring);
        System.out.println("A=\t" + Arrays.toString(shared));
        System.out.println();

        Key alice = new Key(dimention, ring, modulus, shared);
        Key bob = new Key(dimention, ring, modulus, shared);

        System.out.println("alice:\n" + alice);
        System.out.println("bob:\n" + bob);

        Poly alice_key = alice.calculateKey(bob.getSecret());
        Poly bob_key = bob.calculateKey(alice.getSecret());

        System.out.println("alice's calculated key=\t" + alice_key);
        System.out.println("bob's calculated key=\t" + bob_key);

        System.out.println("calculated key1=\t" + alice.reconcile(alice_key));
        System.out.println("calculated key2=\t" + bob.reconcile(bob_key));
    }
}