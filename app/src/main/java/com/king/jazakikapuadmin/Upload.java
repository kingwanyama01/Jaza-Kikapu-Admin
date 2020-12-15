package com.king.jazakikapuadmin;

public class Upload {
    private String deposit_type,amount,image_url,depositing_email,date, key;

    public Upload(String deposit_type, String amount, String image_url, String depositing_email, String date, String key) {
        this.deposit_type = deposit_type;
        this.amount = amount;
        this.image_url = image_url;
        this.depositing_email = depositing_email;
        this.date = date;
        this.key = key;
    }

    public Upload() {
    }

    public String getDeposit_type() {
        return deposit_type;
    }

    public void setDeposit_type(String deposit_type) {
        this.deposit_type = deposit_type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getDepositing_email() {
        return depositing_email;
    }

    public void setDepositing_email(String depositing_email) {
        this.depositing_email = depositing_email;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
