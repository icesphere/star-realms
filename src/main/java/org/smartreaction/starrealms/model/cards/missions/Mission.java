package org.smartreaction.starrealms.model.cards.missions;

import org.smartreaction.starrealms.model.players.Player;

public abstract class Mission {
    protected String name;

    protected String objectiveText;

    protected String rewardText;

    protected boolean missionClaimed;

    public abstract boolean isMissionCompleted(Player player);

    public abstract void onMissionClaimed(Player player);

    public Mission copyMissionForSimulation() {
        Mission mission;
        try {
            mission = this.getClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Unable to copy mission");
        }

        mission.setName(getName());
        mission.setObjectiveText(getObjectiveText());
        mission.setRewardText(getRewardText());
        mission.setMissionClaimed(isMissionClaimed());

        return mission;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getObjectiveText() {
        return objectiveText;
    }

    public void setObjectiveText(String objectiveText) {
        this.objectiveText = objectiveText;
    }

    public String getRewardText() {
        return rewardText;
    }

    public void setRewardText(String rewardText) {
        this.rewardText = rewardText;
    }

    public boolean isMissionClaimed() {
        return missionClaimed;
    }

    public void setMissionClaimed(boolean missionClaimed) {
        this.missionClaimed = missionClaimed;
    }
}
