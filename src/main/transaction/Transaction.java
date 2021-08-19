package main.transaction;

import java.security.*;
import java.util.ArrayList;
import java.util.PrimitiveIterator;

import main.NottACoin;
import main.string.StringUtil;

public class Transaction {
    public String transactionId;
    public PublicKey sender;
    public PublicKey receipient;
    public float value;
    public byte[] signature;

    public ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
    public ArrayList<TransactionOutput> outputs = new ArrayList<TransactionOutput>();

    private static int sequence = 0;

    public Transaction(PublicKey from, PublicKey to, float value, ArrayList<TransactionInput> inputs) {
        this.sender = from;
        this.receipient = to;
        this.value = value;
        this.inputs = inputs;
    }

    private String calculateHash() {
        sequence++;

        return StringUtil.applySha256(StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(receipient) + Float.toString(value) + sequence);
    }

    public void generateSignature(PrivateKey privateKey) {
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(receipient) + Float.toString(value);
        signature = StringUtil.applyECDSASig(privateKey, data);
    }

    public boolean verifySignature() {
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(receipient) + Float.toString(value);
        return StringUtil.verifyECDSASig(sender, data, signature);
    }

    public boolean processTransaction() {
        if(verifySignature() == false) {
            System.out.println("-> Transaction Signature Failed to Verify");
            return false;
        }

        for(TransactionInput ti : inputs) {
            ti.UTXO = NottACoin.UTXOs.get(ti.transactionOutputId);
        }

        if(getInputsValue() < NottACoin.minimumTransaction) {
            System.out.println("-> Transaction Input to Small: " + getInputsValue());
            return false;
        }

        float leftOver = getInputsValue() - value;
        transactionId = calculateHash();
        outputs.add(new TransactionOutput(this.receipient, value, transactionId));
        outputs.add(new TransactionOutput(this.sender, leftOver, transactionId));

        for(TransactionOutput to : outputs) {
            NottACoin.UTXOs.put(to.id, to);
        }

        for(TransactionInput ti : inputs) {
            if(ti.UTXO == null) continue;
            NottACoin.UTXOs.remove(ti.UTXO.id);
        }

        return true;
    }

    public float getInputsValue() {
        float total = 0;
        for(TransactionInput ti : inputs) {
            if(ti.UTXO == null) continue;
            total += ti.UTXO.value;
        }

        return total;
    }

    public float getOutputsValue() {
        float total = 0;

        for(TransactionOutput to : outputs) {
            total += to.value;
        }

        return total;
    }
}
