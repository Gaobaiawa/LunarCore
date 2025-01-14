package emu.lunarcore.game.rogue;

import java.util.ArrayList;
import java.util.List;

import emu.lunarcore.data.GameDepot;
import emu.lunarcore.data.excel.RogueBuffExcel;
import emu.lunarcore.proto.RogueBuffSelectInfoOuterClass.RogueBuffSelectInfo;
import emu.lunarcore.util.WeightedList;
import lombok.Getter;

@Getter
public class RogueBuffSelectMenu {
    private transient RogueInstance rogue;
    
    private int maxBuffs;
    private int rerolls;
    private int maxRerolls;
    private List<RogueBuffData> buffs;
    
    // Cache
    private transient WeightedList<RogueBuffExcel> randomBuffs;
    
    @Deprecated // Morphia only!
    public RogueBuffSelectMenu() {}
    
    public RogueBuffSelectMenu(RogueInstance rogue) {
        this.rogue = rogue;
        this.maxBuffs = 3;
        this.maxRerolls = rogue.getBaseRerolls();
        this.buffs = new ArrayList<>();
        
        this.generateRandomBuffs();
    }
    
    public void setMaxRerolls(int i) {
        this.maxBuffs = i;
    }
    
    public void reroll() {
        this.generateRandomBuffs();
        this.rerolls++;
    }
    
    public boolean hasRerolls() {
        return this.maxRerolls > this.rerolls;
    }
    
    private void generateRandomBuffs() {
        if (this.randomBuffs == null) {
            this.randomBuffs = new WeightedList<>();
            
            for (var excel : GameDepot.getRogueRandomBuffList()) {
                if (rogue.getBuffs().containsKey(excel.getMazeBuffID())) {
                    continue;
                }
                
                // Calculate buff weights
                double weight = 10.0 / excel.getRogueBuffRarity();
                
                if (this.getRogue().getAeonBuffType() == excel.getRogueBuffType()) {
                    weight *= 2;
                }
                
                this.randomBuffs.add(weight, excel);
            };
        }
        
        this.getBuffs().clear();
        
        while (this.getBuffs().size() < this.getMaxBuffs()) {
            var excel = this.randomBuffs.next();
            this.getBuffs().add(new RogueBuffData(excel.getMazeBuffID(), 1));
        }
    }
    
    protected void onLoad(RogueInstance rogue) {
        this.rogue = rogue;
    }
    
    public RogueBuffSelectInfo toProto() {
        var proto = RogueBuffSelectInfo.newInstance();
        
        if (this.getMaxRerolls() > 0) {
            proto.setCanRoll(true);
            proto.setRollBuffTimes(this.getRerolls());
            proto.setRollBuffMaxTimes(this.getMaxRerolls());
        }
        
        for (var buff : this.getBuffs()) {
            proto.addMazeBuffList(buff.toProto());
        }
        
        proto.getMutableRollBuffsCost();
        
        return proto;
    }
}
