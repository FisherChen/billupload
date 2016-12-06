package com.pab.billupload.pojo;

/**
 * Created by fisher on 16-10-20.
 */
public class BillIndexInfo {
    private String email;
    private String accNo;
    private String fileName;
    private String CityCode;
    private String billPath;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccNo() {
        return accNo;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getBillPath() {
        return billPath;
    }

    public void setBillPath(String billPath) {
        this.billPath = billPath;
    }

    public String getCityCode() {
        return CityCode;
    }

    public void setCityCode(String cityCode) {
        CityCode = cityCode;
    }

    public String toString(String decollator){

        return this.getEmail()+decollator+this.getAccNo()+decollator+this.getFileName()+"\n";
    }
}
