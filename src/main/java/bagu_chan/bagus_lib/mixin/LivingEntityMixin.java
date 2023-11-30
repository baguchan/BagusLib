package bagu_chan.bagus_lib.mixin;

import bagu_chan.bagus_lib.api.IBaguData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements IBaguData {
    @Unique
    private CompoundTag data = new CompoundTag();

    public LivingEntityMixin(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    @Inject(at = @At("TAIL"), method = "addAdditionalSaveData")
    protected void addAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci) {
        CompoundTag data = this.getData();
        if (data != null) {
            compoundTag.put("BaguData", data);
        }
    }

    @Inject(at = @At("TAIL"), method = "readAdditionalSaveData")
    protected void readAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci) {
        if (compoundTag.contains("BaguData")) {
            this.setData(compoundTag.getCompound("BaguData"));
        }
    }

    @Override
    public void setData(CompoundTag compoundTag) {
        this.data = compoundTag;
    }

    @Override
    public CompoundTag getData() {
        return data;
    }
}
