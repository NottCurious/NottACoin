package main.transaction;

public class TransactionInput {
    public String transactionOutputId;
    public TransactionOutput UTXO; // Contains the Unspent Transaction Output

    public TransactionInput(String transactionOutputId) {
        this.transactionOutputId = transactionOutputId;
    }
}
