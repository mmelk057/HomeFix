package org.homefix.homefix;

public class ServiceCategory {
    private String name;
    private double rate;
    private String info;

    public ServiceCategory(String name,double rate,String info){
        this.name=name;
        this.rate=rate;
        this.info=info;
    }

    public String getName(){
        return name;
    }
    public double getRate(){
        return rate;
    }
    public String getInfo(){
        return info;
    }
}
