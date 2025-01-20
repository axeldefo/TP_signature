import util.ByteToHex;

import javax.crypto.Cipher;
import java.security.*;
import java.util.Arrays;

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

    // Méthode pour déchiffrer avec la clé publique
    private byte[] decrypt(byte[] pCondensat) throws Exception {
        // Crée une instance de Cipher pour RSA en mode déchiffrement
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, publicKey); // Initialisation en mode déchiffrement avec la clé publique

        // Déchiffre le condensat signé
        byte[] decrypted = cipher.doFinal(pCondensat);

        // Affiche la chaîne déchiffrée
        System.out.println("Déchiffrement du condensat : " + new String(decrypted));

        return decrypted;
    }


    // Méthode pour signer le message
    public byte[] sign() throws Exception {
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

    // Méthode pour vérifier la signature
    public boolean verifySignature(byte[] pCondensat) throws Exception {
        // 1) Haché du message original
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] messageHash = messageDigest.digest(myMessage.getBytes());

        // 2) Déchiffrement du condensat signé avec la clé publique
        byte[] decryptedCondensat = decrypt(pCondensat);

        // 3) Comparaison des deux hachés
        return Arrays.equals(messageHash, decryptedCondensat);
    }


    // Méthode principale pour tester
    public static void main(String[] args) throws Exception {
        // Création de l'objet MyFirstSignature avec le message à signer
        MyFirstSignature lFirstSignature = new MyFirstSignature("Je signe un message électroniquement");

        // Génération de la signature
        byte[] lTabByteResSignature = lFirstSignature.sign();

        // Vérification de la signature
        boolean lValide = lFirstSignature.verifySignature(lTabByteResSignature);

        // Affichage du résultat
        System.out.println("La signature est " + (lValide ? "Valide" : "Invalide"));
    }
}
