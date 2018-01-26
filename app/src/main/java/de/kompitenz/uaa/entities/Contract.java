package de.kompitenz.uaa.entities;

/**
 * Created by User on 03-Oct-17.
 */
public class Contract {

    String contract_no;
    String contract_type_id;
    String quantity_amount;


    public String getContractNo() {
        return contract_no;
    }
    public String getContractTypeId() {
        return contract_type_id;
    }
    public String getQuantityAmount() {
        return quantity_amount;
    }


    public void setContractNo(String contract_no) {
        this.contract_no = contract_no;
    }
    public void setContractTypeId(String contract_type_id) { this.contract_type_id = contract_type_id; }
    public void setQuantityAmount(String quantity_amount) { this.quantity_amount = quantity_amount; }

}
