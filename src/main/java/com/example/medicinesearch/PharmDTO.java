package com.example.medicinesearch;

public class PharmDTO {

    private double xpos;
    private double ypos;
    private String name;
    private String telno;
    private String addr;


    public PharmDTO(){
        super();
    }

    public PharmDTO(String name, double ypos, double xpos, String telno, String addr)
    {
        this.name = name;
        this.ypos = ypos;
        this.xpos = xpos;
        this.telno = telno;
        this.addr = addr;
    }

    public double getXpos() {
        return xpos;
    }
    public void setXpos(double xpos) {
        this.xpos = xpos;
    }
    public double getYpos() {
        return ypos;
    }
    public void setYpos(double ypos) {
        this.ypos = ypos;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getTelno() {
        return telno;
    }
    public void setTelno(String telno) {
        this.telno = telno;
    }

    public String getAddr() {
        return addr;
    }
    public void setAddr(String addr) {
        this.addr = addr;
    }

}
