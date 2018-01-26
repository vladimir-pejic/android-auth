package de.kompitenz.uaa.entities;


import com.squareup.moshi.Json;



/**
 * Created by User on 04-Oct-17.
 */
public class ContractDetail {

    String contract_no;
    String seller;
    String buyer;
    String amount;
    String price;
    String delivery;

    public String getContractNo() {
        return contract_no;
    }
    public String getSeller() {
        return seller;
    }
    public String getBuyer() {
        return buyer;
    }
    public String getAmount() { return amount; }
    public String getPrice() { return price; }
    public String getDelivery() { return delivery; }

    public void setContract(String contract_no) {
        this.contract_no = contract_no;
    }
    public void setSeller(String seller) { this.seller = seller; }
    public void setBuyer(String buyer) { this.buyer = buyer; }
    public void setAmount(String amount) {
        this.amount = amount;
    }
    public void setPrice(String price) { this.seller = price; }
    public void setDelivery(String delivery) { this.buyer = delivery; }

}
