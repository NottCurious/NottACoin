package main.transaction;

import main.string.StringUtil;

import java.security.PublicKey;

public class TransactionOutput {
    public String id;
    public PublicKey receipient;
    public float value;
    public String parentTransactionId;

    public TransactionOutput(PublicKey receipient, float value, String parentTransactionId) {
        this.receipient = receipient;
        this.value = value;
        this.parentTransactionId = parentTransactionId;

        this.id = StringUtil.applySha256(StringUtil.getStringFromKey(receipient) + Float.toString(value) + parentTransactionId);
    }

    public boolean isMine(PublicKey publicKey) {
        return (publicKey == receipient);
    }
}
