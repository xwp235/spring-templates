package jp.onehr.base.controller;

public class DemoModel {
    private String name;
    private long visits;

    public DemoModel(String name, long visits) {
        this.name = name;
        this.visits = visits;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setVisits(long visits) {
        this.visits = visits;
    }

    public long getVisits(){
        return this.visits;
    }

}
