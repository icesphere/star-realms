package org.smartreaction.starrealms.model;

import java.util.Random;

public class GameOptions {

    public String includeBaseSet = "Y";

    public String includeColonyWars = "N";

    public String includeYearOnePromos = "N";

    public String includeCrisisBasesAndBattleships = "N";

    public String includeCrisisEvents = "N";

    public String includeCrisisFleetsAndFortresses = "N";

    public String includeCrisisHeroes = "N";

    public String includeGambits = "N";

    public String getIncludeBaseSet() {
        return includeBaseSet;
    }

    public void setIncludeBaseSet(String includeBaseSet) {
        this.includeBaseSet = includeBaseSet;
    }

    public String getIncludeColonyWars() {
        return includeColonyWars;
    }

    public void setIncludeColonyWars(String includeColonyWars) {
        this.includeColonyWars = includeColonyWars;
    }

    public String getIncludeYearOnePromos() {
        return includeYearOnePromos;
    }

    public void setIncludeYearOnePromos(String includeYearOnePromos) {
        this.includeYearOnePromos = includeYearOnePromos;
    }

    public String getIncludeCrisisBasesAndBattleships() {
        return includeCrisisBasesAndBattleships;
    }

    public void setIncludeCrisisBasesAndBattleships(String includeCrisisBasesAndBattleships) {
        this.includeCrisisBasesAndBattleships = includeCrisisBasesAndBattleships;
    }

    public String getIncludeCrisisEvents() {
        return includeCrisisEvents;
    }

    public void setIncludeCrisisEvents(String includeCrisisEvents) {
        this.includeCrisisEvents = includeCrisisEvents;
    }

    public String getIncludeCrisisFleetsAndFortresses() {
        return includeCrisisFleetsAndFortresses;
    }

    public void setIncludeCrisisFleetsAndFortresses(String includeCrisisFleetsAndFortresses) {
        this.includeCrisisFleetsAndFortresses = includeCrisisFleetsAndFortresses;
    }

    public String getIncludeCrisisHeroes() {
        return includeCrisisHeroes;
    }

    public void setIncludeCrisisHeroes(String includeCrisisHeroes) {
        this.includeCrisisHeroes = includeCrisisHeroes;
    }

    public String getIncludeGambits() {
        return includeGambits;
    }

    public void setIncludeGambits(String includeGambits) {
        this.includeGambits = includeGambits;
    }

    public boolean determineIncludeBaseSet() {
        return determineBoolean(includeBaseSet);
    }

    public boolean determineIncludeColonyWars() {
        return determineBoolean(includeColonyWars);
    }

    public boolean determineIncludeYearOnePromos() {
        return determineBoolean(includeYearOnePromos);
    }

    public boolean determineIncludeCrisisBasesAndBattleships() {
        return determineBoolean(includeCrisisBasesAndBattleships);
    }

    public boolean determineIncludeCrisisEvents() {
        return determineBoolean(includeCrisisEvents);
    }

    public boolean determineIncludeCrisisFleetsAndFortresses() {
        return determineBoolean(includeCrisisFleetsAndFortresses);
    }

    public boolean determineIncludeCrisisHeroes() {
        return determineBoolean(includeCrisisHeroes);
    }

    public boolean determineIncludeGambits() {
        return determineBoolean(includeGambits);
    }

    private boolean determineBoolean(String stringValue) {
        return "Y".equalsIgnoreCase(stringValue) || stringValue.equals("R") && getRandomBoolean();
    }

    public boolean getRandomBoolean() {
        Random random = new Random();
        return random.nextBoolean();
    }
}
