package com.jigokusaru.cobbledresearch.mixin;

import com.cobblemon.mod.common.client.gui.pc.IconButton;
import com.cobblemon.mod.common.client.gui.pc.PCGUI;
import com.cobblemon.mod.common.client.gui.pc.StorageWidget;
import com.cobblemon.mod.common.client.gui.pc.StorageSlot;
import com.cobblemon.mod.common.client.gui.pc.BoxStorageSlot;
import com.cobblemon.mod.common.client.gui.pc.PartyStorageSlot;
import com.cobblemon.mod.common.api.storage.pc.PCPosition;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.CobblemonSounds;
import com.cobblemon.mod.common.net.messages.server.storage.pc.MovePCPokemonPacket;
import com.jigokusaru.cobbledresearch.util.PcAddonHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mixin(value = PCGUI.class, remap = false)
public abstract class PCGUIMixin extends Screen {

    @Unique private static final ResourceLocation MULTI_SELECT_ICON = ResourceLocation.fromNamespaceAndPath("cobblemon", "textures/gui/pc/pc_icon_options.png");
    @Unique private static final ResourceLocation SELECTION_ARROW = ResourceLocation.fromNamespaceAndPath("cobblemon", "textures/gui/pokedex/arrow_down.png");

    @Unique private IconButton multiSelectBtn;
    @Unique private double dragAnchorX;
    @Unique private double dragAnchorY;
    @Unique private boolean isInitialHold = false;

    protected PCGUIMixin(Component title) { super(title); }

    @Inject(method = "init", at = @At("TAIL"))
    private void onInit(CallbackInfo ci) {
        PcAddonHandler.clear();
        int x = (this.width - 349) / 2;
        int y = (this.height - 205) / 2;
        this.multiSelectBtn = new IconButton(x + 228, y + 31, 20, 20, MULTI_SELECT_ICON, null, "ui.multi_select.tooltip", "multi_select", (button) -> {
            PcAddonHandler.toggleMultiSelect();
            if (button instanceof IconButton iconBtn) iconBtn.setHighlighted(PcAddonHandler.isMultiSelectActive());
        });
        this.addRenderableWidget(multiSelectBtn);
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void onRenderUpdate(GuiGraphics context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (this.multiSelectBtn != null) {
            PCGUI gui = (PCGUI) (Object) this;
            this.multiSelectBtn.visible = gui.getDisplayOptions();
            this.multiSelectBtn.setHighlighted(PcAddonHandler.isMultiSelectActive());

            if (PcAddonHandler.isMultiSelectActive() && this.multiSelectBtn.visible) {
                StorageWidgetAccessor accessor = (StorageWidgetAccessor) (Object) gui.getStorage();

                if (isInitialHold && !PcAddonHandler.isDragging() && !Screen.hasShiftDown()) {
                    if (Math.abs(mouseX - dragAnchorX) > 2 || Math.abs(mouseY - dragAnchorY) > 2) {
                        PcAddonHandler.setDragging(true);
                        gui.playSound(CobblemonSounds.PC_GRAB);
                        accessor.setGrabbedSlot(null);
                    }
                }

                if (!PcAddonHandler.isDragging()) {
                    renderArrows(context, accessor);
                } else {
                    renderFormation(context, gui, accessor, mouseX, mouseY, delta);
                }
            }
        }
    }

    @Unique
    private void renderArrows(GuiGraphics context, StorageWidgetAccessor accessor) {
        List<StorageSlot> allSlots = new ArrayList<>();
        allSlots.addAll(accessor.getBoxSlots());
        allSlots.addAll(accessor.getPartySlots());
        float bounce = Mth.sin((System.currentTimeMillis() % 1000L / 1000.0F) * (float)Math.PI * 2.0F) * 2.0F;
        for (StorageSlot slot : allSlots) {
            if (slot.getPokemon() != null && PcAddonHandler.getSelectedPokemon().containsKey(slot.getPokemon().getUuid())) {
                context.blit(SELECTION_ARROW, slot.getX() + 8, slot.getY() - 10 + (int)bounce, 0, 0, 8, 8, 8, 16);
            }
        }
    }

    @Unique
    private void renderFormation(GuiGraphics context, PCGUI gui, StorageWidgetAccessor accessor, int mouseX, int mouseY, float delta) {
        double deltaX = mouseX - dragAnchorX;
        double deltaY = mouseY - dragAnchorY;
        List<StorageSlot> allSlots = new ArrayList<>();
        allSlots.addAll(accessor.getBoxSlots());
        allSlots.addAll(accessor.getPartySlots());
        for (StorageSlot slot : allSlots) {
            Pokemon p = slot.getPokemon();
            if (p != null && PcAddonHandler.getSelectedPokemon().containsKey(p.getUuid())) {
                int rx = (int) (slot.getX() + deltaX);
                int ry = (int) (slot.getY() + deltaY);
                slot.renderSlot(context, rx, ry, delta);
                context.pose().pushPose();
                context.pose().translate(0, 0, 800);
                context.blit(SELECTION_ARROW, rx + 8, ry - 12, 0, 0, 8, 8, 8, 16);
                context.pose().popPose();
            }
        }
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void onMouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (PcAddonHandler.isMultiSelectActive()) {
            PCGUI gui = (PCGUI) (Object) this;
            StorageWidgetAccessor accessor = (StorageWidgetAccessor) (Object) gui.getStorage();

            int guiLeft = (this.width - 349) / 2;
            if (mouseX > guiLeft + 180) return;

            if (PcAddonHandler.isDragging()) {
                for (StorageSlot slot : accessor.getBoxSlots()) {
                    if (slot.isHovered((int)mouseX, (int)mouseY) && slot.getPokemon() == null) {
                        moveSelectionTo(gui, accessor);
                        PcAddonHandler.setDragging(false);
                        cir.setReturnValue(true);
                        return;
                    }
                }
                PcAddonHandler.setDragging(false);
                return;
            }

            // Corrected: Loop through ALL slots (Box + Party) for selection
            List<StorageSlot> allSlots = new ArrayList<>();
            allSlots.addAll(accessor.getBoxSlots());
            allSlots.addAll(accessor.getPartySlots());

            for (StorageSlot slot : allSlots) {
                if (slot.isHovered((int)mouseX, (int)mouseY) && slot.getPokemon() != null) {
                    Pokemon p = slot.getPokemon();
                    if (Screen.hasShiftDown()) {
                        String containerId = slot instanceof PartyStorageSlot ? "party" : "box_" + gui.getStorage().getBox();
                        PcAddonHandler.toggleSelection(p, containerId);
                        gui.playSound(CobblemonSounds.PC_CLICK);
                    } else if (PcAddonHandler.getSelectedPokemon().containsKey(p.getUuid())) {
                        this.isInitialHold = true;
                        this.dragAnchorX = mouseX;
                        this.dragAnchorY = mouseY;
                    }
                    cir.setReturnValue(true);
                    return;
                }
            }
        }
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.isInitialHold = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Unique
    private void moveSelectionTo(PCGUI gui, StorageWidgetAccessor accessor) {
        List<UUID> selection = new ArrayList<>(PcAddonHandler.getSelectedPokemon().keySet());
        int box = gui.getStorage().getBox();
        int selectionIndex = 0;
        for (BoxStorageSlot slot : accessor.getBoxSlots()) {
            if (slot.getPokemon() == null && selectionIndex < selection.size()) {
                new MovePCPokemonPacket(selection.get(selectionIndex), new PCPosition(box, -1), new PCPosition(box, slot.getPosition().getSlot())).sendToServer();
                selectionIndex++;
            }
        }
        gui.playSound(CobblemonSounds.PC_DROP);
        PcAddonHandler.clear();
    }

    @Override
    public void removed() { super.removed(); PcAddonHandler.clear(); }
}