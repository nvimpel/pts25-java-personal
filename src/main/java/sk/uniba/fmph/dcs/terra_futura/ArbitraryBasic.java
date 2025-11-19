package sk.uniba.fmph.dcs.terra_futura;

import sk.uniba.fmph.dcs.terra_futura.enums.Resource;
import sk.uniba.fmph.dcs.terra_futura.interfaces.Effect;

import java.util.List;

class ArbitraryBasic implements Effect {
    private int from;
    private List<Resource> to;
    private int pollution;

    @Override
    public boolean check(List<Resource> input, List<Resource> output, int pollution) {
        return false;
    }

    @Override
    public boolean hasAssistance() {
        return false;
    }

    @Override
    public String state() {
        return "";
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public List<Resource> getTo() {
        return to;
    }

    public void setTo(List<Resource> to) {
        this.to = to;
    }

    public int getPollution() {
        return pollution;
    }

    public void setPollution(int pollution) {
        this.pollution = pollution;
    }
}
