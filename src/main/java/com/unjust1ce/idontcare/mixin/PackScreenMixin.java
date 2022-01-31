package com.unjust1ce.idontcare.mixin;

import com.unjust1ce.idontcare.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.pack.PackListWidget;
import net.minecraft.client.gui.screen.pack.PackScreen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PackScreen.class)
public abstract class PackScreenMixin extends Screen {
    @Shadow private PackListWidget availablePackList;
    @Shadow private PackListWidget selectedPackList;
    @Shadow @Final private static Text DROP_INFO;
    private static final Identifier BUTTON_TEXTURE = new Identifier("idontcare:textures/gui/buttons.png");
    ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

    protected PackScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = @At("RETURN"), method = "init")
    private void init(CallbackInfo ci) {
        this.addDrawableChild(new TexturedButtonWidget(9, 12, 20, 20, (config.displayRedBackground ? 20 : 0), 0, 20, BUTTON_TEXTURE, 80, 80, (button) -> {
            config.displayRedBackground = !config.displayRedBackground;
            this.init(ci);
        }));
        this.addDrawableChild(new TexturedButtonWidget(30, 12, 20, 20, 40 + (config.incompatibleConfirmation ? 20 : 0), 40, 20, BUTTON_TEXTURE, 80, 80, (button) -> {
            config.incompatibleConfirmation = !config.incompatibleConfirmation;
            this.init(ci);
        }));
        this.addDrawableChild(new TexturedButtonWidget(51, 12, 20, 20, 40 + (config.displayWarningText ? 20 : 0), 0, 20, BUTTON_TEXTURE, 80, 80, (button) -> {
            config.displayWarningText = !config.displayWarningText;
            this.init(ci);
        }));
        this.addDrawableChild(new TexturedButtonWidget(72, 12, 20, 20, (config.displayWarningDescription ? 20 : 0), 40, 20, BUTTON_TEXTURE, 80, 80, (button) -> {
            config.displayWarningDescription = !config.displayWarningDescription;
            this.init(ci);
        }));
    }




    @Inject(at = @At("HEAD"), method = "render", cancellable = true)
    private void render(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        this.renderBackgroundTexture(0);
        this.availablePackList.render(matrices, mouseX, mouseY, delta);
        this.selectedPackList.render(matrices, mouseX, mouseY, delta);
        PackScreen.drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 8, 0xFFFFFF);
        PackScreen.drawCenteredText(matrices, this.textRenderer, DROP_INFO, (this.width / 2) + 10, 20, 0xFFFFFF);
        super.render(matrices, mouseX, mouseY, delta);
        ci.cancel();
    }
}
