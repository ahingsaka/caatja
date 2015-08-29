package com.katspow.caatja.modules.data;

import java.util.ArrayList;
import java.util.List;

public class CaatjaHighScores {
    
    private List<CaatjaScore> scores;
    
    public CaatjaHighScores() {
        this.scores = new ArrayList<CaatjaScore>();
    }

    public List<CaatjaScore> getScores() {
        return scores;
    }

    public void setScores(List<CaatjaScore> scores) {
        this.scores = scores;
    }
    
    public void addScore(CaatjaScore score) {
        this.scores.add(score);
    }
    
}
