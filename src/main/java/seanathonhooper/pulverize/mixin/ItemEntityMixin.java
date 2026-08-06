package seanathonhooper.pulverize.mixin;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import seanathonhooper.pulverize.Pulverize;


@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {

    @Unique
    private boolean isTransmuted = false;

    @Inject(method = "tick", at =@At("TAIL"))
        private void detectDripstoneBlock(CallbackInfo ci) {
            ItemEntity itemEntity = (ItemEntity) (Object) this;

            if (itemEntity.level().isClientSide()) return;


            if (!itemEntity.getItem().is(Items.STONE) && !itemEntity.getItem().is(Items.GRAVEL) && !itemEntity.getItem().is(Items.COBBLESTONE)) return;

            Pulverize.LOGGER.info("Survived item type check");

            BlockState state = itemEntity.level().getBlockState(itemEntity.getOnPos());

            if (state.is(Blocks.POINTED_DRIPSTONE) && !isTransmuted) {
                ItemStack oldStack = itemEntity.getItem();

                if (itemEntity.getItem().is(Items.STONE) || itemEntity.getItem().is(Items.COBBLESTONE)) {
                    ItemStack newStack = new ItemStack(Items.GRAVEL, oldStack.getCount());
                    itemEntity.setItem(newStack);
                }
                else if (itemEntity.getItem().is(Items.GRAVEL)){
                    ItemStack newStack = new ItemStack(Items.SAND, oldStack.getCount());
                    itemEntity.setItem(newStack);
                }

                isTransmuted = true;
            }
        }

    }

