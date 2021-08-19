package main;

import main.blockchain.Block;
import main.blockchain.Wallet;
import java.security.Security;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;

import com.google.gson.GsonBuilder;
import main.string.StringUtil;
import main.transaction.Transaction;
import main.transaction.TransactionOutput;


public class NottACoin {
    public static ArrayList<Block> blockchain = new ArrayList<Block>();
    public static HashMap<String, TransactionOutput> UTXOs = new HashMap<String, TransactionOutput>();
    public static int difficulty = 5;
    public static Wallet walletA;
    public static Wallet walletB;

    public static void main(String[] args) {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        walletA = new Wallet();
        walletB = new Wallet();

        System.out.println("Private & Public Keys");
        System.out.println(StringUtil.getStringFromKey(walletA.privateKey));
        System.out.println(StringUtil.getStringFromKey(walletB.publicKey));

        Transaction transaction = new Transaction(walletA.publicKey, walletB.publicKey, 5, null);
        transaction.generateSignature(walletA.privateKey);

        System.out.print("Verified Signature?: ");
        System.out.println(transaction.verifySignature());
    }
}