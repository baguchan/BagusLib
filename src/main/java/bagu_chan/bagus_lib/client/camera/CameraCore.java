package bagu_chan.bagus_lib.client.camera;

import bagu_chan.bagus_lib.BagusLib;
import bagu_chan.bagus_lib.client.camera.holder.CameraHolder;
import bagu_chan.bagus_lib.client.camera.holder.EntityCameraHolder;
import bagu_chan.bagus_lib.message.CameraMessage;
import bagu_chan.bagus_lib.message.EntityCameraMessage;
import com.google.common.collect.Lists;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

@Mod.EventBusSubscriber(modid = BagusLib.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CameraCore {
    public static List<CameraHolder> cameraHolderList = Lists.newArrayList();

    @SubscribeEvent
    public static void cameraEvent(ViewportEvent.ComputeCameraAngles event) {
        for (int i = 0; i < cameraHolderList.size(); i++) {
            CameraHolder cameraHolder = cameraHolderList.get(i);
            if (cameraHolder.getDuration() <= cameraHolder.time) {
                cameraHolderList.remove(cameraHolder);
            } else {
                cameraHolder.tick(event);
            }

        }
    }

    public static List<CameraHolder> getCameraHolderList() {
        return cameraHolderList;
    }

    public static void addCameraHolderList(Level level, CameraHolder cameraHolder) {
        if (level.isClientSide()) {
            if (level.dimension() == cameraHolder.getPos().dimension()) {
                cameraHolderList.add(cameraHolder);
            }
        } else if (!level.isClientSide()) {
            for (Player player : level.players()) {
                if (player instanceof ServerPlayer serverPlayer) {
                    if (cameraHolder instanceof EntityCameraHolder<?> entityCameraHolder) {
                        PacketDistributor.PLAYER.with(serverPlayer).send(new EntityCameraMessage(entityCameraHolder.getEntity().getId(), cameraHolder.distance, cameraHolder.duration, cameraHolder.amount, cameraHolder.getPos()));
                    } else {
                        PacketDistributor.PLAYER.with(serverPlayer).send(new CameraMessage(cameraHolder.distance, cameraHolder.duration, cameraHolder.amount, cameraHolder.getPos()));
                    }
                }
            }
        }
    }
}
