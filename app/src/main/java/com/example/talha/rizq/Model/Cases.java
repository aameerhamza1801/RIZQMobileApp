package com.example.talha.rizq.Model;

public class Cases {
    private String account, cid, cnic, description, image, needy_name,needed_amount,collected_amount,verified;

    public Cases() {
    }

    public Cases(String account, String cid, String cnic, String description, String image, String needy_name, String needed_amount, String collected_amount, String verified) {
        this.account = account;
        this.cid = cid;
        this.cnic = cnic;
        this.description = description;
        this.image = image;
        this.needy_name = needy_name;
        this.needed_amount = needed_amount;
        this.collected_amount = collected_amount;
        this.verified = verified;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNeedy_name() {
        return needy_name;
    }

    public void setNeedy_name(String needy_name) {
        this.needy_name = needy_name;
    }

    public String getNeeded_amount() {
        return needed_amount;
    }

    public void setNeeded_amount(String needed_amount) {
        this.needed_amount = needed_amount;
    }

    public String getCollected_amount() {
        return collected_amount;
    }

    public void setCollected_amount(String collected_amount) {
        this.collected_amount = collected_amount;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }
}
