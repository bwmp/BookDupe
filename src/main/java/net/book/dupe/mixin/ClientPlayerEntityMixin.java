package net.book.dupe.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.BookUpdateC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ ClientPlayerEntity.class })
public abstract class ClientPlayerEntityMixin
{
    @Shadow
    private MinecraftClient client;

    @Inject(at = { @At("HEAD") }, method = { "sendChatMessage" }, cancellable = true)
    public void onChatMessage(final String message, final CallbackInfo ci) {
        if (message.equals(".RR")) {
            final ItemStack itemStack = new ItemStack((ItemConvertible)Items.WRITABLE_BOOK, 1);
            final ListTag pages = new ListTag();
            pages.addTag(0, (Tag)StringTag.of("DUPE"));
            itemStack.putSubTag("pages", (Tag)pages);
            itemStack.putSubTag("title", (Tag)StringTag.of("Get Rick Rolled"));
            client.getNetworkHandler().sendPacket((Packet)new BookUpdateC2SPacket(itemStack, true, ((ClientPlayerEntity) (Object) this).inventory.selectedSlot));
            ci.cancel();
        }
    }
}