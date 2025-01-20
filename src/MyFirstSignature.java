import util.ByteToHex;

import javax.crypto.Cipher;
import java.security.*;

public class MyFirstSignature {
    protected String myMessage;
    protected PrivateKey privateKey;
    protected PublicKey publicKey;

    public MyFirstSignature(String myMessage) {
        this.myMessage = myMessage;
        generateKeyPair(); // Appeler la méthode pour générer le bi-clé RSA
    }

    // Méthode pour générer le bi-clé RSA
    private void generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048); // Initialiser à 2048 bits
            KeyPair keyPair = keyGen.generateKeyPair();
            this.privateKey = keyPair.getPrivate();
            this.publicKey = keyPair.getPublic();

            // Afficher les clés dans la console
            System.out.println("Clé privée : " + ByteToHex.convert(privateKey.getEncoded()));
            System.out.println("Clé publique : " + ByteToHex.convert(publicKey.getEncoded()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Méthode pour chiffrer avec la clé privée
    private byte[] encrypt(byte[] pTabSig) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, privateKey); // Initialiser en mode chiffrement
            return cipher.doFinal(pTabSig); // Chiffrer et retourner le résultat
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Méthode pour signer le message
    public byte[] sign() {
        try {
            // Étape 1 : Hachage du message
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(myMessage.getBytes("UTF-8"));

            // Étape 2 : Chiffrement du haché avec la clé privée
            return encrypt(hash);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Méthode principale pour tester
    public static void main(String[] args) {
        // Instanciation de l'objet avec le message à signer
        MyFirstSignature mySignature = new MyFirstSignature("Je signe un message électroniquement");

        // Génération de la signature
        byte[] signature = mySignature.sign();

        // Affichage du résultat en hexadécimal
        if (signature != null) {
            System.out.println("La signature du message [" + mySignature.myMessage + "] est :");
            System.out.println(ByteToHex.convert(signature));
        } else {
            System.out.println("Erreur lors de la génération de la signature.");
        }
    }
}
