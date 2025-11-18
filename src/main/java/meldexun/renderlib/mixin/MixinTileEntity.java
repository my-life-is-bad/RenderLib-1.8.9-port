package meldexun.renderlib.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

@Mixin(TileEntity.class)
public class MixinTileEntity {

	@Shadow
	private BlockPos pos;
	@Shadow
	private World worldObj;

	@Overwrite(remap = false)
	public AxisAlignedBB getRenderBoundingBox() {
		BlockPos pos = this.pos;
		try {
			World theWorld = this.worldObj;
			return worldObj.getBlockState(pos).getBlock().getSelectedBoundingBox(worldObj, pos);//.offset(pos);
		} catch (Exception e) {
			//return new AxisAlignedBB(pos);
			return new AxisAlignedBB((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), (double)(pos.getX() + 1), (double)(pos.getY() + 1), (double)(pos.getZ() + 1));
		}
	}

}
