package sk.uniba.fmph.dcs.terra_futura;

import java.util.List;
import sk.uniba.fmph.dcs.terra_futura.enums.Resource;
import sk.uniba.fmph.dcs.terra_futura.interfaces.Effect;

public class Card {
    private List<Resource> resources;
    private int pollutionSpaces;
    private Effect upperEffect;
    private Effect lowerEffect;

    public boolean canGetResources(List<Resource> resources) {
        return false;
    }

    public void getResources(List<Resource> resources) {
    }

    public boolean canPutResources(List<Resource> resources) {
        return false;
    }

    public void putResources(List<Resource> resources) {
    }

    public boolean check(List<Resource> input, List<Resource> output, int pollution) {
        return false;
    }

    public boolean checkLower(List<Resource> input, List<Resource> output, int pollution) {
        return false;
    }

    public boolean hasAssistance() {
        return false;
    }

    public String state() {
        return "Card state";
    }

    public List<Resource> getResources() {
        return resources;
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }

    public int getPollutionSpaces() {
        return pollutionSpaces;
    }

    public void setPollutionSpaces(int pollutionSpaces) {
        this.pollutionSpaces = pollutionSpaces;
    }

    public Effect getUpperEffect() {
        return upperEffect;

    }

    public void setUpperEffect(Effect upperEffect) {
        this.upperEffect = upperEffect;
    }

    public Effect getLowerEffect() {
        return lowerEffect;
    }

    public void setLowerEffect(Effect lowerEffect) {
        this.lowerEffect = lowerEffect;
    }
}
