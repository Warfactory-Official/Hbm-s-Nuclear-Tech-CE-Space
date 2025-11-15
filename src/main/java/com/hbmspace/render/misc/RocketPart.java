package com.hbmspace.render.misc;

import com.hbm.items.ModItems;
import com.hbm.items.weapon.ItemMissile;
import com.hbm.main.ResourceManager;
import com.hbm.render.loader.IModelCustom;
import com.hbm.render.misc.MissilePart;
import com.hbmspace.items.ModItemsSpace;
import com.hbmspace.main.ResourceManagerSpace;
import com.hbmspace.render.entity.rocket.part.RenderDropPod;
import com.hbmspace.render.entity.rocket.part.RenderRocketPart;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;

public class RocketPart extends MissilePart {
    public static HashMap<Integer, MissilePart> parts = new HashMap<>();

    public RenderRocketPart renderer;

    private RocketPart(Item item, ItemMissile.PartType type, double height, double guiheight, IModelCustom model, ResourceLocation texture) {
        super(item, type, height, guiheight, model, texture);
    }

    private RocketPart withRenderer(RenderRocketPart renderer) {
        this.renderer = renderer;
        return this;
    }

    private RocketPart withDeployed(IModelCustom deployedModel) {
        this.deployedModel = deployedModel;
        return this;
    }

    private RocketPart withShroud(IModelCustom shroudModel) {
        this.shroudModel = shroudModel;
        return this;
    }

    public static void registerAllParts() {
        parts.clear();

        registerPart(ModItemsSpace.rp_capsule_20, ItemMissile.PartType.WARHEAD, 3.5, 2.25, ResourceManagerSpace.soyuz_lander_neo, ResourceManagerSpace.module_lander_tex);
        registerPart(ModItemsSpace.rp_station_core_20, ItemMissile.PartType.WARHEAD, 7, 5, ResourceManagerSpace.mp_w_fairing, ResourceManagerSpace.mp_w_fairing_tex);
        registerPart(ModItemsSpace.rp_pod_20, ItemMissile.PartType.WARHEAD, 3.0, 2.25, ResourceManagerSpace.drop_pod, ResourceManagerSpace.drop_pod_tex).withRenderer(new RenderDropPod());

        // okay you know what, fuck this
        // probably could do it via interface or something, but whatever
        // CLEAN CODE IS NOT MY* SOLUTION

        registerPart(ModItems.mp_thruster_10_kerosene, ItemMissile.PartType.THRUSTER, 1, 1, ResourceManager.mp_t_10_kerosene, ResourceManager.mp_t_10_kerosene_tex);
        registerPart(ModItems.mp_thruster_10_solid, ItemMissile.PartType.THRUSTER, 0.5, 1, ResourceManager.mp_t_10_solid, ResourceManager.mp_t_10_solid_tex);
        //registerPart(ModItems.mp_thruster_10_hydrazine, PartType.THRUSTER, 0.5, 1, ResourceManager.mp_t_10_solid, ResourceManager.mp_t_10_solid_tex);
        registerPart(ModItems.mp_thruster_10_xenon, ItemMissile.PartType.THRUSTER, 0.5, 1, ResourceManager.mp_t_10_xenon, ResourceManager.mp_t_10_xenon_tex);
        //registerPart(ModItems.mp_thruster_10_hydrazine, PartType.THRUSTER, 0.5, 1, ResourceManager.mp_t_10_solid, ResourceManager.mp_t_10_solid_tex);

        //
        registerPart(ModItems.mp_thruster_15_kerosene, ItemMissile.PartType.THRUSTER, 1.5, 1.5, ResourceManager.mp_t_15_kerosene, ResourceManager.mp_t_15_kerosene_tex);
        registerPart(ModItems.mp_thruster_15_kerosene_dual, ItemMissile.PartType.THRUSTER, 1, 1.5, ResourceManager.mp_t_15_kerosene_dual, ResourceManager.mp_t_15_kerosene_dual_tex);
        registerPart(ModItems.mp_thruster_15_kerosene_triple, ItemMissile.PartType.THRUSTER, 1, 1.5, ResourceManager.mp_t_15_kerosene_triple, ResourceManager.mp_t_15_kerosene_dual_tex);
        registerPart(ModItems.mp_thruster_15_solid, ItemMissile.PartType.THRUSTER, 0.5, 1, ResourceManager.mp_t_15_solid, ResourceManager.mp_t_15_solid_tex);
        registerPart(ModItems.mp_thruster_15_solid_hexdecuple, ItemMissile.PartType.THRUSTER, 0.5, 1, ResourceManager.mp_t_15_solid_hexdecuple, ResourceManager.mp_t_15_solid_hexdecuple_tex);
        registerPart(ModItems.mp_thruster_15_hydrogen, ItemMissile.PartType.THRUSTER, 1.5, 1.5, ResourceManager.mp_t_15_kerosene, ResourceManager.mp_t_15_hydrogen_tex);
        registerPart(ModItems.mp_thruster_15_hydrogen_dual, ItemMissile.PartType.THRUSTER, 1, 1.5, ResourceManager.mp_t_15_kerosene_dual, ResourceManager.mp_t_15_hydrogen_dual_tex);
        registerPart(ModItems.mp_thruster_15_balefire_short, ItemMissile.PartType.THRUSTER, 2, 2, ResourceManager.mp_t_15_balefire_short, ResourceManager.mp_t_15_balefire_short_tex);
        registerPart(ModItems.mp_thruster_15_balefire, ItemMissile.PartType.THRUSTER, 3, 2.5, ResourceManager.mp_t_15_balefire, ResourceManager.mp_t_15_balefire_tex);
        registerPart(ModItems.mp_thruster_15_balefire_large, ItemMissile.PartType.THRUSTER, 3, 2.5, ResourceManager.mp_t_15_balefire_large, ResourceManager.mp_t_15_balefire_large_tex);
        registerPart(ModItems.mp_thruster_15_balefire_large_rad, ItemMissile.PartType.THRUSTER, 3, 2.5, ResourceManager.mp_t_15_balefire_large, ResourceManager.mp_t_15_balefire_large_rad_tex);
        //
        registerPart(ModItems.mp_thruster_20_kerosene, ItemMissile.PartType.THRUSTER, 2, 2, ResourceManager.mp_t_20_kerosene, ResourceManager.mp_t_20_kerosene_tex);
        registerPart(ModItems.mp_thruster_20_kerosene_dual, ItemMissile.PartType.THRUSTER, 2, 2, ResourceManager.mp_t_20_kerosene_dual, ResourceManager.mp_t_20_kerosene_dual_tex);
        registerPart(ModItems.mp_thruster_20_kerosene_triple, ItemMissile.PartType.THRUSTER, 2, 2, ResourceManager.mp_t_20_kerosene_triple, ResourceManager.mp_t_20_kerosene_dual_tex);
        //registerPart(ModItems.mp_thruster_20_methalox, PartType.THRUSTER, 2, 2, ResourceManager.mp_t_20_kerosene, ResourceManager.mp_t_20_methalox_tex);
        //registerPart(ModItems.mp_thruster_20_methalox_dual, PartType.THRUSTER, 2, 2, ResourceManager.mp_t_20_kerosene_dual, ResourceManager.mp_t_20_methalox_dual_tex);
        //registerPart(ModItems.mp_thruster_20_methalox_triple, PartType.THRUSTER, 2, 2, ResourceManager.mp_t_20_kerosene_triple, ResourceManager.mp_t_20_methalox_dual_tex);
        //registerPart(ModItems.mp_thruster_20_hydrogen, PartType.THRUSTER, 2, 2, ResourceManager.mp_t_20_kerosene, ResourceManager.mp_t_20_hydrogen_tex);
        //registerPart(ModItems.mp_thruster_20_hydrogen_dual, PartType.THRUSTER, 2, 2, ResourceManager.mp_t_20_kerosene_dual, ResourceManager.mp_t_20_hydrogen_dual_tex);
        //registerPart(ModItems.mp_thruster_20_hydrogen_triple, PartType.THRUSTER, 2, 2, ResourceManager.mp_t_20_kerosene_triple, ResourceManager.mp_t_20_hydrogen_dual_tex);
        registerPart(ModItems.mp_thruster_20_solid, ItemMissile.PartType.THRUSTER, 1, 1.75, ResourceManager.mp_t_20_solid, ResourceManager.mp_t_20_solid_tex);
        registerPart(ModItems.mp_thruster_20_solid_multi, ItemMissile.PartType.THRUSTER, 0.5, 1.5, ResourceManager.mp_t_20_solid_multi, ResourceManager.mp_t_20_solid_multi_tex);
        registerPart(ModItems.mp_thruster_20_solid_multier, ItemMissile.PartType.THRUSTER, 0.5, 1.5, ResourceManager.mp_t_20_solid_multi, ResourceManager.mp_t_20_solid_multier_tex);
        //registerPart(ModItems.mp_thruster_20_hydrazine, PartType.THRUSTER, 3, 2.5, ResourceManager.mp_t_20_azide, ResourceManager.mp_t_20_azide_tex);

        //////

        registerPart(ModItems.mp_stability_10_flat, ItemMissile.PartType.FINS, 0, 2, ResourceManager.mp_s_10_flat, ResourceManager.mp_s_10_flat_tex);
        registerPart(ModItems.mp_stability_10_cruise, ItemMissile.PartType.FINS, 0, 3, ResourceManager.mp_s_10_cruise, ResourceManager.mp_s_10_cruise_tex);
        registerPart(ModItems.mp_stability_10_space, ItemMissile.PartType.FINS, 0, 2, ResourceManager.mp_s_10_space, ResourceManager.mp_s_10_space_tex);
        //
        registerPart(ModItems.mp_stability_15_flat, ItemMissile.PartType.FINS, 0, 3, ResourceManager.mp_s_15_flat, ResourceManager.mp_s_15_flat_tex);
        registerPart(ModItems.mp_stability_15_thin, ItemMissile.PartType.FINS, 0, 3, ResourceManager.mp_s_15_thin, ResourceManager.mp_s_15_thin_tex);
        registerPart(ModItems.mp_stability_15_soyuz, ItemMissile.PartType.FINS, 0, 3, ResourceManager.mp_s_15_soyuz, ResourceManager.mp_s_15_soyuz_tex);
        //
        // TODO move rp_legs_20 to space
        registerPart(ModItemsSpace.rp_legs_20, ItemMissile.PartType.FINS, 2.4, 3, ResourceManagerSpace.rp_s_20_leggy, ResourceManager.universal).withDeployed(ResourceManagerSpace.rp_s_20_leggy_deployed);

        //////

        registerPart(ModItems.mp_fuselage_10_kerosene, ItemMissile.PartType.FUSELAGE, 4, 3, ResourceManager.mp_f_10_kerosene, ResourceManager.mp_f_10_kerosene_tex);
        registerPart(ModItems.mp_fuselage_10_kerosene_camo, ItemMissile.PartType.FUSELAGE, 4, 3, ResourceManager.mp_f_10_kerosene, ResourceManager.mp_f_10_kerosene_camo_tex);
        registerPart(ModItems.mp_fuselage_10_kerosene_desert, ItemMissile.PartType.FUSELAGE, 4, 3, ResourceManager.mp_f_10_kerosene, ResourceManager.mp_f_10_kerosene_desert_tex);
        registerPart(ModItems.mp_fuselage_10_kerosene_sky, ItemMissile.PartType.FUSELAGE, 4, 3, ResourceManager.mp_f_10_kerosene, ResourceManager.mp_f_10_kerosene_sky_tex);
        registerPart(ModItems.mp_fuselage_10_kerosene_insulation, ItemMissile.PartType.FUSELAGE, 4, 3, ResourceManager.mp_f_10_kerosene, ResourceManager.mp_f_10_kerosene_insulation_tex);
        registerPart(ModItems.mp_fuselage_10_kerosene_flames, ItemMissile.PartType.FUSELAGE, 4, 3, ResourceManager.mp_f_10_kerosene, ResourceManager.mp_f_10_kerosene_flames_tex);
        registerPart(ModItems.mp_fuselage_10_kerosene_sleek, ItemMissile.PartType.FUSELAGE, 4, 3, ResourceManager.mp_f_10_kerosene, ResourceManager.mp_f_10_kerosene_sleek_tex);
        registerPart(ModItems.mp_fuselage_10_kerosene_metal, ItemMissile.PartType.FUSELAGE, 4, 3, ResourceManager.mp_f_10_kerosene, ResourceManager.mp_f_10_kerosene_metal_tex);
        registerPart(ModItems.mp_fuselage_10_kerosene_taint, ItemMissile.PartType.FUSELAGE, 4, 3, ResourceManager.mp_f_10_kerosene, ResourceManager.mp_f_10_kerosene_taint_tex);
        registerPart(ModItems.mp_fuselage_10_solid, ItemMissile.PartType.FUSELAGE, 4, 3, ResourceManager.mp_f_10_kerosene, ResourceManager.mp_f_10_solid_tex);
        //registerPart(ModItems.mp_fuselage_10_hydrazine, PartType.FUSELAGE, 4, 3, ResourceManager.mp_f_10_kerosene, ResourceManager.mp_f_10_hydrazine_tex);
        registerPart(ModItems.mp_fuselage_10_solid_flames, ItemMissile.PartType.FUSELAGE, 4, 3, ResourceManager.mp_f_10_kerosene, ResourceManager.mp_f_10_solid_flames_tex);
        registerPart(ModItems.mp_fuselage_10_solid_insulation, ItemMissile.PartType.FUSELAGE, 4, 3, ResourceManager.mp_f_10_kerosene, ResourceManager.mp_f_10_solid_insulation_tex);
        registerPart(ModItems.mp_fuselage_10_solid_sleek, ItemMissile.PartType.FUSELAGE, 4, 3, ResourceManager.mp_f_10_kerosene, ResourceManager.mp_f_10_solid_sleek_tex);
        registerPart(ModItems.mp_fuselage_10_solid_soviet_glory, ItemMissile.PartType.FUSELAGE, 4, 3, ResourceManager.mp_f_10_kerosene, ResourceManager.mp_f_10_solid_soviet_glory_tex);
        registerPart(ModItems.mp_fuselage_10_solid_cathedral, ItemMissile.PartType.FUSELAGE, 4, 3, ResourceManager.mp_f_10_kerosene, ResourceManager.mp_f_10_solid_cathedral_tex);
        registerPart(ModItems.mp_fuselage_10_solid_moonlit, ItemMissile.PartType.FUSELAGE, 4, 3, ResourceManager.mp_f_10_kerosene, ResourceManager.mp_f_10_solid_moonlit_tex);
        registerPart(ModItems.mp_fuselage_10_solid_battery, ItemMissile.PartType.FUSELAGE, 4, 3, ResourceManager.mp_f_10_kerosene, ResourceManager.mp_f_10_solid_battery_tex);
        registerPart(ModItems.mp_fuselage_10_solid_duracell, ItemMissile.PartType.FUSELAGE, 4, 3, ResourceManager.mp_f_10_kerosene, ResourceManager.mp_f_10_solid_duracell_tex);
        registerPart(ModItems.mp_fuselage_10_xenon, ItemMissile.PartType.FUSELAGE, 4, 3, ResourceManager.mp_f_10_kerosene, ResourceManager.mp_f_10_xenon_tex);
        registerPart(ModItems.mp_fuselage_10_xenon_bhole, ItemMissile.PartType.FUSELAGE, 4, 3, ResourceManager.mp_f_10_kerosene, ResourceManager.mp_f_10_xenon_bhole_tex);
        registerPart(ModItems.mp_fuselage_10_long_kerosene, ItemMissile.PartType.FUSELAGE, 7, 5, ResourceManager.mp_f_10_long_kerosene, ResourceManager.mp_f_10_long_kerosene_tex);
        registerPart(ModItems.mp_fuselage_10_long_kerosene_camo, ItemMissile.PartType.FUSELAGE, 7, 5, ResourceManager.mp_f_10_long_kerosene, ResourceManager.mp_f_10_long_kerosene_camo_tex);
        registerPart(ModItems.mp_fuselage_10_long_kerosene_desert, ItemMissile.PartType.FUSELAGE, 7, 5, ResourceManager.mp_f_10_long_kerosene, ResourceManager.mp_f_10_long_kerosene_desert_tex);
        registerPart(ModItems.mp_fuselage_10_long_kerosene_sky, ItemMissile.PartType.FUSELAGE, 7, 5, ResourceManager.mp_f_10_long_kerosene, ResourceManager.mp_f_10_long_kerosene_sky_tex);
        registerPart(ModItems.mp_fuselage_10_long_kerosene_flames, ItemMissile.PartType.FUSELAGE, 7, 5, ResourceManager.mp_f_10_long_kerosene, ResourceManager.mp_f_10_long_kerosene_flames_tex);
        registerPart(ModItems.mp_fuselage_10_long_kerosene_insulation, ItemMissile.PartType.FUSELAGE, 7, 5, ResourceManager.mp_f_10_long_kerosene, ResourceManager.mp_f_10_long_kerosene_insulation_tex);
        registerPart(ModItems.mp_fuselage_10_long_kerosene_sleek, ItemMissile.PartType.FUSELAGE, 7, 5, ResourceManager.mp_f_10_long_kerosene, ResourceManager.mp_f_10_long_kerosene_sleek_tex);
        registerPart(ModItems.mp_fuselage_10_long_kerosene_metal, ItemMissile.PartType.FUSELAGE, 7, 5, ResourceManager.mp_f_10_long_kerosene, ResourceManager.mp_f_10_long_kerosene_metal_tex);
        registerPart(ModItems.mp_fuselage_10_long_kerosene_dash, ItemMissile.PartType.FUSELAGE, 7, 5, ResourceManager.mp_f_10_long_kerosene, ResourceManager.mp_f_10_long_kerosene_dash_tex);
        registerPart(ModItems.mp_fuselage_10_long_kerosene_taint, ItemMissile.PartType.FUSELAGE, 7, 5, ResourceManager.mp_f_10_long_kerosene, ResourceManager.mp_f_10_long_kerosene_taint_tex);
        registerPart(ModItems.mp_fuselage_10_long_kerosene_vap, ItemMissile.PartType.FUSELAGE, 7, 5, ResourceManager.mp_f_10_long_kerosene, ResourceManager.mp_f_10_long_kerosene_vap_tex);
        registerPart(ModItems.mp_fuselage_10_long_solid, ItemMissile.PartType.FUSELAGE, 7, 5, ResourceManager.mp_f_10_long_kerosene, ResourceManager.mp_f_10_long_solid_tex);
        registerPart(ModItems.mp_fuselage_10_long_solid_flames, ItemMissile.PartType.FUSELAGE, 7, 5, ResourceManager.mp_f_10_long_kerosene, ResourceManager.mp_f_10_long_solid_flames_tex);
        registerPart(ModItems.mp_fuselage_10_long_solid_insulation, ItemMissile.PartType.FUSELAGE, 7, 5, ResourceManager.mp_f_10_long_kerosene, ResourceManager.mp_f_10_long_solid_insulation_tex);
        registerPart(ModItems.mp_fuselage_10_long_solid_sleek, ItemMissile.PartType.FUSELAGE, 7, 5, ResourceManager.mp_f_10_long_kerosene, ResourceManager.mp_f_10_long_solid_sleek_tex);
        registerPart(ModItems.mp_fuselage_10_long_solid_soviet_glory, ItemMissile.PartType.FUSELAGE, 7, 5, ResourceManager.mp_f_10_long_kerosene, ResourceManager.mp_f_10_long_solid_soviet_glory_tex);
        registerPart(ModItems.mp_fuselage_10_long_solid_bullet, ItemMissile.PartType.FUSELAGE, 7, 5, ResourceManager.mp_f_10_long_kerosene, ResourceManager.mp_f_10_long_solid_bullet_tex);
        registerPart(ModItems.mp_fuselage_10_long_solid_silvermoonlight, ItemMissile.PartType.FUSELAGE, 7, 5, ResourceManager.mp_f_10_long_kerosene, ResourceManager.mp_f_10_long_solid_silvermoonlight_tex);
        //
        registerPart(ModItems.mp_fuselage_10_15_kerosene, ItemMissile.PartType.FUSELAGE, 9, 5.5, ResourceManager.mp_f_10_15_kerosene, ResourceManager.mp_f_10_15_kerosene_tex);
        registerPart(ModItems.mp_fuselage_10_15_solid, ItemMissile.PartType.FUSELAGE, 9, 5.5, ResourceManager.mp_f_10_15_kerosene, ResourceManager.mp_f_10_15_solid_tex);
        registerPart(ModItems.mp_fuselage_10_15_hydrogen, ItemMissile.PartType.FUSELAGE, 9, 5.5, ResourceManager.mp_f_10_15_kerosene, ResourceManager.mp_f_10_15_hydrogen_tex);
        registerPart(ModItems.mp_fuselage_10_15_balefire, ItemMissile.PartType.FUSELAGE, 9, 5.5, ResourceManager.mp_f_10_15_kerosene, ResourceManager.mp_f_10_15_balefire_tex);
        //
        registerPart(ModItems.mp_fuselage_15_kerosene, ItemMissile.PartType.FUSELAGE, 10, 6, ResourceManager.mp_f_15_kerosene, ResourceManager.mp_f_15_kerosene_tex);
        registerPart(ModItems.mp_fuselage_15_kerosene_camo, ItemMissile.PartType.FUSELAGE, 10, 6, ResourceManager.mp_f_15_kerosene, ResourceManager.mp_f_15_kerosene_camo_tex);
        registerPart(ModItems.mp_fuselage_15_kerosene_desert, ItemMissile.PartType.FUSELAGE, 10, 6, ResourceManager.mp_f_15_kerosene, ResourceManager.mp_f_15_kerosene_desert_tex);
        registerPart(ModItems.mp_fuselage_15_kerosene_sky, ItemMissile.PartType.FUSELAGE, 10, 6, ResourceManager.mp_f_15_kerosene, ResourceManager.mp_f_15_kerosene_sky_tex);
        registerPart(ModItems.mp_fuselage_15_kerosene_insulation, ItemMissile.PartType.FUSELAGE, 10, 6, ResourceManager.mp_f_15_kerosene, ResourceManager.mp_f_15_kerosene_insulation_tex);
        registerPart(ModItems.mp_fuselage_15_kerosene_metal, ItemMissile.PartType.FUSELAGE, 10, 6, ResourceManager.mp_f_15_kerosene, ResourceManager.mp_f_15_kerosene_metal_tex);
        registerPart(ModItems.mp_fuselage_15_kerosene_decorated, ItemMissile.PartType.FUSELAGE, 10, 6, ResourceManager.mp_f_15_kerosene, ResourceManager.mp_f_15_kerosene_decorated_tex);
        registerPart(ModItems.mp_fuselage_15_kerosene_steampunk, ItemMissile.PartType.FUSELAGE, 10, 6, ResourceManager.mp_f_15_kerosene, ResourceManager.mp_f_15_kerosene_steampunk_tex);
        registerPart(ModItems.mp_fuselage_15_kerosene_polite, ItemMissile.PartType.FUSELAGE, 10, 6, ResourceManager.mp_f_15_kerosene, ResourceManager.mp_f_15_kerosene_polite_tex);
        registerPart(ModItems.mp_fuselage_15_kerosene_blackjack, ItemMissile.PartType.FUSELAGE, 10, 6, ResourceManager.mp_f_15_kerosene, ResourceManager.mp_f_15_kerosene_blackjack_tex);
        registerPart(ModItems.mp_fuselage_15_kerosene_lambda, ItemMissile.PartType.FUSELAGE, 10, 6, ResourceManager.mp_f_15_kerosene, ResourceManager.mp_f_15_kerosene_lambda_tex);
        registerPart(ModItems.mp_fuselage_15_kerosene_minuteman, ItemMissile.PartType.FUSELAGE, 10, 6, ResourceManager.mp_f_15_kerosene, ResourceManager.mp_f_15_kerosene_minuteman_tex);
        registerPart(ModItems.mp_fuselage_15_kerosene_pip, ItemMissile.PartType.FUSELAGE, 10, 6, ResourceManager.mp_f_15_kerosene, ResourceManager.mp_f_15_kerosene_pip_tex);
        registerPart(ModItems.mp_fuselage_15_kerosene_taint, ItemMissile.PartType.FUSELAGE, 10, 6, ResourceManager.mp_f_15_kerosene, ResourceManager.mp_f_15_kerosene_taint_tex);
        registerPart(ModItems.mp_fuselage_15_kerosene_yuck, ItemMissile.PartType.FUSELAGE, 10, 6, ResourceManager.mp_f_15_kerosene, ResourceManager.mp_f_15_kerosene_yuck_tex);
        registerPart(ModItems.mp_fuselage_15_solid, ItemMissile.PartType.FUSELAGE, 10, 6, ResourceManager.mp_f_15_kerosene, ResourceManager.mp_f_15_solid_tex);
        registerPart(ModItems.mp_fuselage_15_solid_insulation, ItemMissile.PartType.FUSELAGE, 10, 6, ResourceManager.mp_f_15_kerosene, ResourceManager.mp_f_15_solid_insulation_tex);
        registerPart(ModItems.mp_fuselage_15_solid_desh, ItemMissile.PartType.FUSELAGE, 10, 6, ResourceManager.mp_f_15_kerosene, ResourceManager.mp_f_15_solid_desh_tex);
        registerPart(ModItems.mp_fuselage_15_solid_soviet_glory, ItemMissile.PartType.FUSELAGE, 10, 6, ResourceManager.mp_f_15_kerosene, ResourceManager.mp_f_15_solid_soviet_glory_tex);
        registerPart(ModItems.mp_fuselage_15_solid_soviet_stank, ItemMissile.PartType.FUSELAGE, 10, 6, ResourceManager.mp_f_15_kerosene, ResourceManager.mp_f_15_solid_soviet_stank_tex);
        registerPart(ModItems.mp_fuselage_15_solid_faust, ItemMissile.PartType.FUSELAGE, 10, 6, ResourceManager.mp_f_15_kerosene, ResourceManager.mp_f_15_solid_faust_tex);
        registerPart(ModItems.mp_fuselage_15_solid_silvermoonlight, ItemMissile.PartType.FUSELAGE, 10, 6, ResourceManager.mp_f_15_kerosene, ResourceManager.mp_f_15_solid_silvermoonlight_tex);
        registerPart(ModItems.mp_fuselage_15_solid_snowy, ItemMissile.PartType.FUSELAGE, 10, 6, ResourceManager.mp_f_15_kerosene, ResourceManager.mp_f_15_solid_snowy_tex);
        registerPart(ModItems.mp_fuselage_15_solid_panorama, ItemMissile.PartType.FUSELAGE, 10, 6, ResourceManager.mp_f_15_kerosene, ResourceManager.mp_f_15_solid_panorama_tex);
        registerPart(ModItems.mp_fuselage_15_solid_roses, ItemMissile.PartType.FUSELAGE, 10, 6, ResourceManager.mp_f_15_kerosene, ResourceManager.mp_f_15_solid_roses_tex);
        registerPart(ModItems.mp_fuselage_15_solid_mimi, ItemMissile.PartType.FUSELAGE, 10, 6, ResourceManager.mp_f_15_kerosene, ResourceManager.mp_f_15_solid_mimi_tex);
        registerPart(ModItems.mp_fuselage_15_hydrogen, ItemMissile.PartType.FUSELAGE, 10, 6, ResourceManager.mp_f_15_hydrogen, ResourceManager.mp_f_15_hydrogen_tex);
        registerPart(ModItems.mp_fuselage_15_hydrogen_cathedral, ItemMissile.PartType.FUSELAGE, 10, 6, ResourceManager.mp_f_15_hydrogen, ResourceManager.mp_f_15_hydrogen_cathedral_tex);
        registerPart(ModItems.mp_fuselage_15_balefire, ItemMissile.PartType.FUSELAGE, 10, 6, ResourceManager.mp_f_15_hydrogen, ResourceManager.mp_f_15_balefire_tex);

        registerPart(ModItems.mp_fuselage_15_20_kerosene, ItemMissile.PartType.FUSELAGE, 16, 10, ResourceManager.mp_f_15_20_kerosene, ResourceManager.mp_f_15_20_kerosene_tex);
        registerPart(ModItems.mp_fuselage_15_20_kerosene_magnusson, ItemMissile.PartType.FUSELAGE, 16, 10, ResourceManager.mp_f_15_20_kerosene, ResourceManager.mp_f_15_20_kerosene_magnusson_tex);
        registerPart(ModItems.mp_fuselage_15_20_solid, ItemMissile.PartType.FUSELAGE, 16, 10, ResourceManager.mp_f_15_20_kerosene, ResourceManager.mp_f_15_20_solid_tex);
        //
        // TODO move them all to fucking space
		registerPart(ModItemsSpace.rp_fuselage_20_12, ItemMissile.PartType.FUSELAGE, 12, 8, ResourceManagerSpace.mp_f_20_12_usa, ResourceManager.mp_f_20_kerolox_usa);
		registerPart(ModItemsSpace.rp_fuselage_20_6, ItemMissile.PartType.FUSELAGE, 6, 4.5, ResourceManagerSpace.mp_f_20_6_usa, ResourceManager.mp_f_20_kerolox_usa);
		registerPart(ModItemsSpace.rp_fuselage_20_3, ItemMissile.PartType.FUSELAGE, 3, 2.5, ResourceManagerSpace.mp_f_20_3_usa, ResourceManager.mp_f_20_kerolox).withShroud(ResourceManagerSpace.mp_f_20_6_usa);
		registerPart(ModItemsSpace.rp_fuselage_20_1, ItemMissile.PartType.FUSELAGE, 1, 1.5, ResourceManagerSpace.mp_f_20_1_usa, ResourceManager.mp_f_20_kerolox).withShroud(ResourceManagerSpace.mp_f_20_6_usa);
        registerPart(ModItemsSpace.rp_fuselage_20_12_hydrazine, ItemMissile.PartType.FUSELAGE, 10, 8, ResourceManagerSpace.mp_f_20_neo, ResourceManagerSpace.mp_f_20_hydrazine_tex);


        //////

        registerPart(ModItems.mp_warhead_10_he, ItemMissile.PartType.WARHEAD, 2, 1.5, ResourceManager.mp_w_10_he, ResourceManager.mp_w_10_he_tex);
        registerPart(ModItems.mp_warhead_10_incendiary, ItemMissile.PartType.WARHEAD, 2.5, 2, ResourceManager.mp_w_10_incendiary, ResourceManager.mp_w_10_incendiary_tex);
        registerPart(ModItems.mp_warhead_10_buster, ItemMissile.PartType.WARHEAD, 0.5, 1, ResourceManager.mp_w_10_buster, ResourceManager.mp_w_10_buster_tex);
        registerPart(ModItems.mp_warhead_10_nuclear, ItemMissile.PartType.WARHEAD, 2, 1.5, ResourceManager.mp_w_10_nuclear, ResourceManager.mp_w_10_nuclear_tex);
        registerPart(ModItems.mp_warhead_10_nuclear_large, ItemMissile.PartType.WARHEAD, 2.5, 1.5, ResourceManager.mp_w_10_nuclear_large, ResourceManager.mp_w_10_nuclear_large_tex);
        registerPart(ModItems.mp_warhead_10_taint, ItemMissile.PartType.WARHEAD, 2.25, 1.5, ResourceManager.mp_w_10_taint, ResourceManager.mp_w_10_taint_tex);
        registerPart(ModItems.mp_warhead_10_cloud, ItemMissile.PartType.WARHEAD, 2.25, 1.5, ResourceManager.mp_w_10_taint, ResourceManager.mp_w_10_cloud_tex);
        //
        registerPart(ModItems.mp_warhead_15_he, ItemMissile.PartType.WARHEAD, 2, 1.5, ResourceManager.mp_w_15_he, ResourceManager.mp_w_15_he_tex);
        registerPart(ModItems.mp_warhead_15_incendiary, ItemMissile.PartType.WARHEAD, 2, 1.5, ResourceManager.mp_w_15_incendiary, ResourceManager.mp_w_15_incendiary_tex);
        registerPart(ModItems.mp_warhead_15_nuclear, ItemMissile.PartType.WARHEAD, 3.5, 2, ResourceManager.mp_w_15_nuclear, ResourceManager.mp_w_15_nuclear_tex);
        registerPart(ModItems.mp_warhead_15_nuclear_shark, ItemMissile.PartType.WARHEAD, 3.5, 2, ResourceManager.mp_w_15_nuclear, ResourceManager.mp_w_15_nuclear_shark_tex);
        registerPart(ModItems.mp_warhead_15_thermo, ItemMissile.PartType.WARHEAD, 3.5, 2, ResourceManager.mp_w_15_nuclear, ResourceManager.mp_w_15_thermo_tex);
        registerPart(ModItems.mp_warhead_15_volcano, ItemMissile.PartType.WARHEAD, 3.5, 2, ResourceManager.mp_w_15_nuclear, ResourceManager.mp_w_15_volcano_tex);
        registerPart(ModItems.mp_warhead_15_boxcar, ItemMissile.PartType.WARHEAD, 2.25, 7.5, ResourceManager.mp_w_15_boxcar, ResourceManager.boxcar_tex);
        registerPart(ModItems.mp_warhead_15_n2, ItemMissile.PartType.WARHEAD, 3, 2, ResourceManager.mp_w_15_n2, ResourceManager.mp_w_15_n2_tex);
        registerPart(ModItems.mp_warhead_15_balefire, ItemMissile.PartType.WARHEAD, 2.75, 2, ResourceManager.mp_w_15_balefire, ResourceManager.mp_w_15_balefire_tex);
        registerPart(ModItems.mp_warhead_15_mirv, ItemMissile.PartType.WARHEAD, 3, 2, ResourceManager.mp_w_15_mirv, ResourceManager.mp_w_15_mirv_tex);
        registerPart(ModItems.mp_warhead_15_turbine, ItemMissile.PartType.WARHEAD, 2.25, 2, ResourceManager.mp_w_15_turbine, ResourceManager.mp_w_15_turbine_tex);
    }

    public static RocketPart registerPart(Item item, ItemMissile.PartType type, double height, double guiheight, IModelCustom model, ResourceLocation texture) {
        RocketPart part = new RocketPart(item, type, height, guiheight, model, texture);
        parts.put(item.hashCode(), part);
        return part;
    }

    public static RocketPart getPart(ItemStack stack) {
        if(stack == null)
            return null;

        return getPart(stack.getItem());
    }
    public static RocketPart getPart(Item item) {
        if(item == null)
            return null;

        return (RocketPart) parts.get(item.hashCode());
    }

    public static RocketPart getPart(int id) {
        if(id <= 0)
            return null;

        return getPart(Item.getItemById(id));
    }

    public static int getId(MissilePart m) {
        if(m == null) return 0;
        return Item.getIdFromItem(m.part);
    }
}
