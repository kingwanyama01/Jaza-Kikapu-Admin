package com.king.jazakikapuadmin;

public class Upload {
    private String name,amount,description,image_url,depositing_email,date, key;

    public Upload(String name, String amount, String description, String image_url, String depositing_email, String date, String key) {
        this.name = name;
        this.amount = amount;
        this.description = description;
        this.image_url = image_url;
        this.depositing_email = depositing_email;
        this.date = date;
        this.key = key;
    }

    public Upload() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
