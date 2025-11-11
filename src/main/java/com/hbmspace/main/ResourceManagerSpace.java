package com.hbmspace.main;

import com.hbm.lib.RefStrings;
import com.hbm.render.loader.HFRWavefrontObject;
import com.hbm.render.loader.IModelCustom;
import net.minecraft.util.ResourceLocation;

public class ResourceManagerSpace {

    public static final IModelCustom lpw2 = new HFRWavefrontObject(new ResourceLocation(RefStrings.MODID, "models/machines/lpw2.obj")).asVBO();
    public static final IModelCustom htr3 = new HFRWavefrontObject(new ResourceLocation(RefStrings.MODID, "models/machines/htr3.obj")).asVBO();
    public static final IModelCustom htrf4 = new HFRWavefrontObject(new ResourceLocation(RefStrings.MODID, "models/machines/htrf4.obj")).asVBO();
    public static final IModelCustom xenon_thruster = new HFRWavefrontObject(new ResourceLocation(RefStrings.MODID, "models/machines/xenon_thruster.obj")).asVBO();
    public static final IModelCustom drop_pod = new HFRWavefrontObject(new ResourceLocation(RefStrings.MODID, "models/missile_parts/rp_drop_pod.obj")).asVBO();
    public static final IModelCustom soyuz_lander_neo = (new HFRWavefrontObject(new ResourceLocation("hbm", "models/module_lander.obj"))).asVBO();
    public static final IModelCustom mp_w_fairing = (new HFRWavefrontObject(new ResourceLocation("hbm", "models/missile_parts/mp_w_fairing.obj"))).asVBO();
    public static final IModelCustom mp_f_20_12_usa = new HFRWavefrontObject(new ResourceLocation(RefStrings.MODID, "models/missile_parts/mp_f_20_usa.obj")).asVBO();
    public static final IModelCustom mp_f_20_6_usa = new HFRWavefrontObject(new ResourceLocation(RefStrings.MODID, "models/missile_parts/mp_f_20_6_usa.obj")).asVBO();
    public static final IModelCustom mp_f_20_3_usa = new HFRWavefrontObject(new ResourceLocation(RefStrings.MODID, "models/missile_parts/mp_f_20_3_usa.obj")).asVBO();
    public static final IModelCustom mp_f_20_1_usa = new HFRWavefrontObject(new ResourceLocation(RefStrings.MODID, "models/missile_parts/mp_f_20_1_usa.obj")).asVBO();
    public static final IModelCustom mp_f_20_neo = new HFRWavefrontObject(new ResourceLocation(RefStrings.MODID, "models/missile_parts/mp_f_20_neo.obj")).asVBO();
    public static final IModelCustom rp_s_20_leggy = new HFRWavefrontObject(new ResourceLocation(RefStrings.MODID, "models/missile_parts/rp_s_20_leggy.obj")).asVBO();
    public static final IModelCustom rp_s_20_leggy_deployed = new HFRWavefrontObject(new ResourceLocation(RefStrings.MODID, "models/missile_parts/rp_s_20_leggy_deployed.obj")).asVBO();
    //Space
    public static final IModelCustom solarp = new HFRWavefrontObject(new ResourceLocation(RefStrings.MODID, "models/machines/solar_panel.obj")).asVBO();
    public static final IModelCustom stardar = new HFRWavefrontObject(new ResourceLocation(RefStrings.MODID, "models/machines/antenna.obj")).asVBO();
    public static final IModelCustom drive_processor = new HFRWavefrontObject(new ResourceLocation(RefStrings.MODID, "models/machines/drive_processor.obj")).asVBO();
    public static final IModelCustom dish_controller = new HFRWavefrontObject(new ResourceLocation(RefStrings.MODID, "models/machines/dish_controller.obj")).asVBO();
    public static final IModelCustom air_scrubber = new HFRWavefrontObject(new ResourceLocation(RefStrings.MODID, "models/machines/air_scrubber.obj"));
    public static final IModelCustom orbital_computer = new HFRWavefrontObject(new ResourceLocation(RefStrings.MODID, "models/machines/spaceship_computer.obj")).asVBO();
    public static final IModelCustom transporter_pad = new HFRWavefrontObject(new ResourceLocation(RefStrings.MODID, "models/machines/transporter_pad.obj")).asVBO();
    public static final IModelCustom hydroponic = new HFRWavefrontObject(new ResourceLocation(RefStrings.MODID, "models/machines/hydroponic.obj")).asVBO();
    public static final IModelCustom rocket_assembly = new HFRWavefrontObject(new ResourceLocation(RefStrings.MODID, "models/machines/rocket_assembly.obj")).asVBO();
    public static final IModelCustom rocket_pad = new HFRWavefrontObject(new ResourceLocation(RefStrings.MODID, "models/machines/rocket_pad.obj")).asVBO();
    public static final IModelCustom docking_port = new HFRWavefrontObject(new ResourceLocation(RefStrings.MODID, "models/machines/docking_port.obj")).asVBO();
    public static final IModelCustom vac_cir_station = new HFRWavefrontObject(new ResourceLocation(RefStrings.MODID, "models/machines/vacuum_solderer.obj")).asVBO();
    public static final ResourceLocation lpw2_tex = new ResourceLocation(RefStrings.MODID, "textures/models/machines/lpw2.png");
    public static final ResourceLocation lpw2_error_tex = new ResourceLocation(RefStrings.MODID, "textures/models/machines/lpw2_term_error.png");//Xenon
    public static final ResourceLocation xenon_thruster_tex = new ResourceLocation(RefStrings.MODID, "textures/models/machines/xenon_thruster.png");
    public static final ResourceLocation xenon_exhaust_tex = new ResourceLocation(RefStrings.MODID, "textures/models/machines/xenon_trail.png");
    public static final ResourceLocation docking_port_tex = new ResourceLocation(RefStrings.MODID, "textures/models/machines/docking_port.png");
    public static final ResourceLocation drop_pod_tex = new ResourceLocation(RefStrings.MODID, "textures/models/missile_parts/warheads/rp_drop_pod.png");
    public static final ResourceLocation module_lander_tex = new ResourceLocation("hbm", "textures/models/soyuz_capsule/module_lander_space_elon.png");
    public static final ResourceLocation mp_w_fairing_tex = new ResourceLocation("hbm", "textures/models/missile_parts/warheads/mp_w_fairing.png");
    //Space
    public static final ResourceLocation solarp_tex = new ResourceLocation(RefStrings.MODID, "textures/models/machines/solar_panel.png"); //haha... "larp"
    public static final ResourceLocation stardar_tex = new ResourceLocation(RefStrings.MODID, "textures/models/machines/antenna.png");
    public static final ResourceLocation drive_processor_tex = new ResourceLocation(RefStrings.MODID, "textures/models/machines/drive_processor.png");
    public static final ResourceLocation dish_controller_tex = new ResourceLocation(RefStrings.MODID, "textures/models/machines/dish_controller.png");
    public static final ResourceLocation air_scrubber_tex = new ResourceLocation(RefStrings.MODID, "textures/models/machines/air_scrubber.png");
    public static final ResourceLocation orbital_computer_tex = new ResourceLocation(RefStrings.MODID, "textures/models/machines/spaceship_computer.png");
    public static final ResourceLocation transporter_pad_tex = new ResourceLocation(RefStrings.MODID, "textures/models/machines/transporter_pad.png");
    public static final ResourceLocation hydroponic_tex = new ResourceLocation(RefStrings.MODID, "textures/models/machines/hydroponic.png");
    public static final ResourceLocation rocket_assembly_tex = new ResourceLocation(RefStrings.MODID, "textures/models/machines/rocket_assembly.png");
    public static final ResourceLocation rocket_pad_tex = new ResourceLocation(RefStrings.MODID, "textures/models/machines/rocket_pad.png");
    public static final ResourceLocation rocket_pad_support_tex = new ResourceLocation(RefStrings.MODID, "textures/models/machines/rocket_pad_support.png");
    public static final ResourceLocation vac_cir_station_tex = new ResourceLocation(RefStrings.MODID, "textures/models/machines/vacuum_solderer.png");

    public static final ResourceLocation universal = new ResourceLocation(RefStrings.MODID, "textures/models/TheGadget3_.png");
}
