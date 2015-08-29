package com.katspow.caatja.modules.data;

public class CaatjaScore {
    
    private String name;
    
    private int score;
    
    public CaatjaScore(String name, int score) {
        super();
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
    
}
