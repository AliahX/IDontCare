package com.unjust1ce.idontcare.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.MultilineText;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.pack.PackListWidget;
import net.minecraft.client.gui.screen.pack.ResourcePackOrganizer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.resource.ResourcePackCompatibility;
import net.minecraft.text.OrderedText;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.unjust1ce.idontcare.config.ModConfig;

@Mixin(PackListWidget.ResourcePackEntry.class)
abstract class PackListWidgetRenderMixin {
    private static final Identifier RESOURCE_PACKS_TEXTURE = new Identifier("textures/gui/resource_packs.png");
    @Shadow @Final private ResourcePackOrganizer.Pack pack;
    @Shadow @Final private OrderedText displayName;
    @Shadow @Final private MultilineText description;

    @Shadow protected abstract boolean isSelectable();

    @Shadow @Final protected MinecraftClient client;
    @Shadow @Final private OrderedText incompatibleText;
    @Shadow @Final private MultilineText compatibilityNotificationText;
    ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

    @Inject(at = @At("HEAD"), method = "render", cancellable = true)
    public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta, CallbackInfo ci) {
        ResourcePackCompatibility resourcePackCompatibility = this.pack.getCompatibility();
        if(config.displayRedBackground && !resourcePackCompatibility.isCompatible()) {
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            DrawableHelper.fill(matrices, x - 1, y - 1, x + entryWidth - 9, y + entryHeight + 1, -8978432);
        }
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, this.pack.getIconId());
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        DrawableHelper.drawTexture(matrices, x, y, 0.0f, 0.0f, 32, 32, 32, 32);
        OrderedText orderedText = this.displayName;
        MultilineText multilineText = this.description;
        if (this.isSelectable() && (this.client.options.touchscreen || hovered)) {
            RenderSystem.setShaderTexture(0, RESOURCE_PACKS_TEXTURE);
            DrawableHelper.fill(matrices, x, y, x + 32, y + 32, -1601138544);
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            int i = mouseX - x;
            int j = mouseY - y;
            if (config.displayWarningText) {
                if (!this.pack.getCompatibility().isCompatible()) {
                    orderedText = this.incompatibleText;
                }
            }
            if(config.displayWarningDescription) {
                if (!this.pack.getCompatibility().isCompatible()) {
                    multilineText = this.compatibilityNotificationText;
                }

            }

            if (this.pack.canBeEnabled()) {
                if (i < 32) {
                    DrawableHelper.drawTexture(matrices, x, y, 0.0f, 32.0f, 32, 32, 256, 256);
                } else {
                    DrawableHelper.drawTexture(matrices, x, y, 0.0f, 0.0f, 32, 32, 256, 256);
                }
            } else {
                if (this.pack.canBeDisabled()) {
                    if (i < 16) {
                        DrawableHelper.drawTexture(matrices, x, y, 32.0f, 32.0f, 32, 32, 256, 256);
                    } else {
                        DrawableHelper.drawTexture(matrices, x, y, 32.0f, 0.0f, 32, 32, 256, 256);
                    }
                }
                if (this.pack.canMoveTowardStart()) {
                    if (i < 32 && i > 16 && j < 16) {
                        DrawableHelper.drawTexture(matrices, x, y, 96.0f, 32.0f, 32, 32, 256, 256);
                    } else {
                        DrawableHelper.drawTexture(matrices, x, y, 96.0f, 0.0f, 32, 32, 256, 256);
                    }
                }
                if (this.pack.canMoveTowardEnd()) {
                    if (i < 32 && i > 16 && j > 16) {
                        DrawableHelper.drawTexture(matrices, x, y, 64.0f, 32.0f, 32, 32, 256, 256);
                    } else {
                        DrawableHelper.drawTexture(matrices, x, y, 64.0f, 0.0f, 32, 32, 256, 256);
                    }
                }
            }
        }
        this.client.textRenderer.drawWithShadow(matrices, orderedText, (float)(x + 32 + 2), (float)(y + 1), 0xFFFFFF);
        multilineText.drawWithShadow(matrices, x + 32 + 2, y + 12, 10, 0x808080);
        ci.cancel();
    }

    @Redirect(method = "mouseClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/ResourcePackCompatibility;isCompatible()Z"))
    private boolean isCompatible(ResourcePackCompatibility resourcePackCompatibility) {
            return (!config.incompatibleConfirmation || resourcePackCompatibility.isCompatible());
    }
}
