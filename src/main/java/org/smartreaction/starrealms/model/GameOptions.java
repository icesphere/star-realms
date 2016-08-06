package org.smartreaction.starrealms.model;

import java.util.Objects;

public class GameOptions {

    private boolean customGameOptions;

    private boolean includeBaseSet = true;

    private boolean includeColonyWars = false;

    private boolean includeYearOnePromos = false;

    private boolean includeCrisisBasesAndBattleships = false;

    private boolean includeCrisisEvents = false;

    private boolean includeCrisisFleetsAndFortresses = false;

    private boolean includeCrisisHeroes = false;

    private boolean includeUnitedVarious = false;

    private boolean includeUnitedShipsStationsAndPods = false;

    private boolean includeGambits = false;

    private String startingTradeRowCards = "";

    private boolean playAgainstComputer;

    public boolean isCustomGameOptions() {
        return customGameOptions;
    }

    public void setCustomGameOptions(boolean customGameOptions) {
        this.customGameOptions = customGameOptions;
    }

    public boolean isIncludeBaseSet() {
        return includeBaseSet;
    }

    public void setIncludeBaseSet(boolean includeBaseSet) {
        this.includeBaseSet = includeBaseSet;
    }

    public boolean isIncludeColonyWars() {
        return includeColonyWars;
    }

    public void setIncludeColonyWars(boolean includeColonyWars) {
        this.includeColonyWars = includeColonyWars;
    }

    public boolean isIncludeYearOnePromos() {
        return includeYearOnePromos;
    }

    public void setIncludeYearOnePromos(boolean includeYearOnePromos) {
        this.includeYearOnePromos = includeYearOnePromos;
    }

    public boolean isIncludeCrisisBasesAndBattleships() {
        return includeCrisisBasesAndBattleships;
    }

    public void setIncludeCrisisBasesAndBattleships(boolean includeCrisisBasesAndBattleships) {
        this.includeCrisisBasesAndBattleships = includeCrisisBasesAndBattleships;
    }

    public boolean isIncludeCrisisEvents() {
        return includeCrisisEvents;
    }

    public void setIncludeCrisisEvents(boolean includeCrisisEvents) {
        this.includeCrisisEvents = includeCrisisEvents;
    }

    public boolean isIncludeCrisisFleetsAndFortresses() {
        return includeCrisisFleetsAndFortresses;
    }

    public void setIncludeCrisisFleetsAndFortresses(boolean includeCrisisFleetsAndFortresses) {
        this.includeCrisisFleetsAndFortresses = includeCrisisFleetsAndFortresses;
    }

    public boolean isIncludeCrisisHeroes() {
        return includeCrisisHeroes;
    }

    public void setIncludeCrisisHeroes(boolean includeCrisisHeroes) {
        this.includeCrisisHeroes = includeCrisisHeroes;
    }

    public boolean isIncludeUnitedVarious() {
        return includeUnitedVarious;
    }

    public void setIncludeUnitedVarious(boolean includeUnitedVarious) {
        this.includeUnitedVarious = includeUnitedVarious;
    }

    public boolean isIncludeUnitedShipsStationsAndPods() {
        return includeUnitedShipsStationsAndPods;
    }

    public void setIncludeUnitedShipsStationsAndPods(boolean includeUnitedShipsStationsAndPods) {
        this.includeUnitedShipsStationsAndPods = includeUnitedShipsStationsAndPods;
    }

    public boolean isIncludeGambits() {
        return includeGambits;
    }

    public void setIncludeGambits(boolean includeGambits) {
        this.includeGambits = includeGambits;
    }

    public String getStartingTradeRowCards() {
        return startingTradeRowCards;
    }

    public void setStartingTradeRowCards(String startingTradeRowCards) {
        this.startingTradeRowCards = startingTradeRowCards;
    }

    public boolean isPlayAgainstComputer() {
        return playAgainstComputer;
    }

    public void setPlayAgainstComputer(boolean playAgainstComputer) {
        this.playAgainstComputer = playAgainstComputer;
    }

    @Override
    public int hashCode() {
        return Objects.hash(includeBaseSet, includeColonyWars, includeYearOnePromos, includeCrisisBasesAndBattleships,
                includeCrisisEvents, includeCrisisFleetsAndFortresses, includeCrisisHeroes, includeUnitedVarious,
                includeUnitedShipsStationsAndPods, includeGambits, startingTradeRowCards);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final GameOptions other = (GameOptions) obj;
        return Objects.equals(this.includeBaseSet, other.includeBaseSet)
                && Objects.equals(this.includeColonyWars, other.includeColonyWars)
                && Objects.equals(this.includeYearOnePromos, other.includeYearOnePromos)
                && Objects.equals(this.includeCrisisBasesAndBattleships, other.includeCrisisBasesAndBattleships)
                && Objects.equals(this.includeCrisisEvents, other.includeCrisisEvents)
                && Objects.equals(this.includeCrisisFleetsAndFortresses, other.includeCrisisFleetsAndFortresses)
                && Objects.equals(this.includeCrisisHeroes, other.includeCrisisHeroes)
                && Objects.equals(this.includeUnitedVarious, other.includeUnitedVarious)
                && Objects.equals(this.includeUnitedShipsStationsAndPods, other.includeUnitedShipsStationsAndPods)
                && Objects.equals(this.includeGambits, other.includeGambits)
                && Objects.equals(this.startingTradeRowCards, other.startingTradeRowCards);
    }
}
