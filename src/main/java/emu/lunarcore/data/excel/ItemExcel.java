package emu.lunarcore.data.excel;

import java.util.List;

import emu.lunarcore.data.GameResource;
import emu.lunarcore.data.ResourceType;
import emu.lunarcore.data.common.ItemParam;
import emu.lunarcore.game.enums.ItemMainType;
import emu.lunarcore.game.enums.ItemRarity;
import emu.lunarcore.game.enums.ItemSubType;
import lombok.Getter;
import lombok.Setter;

@Getter
@ResourceType(name = {"ItemConfig.json", "ItemConfigAvatar.json", "ItemConfigAvatarPlayerIcon.json", "ItemConfigAvatarRank.json",
        "ItemConfigBook.json", "ItemConfigDisk.json", "ItemConfigEquipment.json", "ItemConfigRelic.json", "ItemPlayerCard.json"})
public class ItemExcel extends GameResource {
    // General item data
    private int ID;
    private long ItemName;
    private ItemMainType ItemMainType;
    private ItemSubType ItemSubType;
    private ItemRarity Rarity;
    private int PileLimit;
    private int PurposeType;

    private List<ItemParam> ReturnItemIDList;

    // Transient cache
    @Setter private transient EquipmentExcel equipmentExcel;
    @Setter private transient RelicExcel relicExcel;

    @Setter private transient int avatarExp;
    @Setter private transient int relicExp;
    @Setter private transient int equipmentExp;
    @Setter private transient int expCost;

    @Override
    public int getId() {
        return ID;
    }

    public boolean isEquipment() {
        return ItemMainType == emu.lunarcore.game.enums.ItemMainType.Equipment && this.getEquipmentExcel() != null;
    }

    public boolean isRelic() {
        return ItemMainType == emu.lunarcore.game.enums.ItemMainType.Relic && this.getRelicExcel() != null;
    }

    public boolean isEquippable() {
        return ItemMainType == emu.lunarcore.game.enums.ItemMainType.Relic || ItemMainType == emu.lunarcore.game.enums.ItemMainType.Equipment;
    }

    public int getRelicExp() {
        if (this.relicExcel != null) {
            return this.relicExcel.getExpProvide();
        }
        return this.relicExp;
    }

    public int getRarityNum() {
        if (this.getRarity() == ItemRarity.Unknown) {
            return 0;
        }
        if (this.getRarity() == ItemRarity.Normal) {
            return 1;
        }
        if (this.getRarity() == ItemRarity.NotNormal) {
            return 2;
        }
        if (this.getRarity() == ItemRarity.Rare) {
            return 3;
        }
        if (this.getRarity() == ItemRarity.VeryRare) {
            return 4;
        }
        if (this.getRarity() == ItemRarity.SuperRare) {
            return 5;
        }
        return 0;
    }

    public int getRelicExpCost() {
        if (this.relicExcel != null) {
            return this.relicExcel.getCoinCost();
        }
        return this.expCost;
    }

    public int getEquipmentExp() {
        if (this.equipmentExcel != null) {
            return this.equipmentExcel.getExpProvide();
        }
        return this.equipmentExp;
    }

    public int getEquipmentExpCost() {
        if (this.equipmentExcel != null) {
            return this.equipmentExcel.getCoinCost();
        }
        return this.expCost;
    }

    public int getEquipSlot() {
        if (this.getRelicExcel() != null) {
            return this.getRelicExcel().getType().getVal();
        } else if (this.getEquipmentExcel() != null) {
            return 100;
        }
        return 0;
    }
}
