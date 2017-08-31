package org.smartreaction.starrealms.model.cards.scenarios;

public abstract class Scenario {

    private final String name;

    private String text;

    public Scenario(String name, String text) {
        this.name = name;
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public void addText(String extraText) {
        text += extraText;
    }
}
